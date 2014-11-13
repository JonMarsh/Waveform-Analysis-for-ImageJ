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
 * This plug-in filter moves a sliding gate along each waveform (horizontal
 * line) in an image, computes the variance within the window, and then returns
 * the variance as the new value at that index. The input data are overwritten
 * by the new computed values. The length of the moving window is equal to
 * {@code 2*radius+1}. At positions where portions of the moving window lie
 * outside the bounds of the waveform, the waveform values are mirrored about
 * the end points.
 *
 * @author Jon N. Marsh
 * @version 2014-01-03
 */

public class MovingWindowVariance implements ExtendedPlugInFilter, DialogListener
{
    private ImagePlus imp;
    private int width, height;
	private static int radius = 1;
	private static boolean useUnbiasedEstimateOfVariance = false;
    private GenericDialog gd;
    private PlugInFilterRunner pfr;
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
        gd = new GenericDialog("Moving Window Variance...");
		gd.addNumericField("Radius", radius, 0);
		gd.addCheckbox("Use unbiased estimate of variance", useUnbiasedEstimateOfVariance);
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
		radius = (int)gd.getNextNumber();
		useUnbiasedEstimateOfVariance = gd.getNextBoolean();
		
		return (radius >= 0 && !gd.invalidNumber());
    }

    public void run(ImageProcessor ip) 
    {
        int currentSlice = pfr.getSliceNumber();
        float[] pixels = (float[])ip.getPixels();
		
		execute(pixels, width, radius, useUnbiasedEstimateOfVariance);
		
    }

	/**
	 * Applies a moving window to each record in {@code waveforms}, where each
	 * record is of length {@code recordLength}, computes the variance in the
	 * window, and replaces the value at the central point of the window with
	 * that variance. The moving window is of length {@code 2*radius+1}. Input
	 * waveforms are left unchanged if the array representing them is null,
	 * {@code 2*radius+1>recordLength}, {@code radius<=0}, or
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * At positions where a part of the moving window lies outside the bounds of
	 * the waveform, the waveform values are mirrored around the appropriate end
	 * point. This method is adapted from an algorithm described by D. H. D. West
	 * in Communications of the ACM, 22, 9, 532-535 (1979): "Updating Mean and
	 * Variance Estimates: An Improved Method."
	 *
	 * @param waveforms	                    one-dimensional array composed of a
	 *                                      series of concatenated records, each
	 *                                      of size equal to
	 *                                      {@code recordLength}
	 * @param recordLength                  size of each record in
	 *                                      {@code waveforms}
	 * @param radius                        radius of moving window; length of two-sided window function
	 *                                      is equal to {@code 2*radius+1}
	 * @param useUnbiasedEstimateOfVariance if checked, the unbiased estimate of
	 *                                      variance is returned (i.e. use
	 *                                      {@code n-1} in the denominator of
	 *                                      the computation; if unchecked,
	 *                                      {@code n} is used)
	 */
	public static final void execute(float[] waveforms, int recordLength, int radius, boolean useUnbiasedEstimateOfVariance)
	{
		int windowLength = 2*radius + 1;
		
		double norm = useUnbiasedEstimateOfVariance ? 1.0/(windowLength-1) : 1.0/windowLength;
		
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius > 0) {
								
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// initialize double-precision copy of current waveform
				double[] currentWaveformCopy = new double[recordLength];
				for (int j=0; j<recordLength; j++) {
					currentWaveformCopy[j] = waveforms[offset+j];
				}
				
				// move window and compute means
				for (int j=0; j<recordLength; j++) {
					
					// initialize variables at center of windowed segment
					long n = 1;
					double mean = currentWaveformCopy[j];
					double m2 = 0.0;
					double delta;
					
					// finish computations at the current index
					for (int k=-radius; k<0; k++) {
						int index = j+k;
						if (index < 0) {
							index = -index;
						}
						n += 1;
						delta = currentWaveformCopy[index] - mean;
						mean += delta/n;
						m2 += delta*(currentWaveformCopy[index] - mean);
					}
					for (int k=1; k<=radius; k++) {
						int index = j+k;
						if (index > recordLength-1) {
							index = 2*(recordLength-1) - index;
						}
						n += 1;
						delta = currentWaveformCopy[index] - mean;
						mean += delta/n;
						m2 += delta*(currentWaveformCopy[index] - mean);
					}
					
					waveforms[offset+j] = (float)(m2*norm);
					
				}

			}
			
		}
		
	}
	
	/**
	 * Applies a moving window to each record in {@code waveforms}, where each
	 * record is of length {@code recordLength}, computes the variance in the
	 * window, and replaces the value at the central point of the window with
	 * that variance. The moving window is of length {@code 2*radius+1}. Input
	 * waveforms are left unchanged if the array representing them is null,
	 * {@code 2*radius+1>recordLength}, {@code radius<=0}, or
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * At positions where a part of the moving window lies outside the bounds of
	 * the waveform, the waveform values are mirrored around the appropriate end
	 * point. This method is adapted from an algorithm described by D. H. D. West
	 * in Communications of the ACM, 22, 9, 532-535 (1979): "Updating Mean and
	 * Variance Estimates: An Improved Method."
	 *
	 * @param waveforms	                    one-dimensional array composed of a
	 *                                      series of concatenated records, each
	 *                                      of size equal to
	 *                                      {@code recordLength}
	 * @param recordLength                  size of each record in
	 *                                      {@code waveforms}
	 * @param radius                        radius of moving window; length of two-sided window function
	 *                                      is equal to {@code 2*radius+1}
	 * @param useUnbiasedEstimateOfVariance if checked, the unbiased estimate of
	 *                                      variance is returned (i.e. use
	 *                                      {@code n-1} in the denominator of
	 *                                      the computation; if unchecked,
	 *                                      {@code n} is used)
	 */
	public static final void execute(double[] waveforms, int recordLength, int radius, boolean useUnbiasedEstimateOfVariance)
	{
		int windowLength = 2*radius + 1;
		
		double norm = useUnbiasedEstimateOfVariance ? 1.0/(windowLength-1) : 1.0/windowLength;
		
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius > 0) {
								
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// initialize double-precision copy of current waveform
				double[] currentWaveformCopy = Arrays.copyOfRange(waveforms, offset, offset+recordLength);
				
				// move window and compute means
				for (int j=0; j<recordLength; j++) {
					
					// initialize variables at center of windowed segment
					long n = 1;
					double mean = currentWaveformCopy[j];
					double m2 = 0.0;
					double delta;
					
					// finish computations at the current index
					for (int k=-radius; k<0; k++) {
						int index = j+k;
						if (index < 0) {
							index = -index;
						}
						n += 1;
						delta = currentWaveformCopy[index] - mean;
						mean += delta/n;
						m2 += delta*(currentWaveformCopy[index] - mean);
					}
					for (int k=1; k<=radius; k++) {
						int index = j+k;
						if (index > recordLength-1) {
							index = 2*(recordLength-1) - index;
						}
						n += 1;
						delta = currentWaveformCopy[index] - mean;
						mean += delta/n;
						m2 += delta*(currentWaveformCopy[index] - mean);
					}
					
					waveforms[offset+j] = m2*norm;
					
				}

			}
			
		}
		
	}

		
    public void setNPasses(int nPasses) {}
	
}
