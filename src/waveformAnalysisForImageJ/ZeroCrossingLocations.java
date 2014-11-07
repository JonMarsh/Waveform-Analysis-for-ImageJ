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
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Computes the positions of the zero-crossings (or "roots") in each input
 * waveform and returns the results (given in units of fractional indices) in a
 * new image. The user can select the interpolation method employed for
 * determining zero-crossing positions (linear or cubic spline). Each row in the
 * input image is assumed to represent a single waveform. The zero-crossing
 * positions of the {@code i}<SUP>th</SUP> waveform (row) in the
 * {@code j}<SUP>th</SUP> slice are displayed in the {@code i}<SUP>th</SUP> row
 * and {@code j}<SUP>th</SUP> slice of the output image. The zero positions
 * range in value between zero and the record length and are given in increasing
 * order. Any output values after the last root is found are invalid and
 * displayed as zeroes in the output image.
 *
 * @author Jon N. Marsh
 * @version 2014-11-05
 */
public class ZeroCrossingLocations implements ExtendedPlugInFilter, DialogListener
{

	private ImagePlus imp, rootsImp;
	private ImageStack rootsStack;
	private int width, height, stackSize;
	private String title;
	private GenericDialog gd;
	public static final int LINEAR = 0, CUBIC_SPLINE = 1;
	private static final String[] interpolationChoices = {"Linear", "Cubic spline"};
	private static int interpolationChoice = CUBIC_SPLINE;
	private PlugInFilterRunner pfr;
	private final int flags = DOES_8G + DOES_16 + DOES_32 + CONVERT_TO_FLOAT + DOES_STACKS + PARALLELIZE_STACKS + FINAL_PROCESSING;

	public int setup(String arg, ImagePlus imp)
	{
		if (arg.equals("final")) {
			if (rootsImp != null) {
				rootsImp.show();
				IJ.resetMinAndMax();
				return DONE;
			}
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

		rootsImp = IJ.createImage(title + " zero-crossing positions", "32-bit", width, height, stackSize);
		rootsStack = rootsImp.getStack();

		return flags;
	}

	// No dialog needed here, but we use it to access the PlugInFilterRunner for parallel processing
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
	{
		this.pfr = pfr;
		gd = new GenericDialog("Zero Crossing Locations");
		gd.addChoice("Interpolation method:", interpolationChoices, interpolationChoices[interpolationChoice]);
		gd.addDialogListener(this);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return DONE;
		}

		return flags;
	}

	public boolean dialogItemChanged(GenericDialog gd, AWTEvent e)
	{
		interpolationChoice = gd.getNextChoiceIndex();

		return true;
	}

	public void run(ImageProcessor ip)
	{
		int currentSlice = pfr.getSliceNumber();
		float[] pixels = (float[]) ip.getPixels();	// CONVERT_TO_FLOAT flag is set, so this always works
		float[] rootsPixels = (float[]) (rootsStack.getProcessor(currentSlice).getPixels());

		double[][][] results = execute(pixels, width, interpolationChoice);
		for (int i = 0; i < height; i++) {
			int offset = i * width;
			int w = Math.min(width, results[i].length);
			for (int j = 0; j < w; j++) {
				rootsPixels[offset + j] = (float) (results[i][j][0] + results[i][j][1]);
			}
		}
	}

