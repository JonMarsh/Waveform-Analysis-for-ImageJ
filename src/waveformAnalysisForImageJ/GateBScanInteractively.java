package waveformAnalysisForImageJ;

import waveformAnalysisForImageJ.WaveformUtils;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import ij.util.Tools;
import java.awt.AWTEvent;
import java.awt.Choice;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.TextField;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * This plug-in filter is used to auto-detect borders in a B-scan for the
 * purpose of creating gates and/or windowed waveforms from input data. Each
 * horizontal line of the input image is assumed to represent an individual
 * record. Gate positions are set by searching for the first peak (or nadir)
 * above (or below) a desired threshold level, or the first threshold crossing.
 * The output can be either a new image comprised of the gated waveform
 * segments, the original waveforms set to zero outside the gates, a single ROI
 * corresponding to the gated portion of each individual record, a single
 * polygon ROI that encircles each all gated regions, or a single polyline ROI
 * that follows the detected border.  The output gates can be applied to the 
 * input image, or a different image with the same dimensions.
  * 
 * @author Jon N. Marsh
 * @version 2014-09-15
 */

public class GateBScanInteractively implements ExtendedPlugInFilter, DialogListener
{
	private ImagePlus imp, altImage;
	private ImageProcessor processor;
	private float[] pixels;
	private int[] gatePositions = null;
	private int recordLength, numberOfRecords;
	private static int autoStartSearchIndex = 0;
	private static int offsetIndex = 0;
	private static int gateLengthPoints = 10;
	private static float threshold = 0.0f;
	private static int smoothingRadius = 1;
	private String[] suitableImageTitles;
	private static final int POS_THRESHOLD = 0, NEG_THRESHOLD = 1, POS_PEAK = 2, NEG_PEAK = 3;
	private static final String[] detectionTypes = {"Positive-going threshold", "Negative-going threshold", "First peak above threshold", "First nadir below threshold"};
	private static int detectionChoice = POS_PEAK;
	private static final int SINGLE_ROI = 0, MULTIPLE_ROIS = 1, BORDER_LINE_ROI = 2;
	private static final String[] roiOutputTypes = {"Gated region (polygon ROI)", "Gated region (multiple line ROIs)", "Border line ROI"};
	private static int roiOutputChoice = MULTIPLE_ROIS;
	private static final int NO_SIGNAL_OUTPUT = 0, GATED_SEGMENTS = 1, GATED_WAVEFORMS = 2;
	private static final String[] signalOutputTypes = {"None", "Gated segments", "Gated waveforms"};
	private static int signalOutputChoice = NO_SIGNAL_OUTPUT;
	private static boolean isWindowFunctionApplied = false;
	private static final String[] windowTypes = WaveformUtils.WindowType.stringValues();
	private static int windowChoice = WaveformUtils.WindowType.HAMMING.ordinal();
	private Choice windowTypeComboBox; 
	private static double windowParameter = 0.5;
	private TextField windowParameterTextField;
	private int altImageIndex = 0;
	private GenericDialog gd;
	private final int flags = DOES_32 + FINAL_PROCESSING;
	
	public int setup(String arg, ImagePlus imp) 
	{
		if (arg.equals("final")) {
			doFinalProcessing();
			return DONE;
		}
		
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
			imp.setRoi(0, 0, 0, 0);
		}
		
		processor = imp.getProcessor();
		pixels = (float[])processor.getPixels();
		recordLength = imp.getWidth();
		numberOfRecords = imp.getHeight();
		gatePositions = new int[numberOfRecords];
		suitableImageTitles = getMatchingImages();		
				
