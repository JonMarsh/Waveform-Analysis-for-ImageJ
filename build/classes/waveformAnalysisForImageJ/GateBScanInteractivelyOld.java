package waveformAnalysisForImageJ;


import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.GenericDialog;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.plugin.filter.PlugInFilter;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import ij.util.Tools;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Panel;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

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
 * @version 2013-12-11
 */
public class GateBScanInteractivelyOld implements PlugInFilter, ActionListener, ItemListener
{
	private ImagePlus imp, gatesAppliedToImage;
	private ImageProcessor processor;
	private float[] pixels;
	private int[] gatePositions = null;
	private int recordLength, numberOfRecords;
	private static int startSearchIndex = 0;
	private static int offsetPoints = 0;
	private static int gateLengthPoints = 10;
	private static float threshold = 0.0f;
	private static int smoothingRadius = 1;
	private static boolean outputSingleRoi = true;
	private static boolean outputMultipleRois = false;
	private static boolean outputBorderRoi = false;
	private static boolean outputGatedSegments = false;
	private static boolean outputGatedWaveforms = false;
	private static boolean detectPosThreshold = true;
	private static boolean detectNegThreshold = false;
	private static boolean detectPosPeak = false;
	private static boolean detectNegPeak = false;
	private static boolean isWindowFunctionApplied = false;
	private static final WaveformUtils.WindowType[] windowTypes = WaveformUtils.WindowType.values();
	private static WaveformUtils.WindowType windowChoice = WaveformUtils.WindowType.HAMMING;
	private static double windowParameter = 0.5;
	private String[] suitableImageTitles;
	private JTextField searchStartIndexTextField, offsetTextField, thresholdTextField, gateLengthTextField, smoothingRadiusTextField, windowParameterTextField;
	private JButton createGatesButton, smoothGatesButton;
	private JRadioButton singleRoiRadioButton, multipleRoisRadioButton, borderRoiRadioButton, gatedSegmentsRadioButton, gatedWaveformsRadioButton, detectPosThresholdRadioButton, detectNegThresholdRadioButton, detectPosPeakRadioButton, detectNegPeakRadioButton;
	private JCheckBox applyWindowFunctionCheckBox;
	private JComboBox matchingImageComboBox, windowFunctionComboBox;
	private JLabel windowParameterLabel;
	private ButtonGroup selectionButtonGroup;
	private ButtonGroup detectionMethodButtonGroup;
	private GridBagLayout gridbag;
	private Panel panel;

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
		processor = imp.getProcessor();
		pixels = (float[]) processor.getPixels();
		recordLength = imp.getWidth();
		numberOfRecords = imp.getHeight();
		gatePositions = new int[numberOfRecords];
		suitableImageTitles = getMatchingImages();

		GenericDialog gd = new GenericDialog("Create B-Scan Gates for " + imp.getTitle());

		searchStartIndexTextField = new JTextField("" + startSearchIndex);
		searchStartIndexTextField.addActionListener(this);
		offsetTextField = new JTextField("" + offsetPoints);
		offsetTextField.addActionListener(this);
		thresholdTextField = new JTextField("" + threshold);
		thresholdTextField.addActionListener(this);
		gateLengthTextField = new JTextField("" + gateLengthPoints);
		gateLengthTextField.addActionListener(this);

		detectPosThresholdRadioButton = new JRadioButton("Positive-going threshold", detectPosThreshold);
		detectPosThresholdRadioButton.addActionListener(this);
		detectNegThresholdRadioButton = new JRadioButton("Negative-going threshold", detectNegThreshold);
		detectNegThresholdRadioButton.addActionListener(this);
		detectPosPeakRadioButton = new JRadioButton("First maximum above threshold", detectPosPeak);
		detectPosPeakRadioButton.addActionListener(this);
		detectNegPeakRadioButton = new JRadioButton("First minimum below threshold", detectNegPeak);
		detectNegPeakRadioButton.addActionListener(this);
		detectionMethodButtonGroup = new ButtonGroup();
		detectionMethodButtonGroup.add(detectPosThresholdRadioButton);
		detectionMethodButtonGroup.add(detectNegThresholdRadioButton);
		detectionMethodButtonGroup.add(detectPosPeakRadioButton);
		detectionMethodButtonGroup.add(detectNegPeakRadioButton);

		createGatesButton = new JButton("Create gates");
		createGatesButton.addActionListener(this);
		smoothGatesButton = new JButton("Smooth gates");
		smoothGatesButton.addActionListener(this);
		smoothingRadiusTextField = new JTextField("" + smoothingRadius);