	/**
	 * Returns an array of arrays of {@code [base_index, fractional_index]}
	 * pairs representing the positions of zero-crossings in each record in
	 * {@code waveforms}, where the number of elements in each record is given
	 * by {@code recordLength}. Output is null if {@code waveforms==null}, {@code recordLength<=3},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}. Each zero-crossing location
	 * can be computed by adding the fractional index to the corresponding base
	 * index. The fractional index is always greater than or equal to zero or
	 * less than 1.0, so that accuracy is maintained and precision is
	 * consistent, even when the integer base index is large.
	 *
	 * @param waveforms           one-dimensional array composed of a series of
	 *                            concatenated records, each of size equal to
	 *                            {@code recordLength}
	 * @param recordLength        size of each record in {@code waveforms}
	 * @param interpolationMethod {@link #LINEAR} for linear interpolation or
	 *                            {@link #CUBIC_SPLINE} for natural cubic spline
	 *                            interpolation between points in a waveform
	 * @return array of variable length arrays of
	 *         {@code [base_index, fractional_index]} pairs. Zero crossings
	 *         locations can be read for the k<sup>th</sup> record off from
	 *         {@code [recordK][[base_index0, fractional_index0],...,[base_indexN, fractional_indexN]]}.
	 *         Each zero-crossing location can be computed by adding the
	 *         fractional index to the corresponding base index. The fractional
	 *         index is always greater than or equal to zero or less than 1.0,
	 *         and the base index should always be interpreted as an integer. If
	 *         no zero-crossings occur in an individual record, the
	 *         corresponding subarray is of length zero.
	 */
	public static double[][][] execute(float[] waveforms, int recordLength, int interpolationMethod)
	{
		if (waveforms != null && recordLength > 3 && waveforms.length >= recordLength && waveforms.length % recordLength == 0) {

			// compute number of records
			int numRecords = waveforms.length / recordLength;

			// allocate output array of arrays of {baseIndex, fractionalIndex} pairs
			double[][][] roots = new double[numRecords][][];

			// loop over all records
			for (int i = 0; i < numRecords; i++) {

				// compute row offset
				int offset = i * recordLength;

				// initialize temporary ArrayList to hold roots
				ArrayList<double[]> rootList = new ArrayList<double[]>();

				switch (interpolationMethod) {

					case LINEAR: {
						double y0 = waveforms[offset];
						// loop over intervals between knots
						for (int j = 0; j < recordLength - 1; j++) {
							double y1 = waveforms[offset + j + 1];
							if (y0 * y1 < 0.0) {	// zero-crossing occurs in this interval
								rootList.add(new double[]{j, y0 / (y0 - y1)});
							} else if (y0 == 0.0 && y1 != 0.0) {	// zero-crossing occurs at the beginning of the interval
								rootList.add(new double[]{j, 0.0});
							}
							y0 = y1;
						}
						if (waveforms[offset + recordLength - 1] == 0.0 && waveforms[offset + recordLength - 2] != 0.0) {	// check last point to see if it's a zero crossing
							rootList.add(new double[]{recordLength - 1, 0.0});
						}
						break;
					}

					case CUBIC_SPLINE: {
						// get spline coefficients for this waveform
						double[][] splineCoeffs = WaveformUtils.cubicSplineInterpolantUniformSpacing(waveforms, offset, offset + recordLength, 1.0);
						// loop over intervals between knots
						for (int j = 0; j < recordLength - 1; j++) {
							// determine roots of cubic polynomial in this interval
							double q = 1.0 / splineCoeffs[3][j];
							double[] r = WaveformUtils.cubicRoots(q * splineCoeffs[2][j], q * splineCoeffs[1][j], q * splineCoeffs[0][j]);
							for (int k = 0; k < r.length; k++) {
								if (r[k] >= 0 && j + r[k] < j + 1) {
									rootList.add(new double[]{j, r[k]});
								}
							}
						}
						// include last point if it equals zero and slope is nonzero
						double penultimateX = Math.nextAfter(recordLength - 1, 0.0);
						double h = penultimateX - (recordLength - 2);
						double penultimateY = splineCoeffs[0][recordLength - 2] + h * (splineCoeffs[1][recordLength - 2] + h * (splineCoeffs[2][recordLength - 2] + h * splineCoeffs[3][recordLength - 2]));
						if (waveforms[offset + recordLength - 1] == 0.0 && penultimateY != 0.0) {
							rootList.add(new double[]{recordLength - 1, 0.0});
						}
						break;
					}

					default: {
						break;
					}

				}

				roots[i] = new double[rootList.size()][2];
				for (int j = 0; j < rootList.size(); j++) {
					roots[i][j] = rootList.get(j);
				}
			}

			return roots;
		}

		return null;
	}

