package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import java.awt.AWTEvent;

/**
 * Computes the skewness of each input waveform and returns the result in a new
 * image. Each row in the input image is assumed to represent a single waveform.
 * The skewness of the {@code i}<SUP>th</SUP> waveform (row) and
 * {@code j}<SUP>th</SUP> slice is displayed in the {@code i}<SUP>th</SUP> row
 * and {@code j}<SUP>th</SUP> column of the output image. The skewness is
 * computed using a numerically stable algorithm for computing higher order
 * statistical moments described by <a href="http://people.xiph.org/~tterribe/notes/homs.html">Timothy B. Terriberry (2007)</a>.
 *
 * @author Jon N. Marsh
 * @version 2014-02-26
 */

public class Skewness implements ExtendedPlugInFilter, DialogListener
{
    private ImagePlus imp, resultImp;
	private ImageProcessor resultProcessor;
	private float[] resultPixels;
    private int width, height, stackSize, resultWidth;
	private String title;
	private GenericDialog gd;
	private static final String[] radioButtonLabels = {"sample skewness", "population skewness"};
	private static boolean isSampleSkewness = true;
    private PlugInFilterRunner pfr;
    private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + FINAL_PROCESSING;
	
    public int setup(String arg, ImagePlus imp) 
    {
        // perform final processing here
		if (arg.equals("final")) {
            if (resultImp != null) {
				resultImp.setTitle(title + " " + radioButtonLabels[isSampleSkewness ? 0 : 1]);
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
		
		resultImp = IJ.createImage("", "32-bit", resultWidth, height, 1);
		resultProcessor = resultImp.getProcessor();
		resultPixels = (float[])resultProcessor.getPixels();

        return flags;
    }	

	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
		this.pfr = pfr;
		
		gd = new GenericDialog("Skewness...");
		gd.addRadioButtonGroup("Output", radioButtonLabels, 2, 1, radioButtonLabels[isSampleSkewness ? 0 : 1]);
		gd.addDialogListener(this);
		gd.showDialog();
		
		if (gd.wasCanceled()) {
			return DONE;
		}
		
		return flags;
    }

	public boolean dialogItemChanged(GenericDialog gd, AWTEvent awte)
	{
		isSampleSkewness = gd.getNextRadioButton().equals(radioButtonLabels[0]);
		
		return true;
	}
	
    public void run(ImageProcessor ip) 
    {
        int currentSlice = pfr.getSliceNumber();
        float[] pixels = (float[])ip.getPixels();

		float[] kurtoses = execute(pixels, width, isSampleSkewness);
		for (int i=0; i<height; i++) {
			resultPixels[i*resultWidth+(currentSlice-1)] = kurtoses[i];
		}
    }

	/**
	 * Returns an array representing the skewness of each record in
	 * {@code waveforms}, where each record has {@code recordLength} elements.
	 * The skewness is computed using a numerically stable algorithm for computing 
	 * higher order statistical moments described by 
	 * <a href="http://people.xiph.org/~tterribe/notes/homs.html">Timothy B. Terriberry (2007)</a>.  
	 * Output is null if {@code waveforms==null}, {@code recordLength<=2},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}.
	 *
	 * @param waveforms        one-dimensional array composed of a series of
	 *                         concatenated records, each of size equal to
	 *                         {@code recordLength}
	 * @param recordLength     size of each record in {@code waveforms}
	 * @param isSampleSkewness set true to output sample skewness, false to
	 *                         output population skewness
	 * @return array of skewness values of input waveforms
	 */
	public static float[] execute(float[] waveforms, int recordLength, boolean isSampleSkewness)
	{
		if (waveforms != null && recordLength > 2 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			float[] skewnesses = new float[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// find skewness of current waveform
				skewnesses[i] = (float)skewness(waveforms, offset, offset+recordLength, isSampleSkewness);

			}
			
			return skewnesses;
				
		}
		
		return null;
	}
	
	/**
	 * Returns an array representing the skewness of each record in
	 * {@code waveforms}, where each record has {@code recordLength} elements.
	 * The skewness is computed using a numerically stable algorithm for computing 
	 * higher order statistical moments described by 
	 * <a href="http://people.xiph.org/~tterribe/notes/homs.html">Timothy B. Terriberry (2007)</a>.  
	 * Output is null if {@code waveforms==null}, {@code recordLength<=2},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}.
	 *
	 * @param waveforms        one-dimensional array composed of a series of
	 *                         concatenated records, each of size equal to
	 *                         {@code recordLength}
	 * @param recordLength     size of each record in {@code waveforms}
	 * @param isSampleSkewness set true to output sample skewness, false to
	 *                         output population skewness
	 * @return array of skewness values of input waveforms
	 */
	public static double[] execute(double[] waveforms, int recordLength, boolean isSampleSkewness)
	{
		if (waveforms != null && recordLength > 2 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			double[] skewnesses = new double[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// find skewness of current waveform
				skewnesses[i] = (float)skewness(waveforms, offset, offset+recordLength, isSampleSkewness);

			}
			
			return skewnesses;
				
		}
		
		return null;
	}
	
	private static double skewness(float[] a, int from, int to, boolean isSampleSkewness)
	{
		long n = 0;
		double mean = 0.0;
		double m2 = 0.0;
		double m3 = 0.0;

		for (int i = from; i < to; i++) {
			n++;
			double delta = a[i] - mean;
			double deltaOverN = delta / n;
			double term1 = delta * deltaOverN * (n-1);
			mean += deltaOverN;
			m3 += term1 * deltaOverN * (n - 2) - 3.0 * deltaOverN * m2;
			m2 += term1;
		}

		double output = (m3/n)/Math.pow(m2/n, 1.5);
		if (!isSampleSkewness) {
			output *= Math.sqrt(n*(n-1))/(n-2);
		}
		
		return output;
	}
	
	private static double skewness(double[] a, int from, int to, boolean isSampleSkewness)
	{
		long n = 0;
		double mean = 0.0;
		double m2 = 0.0;
		double m3 = 0.0;

		for (int i = from; i < to; i++) {
			n++;
			double delta = a[i] - mean;
			double deltaOverN = delta / n;
			double term1 = delta * deltaOverN * (n-1);
			mean += deltaOverN;
			m3 += term1 * deltaOverN * (n - 2) - 3.0 * deltaOverN * m2;
			m2 += term1;
		}

		double output = (m3/n)/Math.pow(m2/n, 1.5);
		if (!isSampleSkewness) {
			output *= Math.sqrt(n*(n-1))/(n-2);
		}
		
		return output;
	}
	
    public void setNPasses(int nPasses) {}

}
