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
 * This plug-in filter aligns each row of the image to a user-specified row (or 
 * row segment) using FFT-based cross-correlation.  Each row is assumed to 
 * represent a single waveform. Shifting is accomplished by simple rotation of 
 * the row.  For stacks, all rows are aligned to the row or row segment selected 
 * from the currently displayed slice.
 * 
 * @author	Jon N. Marsh
 * @version	2013-11-11
 */

public class AlignWaveforms implements ExtendedPlugInFilter, DialogListener
{
	private ImagePlus imp;
	private static int seedIndex = 0;
	private static int loIndex = 0;
	private static int hiIndex = 1;
	private int width, height;
	private GenericDialog gd;
	private PlugInFilterRunner pfr;
	private float[] seedPixels;
	private float[] seedWaveform;
	private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + KEEP_PREVIEW;
	
	public int setup(String arg, ImagePlus imp) 
	{
		this.imp = imp;
		if (imp == null) {
			IJ.noImage();
			return DONE;
		}
		
		if (imp.getType() != ImagePlus.GRAY32) {
			IJ.error("Image must be 32-bit floating point format");
			return DONE;
		}
		
		width = imp.getWidth();
		height = imp.getHeight();
		seedPixels = (float[])imp.getProcessor().getPixelsCopy();
		seedWaveform = new float[width];

		return flags;
	}
	
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
	{
		this.pfr = pfr;
		gd = new GenericDialog("Align Waveforms...");
		gd.addNumericField("Waveform to align with:", seedIndex, 0);
		gd.addNumericField("Start index", loIndex, 0);
		gd.addNumericField("End index", hiIndex, 0);
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
		seedIndex = (int)gd.getNextNumber();
		loIndex = (int)gd.getNextNumber();
		hiIndex = (int)gd.getNextNumber();
		
		boolean noError = (!gd.invalidNumber()
				&& seedIndex>=0 
				&& seedIndex<height 
				&& loIndex>=0 
				&& loIndex<(width-1) 
				&& hiIndex>loIndex 
				&& hiIndex<width);
		
		if (noError) {
			seedWaveform = Arrays.copyOfRange(seedPixels, seedIndex*width, (seedIndex+1)*width);
		}

		return noError;
	}
	
	public void run(ImageProcessor ip) 
	{
		float[] pixels = (float[])ip.getPixels();
		
		execute(pixels, seedWaveform, loIndex, hiIndex);
	}

	
	/**
	 * Performs an in-place alignment of each array segment in {@code waveforms}
	 * with {@code seedWavefom} within the specified range of indices.  {@code waveforms}
	 * is assumed to be composed of a series of signals (of size given by 
	 * {@code seedWaveform.length}) concatenated sequentially in a one-dimensional 
	 * array. The alignment is performed by simple rotation of each segment.
	 * 
	 * @param waveforms	array of concatenated waveforms, each of length {@code seedWaveform.length}
	 * @param seedWaveform	array with which to align each signal in {@code waveforms}
	 * @param from	start index (inclusive) of subsection of {@code seedWaveform} to be used for alignment
	 * @param to end index (exclusive) of subsection of {@code seedWaveform} to be used for alignment
	 */
	public static void execute(final float[] waveforms, float[] seedWaveform, int from, int to)
	{
		final int w = seedWaveform.length;

		// determine number of records
		final int h = waveforms.length/w;
		
		// compute padded waveform length
		final int pw = w + WaveformUtils.amountToPadToNextPowerOf2(w);
		
		// initialize seed waveform copy and compute FFT
		final double[] seedRe = new double[pw];
		final double[] seedIm = new double[pw];
		for (int j=from; j<to; j++) {
			seedRe[j] = (double)seedWaveform[j];
		}
		WaveformUtils.fftComplexPowerOf2(seedRe, seedIm, true);
				
		// loop over each waveform
		for (int i=0; i<h; i++) {
			// compute row offset
			int offset = i*w;
			
			// initialize waveform copies
			double[] tempRe = new double[pw];
			double[] tempIm = new double[pw];
			for (int j=0; j<w; j++) {
				tempRe[j] = (double)waveforms[offset+j];
			}
			
			// compute cross-correlation
			WaveformUtils.fftComplexPowerOf2(tempRe, tempIm, true);
			double[] corrRe = new double[pw];
			double[] corrIm = new double[pw];
			for (int j=0; j<pw; j++) {
				corrRe[j] = tempRe[j]*seedRe[j] + tempIm[j]*seedIm[j];
				corrIm[j] = tempRe[j]*seedIm[j] - tempIm[j]*seedRe[j];
			}
			WaveformUtils.fftComplexPowerOf2(corrRe, corrIm, false);
			
			// find index of maximum value of cross-correlation array
			int maxIndex = WaveformUtils.maxIndex(corrRe);
			
			// because of symmetry of fft, shift > pw/2 corresponds to leftward (negative) rotation
			if (maxIndex >= pw/2) {
				maxIndex -= pw;
			}
			
			// rotate waveform in place
			WaveformUtils.rotateArrayInPlace(waveforms, maxIndex, offset, offset+w);	
		}
	}
	
