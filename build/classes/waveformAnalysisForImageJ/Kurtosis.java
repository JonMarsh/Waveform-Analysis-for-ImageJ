package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;

/**
 * Computes the kurtosis of each input waveform and returns the result in a new
 * image. Each row in the input image is assumed to represent a single waveform.
 * The kurtosis of the {@code i}<SUP>th</SUP> waveform (row) and
 * {@code j}<SUP>th</SUP> slice is displayed in the {@code i}<SUP>th</SUP> row
 * and {@code j}<SUP>th</SUP> column of the output image. The kurtosis is 
 * computed using a numerically stable algorithm for computing higher order statistical moments described by 
 * <a href="http://people.xiph.org/~tterribe/notes/homs.html">Timothy B. Terriberry (2007)</a>. 
 * The returned values technically represent the excess kurtosis of the input data; i.e., a 
 * normally-distributed dataset should have approximately zero excess kurtosis.
 *
 * @author Jon N. Marsh
 * @version 2014-01-27
 */

public class Kurtosis implements ExtendedPlugInFilter
{
    private ImagePlus imp, resultImp;
	private ImageProcessor resultProcessor;
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
		resultTitle = title + " kurtosis";
		
		resultImp = IJ.createImage(resultTitle, "32-bit", resultWidth, height, 1);
		resultProcessor = resultImp.getProcessor();
		resultPixels = (float[])resultProcessor.getPixels();

        return flags;
    }	

    // No dialog needed for this plugin, but this method allows access to the PlugInFilterRunner
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
		this.pfr = pfr;
		
		return flags;
    }

    public void run(ImageProcessor ip) 
    {
        int currentSlice = pfr.getSliceNumber();
        float[] pixels = (float[])ip.getPixels();

		float[] kurtoses = execute(pixels, width);
		for (int i=0; i<height; i++) {
			resultPixels[i*resultWidth+(currentSlice-1)] = kurtoses[i];
		}
    }

	/**
	 * Returns an array representing the kurtosis of each record in
	 * {@code waveforms}, where each record has {@code recordLength} elements.
	 * The kurtosis is computed using a numerically stable algorithm for computing 
	 * higher order statistical moments described by 
	 * <a href="http://people.xiph.org/~tterribe/notes/homs.html">Timothy B. Terriberry (2007)</a>.  
	 * Output is null if {@code waveforms==null}, {@code recordLength<=1},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}.
	 *
	 * @param waveforms    one-dimensional array composed of a series of
	 *                     concatenated records, each of size equal to
	 *                     {@code recordLength}
	 * @param recordLength size of each record in {@code waveforms}
	 * @return array of kurtosis values of input waveforms
	 */
	public static float[] execute(float[] waveforms, int recordLength)
	{
		if (waveforms != null && recordLength > 1 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			float[] kurtoses = new float[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// find kurtosis of current waveform
				kurtoses[i] = (float)kurtosis(waveforms, offset, offset+recordLength);

			}
			
			return kurtoses;
				
		}
		
		return null;
	}
	
	/**
	 * Returns an array representing the kurtosis of each record in
	 * {@code waveforms}, where each record has {@code recordLength} elements.
	 * The kurtosis is computed using a numerically stable algorithm for computing 
	 * higher order statistical moments described by 
	 * <a href="http://people.xiph.org/~tterribe/notes/homs.html">Timothy B. Terriberry (2007)</a>.  
	 * Output is null if {@code waveforms==null}, {@code recordLength<=1},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}.
	 *
	 * @param waveforms    one-dimensional array composed of a series of
	 *                     concatenated records, each of size equal to
	 *                     {@code recordLength}
	 * @param recordLength size of each record in {@code waveforms}
	 * @return array of kurtosis values of input waveforms
	 */
	public static double[] execute(double[] waveforms, int recordLength)
	{
		if (waveforms != null && recordLength > 1 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			double[] kurtoses = new double[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// find kurtosis of current waveform
				kurtoses[i] = kurtosis(waveforms, offset, offset+recordLength);

			}
			
			return kurtoses;
				
		}
		
		return null;
	}
	
	private static double kurtosis(float[] a, int from, int to)
	{
		long n = 0;
		double mean = 0.0;
		double m2 = 0.0;
		double m3 = 0.0;
		double m4 = 0.0;

		for (int i = from; i < to; i++) {
			n++;
			double delta = a[i] - mean;
			double deltaOverN = delta / n;
			double deltaOverNSquared = deltaOverN * deltaOverN;
			double term1 = delta * deltaOverN * (n-1);
			mean += deltaOverN;
			m4 += term1 * deltaOverNSquared * (n*n - 3*n + 3.0) + 6.0 * deltaOverNSquared * m2 - 4.0 * deltaOverN * m3;
			m3 += term1 * deltaOverN * (n - 2) - 3.0 * deltaOverN * m2;
			m2 += term1;
		}

		return (((n*m4) / (m2*m2)) - 3.0);
	}
	
	private static double kurtosis(double[] a, int from, int to)
	{
		long n = 0;
		double mean = 0.0;
		double m2 = 0.0;
		double m3 = 0.0;
		double m4 = 0.0;

		for (int i = from; i < to; i++) {
			n++;
			double delta = a[i] - mean;
			double deltaOverN = delta / n;
			double deltaOverNSquared = deltaOverN * deltaOverN;
			double term1 = delta * deltaOverN * (n-1);
			mean += deltaOverN;
			m4 += term1 * deltaOverNSquared * (n*n - 3*n + 3.0) + 6.0 * deltaOverNSquared * m2 - 4.0 * deltaOverN * m3;
			m3 += term1 * deltaOverN * (n - 2) - 3.0 * deltaOverN * m2;
			m2 += term1;
		}

		return (((n*m4) / (m2*m2)) - 3.0);
	}
	
    public void setNPasses(int nPasses) {}
	
}
