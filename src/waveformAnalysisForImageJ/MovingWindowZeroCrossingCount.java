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
 * line) in an image, counts the number of zero-crossings in each gated segment,
 * and then returns the count as the new value at that index. The input data are
 * overwritten by the new computed values. The length of the moving window is
 * equal to {@code 2*radius+1}. At positions where portions of the moving window
 * lie outside the bounds of the waveform, the waveform values are mirrored
 * about the end points.
 *
 * @author Jon N. Marsh
 * @version 2014-10-10
 */

public class MovingWindowZeroCrossingCount implements ExtendedPlugInFilter, DialogListener
{
    private ImagePlus imp;
    private int width, height;
	private static int radius = 1;
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
        gd = new GenericDialog("Moving Window Zero Crossings...");
		gd.addNumericField("Radius", radius, 0);
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
		
		return (radius >= 0 && !gd.invalidNumber());
    }

    public void run(ImageProcessor ip) 
    {
        float[] pixels = (float[])ip.getPixels();
		
		execute(pixels, width, radius);
    }

	/**
	 * Applies a moving window to each record in {@code waveforms}, where each
	 * record is of length {@code recordLength}, counts the number of
	 * zero-crossings in the window, and replaces the value at the central point
	 * of the window with the count. The moving window is of length
	 * {@code 2*radius+1}. Input waveforms are left unchanged if the array
	 * representing them is null, {@code 2*radius+1>recordLength},
	 * {@code radius<0}, or {@code waveforms.length} is not evenly divisible by
	 * {@code recordLength}. At positions where a part of the moving window lies
	 * outside the bounds of the waveform, the waveform values are reflected
	 * around the appropriate end point.
	 *
	 * @param waveforms	   one-dimensional array composed of a series of
	 *                     concatenated records, each of size equal to
	 *                     {@code recordLength}
	 * @param recordLength size of each record in {@code waveforms}
	 * @param radius       length of two-sided window function is equal to
	 *                     {@code 2*radius+1}
	 */
	public static final void execute(float[] waveforms, int recordLength, int radius)
	{
		int windowLength = 2*radius + 1;
		
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius >= 0) {
								
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// initialize copy of current waveform
				float[] currentWaveformCopy = Arrays.copyOfRange(waveforms, offset, offset+recordLength);
				
				// initialize zero-crossing count when window is centered at array index 0
				int count = 0;
				int index;
				for (int j = -radius; j < radius; j++) {
					index = j;
					int index1 = j+1;
					if (index < 0) {
						index = -index;
						if (index1 < 0) {
							index1 = -index1;
						}
					}
					if (currentWaveformCopy[index] * currentWaveformCopy[index1] < 0.0f) {
						count++;
					}
				}
				waveforms[offset] =  count;
				float lastValue = currentWaveformCopy[radius];
				float firstValue = lastValue; // reflect around boundary point
				
				// move window and count zero-crossings
				for (int j=1; j<recordLength; j++) {
					
					// check if window moved past a zero crossing on the left
					index = j-radius;
					if (index < 0) {
						index = -index;
					}
					float newFirstValue = currentWaveformCopy[index];
					if (firstValue*newFirstValue < 0.0f) {
						count--;
					}
					firstValue = newFirstValue;
					
					// check if window moved over a new zero crossing on the right
					index = j+radius;
					if (index > recordLength-1) {
						index = 2*(recordLength-1) - index;
					}
					float newLastValue = currentWaveformCopy[index];
					if (lastValue*newLastValue < 0.0f) {
						count++;
					}
					lastValue = newLastValue;
					
					waveforms[offset+j] = count;
					
				}

			}
			
		}
		
	}
	
	/**
	 * Applies a moving window to each record in {@code waveforms}, where each
	 * record is of length {@code recordLength}, counts the number of
	 * zero-crossings in the window, and replaces the value at the central point
	 * of the window with the count. The moving window is of length
	 * {@code 2*radius+1}. Input waveforms are left unchanged if the array
	 * representing them is null, {@code 2*radius+1>recordLength},
	 * {@code radius<0}, or {@code waveforms.length} is not evenly divisible by
	 * {@code recordLength}. At positions where a part of the moving window lies
	 * outside the bounds of the waveform, the waveform values are reflected
	 * around the appropriate end point.
	 *
	 * @param waveforms	   one-dimensional array composed of a series of
	 *                     concatenated records, each of size equal to
	 *                     {@code recordLength}
	 * @param recordLength size of each record in {@code waveforms}
	 * @param radius       length of two-sided window function is equal to
	 *                     {@code 2*radius+1}
	 */
	public static final void execute(double[] waveforms, int recordLength, int radius)
	{
		int windowLength = 2*radius + 1;
		
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius >= 0) {
								
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// initialize copy of current waveform
				double[] currentWaveformCopy = new double[recordLength];
				for (int j=0; j<recordLength; j++) {
					currentWaveformCopy[j] = waveforms[offset+j];
				}
				
				// initialize zero-crossing count when window is centered at array index 0
				int count = 0;
				int index;
				for (int j = -radius; j < radius; j++) {
					index = j;
					int index1 = j+1;
					if (index < 0) {
						index = -index;
						if (index1 < 0) {
							index1 = -index1;
						}
					}
					if (currentWaveformCopy[index] * currentWaveformCopy[index1] < 0.0) {
						count++;
					}
				}
				waveforms[offset] =  count;
				double lastValue = currentWaveformCopy[radius];
				double firstValue = lastValue; // reflect around boundary point
				
				// move window and count zero-crossings
				for (int j=1; j<recordLength; j++) {
					
					// check if window moved past a zero crossing on the left
					index = j-radius;
					if (index < 0) {
						index = -index;
					}
					double newFirstValue = currentWaveformCopy[index];
					if (firstValue*newFirstValue < 0.0) {
						count--;
					}
					firstValue = newFirstValue;
					
					// check if window moved over a new zero crossing on the right
					index = j+radius;
					if (index > recordLength-1) {
						index = 2*(recordLength-1) - index;
					}
					double newLastValue = currentWaveformCopy[index];
					if (lastValue*newLastValue < 0.0) {
						count++;
					}
					lastValue = newLastValue;
					
					waveforms[offset+j] = count;
					
				}

			}
			
		}
		
	}
		
    public void setNPasses(int nPasses) {}
	
}
