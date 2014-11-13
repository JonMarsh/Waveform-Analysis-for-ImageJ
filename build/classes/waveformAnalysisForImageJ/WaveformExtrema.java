package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import java.awt.AWTEvent;
import java.util.ArrayList;

/**
 * Computes the positions and values of the extrema in each input waveform and
 * returns the respective results in new images. The user can select the
 * interpolation method employed for determining zero-crossing positions (none
 * or cubic spline). Each row in the input image is assumed to represent a
 * single waveform. The zero-crossing positions of the {@code i}<SUP>th</SUP>
 * waveform (row) in the {@code j}<SUP>th</SUP> slice are displayed in the
 * {@code i}<SUP>th</SUP> row and {@code j}<SUP>th</SUP> slice of the output
 * image. The zero positions range in value between zero and the record length
 * and are given in increasing order. Any output values after the last root is
 * found are invalid and displayed as zeroes in the output image. If no
 * interpolation is used, waveforms with segments of constant amplitude will
 * report maxima positions at the initial index of the segments.
 *
 * @author Jon N. Marsh
 * @version 2014-11-10
 */
public class WaveformExtrema implements ExtendedPlugInFilter, DialogListener
{

	private ImagePlus imp, maxPositionImage, minPositionImage, maxValueImage, minValueImage;
	private ImageStack maxPositionStack, maxValueStack, minPositionStack, minValueStack;
	private int width, height, stackSize;
	private String title;
	private GenericDialog gd;
	private static final String[] outputHeading = {"Output"};
	private static final String[] outputLabels = {"Maxima positions", "Maxima values", "Minima positions", "Minima values"};
	private static boolean outputMaximaPositions = true;
	private static boolean outputMaximaValues = false;
	private static boolean outputMinimaPositions = false;
	private static boolean outputMinimaValues = false;
	public static final int NONE = 0, CUBIC_SPLINE = 1;
	private static final String[] interpolationChoices = {"None", "Cubic spline"};
	private static int interpolationChoice = CUBIC_SPLINE;
	private PlugInFilterRunner pfr;
	private final int flags = DOES_8G + DOES_16 + DOES_32 + CONVERT_TO_FLOAT + DOES_STACKS + PARALLELIZE_STACKS + FINAL_PROCESSING;

	public int setup(String arg, ImagePlus imp)
	{
		if (arg.equals("final")) {
			if (outputMaximaPositions && maxPositionImage != null) {
				maxPositionImage.show();
				IJ.resetMinAndMax();
			}
			if (outputMinimaPositions && minPositionImage != null) {
				minPositionImage.show();
				IJ.resetMinAndMax();
			}
			if (outputMaximaValues && maxValueImage != null) {
				maxValueImage.show();
				IJ.resetMinAndMax();
			}
			if (outputMinimaValues && minValueImage != null) {
				minValueImage.show();
				IJ.resetMinAndMax();
			}
			return DONE;
		}

		this.imp = imp;
		if (imp == null) {
			IJ.noImage();
			return DONE;
		}

		width = imp.getWidth();
		height = imp.getHeight();
		stackSize = imp.getStackSize();
		title = imp.getTitle();

		return flags;
	}

	// No dialog needed here, but we use it to access the PlugInFilterRunner for parallel processing
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
	{
		this.pfr = pfr;
		gd = new GenericDialog("Zero Crossing Locations");
		gd.addCheckboxGroup(outputLabels.length, 1, outputLabels, new boolean[]{outputMaximaPositions, outputMaximaValues, outputMinimaPositions, outputMinimaValues}, outputHeading);
		gd.addChoice("Interpolation method:", interpolationChoices, interpolationChoices[interpolationChoice]);
		gd.addDialogListener(this);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return DONE;
		}

		if (!outputMaximaPositions && !outputMaximaValues && !outputMinimaPositions && !outputMinimaValues) {
			return DONE;
		}

		if (outputMaximaPositions) {
			maxPositionImage = IJ.createImage(title + " maxima positions", "32-bit", width, height, stackSize);
			maxPositionStack = maxPositionImage.getStack();
		}
		if (outputMaximaValues) {
			maxValueImage = IJ.createImage(title + " maxima values", "32-bit", width, height, stackSize);
			maxValueStack = maxValueImage.getStack();
		}
		if (outputMinimaPositions) {
			minPositionImage = IJ.createImage(title + " minima positions", "32-bit", width, height, stackSize);
			minPositionStack = minPositionImage.getStack();
		}
		if (outputMinimaValues) {
			minValueImage = IJ.createImage(title + " minima values", "32-bit", width, height, stackSize);
			minValueStack = minValueImage.getStack();
		}

