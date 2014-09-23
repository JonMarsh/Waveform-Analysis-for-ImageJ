package waveformAnalysisForImageJ;

import waveformAnalysisForImageJ.WaveformUtils;
import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.frame.RoiManager;
import ij.process.*;
import ij.util.Tools;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * This plug-in filter is used to auto-detect borders in a B-scan for the
 * purpose of creating gates and/or windowed waveforms from input data. Each
 * horizontal line of the input image is assumed to represent an individual
 * record. Gate positions are set by searching for the first peak (or nadir)
 * above (or below) a desired threshold level, or the first threshold crossing.
 * The output can be either a new image comprised of the gated waveform
 * segments, multiple line ROIs corresponding to the gated portions of each
 * individual record, a single polygon ROI that encircles each all gated
 * regions, or a single polyline ROI that follows the detected border. The
 * output gates can be applied to the input image, or a different image with the
 * same dimensions.
 * 
 * @author Jon N. Marsh
 * @version 2013-12-27
 */

public class TestPluginPanel implements ExtendedPlugInFilter, ActionListener, DocumentListener, ItemListener
{
    private ImagePlus imp, gatesAppliedToImage;
	private ImageProcessor processor;
	private float[] pixels;
	private int[] gatePositions = null;
    private int recordLength, numberOfRecords;
    private GenericDialog gd;
	private TestJPanel jPanel;
	private Panel panel;
//	private JButton createGatesButton, smoothGatesButton;
	private static int startSearchIndex = 0;
	private static int offsetPoints = 0;
	private static float threshold = 0.0f;
	private static int gateLengthPoints = 10;
	private static int smoothingRadius = 1;
	private static boolean outputSingleRoi = true;
	private static boolean outputMultipleRois = false;
	private static boolean outputBorderRoi = false;
	private static boolean outputNoSignals = true;
	private static boolean outputGatedSegments = false;
	private static boolean outputGatedWaveforms = false;
	private static boolean detectPosThreshold = true;
	private static boolean detectNegThreshold = false;
	private static boolean detectPosPeak = false;
	private static boolean detectNegPeak = false;
	private static boolean isWindowFunctionApplied = false;
	private static WaveformUtils.WindowType[] windowTypes = WaveformUtils.WindowType.values();
    private static WaveformUtils.WindowType windowChoice = WaveformUtils.WindowType.HAMMING;
	private static double windowParameter = 0.5;
	private String[] suitableImageTitles;
	private final int flags = DOES_32;
	
    public int setup(String arg, ImagePlus imp) 
    {
        this.imp = imp;
        if (imp == null) {
            IJ.noImage();
            return DONE;
        }

		if (imp.getType() != ImagePlus.GRAY32) {
			IJ.error("Image must be 32-bit grayscale");
			return DONE;
		}
		
		if (imp.getRoi() != null) {
			imp.setRoi(new Rectangle(0, 0, 0, 0));
		}

		recordLength = imp.getWidth();
        numberOfRecords = imp.getHeight();
		processor = imp.getProcessor();
		pixels = (float[]) processor.getPixels();
		gatePositions = new int[numberOfRecords];
		suitableImageTitles = getMatchingImages();
		
		jPanel = new TestJPanel(	
				startSearchIndex, 
				offsetPoints, 
				gateLengthPoints, 
				smoothingRadius, 
				threshold, 
				detectPosThreshold,
				detectNegThreshold,
				detectPosPeak,
				detectNegPeak,
				isWindowFunctionApplied, 
				windowTypes, 
				windowChoice, 
				windowParameter,
				outputSingleRoi,
				outputMultipleRois,
				outputBorderRoi,
				outputNoSignals,
				outputGatedSegments,
				outputGatedWaveforms,
				suitableImageTitles
		);
		
		addActionListeners(jPanel);
		addDocumentListeners(jPanel);
		addItemListeners(jPanel);
		
//		createGatesButton = jPanel.getCreateGatesButton();
//		createGatesButton.addActionListener(this);
//		smoothGatesButton = jPanel.getSmoothGatesButton();
//		smoothGatesButton.addActionListener(this);
		panel = new Panel();
		panel.add(jPanel);

        return flags;
    }	

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
        gd = new GenericDialog("Create B-Scan Gates for " + imp.getTitle());
		gd.addPanel(panel);

        gd.showDialog();
        if (gd.wasCanceled()) {
            return DONE;
        }