	/**
	 * Returns an array of arrays of {@code [base_index, fractional_index]}
	 * pairs representing the positions of zero-crossings in each record in
	 * {@code waveforms}, where the number of elements in each record is given
	 * by {@code recordLength}. Output is null if {@code waveforms==null}, {@code recordLength<=3},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}. Each zero-crossing location
	 * can be computed by adding the fractional index to the corresponding base
	 * index. The fractional index is always greater than or equal to zero or
	 * less than 1.0, so that accuracy is maintained and precision is
	 * consistent, even when the integer base index is large.
	 *
	 * @param waveforms           one-dimensional array composed of a series of
	 *                            concatenated records, each of size equal to
	 *                            {@code recordLength}
	 * @param recordLength        size of each record in {@code waveforms}
	 * @param interpolationMethod {@link #LINEAR} for linear interpolation or
	 *                            {@link #CUBIC_SPLINE} for natural cubic spline
	 *                            interpolation between points in a waveform
	 * @return array of variable length arrays of
	 *         {@code [base_index, fractional_index]} pairs. Zero crossings
	 *         locations can be read for the k<sup>th</sup> record off from
	 *         {@code [recordK][[base_index0, fractional_index0],...,[base_indexN, fractional_indexN]]}.
	 *         Each zero-crossing location can be computed by adding the
	 *         fractional index to the corresponding base index. The fractional
	 *         index is always greater than or equal to zero or less than 1.0,
	 *         and the base index should always be interpreted as an integer. If
	 *         no zero-crossings occur in an individual record, the
	 *         corresponding subarray is of length zero.
	 */
	public static double[][][] execute(double[] waveforms, int recordLength, int interpolationMethod)
	{
		if (waveforms != null && recordLength > 3 && waveforms.length >= recordLength && waveforms.length % recordLength == 0) {

			// compute number of records
			int numRecords = waveforms.length / recordLength;

			// allocate output array of arrays of {baseIndex, fractionalIndex} pairs
			double[][][] roots = new double[numRecords][][];

			// loop over all records
			for (int i = 0; i < numRecords; i++) {

				// compute row offset
				int offset = i * recordLength;

				// initialize temporary ArrayList to hold roots
				ArrayList<double[]> rootList = new ArrayList<double[]>();

				switch (interpolationMethod) {

					case LINEAR: {
						double y0 = waveforms[offset];
						// loop over intervals between knots
						for (int j = 0; j < recordLength - 1; j++) {
							double y1 = waveforms[offset + j + 1];
							if (y0 * y1 < 0.0) {	// zero-crossing occurs in this interval
								rootList.add(new double[]{j, y0 / (y0 - y1)});
							} else if (y0 == 0.0 && y1 != 0.0) {	// zero-crossing occurs at the beginning of the interval
								rootList.add(new double[]{j, 0.0});
							}
							y0 = y1;
						}
						if (waveforms[offset + recordLength - 1] == 0.0 && waveforms[offset + recordLength - 2] != 0.0) {	// check last point to see if it's a zero crossing
							rootList.add(new double[]{recordLength - 1, 0.0});
						}
						break;
					}

					case CUBIC_SPLINE: {
						// get spline coefficients for this waveform
						double[][] splineCoeffs = WaveformUtils.cubicSplineInterpolantUniformSpacing(waveforms, offset, offset + recordLength, 1.0);
						// loop over intervals between knots
						for (int j = 0; j < recordLength - 1; j++) {
							// determine roots of cubic polynomial in this interval
							double q = 1.0 / splineCoeffs[3][j];
							double[] r = WaveformUtils.cubicRoots(q * splineCoeffs[2][j], q * splineCoeffs[1][j], q * splineCoeffs[0][j]);
							for (int k = 0; k < r.length; k++) {
								if (r[k] >= 0 && j + r[k] < j + 1) {
									rootList.add(new double[]{j, r[k]});
								}
							}
						}
						// include last point if it equals zero and slope is nonzero
						double penultimateX = Math.nextAfter(recordLength - 1, 0.0);
						double h = penultimateX - (recordLength - 2);
						double penultimateY = splineCoeffs[0][recordLength - 2] + h * (splineCoeffs[1][recordLength - 2] + h * (splineCoeffs[2][recordLength - 2] + h * splineCoeffs[3][recordLength - 2]));
						if (waveforms[offset + recordLength - 1] == 0.0 && penultimateY != 0.0) {
							rootList.add(new double[]{recordLength - 1, 0.0});
						}
						break;
					}

					default: {
						break;
					}

				}

				roots[i] = new double[rootList.size()][2];
				for (int j = 0; j < rootList.size(); j++) {
					roots[i][j] = rootList.get(j);
				}
			}

			return roots;
		}

		return null;
	}

	public final double[][] toDouble(BigDecimal[][] a)
	{
		if (a != null) {
			double[][] b = new double[a.length][];
			for (int i = 0; i < a.length; i++) {
				if (a[i] != null) {
					b[i] = new double[a[i].length];
					for (int j = 0; j < a[i].length; j++) {
						b[i][j] = a[i][j].doubleValue();
					}
				}
			}
			return b;
		}

		return null;
	}

	public void setNPasses(int nPasses)
	{
	}

}