	/**
	 * Performs an in-place alignment of each array segment in {@code waveforms}
	 * with {@code seedWavefom} within the specified range of indices.  {@code waveforms}
	 * is assumed to be composed of a series of signals (of size given by 
	 * {@code seedWaveform.length}) concatenated sequentially in a one-dimensional 
	 * array. The alignment is performed by simple rotation of each segment.
	 * 
	 * @param waveforms	array of concatenated waveforms, each of length {@code seedWaveform.length}
	 * @param seedWaveform	array with which to align each signal in {@code waveforms}
	 * @param from	start index (inclusive) of subsection of {@code seedWaveform} to be used for alignment
	 * @param to end index (exclusive) of subsection of {@code seedWaveform} to be used for alignment
	 */
	public static void execute(double[] waveforms, double[] seedWaveform, int from, int to)
	{
		int w = seedWaveform.length;

		// determine number of records
		int h = waveforms.length/w;
		
		// compute padded waveform length
		int pw = w + WaveformUtils.amountToPadToNextPowerOf2(w);
				
		// loop over each waveform
		for (int i=0; i<h; i++) {
			// compute row offset
			int offset = i*w;
			
			// initialize waveform copies
			double[] tempRe = new double[pw];
			double[] tempIm = new double[pw];
			System.arraycopy(waveforms, offset, tempRe, 0, w);
			double[] seedRe = new double[pw];
			double[] seedIm = new double[pw];
			System.arraycopy(seedWaveform, from, seedRe, from, to-from);
			
			// compute cross-correlation
			WaveformUtils.fftComplexPowerOf2(tempRe, tempIm, true);
			WaveformUtils.fftComplexPowerOf2(seedRe, seedIm, true);
			double[] corrRe = new double[pw];
			double[] corrIm = new double[pw];
			for (int j=0; j<pw; j++) {
				corrRe[j] = tempRe[j]*seedRe[j] + tempIm[j]*seedIm[j];
				corrIm[j] = tempRe[j]*seedIm[j] - tempIm[j]*seedRe[j];
			}
			WaveformUtils.fftComplexPowerOf2(corrRe, corrIm, false);
			
			// find index of maximum value of cross-correlation array
			int maxIndex = WaveformUtils.maxIndex(corrRe);
			
			// because of symmetry of fft, shift > pw/2 corresponds to leftward (negative) rotation
			if (maxIndex >= pw/2) {
				maxIndex -= pw;
			}
			
			// rotate waveform in place
			WaveformUtils.rotateArrayInPlace(waveforms, maxIndex, offset, offset+w);	
		}
	}
	
	public void setNPasses(int nPasses) {}
}
