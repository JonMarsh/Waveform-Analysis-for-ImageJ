package waveformAnalysisForImageJ;

import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.process.*;
import java.awt.*;

/**
 * This plug-in filter computes the envelope (i.e., the magnitude of analytic
 * signal) of each horizontal line in an image. It works on arbitrarily-sized
 * array lengths, by zero-padding up to the next power of 2 and implementing an
 * efficient FFT routine. The returned data is truncated to the original length
 * and written over the original image data.
 *
 * @author Jon N. Marsh
 * @version 2014-01-31
 */

public class Envelope implements ExtendedPlugInFilter, DialogListener
{
	private ImagePlus imp;
	private int width;
	private GenericDialog gd;
	private static boolean subtractMean = true;
	private static boolean logOutput = true;
	private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + KEEP_PREVIEW + FINAL_PROCESSING;
	
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

		return flags;
	}

	public int showDialog(ImagePlus imp, String command,  PlugInFilterRunner pfr)
	{	    
		gd = new GenericDialog("Envelope...");
		gd.addCheckbox("Subtract mean value", subtractMean);
		gd.addCheckbox("20Log(10)_Output", logOutput);
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
	    subtractMean = gd.getNextBoolean();
		logOutput = gd.getNextBoolean();
	    
	    return true;
	}
	
	public void run(ImageProcessor ip)
	{
		// get pixel values of current processor
		float[] pixels = (float[])ip.getPixels();
			    
		// compute envelopes
		execute(pixels, width, subtractMean);

		// compute log values if specified
		if (logOutput) {
	    	for (int i=0; i<pixels.length; i++) {
	    	    pixels[i] = (float)(20.0*Math.log10(pixels[i]));
	    	}
	    }	
		
	}
	
	/**
	 * Computes envelope (magnitude of analytic signal) of input waveforms,
	 * assumed to be of length {@code w} and concatenated and stored in
	 * one-dimensional input array {@code waveforms}. Each waveform is
	 * zero-padded to the next largest power of 2 if necessary in order to make
	 * use of FFTs for efficiency, and the final output is truncated to the
	 * original length. Results are computed in place. For efficiency, no error
	 * checking is performed on validity of inputs.
	 *
	 * @param waveforms	   input waveforms concatenated together
	 * @param recordLength length of each waveform in points
	 * @param subtractMean set to true to remove any DC offset before computing
	 *                     envelope
	 */
	public static final void execute(float[] waveforms, int recordLength, boolean subtractMean)
	{
		int numberOfRecords = waveforms.length/recordLength;
		
		int paddedWidth = recordLength + WaveformUtils.amountToPadToNextPowerOf2(recordLength);
	    	    
		// perform computations on row-by-row basis
	    for (int i=0; i<numberOfRecords; i++) {
			
	        // compute row offset 
	        int offset = i*recordLength;
			
			double valueToSubtract = 0.0;

			// compute mean if necessary
			if (subtractMean) {
				valueToSubtract = WaveformUtils.mean(waveforms, offset, offset+recordLength);
			}
	    	
			// initialize temporary copy padded array
			double[] waveformCopy = new double[paddedWidth];
			for (int j=0; j<recordLength; j++) {
				waveformCopy[j] = waveforms[offset+j] - valueToSubtract;
			}
	    		    	
	    	// compute Hilbert Transform
	    	WaveformUtils.fastHilbertTransformPowerOf2(waveformCopy, true);
	    	
	    	// copy magnitude of Hilbert Transform into original waveform array, truncating at original width
	    	for (int j=0; j<recordLength; j++) {
				double currentValue = waveforms[offset+j] - valueToSubtract;
	    	    waveforms[offset+j] = (float)Math.sqrt(waveformCopy[j]*waveformCopy[j] + currentValue*currentValue);
	    	}
	   	}
	}
	
	/**
	 * Computes envelope (magnitude of analytic signal) of input waveforms,
	 * assumed to be of length {@code w} and concatenated and stored in
	 * one-dimensional input array {@code waveforms}. Each waveform is
	 * zero-padded to the next largest power of 2 if necessary in order to make
	 * use of FFTs for efficiency, and the final output is truncated to the
	 * original length. Results are computed in place. For efficiency, no error
	 * checking is performed on validity of inputs.
	 *
	 * @param waveforms	   input waveforms concatenated together
	 * @param recordLength length of each waveform in points
	 * @param subtractMean set to true to remove any DC offset before computing
	 *                     envelope
	 */
	public static final void execute(double[] waveforms, int recordLength, boolean subtractMean)
	{
		int numberOfRecords = waveforms.length/recordLength;
		
		int paddedWidth = recordLength + WaveformUtils.amountToPadToNextPowerOf2(recordLength);
	    	    
		// perform computations on row-by-row basis
	    for (int i=0; i<numberOfRecords; i++) {
			
	        // compute row offset 
	        int offset = i*recordLength;
			
			double valueToSubtract = 0.0;

			// compute mean if necessary
			if (subtractMean) {
				valueToSubtract = WaveformUtils.mean(waveforms, offset, offset+recordLength);
			}
	    	
			// initialize temporary copy padded array
			double[] waveformCopy = new double[paddedWidth];
			for (int j=0; j<recordLength; j++) {
				waveformCopy[j] = waveforms[offset+j] - valueToSubtract;
			}
	    		    	
	    	// compute Hilbert Transform
	    	WaveformUtils.fastHilbertTransformPowerOf2(waveformCopy, true);
	    	
	    	// copy magnitude of Hilbert Transform into original waveform array, truncating at original width
	    	for (int j=0; j<recordLength; j++) {
				double currentValue = waveforms[offset+j] - valueToSubtract;
	    	    waveforms[offset+j] = Math.sqrt(waveformCopy[j]*waveformCopy[j] + currentValue*currentValue);
	    	}
	   	}
	}
    
	public void setNPasses(int nPasses) {}
    
}