		singleRoiRadioButton = new JRadioButton("Gated region (polygon ROI)", outputSingleRoi);
		singleRoiRadioButton.addActionListener(this);
		multipleRoisRadioButton = new JRadioButton("Gated region (multiple line ROIs)", outputMultipleRois);
		multipleRoisRadioButton.addActionListener(this);
		borderRoiRadioButton = new JRadioButton("Border line ROI", outputBorderRoi);
		borderRoiRadioButton.addActionListener(this);
		gatedSegmentsRadioButton = new JRadioButton("Gated segments", outputGatedSegments);
		gatedSegmentsRadioButton.addActionListener(this);
		gatedWaveformsRadioButton = new JRadioButton("Gated waveforms", outputGatedWaveforms);
		gatedWaveformsRadioButton.addActionListener(this);
		selectionButtonGroup = new ButtonGroup();
		selectionButtonGroup.add(singleRoiRadioButton);
		selectionButtonGroup.add(multipleRoisRadioButton);
		selectionButtonGroup.add(borderRoiRadioButton);
		selectionButtonGroup.add(gatedSegmentsRadioButton);
		selectionButtonGroup.add(gatedWaveformsRadioButton);
		
		applyWindowFunctionCheckBox = new JCheckBox("Apply window function", isWindowFunctionApplied);
		applyWindowFunctionCheckBox.setHorizontalTextPosition(SwingConstants.LEADING);
		applyWindowFunctionCheckBox.addItemListener(this);
		windowFunctionComboBox = new JComboBox(windowTypes);
		windowFunctionComboBox.setMaximumRowCount(windowTypes.length);
		windowFunctionComboBox.setSelectedItem(windowChoice);
		windowFunctionComboBox.setEnabled(isWindowFunctionApplied);
		windowFunctionComboBox.addActionListener(this);
		windowParameterLabel = new JLabel("Window parameter value");
		windowParameterLabel.setEnabled(isWindowFunctionApplied && windowChoice.usesParameter());
		windowParameterTextField = new JTextField("" + windowParameter);
		windowParameterTextField.setEnabled(isWindowFunctionApplied && windowChoice.usesParameter());
		windowParameterTextField.addActionListener(this);
		
		matchingImageComboBox = new JComboBox(suitableImageTitles);
		matchingImageComboBox.setSelectedIndex(0);
		matchingImageComboBox.addActionListener(this);

