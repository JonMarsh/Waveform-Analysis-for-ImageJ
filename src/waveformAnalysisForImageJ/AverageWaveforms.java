package waveformAnalysisForImageJ;

import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.process.*;

/**
 * Computes the mean waveform from a set of input waveforms.  Each row in the 
 * input image is assumed to represent a single waveform.  If the input image is
 * a stack, each slice of the output is the  mean waveform from the original 
 * input slice. The original image is replaced by the resulting average(s).
 * 
 * @author Jon N. Marsh
 * @version 2013-11-13
 */

public class AverageWaveforms implements ExtendedPlugInFilter
{
    private ImagePlus imp, avgImp;
	ImageStack stack, avgStack;
    private int width, height, stackSize;
	private String title;
    private PlugInFilterRunner pfr;
    private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + FINAL_PROCESSING;
	
    public int setup(String arg, ImagePlus imp) 
    {
        if (arg.equals("final")) {
            if (avgImp != null) {
				imp.setStack(title+" average", avgStack);
				IJ.resetMinAndMax();
                return DONE;
            }
        }

        this.imp = imp;
        if (imp == null) {
            IJ.noImage();
            return DONE;
        }

		stack = imp.getStack();
		width = imp.getWidth();
        height = imp.getHeight();
		stackSize = imp.getStackSize();
		title = imp.getTitle();
		
		if (height == 1 && stackSize == 1) {
			return DONE;
		}
		
		avgImp = IJ.createImage("", "32-bit", width, 1, stackSize);
		avgStack = avgImp.getStack();
		
        return flags;
    }	
		
	public void run(ImageProcessor ip) 
	{
		// retrieve image slice being operated on
		int currentSlice = pfr.getSliceNumber();
		
		// get pixel array reference for output data for this slice 
		ImageProcessor avgProcessor = avgStack.getProcessor(currentSlice);
		float[] avgPixels = (float[])avgProcessor.getPixels();
		
		// get pixel array reference for current input data
		float[] pixels = (float[])ip.getPixels();

		// do averaging
		float[] avg = execute(pixels, width);
		
		// copy average waveform into output pixel array
		System.arraycopy(avg, 0, avgPixels, 0, width);
	}
	
	/**
	 * Returns a single array of size {@code recordLength} that is the element-by-element
	 * average of each record in {@code waveforms}.  {@code waveforms} is a one-dimensional
	 * array composed of a series of concatenated waveforms, each of size {@code recordLength}.
	 * 
	 * @param waveforms		array of concatenated waveforms, assumed to of length {@code recordLength}
	 * @param recordLength	size of each record in {@code waveforms}
	 * @return				array of size {@code recordLength} that represents the 
	 *						element-by-element average of each record in {@code waveforms}
	 *						(null if {@code waveforms==null}, {@code recordLength>waveforms.length}, or {@code recordLength<=0})
	 */
	public static float[] execute(float[] waveforms, int recordLength)
	{
		if (waveforms == null || recordLength > waveforms.length || recordLength <= 0) {
			return null;
		}
		
		float[] avgWaveform = new float[recordLength];
		int numRecords = waveforms.length/recordLength;
		
		// loop over elements in output average waveform
		for (int i=0; i<recordLength; i++) {
			
			double sum = 0.0f;
			
			// loop over all input waveform values at index i
			for (int j=0; j<numRecords; j++) {
				sum += waveforms[j*recordLength+i];
			}
			avgWaveform[i] = (float)(sum/numRecords);
		}
		
		return avgWaveform;
	}

	/**
	 * Returns a single array of size {@code recordLength} that is the element-by-element
	 * average of each record in {@code waveforms}.  {@code waveforms} is a one-dimensional
	 * array composed of a series of concatenated waveforms, each of size {@code recordLength}.
	 * 
	 * @param waveforms		array of concatenated waveforms, assumed to of length {@code recordLength}
	 * @param recordLength	size of each record in {@code waveforms}
	 * @return				array of size {@code recordLength} that represents the 
	 *						element-by-element average of each record in {@code waveforms}
	 *						(null if {@code waveforms==null}, {@code recordLength>waveforms.length}, or {@code recordLength<=0})
	 */
	public static double[] execute(double[] waveforms, int recordLength)
	{
		if (waveforms == null || recordLength > waveforms.length || recordLength <= 0) {
			return null;
		}
		
		double[] avgWaveform = new double[recordLength];
		int numRecords = waveforms.length/recordLength;
		
		// loop over elements in output average waveform
		for (int i=0; i<recordLength; i++) {
			
			double sum = 0.0;
			
			// loop over all input waveform values at index i
			for (int j=0; j<numRecords; j++) {
				sum += waveforms[j*recordLength+i];
			}
			avgWaveform[i] = sum/numRecords;
		}
		
		return avgWaveform;
	}
	
	// no dialog is displayed for this plugin, but we use this method to get a reference to the PlugInFilterRunner
	public int showDialog(ImagePlus ip, String string, PlugInFilterRunner pfr)
	{
		this.pfr = pfr;
		
		return flags;
	}

	public void setNPasses(int i)
	{
	}
		
}