		return flags;
	}

	public boolean dialogItemChanged(GenericDialog gd, AWTEvent e)
	{
		outputMaximaPositions = gd.getNextBoolean();
		outputMaximaValues = gd.getNextBoolean();
		outputMinimaPositions = gd.getNextBoolean();
		outputMinimaValues = gd.getNextBoolean();
		interpolationChoice = gd.getNextChoiceIndex();

		return true;
	}

	public void run(ImageProcessor ip)
	{
		int currentSlice = pfr.getSliceNumber();
		float[] pixels = (float[]) ip.getPixels();	// CONVERT_TO_FLOAT flag is set, so this always works

		SignalExtrema[] results = execute(pixels, width, interpolationChoice);

		if (outputMaximaPositions) {
			float[] maxPositionPixels = (float[]) (maxPositionStack.getProcessor(currentSlice).getPixels());
			for (int i = 0; i < height; i++) {
				int offset = i * width;
				ArrayList<WaveformPoint> maxList = results[i].maximaList;
				for (int j = 0; j < maxList.size(); j++) {
					maxPositionPixels[offset + j] = (float) (maxList.get(j).getFullIndex());
				}
			}
		}
		if (outputMaximaValues) {
			float[] maxValuePixels = (float[]) (maxValueStack.getProcessor(currentSlice).getPixels());
			for (int i = 0; i < height; i++) {
				int offset = i * width;
				ArrayList<WaveformPoint> maxList = results[i].maximaList;
				for (int j = 0; j < maxList.size(); j++) {
					maxValuePixels[offset + j] = (float) (maxList.get(j).getValue());
				}
			}
		}
		if (outputMinimaPositions) {
			float[] minPositionPixels = (float[]) (minPositionStack.getProcessor(currentSlice).getPixels());
			for (int i = 0; i < height; i++) {
				int offset = i * width;
				ArrayList<WaveformPoint> minList = results[i].minimaList;
				for (int j = 0; j < minList.size(); j++) {
					minPositionPixels[offset + j] = (float) (minList.get(j).getFullIndex());
				}
			}
		}
		if (outputMinimaValues) {
			float[] minValuePixels = (float[]) (minValueStack.getProcessor(currentSlice).getPixels());
			for (int i = 0; i < height; i++) {
				int offset = i * width;
				ArrayList<WaveformPoint> minList = results[i].minimaList;
				for (int j = 0; j < minList.size(); j++) {
					minValuePixels[offset + j] = (float) (minList.get(j).getValue());
				}
			}
		}

	}

	/**
	 * Computes the positions and values of extrema (both maxima and minima) in
	 * each record in {@code waveforms}, where each record has
	 * {@code recordLength} elements.
	 *
	 * @param waveforms           one-dimensional array composed of a series of
	 *                            concatenated records, each of size equal to
	 *                            {@code recordLength}
	 * @param recordLength        size of each record in {@code waveforms}
	 * @param interpolationMethod {@link #NONE} for no interpolation or
	 *                            {@link #CUBIC_SPLINE} for natural cubic spline
	 *                            interpolation between points in a waveform
	 * @return array of {@link #SignalExtrema} objects that contain extrema
	 *         positions and values. Each element of this array corresponds to
	 *         the local extrema in the corresponding record in
	 *         {@code waveforms}. Output is null if {@code waveforms==null},
	 *         {@code recordLength<=3}, {@code waveforms.length<recordLength},
	 *         or if {@code waveforms.length} is not evenly divisible by
	 *         {@code recordLength}.
	 */
	public static SignalExtrema[] execute(float[] waveforms, int recordLength, int interpolationMethod)
	{
		if (waveforms != null && recordLength > 3 && waveforms.length >= recordLength && waveforms.length % recordLength == 0) {

			// compute number of records
			int numRecords = waveforms.length / recordLength;

			// allocate output array of extrema data
			SignalExtrema[] extrema = new SignalExtrema[numRecords];

			// loop over all records
			for (int i = 0; i < numRecords; i++) {

				// compute row offset
				int offset = i * recordLength;

				// initialize lists to hold extrema data
				ArrayList<WaveformPoint> maximaList = new ArrayList<WaveformPoint>();
				ArrayList<WaveformPoint> minimaList = new ArrayList<WaveformPoint>();

				switch (interpolationMethod) {

					case NONE: {
						double yl = waveforms[offset];
						double ym = waveforms[offset + 1];
						for (int j = 1; j < recordLength - 1; j++) {
							double yr = waveforms[offset + j + 1];
							if (yl < ym && yr <= ym) {
								maximaList.add(new WaveformPoint(j, 0.0, ym));
							} else if (yl > ym && yr >= ym) {
								minimaList.add(new WaveformPoint(j, 0.0, ym));
							}
							yl = ym;
							ym = yr;
						}
						break;
					}

					case CUBIC_SPLINE: {
						// get spline coefficients for this waveform
						double[][] splineCoeffs = WaveformUtils.cubicSplineInterpolantUniformSpacing(waveforms, offset, offset + recordLength, 1.0);
						// loop over intervals between knots
						for (int j = 0; j < recordLength - 1; j++) {
							// determine roots of derivative in this interval
							double[] r = WaveformUtils.quadraticRoots(3.0 * splineCoeffs[3][j], 2.0 * splineCoeffs[2][j], splineCoeffs[1][j]);
							if (r.length > 0) {
								for (int k = 0; k < r.length; k++) {
									// make sure root lies within this interval
									if (r[k] >= 0 && j + r[k] < j + 1) {
										// determine curvature of function
										double curvature = 2.0 * splineCoeffs[2][j] + r[k] * 6.0 * splineCoeffs[3][j];
										if (curvature < 0.0) {
											maximaList.add(new WaveformPoint(j, r[k], splineCoeffs[0][j] + r[k] * (splineCoeffs[1][j] + r[k] * (splineCoeffs[2][j] + r[k] * splineCoeffs[3][j]))));
										} else {
											minimaList.add(new WaveformPoint(j, r[k], splineCoeffs[0][j] + r[k] * (splineCoeffs[1][j] + r[k] * (splineCoeffs[2][j] + r[k] * splineCoeffs[3][j]))));
										}
									}
								}
							}
						}
						break;
					}

					default: {
						break;
					}

				}

				extrema[i] = new SignalExtrema(maximaList, minimaList);

			}

			return extrema;

		}

		return null;
	}

	/**
	 * Computes the positions and values of extrema (both maxima and minima) in
	 * each record in {@code waveforms}, where each record has
	 * {@code recordLength} elements.
	 *
	 * @param waveforms           one-dimensional array composed of a series of
	 *                            concatenated records, each of size equal to
	 *                            {@code recordLength}
	 * @param recordLength        size of each record in {@code waveforms}
	 * @param interpolationMethod {@link #NONE} for no interpolation or
	 *                            {@link #CUBIC_SPLINE} for natural cubic spline
	 *                            interpolation between points in a waveform
	 * @return array of {@link #SignalExtrema} objects that contain extrema
	 *         positions and values. Each element of this array corresponds to
	 *         the local extrema in the corresponding record in
	 *         {@code waveforms}. Output is null if {@code waveforms==null},
	 *         {@code recordLength<=3}, {@code waveforms.length<recordLength},
	 *         or if {@code waveforms.length} is not evenly divisible by
	 *         {@code recordLength}.
	 */
	public static SignalExtrema[] execute(double[] waveforms, int recordLength, int interpolationMethod)
	{
		if (waveforms != null && recordLength > 3 && waveforms.length >= recordLength && waveforms.length % recordLength == 0) {

			// compute number of records
			int numRecords = waveforms.length / recordLength;

			// allocate output array of extrema data
			SignalExtrema[] extrema = new SignalExtrema[numRecords];

			// loop over all records
			for (int i = 0; i < numRecords; i++) {

				// compute row offset
				int offset = i * recordLength;

				// initialize lists to hold extrema data
				ArrayList<WaveformPoint> maximaList = new ArrayList<WaveformPoint>();
				ArrayList<WaveformPoint> minimaList = new ArrayList<WaveformPoint>();

				switch (interpolationMethod) {

					case NONE: {
						double yl = waveforms[offset];
						double ym = waveforms[offset + 1];
						for (int j = 1; j < recordLength - 1; j++) {
							double yr = waveforms[offset + j + 1];
							if (yl < ym && yr <= ym) {
								maximaList.add(new WaveformPoint(j, 0.0, ym));
							} else if (yl > ym && yr >= ym) {
								minimaList.add(new WaveformPoint(j, 0.0, ym));
							}
							yl = ym;
							ym = yr;
						}
						break;
					}

					case CUBIC_SPLINE: {
						// get spline coefficients for this waveform
						double[][] splineCoeffs = WaveformUtils.cubicSplineInterpolantUniformSpacing(waveforms, offset, offset + recordLength, 1.0);
						// loop over intervals between knots
						for (int j = 0; j < recordLength - 1; j++) {
							// determine roots of derivative in this interval
							double[] r = WaveformUtils.quadraticRoots(3.0 * splineCoeffs[3][j], 2.0 * splineCoeffs[2][j], splineCoeffs[1][j]);
							if (r.length > 0) {
								for (int k = 0; k < r.length; k++) {
									// make sure root lies within this interval
									if (r[k] >= 0 && j + r[k] < j + 1) {
										// determine curvature of function
										double curvature = 2.0 * splineCoeffs[2][j] + r[k] * 6.0 * splineCoeffs[3][j];
										if (curvature < 0.0) {
											maximaList.add(new WaveformPoint(j, r[k], splineCoeffs[0][j] + r[k] * (splineCoeffs[1][j] + r[k] * (splineCoeffs[2][j] + r[k] * splineCoeffs[3][j]))));
										} else {
											minimaList.add(new WaveformPoint(j, r[k], splineCoeffs[0][j] + r[k] * (splineCoeffs[1][j] + r[k] * (splineCoeffs[2][j] + r[k] * splineCoeffs[3][j]))));
										}
									}
								}
							}
						}
						break;
					}

					default: {
						break;
					}

				}

				extrema[i] = new SignalExtrema(maximaList, minimaList);

			}

			return extrema;

		}

		return null;
	}

	/**
	 * Simple container object for describing positions and values of local
	 * extrema of a single waveform. Minima and maxima positions are each stored
	 * as an {@link java.util.ArrayList} of {@link WaveformPoint}s so as to
	 * preserve the numerical precision of the extrema locations. These
	 * positions should be listed in order of occurrence.
	 */
	public static final class SignalExtrema
	{

		private ArrayList<WaveformPoint> maximaList;
		private ArrayList<WaveformPoint> minimaList;

		/**
		 * Initializes empty lists of waveform extrema positions and values.
		 */
		public SignalExtrema()
		{
			maximaList = new ArrayList<WaveformPoint>();
			minimaList = new ArrayList<WaveformPoint>();
		}

		/**
		 * Initializes lists of minima and maxima positions and values.
		 *
		 * @param maximaList list of local maxima positions where each element
		 *                   is in the form
		 *                   {@code [base_index, fractional_index]}; {@code base_index}
		 *                   should be interpreted as an integer, and
		 *                   {@code fractional_index} as a value between
		 *                   {@code 0} (inclusive) and {@code 1.0} (exclusive)
		 *                   that is added to {@code base_index} to obtain the
		 *                   position of the respective maximum. The list should
		 *                   be arranged ascending order of indices. The
		 *                   {@code i}<sup>th</sup> element of this list should
		 *                   correspond with the value of the
		 *                   {@code i}<sup>th</sup> component of
		 *                   {@link #maximaValueList}.
		 * @param minimaList list of local minima positions where each element
		 *                   is in the form
		 *                   {@code [base_index, fractional_index]}; {@code base_index}
		 *                   should be interpreted as an integer, and
		 *                   {@code fractional_index} as a value between
		 *                   {@code 0} (inclusive) and {@code 1.0} (exclusive)
		 *                   that is added to {@code base_index} to obtain the
		 *                   position of the respective minimum. The list should
		 *                   be arranged ascending order of indices. The
		 *                   {@code i}<sup>th</sup> element of this list should
		 *                   correspond with the value of the
		 *                   {@code i}<sup>th</sup> component of
		 *                   {@link #minimaValueList}.
		 */
		public SignalExtrema(ArrayList<WaveformPoint> maximaList, ArrayList<WaveformPoint> minimaList)
		{
			this.maximaList = maximaList;
			this.minimaList = minimaList;
		}

		public ArrayList<WaveformPoint> getMaxima()
		{
			return maximaList;
		}

		public void setMaxima(ArrayList<WaveformPoint> peakPositions)
		{
			this.maximaList = peakPositions;
		}

		public ArrayList<WaveformPoint> getMinima()
		{
			return minimaList;
		}

		public void setMinimaPositions(ArrayList<WaveformPoint> valleyPositions)
		{
			this.minimaList = valleyPositions;
		}

		public double[] getMaximaPositionsAsPrimitiveDoubleArray()
		{
			double[] output = new double[maximaList.size()];
			for (int i = 0; i < maximaList.size(); i++) {
				output[i] = maximaList.get(i).getFullIndex();
			}
			return output;
		}

		public double[] getMaximaValuesAsPrimitiveDoubleArray()
		{
			double[] output = new double[maximaList.size()];
			for (int i = 0; i < maximaList.size(); i++) {
				output[i] = maximaList.get(i).getValue();
			}
			return output;
		}

		public double[] getMinimaPositionsAsPrimitiveDoubleArray()
		{
			double[] output = new double[minimaList.size()];
			for (int i = 0; i < minimaList.size(); i++) {
				output[i] = minimaList.get(i).getFullIndex();
			}
			return output;
		}

		public double[] getMinimaValuesAsPrimitiveDoubleArray()
		{
			double[] output = new double[minimaList.size()];
			for (int i = 0; i < minimaList.size(); i++) {
				output[i] = minimaList.get(i).getValue();
			}
			return output;
		}

	}

	public void setNPasses(int nPasses)
	{
	}

}