		gridbag = new GridBagLayout();
		panel = new Panel();
		panel.setLayout(gridbag);
		addComponent(new JLabel("Start searching at index"), 0, 0, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(searchStartIndexTextField, 1, 0, 1, 1, 50, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		addComponent(new JLabel("Offset from detected border (points)"), 0, 1, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(offsetTextField, 1, 1, 1, 1, 50, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		addComponent(new JLabel("Threshold"), 0, 2, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(thresholdTextField, 1, 2, 1, 1, 50, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		addComponent(new JLabel("Gate length (points)"), 0, 3, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(gateLengthTextField, 1, 3, 1, 1, 50, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);

		addComponent(new JLabel("Detection method:"), 0, 4, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(detectPosThresholdRadioButton, 1, 4, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(detectNegThresholdRadioButton, 1, 5, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(detectPosPeakRadioButton, 1, 6, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(detectNegPeakRadioButton, 1, 7, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);

		addComponent(createGatesButton, 0, 8, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(smoothGatesButton, 1, 8, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(new JLabel("Smoothing radius (points)"), 0, 9, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(smoothingRadiusTextField, 1, 9, 1, 1, 50, 100, GridBagConstraints.HORIZONTAL, GridBagConstraints.WEST);
		addComponent(new JLabel("Output:"), 0, 10, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(singleRoiRadioButton, 1, 10, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(multipleRoisRadioButton, 1, 11, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(borderRoiRadioButton, 1, 12, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(gatedSegmentsRadioButton, 1, 13, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(gatedWaveformsRadioButton, 1, 14, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(applyWindowFunctionCheckBox, 0, 15, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(windowFunctionComboBox, 1, 15, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(windowParameterLabel, 0, 16, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(windowParameterTextField, 1, 16, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);
		addComponent(new JLabel("Apply gates to"), 0, 17, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.EAST);
		addComponent(matchingImageComboBox, 1, 17, 1, 1, 50, 100, GridBagConstraints.NONE, GridBagConstraints.WEST);

		gd.addPanel(panel);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return DONE;
		}
		IJ.register(this.getClass());
		return DOES_32;
	}

	public void run(ImageProcessor ip)
	{
		int index = matchingImageComboBox.getSelectedIndex();

		float[] weights;
		if (applyWindowFunctionCheckBox.isSelected()) {
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

		if (gatedSegmentsRadioButton.isSelected()) {
			ImagePlus gatedImage = IJ.createImage(gatesAppliedToImage.getTitle() + " gated segments", "32-bit", gateLengthPoints, numberOfRecords, 1);
			ImageProcessor gatedImageProcessor = gatedImage.getProcessor();
			float[] gatedPixels = gateSegments(gatesAppliedToImage, gatePositions, gateLengthPoints, weights);
			gatedImageProcessor.setPixels(gatedPixels);
			gatedImage.show();
			IJ.resetMinAndMax();
		} else if (gatedWaveformsRadioButton.isSelected()) {
			ImagePlus gatedImage = IJ.createImage(gatesAppliedToImage.getTitle() + " gated waveforms", "32-bit", recordLength, numberOfRecords, 1);
			ImageProcessor gatedImageProcessor = gatedImage.getProcessor();
			float[] gatedPixels = gateWaveforms(gatesAppliedToImage, gatePositions, gateLengthPoints, weights);
			gatedImageProcessor.setPixels(gatedPixels);
			gatedImage.show();
			IJ.resetMinAndMax();			
		} else {
			RoiManager rm = RoiManager.getInstance();
			if (rm == null) {
				rm = new RoiManager();
			}
			if (multipleRoisRadioButton.isSelected()) {
				generateMultipleLineROIs(rm, gatePositions, gateLengthPoints);
			} else {
				rm.add(imp, imp.getRoi(), -1);
			}
		}

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
				// System.arraycopy(pix, offset1, gatedArray, offset2, gateLength);
				for (int j=0; j<gateLength; j++) {
					gatedArray[offset2+j] = weights[j]*pix[offset1+j];
				}
			} else if (gateStartPositions[i] + gateLength > w) { // end of gate set past end of waveform, so just pad with zeroes
				// System.arraycopy(pix, offset1 + gateStartPositions[i], gatedArray, offset2, w - gateStartPositions[i]);
				for (int j=0; j<w-gateStartPositions[i]; j++) {
					gatedArray[offset2+j] = weights[j]*pix[offset1+gateStartPositions[i]+j];
				}
			} else {	// gate is within waveform limits
				// System.arraycopy(pix, offset1 + gateStartPositions[i], gatedArray, offset2, gateLength);
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
			if (detectPosThresholdRadioButton.isSelected()) {
				gateStartPositions[i] = thresholdDetectPositive(tempArray, threshold) + offsetPoint + searchStartPoint;
			} else if (detectNegThresholdRadioButton.isSelected()) {
				gateStartPositions[i] = thresholdDetectNegative(tempArray, threshold) + offsetPoint + searchStartPoint;
			} else if (detectPosPeakRadioButton.isSelected()) {
				gateStartPositions[i] = peakDetectMaximum(tempArray, threshold) + offsetPoint + searchStartPoint;
			} else {
				gateStartPositions[i] = peakDetectMinimum(tempArray, threshold) + offsetPoint + searchStartPoint;
			}
		}

		return gateStartPositions;
	}

	// Returns the index of the first maximum peak value found which exceeds the specified threshold.  Returns -1 if no peaks above threshold are detected.
	private int peakDetectMaximum(float[] a, float threshold)
	{
		int i;
		float[] d = WaveformUtils.simpleDerivative(a, 1.0f);
		float peakValue = Float.MIN_VALUE;
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
		float peakValue = Float.MAX_VALUE;
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

	public void actionPerformed(ActionEvent e)
	{
		Object source = e.getSource();

		startSearchIndex = Integer.parseInt(searchStartIndexTextField.getText());
		offsetPoints = Integer.parseInt(offsetTextField.getText());
		threshold = Float.parseFloat(thresholdTextField.getText());
		gateLengthPoints = Integer.parseInt(gateLengthTextField.getText());
		smoothingRadius = Integer.parseInt(smoothingRadiusTextField.getText());
		outputSingleRoi = singleRoiRadioButton.isSelected();
		outputMultipleRois = multipleRoisRadioButton.isSelected();
		outputBorderRoi = borderRoiRadioButton.isSelected();
		outputGatedSegments = gatedSegmentsRadioButton.isSelected();
		outputGatedWaveforms = gatedWaveformsRadioButton.isSelected();
		detectPosThreshold = detectPosThresholdRadioButton.isSelected();
		detectNegThreshold = detectNegThresholdRadioButton.isSelected();
		detectPosPeak = detectPosPeakRadioButton.isSelected();
		detectNegPeak = detectNegPeakRadioButton.isSelected();
		windowChoice = (WaveformUtils.WindowType)windowFunctionComboBox.getSelectedItem();
		windowParameter = Double.parseDouble(windowParameterTextField.getText());
		isWindowFunctionApplied = applyWindowFunctionCheckBox.isSelected();


		if (source == createGatesButton) {
			gatePositions = computeGateStartPositions(pixels, recordLength, numberOfRecords, startSearchIndex, offsetPoints, threshold);
			if (borderRoiRadioButton.isSelected()) {
				drawBorder(imp, gatePositions, gateLengthPoints);
			} else {
				drawSingleROI(imp, gatePositions, gateLengthPoints);
			}
		}

		if (source == smoothGatesButton) {
			if (gatePositions != null) {
				int[] tempArray = medianFilter1D(gatePositions, smoothingRadius);
				System.arraycopy(tempArray, 0, gatePositions, 0, gatePositions.length);
				if (borderRoiRadioButton.isSelected()) {
					drawBorder(imp, gatePositions, gateLengthPoints);
				} else {
					drawSingleROI(imp, gatePositions, gateLengthPoints);
				}
			}
		}
		
		if (source == windowFunctionComboBox) {
			windowParameterLabel.setEnabled(isWindowFunctionApplied && windowChoice.usesParameter());
			windowParameterTextField.setEnabled(isWindowFunctionApplied && windowChoice.usesParameter());
		}
		
	}

	private int[] medianFilter1D(int[] a, int radius)
	{
		int[] filteredArray = new int[a.length];
		for (int i = 0; i < a.length; i++) {
			filteredArray[i] = getMedianValueAtIndex(a, i, radius, -1);
		}
		return filteredArray;
	}

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

	// Private method for adding gridbag layout constraints to components on user interface
	private void addComponent(Component component, int gridx, int gridy, int gridwidth, int gridheight, int weightx, int weighty, int fill, int anchor)
	{
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridwidth;
		constraints.gridheight = gridheight;
		constraints.weightx = weightx;
		constraints.weighty = weighty;
		constraints.fill = fill;
		constraints.anchor = anchor;
		gridbag.setConstraints(component, constraints);
		panel.add(component);
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

	@Override
	public void itemStateChanged(ItemEvent ie)
	{
		Object source = ie.getItemSelectable();
		
		startSearchIndex = Integer.parseInt(searchStartIndexTextField.getText());
		offsetPoints = Integer.parseInt(offsetTextField.getText());
		threshold = Float.parseFloat(thresholdTextField.getText());
		gateLengthPoints = Integer.parseInt(gateLengthTextField.getText());
		smoothingRadius = Integer.parseInt(smoothingRadiusTextField.getText());
		outputSingleRoi = singleRoiRadioButton.isSelected();
		outputMultipleRois = multipleRoisRadioButton.isSelected();
		outputBorderRoi = borderRoiRadioButton.isSelected();
		outputGatedSegments = gatedSegmentsRadioButton.isSelected();
		outputGatedWaveforms = gatedWaveformsRadioButton.isSelected();
		detectPosThreshold = detectPosThresholdRadioButton.isSelected();
		detectNegThreshold = detectNegThresholdRadioButton.isSelected();
		detectPosPeak = detectPosPeakRadioButton.isSelected();
		detectNegPeak = detectNegPeakRadioButton.isSelected();
		windowChoice = (WaveformUtils.WindowType)windowFunctionComboBox.getSelectedItem();
		windowParameter = Double.parseDouble(windowParameterTextField.getText());
		
		if (source == applyWindowFunctionCheckBox) {
			isWindowFunctionApplied = ie.getStateChange() == ItemEvent.SELECTED;
			windowFunctionComboBox.setEnabled(isWindowFunctionApplied);
			windowParameterLabel.setEnabled(isWindowFunctionApplied && windowChoice.usesParameter());
			windowParameterTextField.setEnabled(isWindowFunctionApplied && windowChoice.usesParameter());
		}
	}


}
