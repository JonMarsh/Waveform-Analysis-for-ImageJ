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
 * This plugin applies a binomial filter (i.e., convolution with a [1, 2, 1]
 * kernel) to each waveform (row) of the input image the specified number of
 * times; the output is normalized by the sum of the coefficients. Note that
 * applying this filter {@code n} times is equivalent to convolving with a kernel
 * with weights that can be read from the {@code (2*n+1)}<sup>th</sup> row of
 * Pascal's triangle. The filter is acausal, thus no time-shifting occurs. At
 * record endpoints where the kernel multiplication requires elements lying
 * outside the original record, the missing values are "mirrored"; i.e., the
 * result of the kernel being applied at element {@code 0} of waveform
 * {@code a[]} is given by {@code
 * 0.25*(a[-1]+2*a[0]+a[1]) = 0.25*(a[1]+2*a[0]+a[1])}.
  * 
 * @author Jon N. Marsh
 * @version 2013-11-14
 */

public class BinomialFilter implements ExtendedPlugInFilter, DialogListener
{
    private ImagePlus imp;
    private int width;
    private GenericDialog gd;
    private PlugInFilterRunner pfr;
	private static int nPasses = 1;
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

        return flags;
    }	

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
        this.pfr = pfr;
        gd = new GenericDialog("Binomial Filter...");
		gd.addNumericField("Number of passes", nPasses, 0);
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
        nPasses = (int)gd.getNextNumber();
		
		return (nPasses >= 0 && !gd.invalidNumber());
    }

    public void run(ImageProcessor ip) 
    {
        float[] pixels = (float[])ip.getPixels();
		
		execute(pixels, width, nPasses);
    }

	/**
	 * Applies a binomial filter (i.e., a [1, 2, 1] kernel) to each record in 
	 * {@code waveforms} the specified number of times, where each record is of 
	 * size {@code recordLength}. Output values are normalized by the sum of the 
	 * coefficients.  Input array is left unchanged if it is null, {@code recordLength<=2}, 
	 * {@code nPasses<=0}, or {@code waveforms.length} is not evenly divisible
	 * by {@code recordLength}.
	 * 
	 * @param waveforms		one-dimensional array composed of a series of concatenated 
	 *						records, each of size equal to {@code recordLength}
	 * @param recordLength	size of each record in {@code waveforms}
	 * @param nPasses		number of filter passes (larger numbers yield more smoothing)
	 */
	public static void execute(float[] waveforms, int recordLength, int nPasses)
	{
		if (waveforms != null && recordLength > 2 && nPasses > 0 && waveforms.length%recordLength == 0) {
						
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// initialize double-precision copy of current record for temporary computations with better accuracy
				double[] currentWaveform = new double[recordLength];
				for (int k=0; k<recordLength; k++) {
					currentWaveform[k] = waveforms[offset+k];
				}
				
				// perform specified number of passes on current record
				for (int passNumber=0; passNumber<nPasses; passNumber++) {
					
					// temporary variables for first and last filtered values
					double firstValue = 0.5*(currentWaveform[0] + currentWaveform[1]);
					double lastValue = 0.5*(currentWaveform[recordLength-2] + currentWaveform[recordLength-1]);
					
					// more temporary variables for next loop
					double previousValue = currentWaveform[0];
					double currentValue = currentWaveform[1];
					
					// loop over all values of current waveform except for ends
					for (int j=1; j<recordLength-1; j++) {
						double filteredValue = 0.25*(previousValue + currentValue + currentValue + currentWaveform[j+1]);
						previousValue = currentValue;
						currentValue = currentWaveform[j+1];
						currentWaveform[j] = filteredValue;
					}
					
					// take care of endpoints
					currentWaveform[0] = firstValue;
					currentWaveform[recordLength-1] = lastValue;
					
				}
				
				// copy results back into input array
				for (int j=0; j<recordLength; j++) {
					waveforms[offset+j] = (float)currentWaveform[j];
				}
					
			}
			
		}
		
	}
	
	/**
	 * Applies a binomial filter (i.e., a [1, 2, 1] kernel) to each record in 
	 * {@code waveforms} the specified number of times, where each record is of 
	 * size {@code recordLength}. Output values are normalized by the sum of the 
	 * coefficients.  Input array is left unchanged if it is null, {@code recordLength<=2}, 
	 * {@code nPasses<=0}, or {@code waveforms.length} is not evenly divisible
	 * by zero.
	 * 
	 * @param waveforms		one-dimensional array composed of a series of concatenated 
	 *						records, each of size equal to {@code recordLength}
	 * @param recordLength	size of each record in {@code waveforms}
	 * @param nPasses		number of filter passes (larger numbers yield more smoothing)
	 */
	public static void execute(double[] waveforms, int recordLength, int nPasses)
	{
		if (waveforms != null && recordLength > 2 && nPasses > 0 && waveforms.length%recordLength == 0) {
						
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// perform specified number of passes on current record
				for (int passNumber=0; passNumber<nPasses; passNumber++) {
					
					// temporary variables for first and last filtered values
					double firstValue = 0.5*(waveforms[offset] + waveforms[offset+1]);
					double lastValue = 0.5*(waveforms[offset+recordLength-2] + waveforms[offset+recordLength-1]);
					
					// more temporary variables for next loop
					double previousValue = waveforms[offset];
					double currentValue = waveforms[offset+1];
					
					// loop over all values of current waveform except for ends
					for (int j=1; j<recordLength-1; j++) {
						double filteredValue = 0.25*(previousValue + currentValue + currentValue + waveforms[offset+j+1]);
						previousValue = currentValue;
						currentValue = waveforms[offset+j+1];
						waveforms[offset+j] = filteredValue;
					}
					
					// take care of endpoints
					waveforms[offset] = firstValue;
					waveforms[offset+recordLength-1] = lastValue;
					
				}
				
			}
			
		}
		
	}
	
    public void setNPasses(int nPasses) {}
	
}
