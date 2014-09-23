package waveformAnalysisForImageJ;


import waveformAnalysisForImageJ.WaveformUtils;
import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import java.awt.AWTEvent;
import java.awt.TextField;
import java.util.Arrays;


/**
 * This plug-in filter moves a sliding gate along each waveform (horizontal
 * line) in an image, applies a symmetric window function to each gated segment
 * centered around the current index, and then returns the sum of the windowed
 * values as the new value at that index. The input data are overwritten by the
 * new computed values. The length of the moving window is equal to
 * {@code 2*radius+1}. At positions where portions of the moving window lie
 * outside the bounds of the waveform, the waveform values are mirrored about
 * the end points.
 *
 * @author Jon N. Marsh
 * @version 2014-01-03
 */

public class MovingWindowWeightedAverage implements ExtendedPlugInFilter, DialogListener
{
    private ImagePlus imp;
    private int width, height;
	private static int radius = 1;
	private static final String[] windowTypes = WaveformUtils.WindowType.stringValues();
	private static int windowChoice = WaveformUtils.WindowType.HAMMING.ordinal();
	private static double windowParameter = 0.5;
	private static TextField windowParameterTextField;
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
        gd = new GenericDialog("Moving Window Weighted Average...");
		gd.addNumericField("Radius", radius, 0);
		gd.addChoice("Weight function", windowTypes, windowTypes[windowChoice]);
		gd.addNumericField("Window parameter", windowParameter, 4);
		windowParameterTextField = (TextField)(gd.getNumericFields().get(1));
		windowParameterTextField.setEnabled(WaveformUtils.WindowType.values()[windowChoice].usesParameter());
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
		windowChoice = gd.getNextChoiceIndex();
		windowParameter = gd.getNextNumber();
		
		windowParameterTextField.setEnabled(WaveformUtils.WindowType.values()[windowChoice].usesParameter());
		
		return (radius >= 0 && !gd.invalidNumber());
    }

    public void run(ImageProcessor ip) 
    {
        float[] pixels = (float[])ip.getPixels();
		
		execute(pixels, width, radius, WaveformUtils.WindowType.values()[windowChoice], windowParameter);
    }

	/**
	 * Applies a moving window with weights specified by {@code windowType} to
	 * each record in {@code waveforms}, where each record is of length
	 * {@code recordLength}. The moving window is of length {@code 2*radius+1}.
	 * Output values are normalized by the sum of the window coefficients. Input
	 * waveforms are left unchanged if the array representing them is null,
	 * {@code 2*radius+1>recordLength}, {@code radius<0}, or
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * At positions where a part of the moving window lies outside the bounds of
	 * the waveform, the waveform values are reflected around the appropriate
	 * end point.
	 *
	 * @param waveforms	      one-dimensional array composed of a series of
	 *                        concatenated records, each of size equal to
	 *                        {@code recordLength}
	 * @param recordLength    size of each record in {@code waveforms}
	 * @param radius          length of two-sided window function is equal to
	 *                        {@code 2*radius+1}
	 * @param windowType      window function
	 * @param windowParameter used only for window functions that require it,
	 *                        ignored otherwise
	 */
	public static final void execute(float[] waveforms, int recordLength, int radius, WaveformUtils.WindowType windowType, double windowParameter)
	{
		int windowLength = 2*radius + 1;
		
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius >= 0) {
								
			// initialize single-sided window weight array (normalized)
			double[] weights = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParameter, true);
			
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
					
					// initialize running sum (at center of windowed segment)
					double sum = currentWaveformCopy[j]*weights[0];
					
					// finish computing the sum at the current index
					for (int k=-radius; k<0; k++) {
						int index = j+k;
						if (index < 0) {
							index = -index;
						}
						sum += weights[-k]*currentWaveformCopy[index];
					}
					for (int k=1; k<=radius; k++) {
						int index = j+k;
						if (index > recordLength-1) {
							index = 2*(recordLength-1) - index;
						}
						sum += weights[k]*currentWaveformCopy[index];
					}
					
					waveforms[offset+j] = (float)sum;
					
				}

			}
			
		}
		
	}
	
	/**
	 * Applies a moving window with weights specified by {@code windowType} to
	 * each record in {@code waveforms}, where each record is of length
	 * {@code recordLength}. The moving window is of length {@code 2*radius+1}.
	 * Output values are normalized by the sum of the window coefficients. Input
	 * waveforms are left unchanged if the array representing them is null,
	 * {@code 2*radius+1>recordLength}, {@code radius<0}, or
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * At positions where a part of the moving window lies outside the bounds of
	 * the waveform, the waveform values are reflected around the appropriate
	 * end point.
	 *
	 * @param waveforms	      one-dimensional array composed of a series of
	 *                        concatenated records, each of size equal to
	 *                        {@code recordLength}
	 * @param recordLength    size of each record in {@code waveforms}
	 * @param radius          length of two-sided window function is equal to
	 *                        {@code 2*radius+1}
	 * @param windowType      window function
	 * @param windowParameter used only for window functions that require it,
	 *                        ignored otherwise
	 */
	public static final void execute(double[] waveforms, int recordLength, int radius, WaveformUtils.WindowType windowType, double windowParameter)
	{
		int windowLength = 2*radius + 1;
		
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius >= 0) {
								
			// initialize single-sided window weight array (normalized)
			double[] weights = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParameter, true);
			
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
					
					// initialize running sum (at center of windowed segment)
					double sum = currentWaveformCopy[j]*weights[0];
					
					// finish computing the sum at the current index
					for (int k=-radius; k<0; k++) {
						int index = j+k;
						if (index < 0) {
							index = -index;
						}
						sum += weights[-k]*currentWaveformCopy[index];
					}
					for (int k=1; k<=radius; k++) {
						int index = j+k;
						if (index > recordLength-1) {
							index = 2*(recordLength-1) - index;
						}
						sum += weights[k]*currentWaveformCopy[index];
					}
					
					waveforms[offset+j] = sum;
					
				}

			}
			
		}
		
	}
		
    public void setNPasses(int nPasses) {}
	
}
