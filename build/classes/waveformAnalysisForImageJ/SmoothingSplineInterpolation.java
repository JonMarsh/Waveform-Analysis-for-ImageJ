package waveformAnalysisForImageJ;

import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.process.*;
import java.awt.*;

/**
 * Computes interpolated smoothing spline for each row in the input image using 
 * the method described by Reinsch in Numerische Mathematik 10, 177-183 (1967).  
 * The spline is a natural spline, e.g. the second derivative is zero at the 
 * endpoints. The smoothing spline reverts to a simple cubic spline that passes 
 * through every original data point when the smoothing parameter is zero. For 
 * smoothing splines, setting the smoothing parameter to {@code 1.0} is usually 
 * a good choice. Very large values for the smoothing parameter yield a 
 * straight-line fit to the input data. Also input is the standard deviation,
 * typically a measure of the system noise or the variation in the signal that 
 * should be "smoothed out" by the smoothing spline algorithm.  Each record in 
 * the input image is assumed to have uniform spacing between elements.  Internal 
 * computations are performed with double precision.  The results are written 
 * over the original values.
 * 
 * @author Jon N. Marsh
 * @version 2013-11-18
 */

public class SmoothingSplineInterpolation implements ExtendedPlugInFilter, DialogListener
{
    private ImagePlus imp, interpolatedImp;
	private ImageStack stack, interpolatedStack;
    private int width, height, stackSize;
	private String title, interpolatedTitle;
	private static double smoothingParameter = 1.0;
	private static double stdev = 0.0;
	private static int interpolatedWidth = 2048;
    private GenericDialog gd;
    private PlugInFilterRunner pfr;
    private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + KEEP_PREVIEW + FINAL_PROCESSING;
	
