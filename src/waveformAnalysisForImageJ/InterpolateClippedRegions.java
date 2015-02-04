package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import java.awt.AWTEvent;
import java.util.Arrays;

/**
 *
 *
 * @author Jon N. Marsh
 * @version 2014-11-21
 */
public class InterpolateClippedRegions implements ExtendedPlugInFilter, DialogListener
{

	private ImagePlus imp;
	private int width, height;
	private GenericDialog gd;
	private PlugInFilterRunner pfr;
	private static double clippingThreshold = 1.0;
	private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + KEEP_PREVIEW + FINAL_PROCESSING;

	public int setup(String arg, ImagePlus imp)
	{
		if (arg.equals("final")) {
			if (imp != null) {
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

		return flags;
	}

	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
	{
		this.pfr = pfr;
		gd = new GenericDialog("Interpolate Clipped Waveforms With Cubic Spline");
		gd.addNumericField("Clipping amplitude threshold", clippingThreshold, 4);
		gd.addPreviewCheckbox(pfr);
		gd.addDialogListener(this);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return DONE;
		}

		return flags;
	}

	public boolean dialogItemChanged(GenericDialog gd, AWTEvent e)
	{
		clippingThreshold = gd.getNextNumber();

		return (!gd.invalidNumber() && clippingThreshold >= 0.0);
	}

	public void run(ImageProcessor ip)
	{
		float[] pixels = (float[]) ip.getPixels();
		float[] interpolatedPixels = execute(pixels, width, clippingThreshold);
		System.arraycopy(interpolatedPixels, 0, pixels, 0, pixels.length);
	}

	public static final float[] execute(float[] waveforms, int recordLength, double threshold)
	{
		if (waveforms == null || recordLength <= 3 || waveforms.length < recordLength || waveforms.length % recordLength != 0) {
			return null;
		}

		// compute number of records
		int numRecords = waveforms.length / recordLength;

		// initialize output array
		float[] interpolatedWaveforms = Arrays.copyOf(waveforms, waveforms.length);

		// peform computations on a row-by-row basis
		for (int i = 0; i < numRecords; i++) {

			// offset to current record
			int offset = i * recordLength;

			// create arrays of valid x and y values -- they'll be larger than necessary
			double[] validX = new double[recordLength];
			double[] validY = new double[recordLength];
			int numberValid = 0;
			validX[0] = 0;
			validY[0] = waveforms[offset];
			for (int j = 1; j < recordLength - 1; j++) {
				double valueLo = waveforms[offset + j - 1];
				double valueMid = waveforms[offset + j];
				double valueHi = waveforms[offset + j + 1];
				double absValueLo = Math.abs(valueLo);
				double absValueMid = Math.abs(valueMid);
				double absValueHi = Math.abs(valueHi);
				boolean zeroCrossed = valueLo*valueMid < 0.0 || valueMid*valueHi < 0.0;
				if (absValueMid < threshold // any point less than threshold is valid
						|| (absValueMid >= threshold && absValueLo < threshold) // if the current point is past the threshold but the previous point wasn't, the current point is valid
						|| (absValueMid >= threshold && absValueHi < threshold)
						|| (absValueMid >= threshold && zeroCrossed)) {
					validX[numberValid] = j;
					validY[numberValid] = valueMid;
					numberValid++;
				}
			}
			
//			for (int j = 0; j < recordLength; j++) {
//				System.out.println(validX[j] + "\t" + validY[j]);
//			}

			// if clipping occurred, interpolate the waveform in the invalid regions
			if (numberValid != recordLength) {

				// compute interpolant coefficients
				double[][] coeffs = WaveformUtils.cubicSplineInterpolant(validX, validY);

				// perform interpolation
				for (int j = 0; j < recordLength - 1; j++) {
					int xLo = (int) validX[j];
					int xHi = (int) validX[j + 1];
					if (xHi == 0) { // reached empty part of validX, so we're done
						break;
					} else if (xHi != xLo + 1) { // in a clipped region
						for (int k = 1; k < xHi - xLo; k++) {
							interpolatedWaveforms[offset + xLo + k] = (float) (coeffs[0][j] + k * (coeffs[1][j] + k * (coeffs[2][j] + k * coeffs[3][j])));
						}
					}

				}

			}

		}

		return interpolatedWaveforms;
	}

	public static final double[] execute(double[] waveforms, int recordLength, double threshold)
	{
		if (waveforms == null || recordLength <= 3 || waveforms.length < recordLength || waveforms.length % recordLength != 0) {
			return null;
		}

		// compute number of records
		int numRecords = waveforms.length / recordLength;

		// initialize output array
		double[] interpolatedWaveforms = Arrays.copyOf(waveforms, waveforms.length);

		// peform computations on a row-by-row basis
		for (int i = 0; i < numRecords; i++) {

			// offset to current record
			int offset = i * recordLength;

			// create arrays of valid x and y values -- they'll be larger than necessary
			double[] validX = new double[recordLength];
			double[] validY = new double[recordLength];
			int numberValid = 0;
			for (int j = 0; j < recordLength; j++) {
				double value = waveforms[offset + j];
				if (Math.abs(value) < threshold) {
					validX[numberValid] = j;
					validY[numberValid] = value;
					numberValid++;
				}
			}

			// if clipping occurred, interpolate the waveform in the invalid regions
			if (numberValid != recordLength) {

				// compute interpolant coefficients
				double[][] coeffs = WaveformUtils.cubicSplineInterpolant(validX, validY);

				// perform interpolation
				for (int j = 0; j < recordLength - 1; j++) {
					int xLo = (int) validX[j];
					int xHi = (int) validX[j + 1];
					if (xHi == 0) { // reached empty part of validX, so we're done
						break;
					} else if (xHi != xLo + 1) { // in a clipped region
						for (int k = 1; k < xHi - xLo; k++) {
							interpolatedWaveforms[offset + xLo + k] = (float) (coeffs[0][j] + k * (coeffs[1][j] + k * (coeffs[2][j] + k * coeffs[3][j])));
						}
					}

				}

			}

		}

		return interpolatedWaveforms;
	}

	public void setNPasses(int nPasses)
	{
	}

}
