package waveformAnalysisForImageJ;

import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.process.*;
import java.awt.*;




public class ButterworthFilter implements ExtendedPlugInFilter, DialogListener
{

	private ImagePlus imp;
	private int width, height;
	private GenericDialog gd;
	private PlugInFilterRunner pfr;
	private static final String[] filterTypes = new String[]{"Low pass", "High pass"};
	private static int filterChoiceIndex = 0;
	private static double samplingIntervalMicrosec = 0.0025;
	private static double cutoffFreqMHz = 100.0;
	private static int numPoles = 1;
	private boolean isLowPass = true;

	int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + KEEP_PREVIEW + FINAL_PROCESSING;

	public int setup(String arg, ImagePlus imp)
	{
		if (arg.equals("final")) {
			IJ.resetMinAndMax();
			return DONE;
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

		gd = new GenericDialog("Butterworth Filter...");
		gd.addChoice("Filter type", filterTypes, filterTypes[filterChoiceIndex]);
		gd.addNumericField("Cutoff frequency", cutoffFreqMHz, 3, 8, "MHz");
		gd.addNumericField("Number of poles", numPoles, 0, 3, "");
		gd.addNumericField("Sampling interval", samplingIntervalMicrosec, 4, 8, "µs");
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
		filterChoiceIndex = gd.getNextChoiceIndex();
		cutoffFreqMHz = gd.getNextNumber();
		numPoles = (int) gd.getNextNumber();
		samplingIntervalMicrosec = gd.getNextNumber();

		isLowPass = (filterChoiceIndex == 0);

		return (numPoles >= 1 && samplingIntervalMicrosec > 0.0 && cutoffFreqMHz > 0.0);
	}

	public void run(ImageProcessor ip)
	{
		float[] pixels = (float[])ip.getPixels();
		
		execute(pixels, width, samplingIntervalMicrosec, cutoffFreqMHz, numPoles, isLowPass);
	}

	private static double[] computeFreqDomainCoeffs(double samplingIntervalMicrosec, int length, double cutoffFreqMHz, int numPoles, boolean isLowPass)
	{
		double[] coeffs = new double[length];
		double samplingRateMHz = 1.0 / samplingIntervalMicrosec;
		double deltaFMHz = samplingRateMHz / ((double) length);

		double fRatio;
		coeffs[0] = isLowPass ? 1.0 : 0.0;
		fRatio = deltaFMHz * (length / 2) / cutoffFreqMHz;
		if (!isLowPass) {
			fRatio = 1.0 / fRatio;
		}
		coeffs[length / 2] = 1.0 / Math.hypot(1.0, WaveformUtils.pow(fRatio, 2 * numPoles));
		for (int i = 1; i < length / 2; i++) {
			fRatio = i * deltaFMHz / cutoffFreqMHz;
			if (!isLowPass) {
				fRatio = 1.0 / fRatio;
			}
			coeffs[i] = 1.0 / Math.hypot(1.0, WaveformUtils.pow(fRatio, 2 * numPoles));
			coeffs[length - i] = coeffs[i];
		}

		return coeffs;
	}

	/**
	 * This plug-in applies a Butterworth filter to each waveform in {@code 
	 * waveforms}, which is assumed to be composed of a series of concatenated 
	 * records of size {@code recordlength}.  If {@code recordlength} is not a 
	 * power of two, the waveforms are zero-padded to the next highest
	 * power-of-two length before Fourier transforming. The resulting
	 * complex-valued frequency-domain data are then multiplied by the
	 * Butterworth filter coefficients, and inverse transformed and truncated to
	 * the original record length before being written back over the original
	 * values. If {@code recordLength>waveforms.length}, {@code recordLength<1}, 
	 * {@code samplingIntervalMicrosec<=0.0}, {@code cutoffFreqMHz <= 0.0}, or 
	 * {@code numPoles=0}, the method returns without modifying {@code waveforms}.
	 *
	 * @param	waveforms					input waveforms concatenated into 1-D array
	 * @param	recordLength				length of each waveform in points
	 * @param	samplingIntervalMicrosec	sampling interval in microseconds
	 * @param	cutoffFreqMHz				filter cutoff frequency in MHz
	 * @param	numPoles					number of poles
	 * @param	isLowPass					set to true for low-pass filter, false for high-pass filter
	 */
	public static final void execute(float[] waveforms, int recordLength, double samplingIntervalMicrosec, double cutoffFreqMHz, int numPoles, boolean isLowPass)
	{
		if (recordLength > waveforms.length 
			|| recordLength < 1 
			|| samplingIntervalMicrosec <= 0.0 
			|| cutoffFreqMHz <= 0.0 
			|| numPoles == 0) {
			return;
		}
		
		// pad waveforms to power-of-2 length if necessary
		int paddedWidth = recordLength + WaveformUtils.amountToPadToNextPowerOf2(recordLength);

		// compute filter coefficients for padded waveforms
		double[] filterCoeffs = computeFreqDomainCoeffs(samplingIntervalMicrosec, paddedWidth, cutoffFreqMHz, numPoles, isLowPass);

		// determine number of records in input array
		int h = waveforms.length / recordLength;

		// perform computations on row-by-row basis
		for (int i = 0; i < h; i++) {

			// row offset
			int offset = i * recordLength;

			// initialize real and imaginary temporary arrays for padded data
			double[] re = new double[paddedWidth];
			double[] im = new double[paddedWidth];

			// copy original values into temporary padded real array (imaginary portion is already initialized to zero)
			for (int j = 0; j < recordLength; j++) {
				re[j] = (double) waveforms[offset + j];
			}

			// filter
			WaveformUtils.fftComplexPowerOf2(re, im, true);
//			if (i==0) IJ.log(Arrays.toString(filterCoeffs));
			for (int j = 0; j < paddedWidth; j++) {
				re[j] *= filterCoeffs[j];
				im[j] *= filterCoeffs[j];
			}
			WaveformUtils.fftComplexPowerOf2(re, im, false);

			// copy data into original waveform array, truncating at original width
			for (int j = 0; j < recordLength; j++) {
				waveforms[offset + j] = (float) re[j];
			}
		}
	}

	public void setNPasses(int nPasses)
	{
	}

}
