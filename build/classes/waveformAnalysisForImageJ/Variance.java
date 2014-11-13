package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;

/**
 * Computes the variance of each input waveform and returns the result in a new
 * image. Each row in the input image is assumed to represent a single waveform.
 * The variance of the {@code i}<SUP>th</SUP> waveform (row) and
 * {@code j}<SUP>th</SUP> slice is displayed in the {@code i}<SUP>th</SUP> row
 * and {@code j}<SUP>th</SUP> column of the output image. The variance is
 * computed using a numerically stable algorithm described by 
 * <a href="http://www.jstor.org/stable/1266577">Welford</a>. The user 
 * can choose to output either the biased or unbiased estimate of variance.
 *
 * @author Jon N. Marsh
 * @version 2014-01-27
 */

public class Variance implements ExtendedPlugInFilter
{
    private ImagePlus imp, resultImp;
	private ImageProcessor resultProcessor;
	private static boolean useUnbiasedEstimate = false;
	private GenericDialog gd;
	private float[] resultPixels;
    private int width, height, stackSize, resultWidth;
	private String title, resultTitle;
    private PlugInFilterRunner pfr;
    private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + FINAL_PROCESSING;
	
    public int setup(String arg, ImagePlus imp) 
    {
        // perform final processing here
		if (arg.equals("final")) {
            if (resultImp != null) {
				resultImp.show();
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
		resultWidth = stackSize;
		resultTitle = title + " variance";
		
		resultImp = IJ.createImage(resultTitle, "32-bit", resultWidth, height, 1);
		resultProcessor = resultImp.getProcessor();
		resultPixels = (float[])resultProcessor.getPixels();

        return flags;
    }	

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
		this.pfr = pfr;
		
		gd = new GenericDialog("Variance...");
		gd.addCheckbox("Use unbiased estimate", useUnbiasedEstimate);
		gd.showDialog();
		
		if (gd.wasCanceled()) {
			return DONE;
		}
		
		useUnbiasedEstimate = gd.getNextBoolean();

		return flags;
    }

    public void run(ImageProcessor ip) 
    {
        int currentSlice = pfr.getSliceNumber();
        float[] pixels = (float[])ip.getPixels();

		float[] variances = execute(pixels, width, useUnbiasedEstimate);
		for (int i=0; i<height; i++) {
			resultPixels[i*resultWidth+(currentSlice-1)] = variances[i];
		}
    }

	/**
	 * Returns an array representing the variance of each record in
	 * {@code waveforms}, where each record has {@code recordLength} elements.
	 * The variance is computed using a numerically stable algorithm described by 
	 * <a href="http://www.jstor.org/stable/1266577">Welford</a>. 
	 * Output is null if {@code waveforms==null}, {@code recordLength<=1},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}.
	 *
	 * @param waveforms           one-dimensional array composed of a series of
	 *                            concatenated records, each of size equal to
	 *                            {@code recordLength}
	 * @param recordLength        size of each record in {@code waveforms}
	 * @param useUnbiasedEstimate set to true to output an unbiased estimate of
	 *                            variance
	 * @return array of variances of input waveforms
	 */
	public static float[] execute(float[] waveforms, int recordLength, boolean useUnbiasedEstimate)
	{
		if (waveforms != null && recordLength > 1 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			float[] meanValues = new float[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// find variance of current waveform
				meanValues[i] = (float)(WaveformUtils.meanAndVariance(waveforms, useUnbiasedEstimate, offset, offset+recordLength)[1]);

			}
			
			return meanValues;
				
		}
		
		return null;
	}
	
	/**
	 * Returns an array representing the variance of each record in
	 * {@code waveforms}, where each record has {@code recordLength} elements.
	 * The variance is computed using a numerically stable algorithm described by 
	 * <a href="http://www.jstor.org/stable/1266577">Welford</a>. 
	 * Output is null if {@code waveforms==null}, {@code recordLength<=1},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}.
	 *
	 * @param waveforms           one-dimensional array composed of a series of
	 *                            concatenated records, each of size equal to
	 *                            {@code recordLength}
	 * @param recordLength        size of each record in {@code waveforms}
	 * @param useUnbiasedEstimate set to true to output an unbiased estimate of
	 *                            variance
	 * @return array of variances of input waveforms
	 */
	public static double[] execute(double[] waveforms, int recordLength, boolean useUnbiasedEstimate)
	{
		if (waveforms != null && recordLength > 1 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			double[] meanValues = new double[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// find variance of current waveform
				meanValues[i] = WaveformUtils.meanAndVariance(waveforms, useUnbiasedEstimate, offset, offset+recordLength)[1];

			}
			
			return meanValues;
				
		}
		
		return null;
	}
	
    public void setNPasses(int nPasses) {}
	
}