    public int setup(String arg, ImagePlus imp) 
    {
        if (arg.equals("final")) {
            if (interpolatedImp != null) {
				imp.setStack(interpolatedTitle, interpolatedStack);
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
		stack = imp.getStack();
		stackSize = imp.getStackSize();
		title = imp.getTitle();

        return flags;
    }	

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
        this.pfr = pfr;
        gd = new GenericDialog("Smoothing Spline Interpolation...");
		gd.addNumericField("Smoothed waveform length", interpolatedWidth, 0, 6, "points");
		gd.addNumericField("Smoothing parameter value", smoothingParameter, 1, 6, "");
		gd.addNumericField("Estimated standard deviation", stdev, 4, 6, "");
		gd.addDialogListener(this);
		
        gd.showDialog();
        if (gd.wasCanceled()) {
            return DONE;
        }

		interpolatedTitle = title + (smoothingParameter>0 ? (", S="+smoothingParameter+", stdev="+stdev) : "");
		interpolatedImp = IJ.createImage(interpolatedTitle, "32-bit", interpolatedWidth, height, stackSize);
		interpolatedStack = interpolatedImp.getStack();
		
        return flags;
    }
	
    public boolean dialogItemChanged(GenericDialog gd, AWTEvent e)
    {
        interpolatedWidth = (int)gd.getNextNumber();
		smoothingParameter = gd.getNextNumber();
		stdev = gd.getNextNumber();
		
		return (stdev >= 0.0 && smoothingParameter >= 0.0 && interpolatedWidth >= 3);
    }

    public void run(ImageProcessor ip) 
    {
        int currentSlice = pfr.getSliceNumber();
        float[] pixels = (float[])ip.getPixels();
		ImageProcessor interpolatedProcessor = interpolatedStack.getProcessor(currentSlice);
		float[] interpolatedPixels = (float[])interpolatedProcessor.getPixels();
		
		float[] newPixels = execute(pixels, width, interpolatedWidth, smoothingParameter, stdev);

		System.arraycopy(newPixels, 0, interpolatedPixels, 0, interpolatedPixels.length);
    }

	/**
	 * Computes interpolated smoothing spline for each record in {@code y} using 
	 * the method described by Reinsch in Numerische Mathematik 10, 177-183 
	 * (1967). {@code waveforms} is a one-dimensional array composed of a series 
	 * of concatenated records, each of length {@code recordLength}. The spline 
	 * is a natural spline, e.g. the second derivative is zero at the endpoints. 
	 * The smoothing spline reverts to a simple cubic spline that passes through 
	 * every original data point when {@code smoothingParameter=0}. For 
	 * smoothing splines, setting {@code smoothingParameter=1.0} is usually a 
	 * good choice. Very large values for {@code smoothingParameter} yield a 
	 * straight-line fit to the input data. {@code standardDeviation} is 
	 * typically a measure of the standard deviation of the system noise or the 
	 * variation of the part of the signal that should be "smoothed out" by the 
	 * smoothing spline algorithm. Each record in {@code waveforms} is assumed 
	 * to have uniform spacing between elements. Internal computations are 
	 * performed with double precision. A null value is returned if 
	 * {@code waveforms} is {@code null}, {@code recordLength<=3}, 
	 * {@code waveforms.length<recordLength}, or {@code waveform.length} is not 
	 * evenly divisible by {@code recordLength}.  No error checking is performed 
	 * on range limits; if the values are negative or outside the range of the 
	 * array, a runtime exception may be thrown.
	 * 
	 * @param waveforms
	 * @param recordLength
	 * @param interpolatedRecordLength
	 * @param smoothingParameter
	 * @param stdev
	 * @return
	 */
	public static final float[] execute(float[] waveforms, 
										int recordLength, 
										int interpolatedRecordLength, 
										double smoothingParameter, 
										double stdev)
	{
		if (waveforms == null || recordLength <= 3 || waveforms.length < recordLength || waveforms.length%recordLength != 0) {
			return null;
		}

		// compute number of records
		int numRecords = waveforms.length/recordLength;
		
		// initialize output array
		float[] interpolatedWaveforms = new float[numRecords*interpolatedRecordLength];
		
		// peform computations on a row-by-row basis
		for (int i=0; i<numRecords; i++) {

			// offset to current record
			int offset1 = i*recordLength;
			int offset2 = i*interpolatedRecordLength;

			// compute interpolant coefficients
			double[][] coeffs = WaveformUtils.smoothingSplineInterpolantUniformSpacing(waveforms, offset1, offset1+recordLength, stdev, 1.0, smoothingParameter);
			
			// compute interpolated values for the rest of this row
			double dx = ((double)(recordLength-1))/((double)(interpolatedRecordLength-1));
			
			for (int j=0; j<interpolatedRecordLength; j++) {
				double xx = j*dx;
				int k = (int)Math.floor(xx);
				double h = xx - k;
				interpolatedWaveforms[offset2+j] = (float)(coeffs[0][k] + h*(coeffs[1][k] + h*(coeffs[2][k] + h*coeffs[3][k])));
			}
			
		}
		
		return interpolatedWaveforms;
	}
	
	/**
	 * Computes interpolated smoothing spline for each record in {@code y} using 
	 * the method described by Reinsch in Numerische Mathematik 10, 177-183 
	 * (1967). {@code waveforms} is a one-dimensional array composed of a series 
	 * of concatenated records, each of length {@code recordLength}. The spline 
	 * is a natural spline, e.g. the second derivative is zero at the endpoints. 
	 * The smoothing spline reverts to a simple cubic spline that passes through 
	 * every original data point when {@code smoothingParameter=0}. For 
	 * smoothing splines, setting {@code smoothingParameter=1.0} is usually a 
	 * good choice. Very large values for {@code smoothingParameter} yield a 
	 * straight-line fit to the input data. {@code standardDeviation} is 
	 * typically a measure of the standard deviation of the system noise or the 
	 * variation of the part of the signal that should be "smoothed out" by the 
	 * smoothing spline algorithm. Each record in {@code waveforms} is assumed 
	 * to have uniform spacing between elements. Internal computations are 
	 * performed with double precision. A null value is returned if 
	 * {@code waveforms} is {@code null}, {@code recordLength<=3}, 
	 * {@code waveforms.length<recordLength}, or {@code waveform.length} is not 
	 * evenly divisible by {@code recordLength}.  No error checking is performed 
	 * on range limits; if the values are negative or outside the range of the 
	 * array, a runtime exception may be thrown.
	 * 
	 * @param waveforms
	 * @param recordLength
	 * @param interpolatedRecordLength
	 * @param smoothingParameter
	 * @param stdev
	 * @return
	 */
	public static final double[] execute(double[] waveforms, 
										int recordLength, 
										int interpolatedRecordLength, 
										double smoothingParameter, 
										double stdev)
	{
		if (waveforms == null || recordLength <= 3 || waveforms.length < recordLength || waveforms.length%recordLength != 0) {
			return null;
		}

		// compute number of records
		int numRecords = waveforms.length/recordLength;
		
		// initialize output array
		double[] interpolatedWaveforms = new double[numRecords*interpolatedRecordLength];
		
		// peform computations on a row-by-row basis
		for (int i=0; i<numRecords; i++) {

			// offset to current record
			int offset1 = i*recordLength;
			int offset2 = i*interpolatedRecordLength;

			// compute interpolant coefficients
			double[][] coeffs = WaveformUtils.smoothingSplineInterpolantUniformSpacing(waveforms, offset1, offset1+recordLength, stdev, 1.0, smoothingParameter);
			
			// compute interpolated values for the rest of this row
			double dx = ((double)(recordLength-1))/((double)(interpolatedRecordLength-1));
			
			for (int j=0; j<interpolatedRecordLength; j++) {
				double xx = j*dx;
				int k = (int)Math.floor(xx);
				double h = xx - k;
				interpolatedWaveforms[offset2+j] = (coeffs[0][k] + h*(coeffs[1][k] + h*(coeffs[2][k] + h*coeffs[3][k])));
			}
			
		}
		
		return interpolatedWaveforms;
	}
	
	public void setNPasses(int nPasses) {}
	
}