		return flags;
	}

	public int showDialog(ImagePlus ip, String string, PlugInFilterRunner pfr)
	{
		gd = new GenericDialog("Create B-Scan Gates for \""+imp.getTitle()+"\"");
		
		gd.addNumericField("Start search at index", autoStartSearchIndex, 0, 8, "");
		gd.addNumericField("Offset from detected border", offsetIndex, 0, 8, "points");
		gd.addNumericField("Threshold", threshold, 3, 8, "");
		gd.addNumericField("Gate length", gateLengthPoints, 0, 8, "points");
		gd.addChoice("Detection method:", detectionTypes, detectionTypes[detectionChoice]);
		gd.addNumericField("Smoothing radius", smoothingRadius, 0, 8, "points");
		gd.addChoice("ROI output:", roiOutputTypes, roiOutputTypes[roiOutputChoice]);
		gd.addChoice("Apply gates to:", suitableImageTitles, suitableImageTitles[altImageIndex]);
		gd.addChoice("Signal output:", signalOutputTypes, signalOutputTypes[signalOutputChoice]);
		gd.addCheckbox("Apply window function", isWindowFunctionApplied);
		gd.addChoice("Window type:", windowTypes, windowTypes[windowChoice]);
		windowTypeComboBox = (Choice)(gd.getChoices().get(4));
		windowTypeComboBox.setEnabled(isWindowFunctionApplied);
		gd.addNumericField("Window parameter", windowParameter, 3, 8, "");
		windowParameterTextField = (TextField)(gd.getNumericFields().get(5));
		windowParameterTextField.setEnabled(isWindowFunctionApplied && WaveformUtils.WindowType.values()[windowChoice].usesParameter());
		gd.addPreviewCheckbox(pfr);
		gd.addDialogListener(this);
		
		gd.showDialog();
		if (gd.wasCanceled()) {
			return DONE;
		}
	
		return flags;
	}

	public boolean dialogItemChanged(GenericDialog gd, AWTEvent awte)
	{
		autoStartSearchIndex = (int)gd.getNextNumber();
		offsetIndex = (int)gd.getNextNumber();
		threshold = (float)gd.getNextNumber();
		gateLengthPoints = (int)gd.getNextNumber();		
		detectionChoice = gd.getNextChoiceIndex();
		smoothingRadius = (int)gd.getNextNumber();
		roiOutputChoice = gd.getNextChoiceIndex();
		altImageIndex = gd.getNextChoiceIndex();
		signalOutputChoice = gd.getNextChoiceIndex();
		isWindowFunctionApplied = gd.getNextBoolean();
		windowChoice = gd.getNextChoiceIndex();
		windowParameter = gd.getNextNumber();
		
		windowTypeComboBox.setEnabled(isWindowFunctionApplied);
		windowParameterTextField.setEnabled(isWindowFunctionApplied && WaveformUtils.WindowType.values()[windowChoice].usesParameter());
		
		return (!gd.invalidNumber() 
				&& (autoStartSearchIndex >= 0 && autoStartSearchIndex < recordLength-1)
				&& (gateLengthPoints > 0 && gateLengthPoints <= recordLength )
				&& (smoothingRadius >= 0 && smoothingRadius <= numberOfRecords));

	}
		
	private void doFinalProcessing()
	{
		if (altImageIndex != 0) {
			altImage = WindowManager.getImage(suitableImageTitles[altImageIndex]);
		} else {
			altImage = imp;
		}
		
		float[] weights;
		if (isWindowFunctionApplied) {
			weights = Tools.toFloat(WaveformUtils.windowFunction(WaveformUtils.WindowType.values()[windowChoice], gateLengthPoints, windowParameter, false));
		} else {
			weights = new float[gateLengthPoints];
			Arrays.fill(weights, 1.0f);
		}

		if (roiOutputChoice != BORDER_LINE_ROI) { // Don't output any waveforms if a border ROI is desired
			if (signalOutputChoice == GATED_SEGMENTS) {
				ImagePlus gatedImage = IJ.createImage(altImage.getTitle()+" gated segments", "32-bit", gateLengthPoints, numberOfRecords, 1);
				ImageProcessor gatedImageProcessor = gatedImage.getProcessor();
				float[] gatedPixels = gateSegments(altImage, gatePositions, gateLengthPoints, weights);
				gatedImageProcessor.setPixels(gatedPixels);
				gatedImage.show();
				IJ.resetMinAndMax();
			} else if (signalOutputChoice == GATED_WAVEFORMS) {
				ImagePlus gatedImage = IJ.createImage(altImage.getTitle() + " gated waveforms", "32-bit", recordLength, numberOfRecords, 1);
				ImageProcessor gatedImageProcessor = gatedImage.getProcessor();
				float[] gatedPixels = gateWaveforms(altImage, gatePositions, gateLengthPoints, weights);
				gatedImageProcessor.setPixels(gatedPixels);
				gatedImage.show();
				IJ.resetMinAndMax();			
			}
		}
		
		RoiManager rm = RoiManager.getInstance();
		if (rm == null) {
			rm = new RoiManager();
		}
		if (roiOutputChoice == MULTIPLE_ROIS) {
			generateMultipleLineROIs(rm, gatePositions, gateLengthPoints);
		} else {
			rm.add(imp, imp.getRoi(), -1);
		}
		
	}
	
	public void run(ImageProcessor ip)
	{		
		// All this does is display a ROI showing gate/border values; the actual output is performed in the "final" processing step
		gatePositions = medianFilter1D(computeGateStartPositions(pixels, recordLength, numberOfRecords, autoStartSearchIndex, offsetIndex, threshold), smoothingRadius);
		if (roiOutputChoice == BORDER_LINE_ROI) {	
			drawBorder(imp, gatePositions);
		}
		else {
			drawSingleROI(imp, gatePositions, gateLengthPoints);
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
	private void drawBorder(ImagePlus imp, int[] gateStartPositions)
	{
		int[] yPoints = new int[gateStartPositions.length];
		for (int i=0; i<gateStartPositions.length; i++) {
			yPoints[i] = i;
		}
		PolygonRoi roi = new PolygonRoi(gateStartPositions, yPoints, gateStartPositions.length, Roi.POLYLINE);
		imp.setRoi(roi);
	}

	// Draw single ROI onto image
	private void drawSingleROI(ImagePlus imp, int[] gateStartPositions, int gateLength)
	{
		int length = gateStartPositions.length;
		int[] xPoints = new int[length*2];
		int[] yPoints = new int[length*2];
		for (int i=0; i<length; i++) {
			xPoints[i] = gateStartPositions[i];
			xPoints[length+i] = gateStartPositions[(length-1)-i] + gateLength;
			yPoints[i] = i;
			yPoints[length+i] = (length-1)-i;
		}
		PolygonRoi roi = new PolygonRoi(new Polygon(xPoints, yPoints, length*2), Roi.POLYGON);
		imp.setRoi(roi);
	}
	
	// Generate multiple line ROIs and add to RoiManager
	private void generateMultipleLineROIs(RoiManager rm, int[] gateStartPositions, int gateLength)
	{
		int length = gateStartPositions.length;
		for (int i=0; i<length; i++) {
			Roi line = new ij.gui.Line(gateStartPositions[i], i, gateStartPositions[i]+gateLength, i);
			rm.addRoi(line);
		}
	}
	
	// Computes gate start positions
	private int[] computeGateStartPositions(float[] pixels, int recordLength, int numberOfRecords, int searchStartPoint, int offsetPoint, float threshold)
	{
		float[] tempArray = new float[recordLength];
		int[] gateStartPositions = new int[numberOfRecords];
		ArrayList<Point> validGateStartPositions = new ArrayList<Point>();
		
		for (int i=0; i<numberOfRecords; i++) {
			System.arraycopy(pixels, (i*recordLength)+searchStartPoint, tempArray, 0, recordLength-searchStartPoint);
			int gateStartIndex;
			switch (detectionChoice) {
				case POS_THRESHOLD:
					gateStartIndex = thresholdDetectPositive(tempArray, threshold);
					break;
				case NEG_THRESHOLD:
					gateStartIndex = thresholdDetectNegative(tempArray, threshold);
					break;
				case POS_PEAK:
					gateStartIndex = peakDetectMaximum(tempArray, threshold);
					break;
				case NEG_PEAK:
					gateStartIndex = peakDetectMinimum(tempArray, threshold);
					break;
				default:
					gateStartIndex = -1;
			}
			if (gateStartIndex != -1) {
				validGateStartPositions.add(new Point(i, gateStartIndex));
				gateStartPositions[i] = gateStartIndex + offsetPoint + searchStartPoint;
			} else {
				gateStartPositions[i] = -1;
			}

		}
		
		// use spline interpolation to estimate gate start positions where no peak or threshold was detected
		if (!validGateStartPositions.isEmpty()) {
			// initialize arrays for spline fitting
			int nValid = validGateStartPositions.size();
			double[] x = new double[nValid];
			double[] y = new double[nValid];
			for (int i=0; i<nValid; i++) {
				x[i] = validGateStartPositions.get(i).x;
				y[i] = validGateStartPositions.get(i).y;
			}
			double[][] coeffs = WaveformUtils.smoothingSplineInterpolant(x, y, 1.0, 0.0);	// smoothing parameter value of 0.0 means no smoothing
			
			// now find points where we need to perform interpolation
			// first find out where valid range for spline is; outside this range, use nearest valid gate start values (which may be smoothed, so use "a" coefficient)

			// take care of beginning values here...
			if (x[0] > 0) {
				Arrays.fill(gateStartPositions, 0, (int)x[0]+1, (int)coeffs[0][0]);
			}

			// take care of ending values here...
			if (x[nValid-1] < numberOfRecords-1) {
				Arrays.fill(gateStartPositions, (int)x[nValid-1], numberOfRecords, (int)coeffs[0][nValid-1]);
			}

			// consider inside spline fit bounds here...
			int vIndex = 0;	// used to keep track of a/b/c/d coefficients to use
			for (int i=(int)x[0]+1; i<(int)x[nValid-1]; i++) {
				// check for valid gate start position -- if valid, we substitute the present value with the smoothed value given by the "a" coefficient
				if (gateStartPositions[i] != -1) {
					vIndex++;
					gateStartPositions[i] = (int)coeffs[0][vIndex];
				} else {
					double iLo = x[vIndex];
					double h = i - iLo;
					double a = coeffs[0][vIndex];
					double b = coeffs[1][vIndex];
					double c = coeffs[2][vIndex];
					double d = coeffs[3][vIndex];
					gateStartPositions[i] = (int)(a + h*(b + h*(c + h*d)));
				}
			}
		}

		return gateStartPositions;
	}

	// Returns the index of the first maximum peak value found which exceeds the specified threshold.  Returns -1 if no peaks above threshold are detected.
	private int peakDetectMaximum(float[] a, float threshold)
	{
		int i = 1;
		
		for (i=1; i<a.length-1; i++) {
			if (a[i] > threshold && a[i-1] < a[i] && a[i+1] < a[i]) {
				break;
			}
		}
		
		return (i>=a.length-2) ? -1 : i;
	}

	// Returns the index of the first minimum peak value found which is less than the specified threshold.  Returns -1 if no peaks above threshold are detected.
	private int peakDetectMinimum(float[] a, float threshold)
	{
		int i;
		
		for (i=1; i<a.length-1; i++) {
			if (a[i] < threshold && a[i-1] > a[i] && a[i+1] > a[i]) {
				break;
			}
		}
		
		return (i>=a.length-2) ? -1 : i;
	}

	private int thresholdDetectPositive(float[] a, float threshold)
	{
		int i;
		
		for (i=1; i<a.length-1; i++) {
			if (a[i-1]<threshold && a[i+1]>=threshold) {
				break;
			}
		}
		
		return (i>=a.length-2) ? -1 : i;
	}

	private int thresholdDetectNegative(float[] a, float threshold)
	{
		int i;
		
		for (i=1; i<a.length-1; i++) {
			if (a[i-1]>threshold && a[i+1]<=threshold) {
				break;
			}
		}
		
		return (i>=a.length-2) ? -1 : i;
	}

	private int[] medianFilter1D(int[] a, int radius)
	{
		int[] filteredArray = new int[a.length];
		
		if (radius > 0) {
			for (int i=0; i<a.length; i++) {
				filteredArray[i] = getMedianValueAtIndex(a, i, radius, -1);
			}
		} else {
			System.arraycopy(a, 0, filteredArray, 0, a.length);
		}
		
		return filteredArray;
	}

	private int getMedianValueAtIndex(int[] a, int index, int radius, int valueToIgnore)
	{
		int minIndex = Math.max(0, index-radius);
		int maxIndex = Math.min(a.length-1, index+radius);
		int span = (maxIndex-minIndex)+1;
		int[] temp = new int[span];
		ArrayList<Integer> v = new ArrayList<Integer>();

		System.arraycopy(a, minIndex, temp, 0, span);
		Arrays.sort(temp);

		for (int i=0; i<span; i++) {
			if (temp[i] != valueToIgnore) {
				v.add(temp[i]);
			}
		}

		int size = v.size();
		if (size == 1) {
			return(v.get(0));
		}
		else if (size%2 == 1) {
			return(v.get((size-1)/2));
		}
		else if (size != 0) {
			return (int)( (v.get(size/2)+v.get((size/2)-1))/2 );
		}
		else {
			return valueToIgnore;
		}	
	}

	// Returns list of matching images to blend (i.e. images with same dimensions and stacksizes)
	private String[] getMatchingImages()
	{
		int width = recordLength;
		int height = numberOfRecords;
		int thisID = imp.getID();
		int[] fullList = WindowManager.getIDList();
		ArrayList<String> matches = new ArrayList<String>(fullList.length);
		matches.add("Current image");
		for (int i=0; i<fullList.length; i++) {
			ImagePlus imp2 = WindowManager.getImage(fullList[i]);
			if (imp2.getWidth()==width && imp2.getHeight()==height && fullList[i]!=thisID) {
				String name = imp2.getTitle();
				if (!matches.contains(name)) {
					matches.add(name);
				}
			}
		}
        String[] matchingImages = new String[matches.size()];
		for (int i=0; i<matches.size(); i++) {
			matchingImages[i] = (String)matches.get(i);
		}
		return matchingImages;
	}

	public void setNPasses(int i) {}
	
}