        return flags;
    }
	
    public void run(ImageProcessor ip) 
    {
		int index = jPanel.getApplyGatesToComboBox().getSelectedIndex();

		float[] weights;
		if (jPanel.getApplyWindowFunctionCheckBox().isSelected()) {
			weights = Tools.toFloat(WaveformUtils.windowFunction(windowChoice, gateLengthPoints, windowParameter, false));
		} else {
			weights = new float[gateLengthPoints];
			Arrays.fill(weights, 1.0f);
		}

		if (index != 0) {
			gatesAppliedToImage = WindowManager.getImage(suitableImageTitles[index]);
		} else {
			gatesAppliedToImage = imp;
		}

		if (!jPanel.getBorderROIRadioButton().isSelected()) {	// Don't output any waveforms if a border ROI is desired
			if (jPanel.getGatedSegmentsRadioButton().isSelected()) {
				ImagePlus gatedImage = IJ.createImage(gatesAppliedToImage.getTitle() + " gated segments", "32-bit", gateLengthPoints, numberOfRecords, 1);
				ImageProcessor gatedImageProcessor = gatedImage.getProcessor();
				float[] gatedPixels = gateSegments(gatesAppliedToImage, gatePositions, gateLengthPoints, weights);
				gatedImageProcessor.setPixels(gatedPixels);
				gatedImage.show();
				IJ.resetMinAndMax();
			} else if (jPanel.getWindowedWaveformsRadioButton().isSelected()) {
				ImagePlus gatedImage = IJ.createImage(gatesAppliedToImage.getTitle() + " gated waveforms", "32-bit", recordLength, numberOfRecords, 1);
				ImageProcessor gatedImageProcessor = gatedImage.getProcessor();
				float[] gatedPixels = gateWaveforms(gatesAppliedToImage, gatePositions, gateLengthPoints, weights);
				gatedImageProcessor.setPixels(gatedPixels);
				gatedImage.show();
				IJ.resetMinAndMax();			
			} 
		}
		
		RoiManager rm = RoiManager.getInstance();
		if (rm == null) {
			rm = new RoiManager();
		}
		if (jPanel.getMultipleROIsRadioButton().isSelected()) {
			generateMultipleLineROIs(rm, gatePositions, gateLengthPoints);
		} else {
			rm.add(imp, imp.getRoi(), -1);
		}
    }

	private void addActionListeners(TestJPanel jPanel)
	{
		jPanel.getCreateGatesButton().addActionListener(this);
		jPanel.getSmoothGatesButton().addActionListener(this);
		jPanel.getStartSearchTextField().addActionListener(this);
		jPanel.getOffsetTextField().addActionListener(this);
		jPanel.getThresholdTextField().addActionListener(this);
		jPanel.getGateLengthTextField().addActionListener(this);
		jPanel.getPosThresholdRadioButton().addActionListener(this);
		jPanel.getNegThresholdRadioButton().addActionListener(this);
		jPanel.getPosPeakRadioButton().addActionListener(this);
		jPanel.getNegPeakRadioButton().addActionListener(this);
		jPanel.getSmoothingRadiusTextField().addActionListener(this);
		jPanel.getWindowFunctionComboBox().addActionListener(this);
		jPanel.getWindowParameterTextField().addActionListener(this);
		jPanel.getSingleROIRadioButton().addActionListener(this);
		jPanel.getMultipleROIsRadioButton().addActionListener(this);
		jPanel.getBorderROIRadioButton().addActionListener(this);
		jPanel.getNoOutputRadioButton().addActionListener(this);
		jPanel.getGatedSegmentsRadioButton().addActionListener(this);
		jPanel.getWindowedWaveformsRadioButton().addActionListener(this);
	}
	
	private void addDocumentListeners(TestJPanel jPanel)
	{
		jPanel.getStartSearchTextField().getDocument().addDocumentListener(this);
		jPanel.getSmoothingRadiusTextField().getDocument().addDocumentListener(this);
		jPanel.getThresholdTextField().getDocument().addDocumentListener(this);
		jPanel.getGateLengthTextField().getDocument().addDocumentListener(this);
		jPanel.getSmoothingRadiusTextField().getDocument().addDocumentListener(this);
		jPanel.getWindowParameterTextField().getDocument().addDocumentListener(this);		
	}
	
	private void addItemListeners(TestJPanel jPanel)
	{
		jPanel.getApplyWindowFunctionCheckBox().addItemListener(this);
	}
	
	// Update variables when any user presses return in a text field, if a radio button is clicked, or if a combobox selection is changed
	public void actionPerformed(ActionEvent ae)
	{
		startSearchIndex = Integer.parseInt(jPanel.getStartSearchTextField().getText());
		offsetPoints = Integer.parseInt(jPanel.getOffsetTextField().getText());
		threshold = Float.parseFloat(jPanel.getThresholdTextField().getText());
		gateLengthPoints = Integer.parseInt(jPanel.getGateLengthTextField().getText());
		smoothingRadius = Integer.parseInt(jPanel.getSmoothingRadiusTextField().getText());
		outputSingleRoi = jPanel.getSingleROIRadioButton().isSelected();
		outputMultipleRois = jPanel.getMultipleROIsRadioButton().isSelected();
		outputBorderRoi = jPanel.getBorderROIRadioButton().isSelected();
		outputNoSignals = jPanel.getNoOutputRadioButton().isSelected();
		outputGatedSegments = jPanel.getGatedSegmentsRadioButton().isSelected();
		outputGatedWaveforms = jPanel.getWindowedWaveformsRadioButton().isSelected();
		detectPosThreshold = jPanel.getPosThresholdRadioButton().isSelected();
		detectNegThreshold = jPanel.getNegThresholdRadioButton().isSelected();
		detectPosPeak = jPanel.getPosPeakRadioButton().isSelected();
		detectNegPeak = jPanel.getNegPeakRadioButton().isSelected();
		windowChoice = (WaveformUtils.WindowType)(jPanel.getWindowFunctionComboBox().getSelectedItem());
		windowParameter = Double.parseDouble(jPanel.getWindowParameterTextField().getText());
		isWindowFunctionApplied = jPanel.getApplyWindowFunctionCheckBox().isSelected();

		JButton source = (JButton)ae.getSource();
		
//		if (source == createGatesButton) {
		if (source == jPanel.getCreateGatesButton()) {
			gatePositions = computeGateStartPositions(pixels, recordLength, numberOfRecords, startSearchIndex, offsetPoints, threshold);
			if (jPanel.getBorderROIRadioButton().isSelected()) {
				drawBorder(imp, gatePositions, gateLengthPoints);
			} else {
				drawSingleROI(imp, gatePositions, gateLengthPoints);
			}
		}
		
//		if (source == smoothGatesButton) {
		if (source == jPanel.getSmoothGatesButton()) {
			if (gatePositions != null) {
				int[] tempArray = medianFilter1D(gatePositions, smoothingRadius);
				System.arraycopy(tempArray, 0, gatePositions, 0, gatePositions.length);
				if (jPanel.getBorderROIRadioButton().isSelected()) {
					drawBorder(imp, gatePositions, gateLengthPoints);
				} else {
					drawSingleROI(imp, gatePositions, gateLengthPoints);
				}
			}
		}
	}

	// Update variables when text changes occur
	public void changedUpdate(DocumentEvent de)
	{
		startSearchIndex = Integer.parseInt(jPanel.getStartSearchTextField().getText());
		offsetPoints = Integer.parseInt(jPanel.getOffsetTextField().getText());
		threshold = Float.parseFloat(jPanel.getThresholdTextField().getText());
		gateLengthPoints = Integer.parseInt(jPanel.getGateLengthTextField().getText());
		smoothingRadius = Integer.parseInt(jPanel.getSmoothingRadiusTextField().getText());
		windowParameter = Double.parseDouble(jPanel.getWindowParameterTextField().getText());
	}

	// Update variables when checkbox selection status changes occur
	public void itemStateChanged(ItemEvent ie)
	{
		isWindowFunctionApplied = jPanel.getApplyWindowFunctionCheckBox().isSelected();
	}

	// Returns list of matching images to blend (i.e. images with same dimensions and stacksizes)
	private String[] getMatchingImages()
	{
		int width = recordLength;
		int height = numberOfRecords;
		int channels = imp.getProcessor().getNChannels();
		int thisID = imp.getID();
		int[] fullList = WindowManager.getIDList();
		ArrayList<String> matches = new ArrayList<String>(fullList.length);
		matches.add("Current image");
		for (int i = 0; i < fullList.length; i++) {
			ImagePlus imp2 = WindowManager.getImage(fullList[i]);
			if (imp2.getWidth() == width && imp2.getHeight() == height && fullList[i] != thisID) {
				String name = imp2.getTitle();
				if (!matches.contains(name)) {
					matches.add(name);
				}
			}
		}
		String[] matchingImages = new String[matches.size()];
		for (int i = 0; i < matches.size(); i++) {
			matchingImages[i] = (String) matches.get(i);
		}
		return matchingImages;
	}
	
	// Applies a moving-window median filter to input array, ignoring any value equal to -1
	private int[] medianFilter1D(int[] a, int radius)
	{
		int[] filteredArray = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			filteredArray[i] = getMedianValueAtIndex(a, i, radius, -1);
		}
		return filteredArray;
	}

	// Returns the median value of a subset of the input array centered at index, of size 2*radius+1, ignoring any value equal to valueToIgnore
	private int getMedianValueAtIndex(int[] a, int index, int radius, int valueToIgnore)
	{
		int minIndex = Math.max(0, index - radius);
		int maxIndex = Math.min(a.length - 1, index + radius);
		int span = (maxIndex - minIndex) + 1;
		int[] temp = new int[span];
		ArrayList<Integer> v = new ArrayList<Integer>();

		System.arraycopy(a, minIndex, temp, 0, span);
		Arrays.sort(temp);

		for (int i = 0; i < span; i++) {
			if (temp[i] != valueToIgnore) {
				v.add(temp[i]);
			}
		}

		int size = v.size();
		if (size == 1) {
			return (v.get(0));
		} else if (size % 2 == 1) {
			return (v.get((size - 1) / 2));
		} else if (size != 0) {
			return (int) ((v.get(size / 2) + v.get((size / 2) - 1)) / 2);
		} else {
			return valueToIgnore;
		}
	}	

	// Returns the index of the first maximum peak value found which exceeds the specified threshold.  Returns -1 if no peaks above threshold are detected.
	private int peakDetectMaximum(float[] a, float threshold)
	{
		int i;
		float[] d = WaveformUtils.simpleDerivative(a, 1.0f);
		for (i = 0; i < d.length - 1; i++) {
			if ((a[i + 1] >= threshold) && (d[i] > 0 && d[i + 1] <= 0)) {
				break;
			}
		}
		if (i >= d.length - 2) {
			return (-1);
		} else {
			return i;
		}
	}

	// Returns the index of the first minimum peak value found which is less than the specified threshold.  Returns -1 if no peaks above threshold are detected.
	private int peakDetectMinimum(float[] a, float threshold)
	{
		int i;
		float[] d = WaveformUtils.simpleDerivative(a, 1.0f);
		for (i = 0; i < d.length - 1; i++) {
			if ((a[i + 1] <= threshold) && (d[i] < 0 && d[i + 1] >= 0)) {
				break;
			}
		}
		if (i >= d.length - 2) {
			return (-1);
		} else {
			return i;
		}
	}

	// Returns the index of the first element of the input array  which is greater than the specified threshold.  Returns -1 if no values are above threshold.
	private int thresholdDetectPositive(float[] a, float threshold)
	{
		int i;
		for (i = 1; i < a.length - 1; i++) {
			if (a[i - 1] < threshold && a[i + 1] >= threshold) {
				break;
			}
		}
		if (i > a.length - 2) {
			i = -1;
		}
		return i;
	}

	// Returns the index of the first element of the input array  which is less than the specified threshold.  Returns -1 if no values are below threshold.
	private int thresholdDetectNegative(float[] a, float threshold)
	{
		int i;
		for (i = 1; i < a.length - 1; i++) {
			if (a[i - 1] > threshold && a[i + 1] <= threshold) {
				break;
			}
		}
		if (i > a.length - 2) {
			i = -1;
		}
		return i;
	}
	
	// Extract gated waveform segments and apply window function if necessary
	private float[] gateSegments(ImagePlus imp, int[] gateStartPositions, int gateLength, float[] weights)
	{
		float[] pix = (float[]) (imp.getProcessor()).getPixels();
		int h = imp.getHeight();
		int w = imp.getWidth();
		float[] gatedArray = new float[h * gateLength];

		for (int i = 0; i < h; i++) {
			int offset1 = i*w;
			int offset2 = i*gateLength;
			if (gateStartPositions[i] < 0) {	// gate position set to -1, so no boundary detected; just set start of gate to 0
				for (int j=0; j<gateLength; j++) {
					gatedArray[offset2+j] = weights[j]*pix[offset1+j];
				}
			} else if (gateStartPositions[i] + gateLength > w) { // end of gate set past end of waveform, so just pad with zeroes
				for (int j=0; j<w-gateStartPositions[i]; j++) {
					gatedArray[offset2+j] = weights[j]*pix[offset1+gateStartPositions[i]+j];
				}
			} else {	// gate is within waveform limits
				for (int j=0; j<gateLength; j++) {
					gatedArray[offset2+j] = weights[j]*pix[offset1+gateStartPositions[i]+j];
				}
			}
		}
		
		return gatedArray;
	}
	
	// Gate waveforms and apply window function if necessary
	private float[] gateWaveforms(ImagePlus imp, int[] gateStartPositions, int gateLength, float[] weights)
	{
		float[] pix = (float[])(imp.getProcessor()).getPixels();
		int h = imp.getHeight();
		int w = imp.getWidth();
		float[] gatedArray = new float[h * w];
		
		for (int i = 0; i < h; i++) {
			int offset = i*w;
			if (gateStartPositions[i] < 0) { // gate position set to -1, so no boundary detected; just set start of gate to 0
				for (int j=0; j<gateLength; j++) {
					gatedArray[offset+j] = weights[j]*pix[offset+j];
				}
			} else if (gateStartPositions[i] + gateLength > w) { // end of gate set past end of waveform, so truncate
				for (int j=0; j<w-gateStartPositions[i]; j++) {
					gatedArray[offset+gateStartPositions[i]+j] = weights[j]*pix[offset+gateStartPositions[i]+j];
				}
			} else { // gate is within waveform limits
				for (int j=0; j<gateLength; j++) {
					gatedArray[offset+gateStartPositions[i]+j] = weights[j]*pix[offset+gateStartPositions[i]+j];
				}
			}
		}
		
		return gatedArray;
	}

	// Draw border line ROI on image
	private void drawBorder(ImagePlus imp, int[] gateStartPositions, int gateLength)
	{
		int[] yPoints = new int[gateStartPositions.length];
		for (int i = 0; i < gateStartPositions.length; i++) {
			yPoints[i] = i;
		}
		PolygonRoi roi = new PolygonRoi(gateStartPositions, yPoints, gateStartPositions.length, Roi.POLYLINE);
		imp.setRoi(roi);
	}

	// Draw single ROI onto image
	private void drawSingleROI(ImagePlus imp, int[] gateStartPositions, int gateLength)
	{
		int length = gateStartPositions.length;
		int[] xPoints = new int[length * 2];
		int[] yPoints = new int[length * 2];
		for (int i = 0; i < length; i++) {
			xPoints[i] = gateStartPositions[i];
			xPoints[length + i] = gateStartPositions[(length - 1) - i] + gateLength;
			yPoints[i] = i;
			yPoints[length + i] = (length - 1) - i;
		}
		PolygonRoi roi = new PolygonRoi(new Polygon(xPoints, yPoints, length * 2), Roi.POLYGON);
		imp.setRoi(roi);
	}

	// Generate multiple line ROIs and add to RoiManager
	private void generateMultipleLineROIs(RoiManager rm, int[] gateStartPositions, int gateLength)
	{
		int length = gateStartPositions.length;
		for (int i = 0; i < length; i++) {
			Roi line = new ij.gui.Line(gateStartPositions[i], i, gateStartPositions[i] + gateLength, i);
			rm.addRoi(line);
		}
	}

	// Computes gate start positions
	private int[] computeGateStartPositions(float[] pixels, int recordLength, int numberOfRecords, int searchStartPoint, int offsetPoint, float threshold)
	{
		float[] tempArray = new float[recordLength];
		int[] gateStartPositions = new int[numberOfRecords];

		for (int i = 0; i < numberOfRecords; i++) {
			System.arraycopy(pixels, (i * recordLength) + searchStartPoint, tempArray, 0, recordLength - searchStartPoint);
			if (jPanel.getPosThresholdRadioButton().isSelected()) {
				gateStartPositions[i] = thresholdDetectPositive(tempArray, threshold) + offsetPoint + searchStartPoint;
			} else if (jPanel.getNegThresholdRadioButton().isSelected()) {
				gateStartPositions[i] = thresholdDetectNegative(tempArray, threshold) + offsetPoint + searchStartPoint;
			} else if (jPanel.getPosPeakRadioButton().isSelected()) {
				gateStartPositions[i] = peakDetectMaximum(tempArray, threshold) + offsetPoint + searchStartPoint;
			} else {
				gateStartPositions[i] = peakDetectMinimum(tempArray, threshold) + offsetPoint + searchStartPoint;
			}
		}

		return gateStartPositions;
	}
	
	public void insertUpdate(DocumentEvent de) {}

	public void removeUpdate(DocumentEvent de) {}

	public void setNPasses(int nPasses) {}
}
