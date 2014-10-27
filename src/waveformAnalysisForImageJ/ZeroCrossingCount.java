package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;

/**
 * Computes the number of zero-crossings of each input waveform and returns the
 * result in a new image. A zero-crossing is deemed to have occurred if the
 * product of two neighboring elements in a waveform has a value less than zero.
 * Each row in the input image is assumed to represent a single waveform. The
 * number of zero-crossings in the {@code i}<SUP>th</SUP>
 * waveform (row) and {@code j}<SUP>th</SUP> slice is displayed in the
 * {@code i}<SUP>th</SUP> row and {@code j}<SUP>th</SUP> column of the output
 * image.
 *
 * @author Jon N. Marsh
 * @version 2014-01-24
 */

public class ZeroCrossingCount implements ExtendedPlugInFilter
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
		resultTitle = title + " zero crossings";
		
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

		int[] numberOfZeroCrossings = execute(pixels, width);
		for (int i=0; i<height; i++) {
			resultPixels[i*resultWidth+(currentSlice-1)] = numberOfZeroCrossings[i];
		}
    }

	/**
	 * Returns an array representing the number of zero-crossings in each record
	 * in {@code waveforms}, where each record {@code recordLength} elements.
	 * Output is null if {@code waveforms==null}, {@code recordLength<=1},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}. A zero-crossing occurs if
	 * {@code waveforms[i]*waveforms[i+1]<0}.
	 *
	 * @param waveforms    one-dimensional array composed of a series of
	 *                     concatenated records, each of size equal to
	 *                     {@code recordLength}
	 * @param recordLength size of each record in {@code waveforms}
	 * @return array containing number of zero-crossings of each input waveform
	 */
	public static int[] execute(float[] waveforms, int recordLength)
	{
		if (waveforms != null && recordLength > 1 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			int[] numberOfZeroCrossings = new int[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// compute number of zero crossings
				float currentValue = waveforms[offset];
				int count = 0;
				for (int j=1; j<recordLength; j++) {
					float newValue = waveforms[offset+j];
					if (currentValue*newValue<0.0f) {
						count++;
					}
					currentValue = newValue;
				}
				numberOfZeroCrossings[i] = count;

			}
			
			return numberOfZeroCrossings;
				
		}
		
		return null;
	}
	
	/**
	 * Returns an array representing the number of zero-crossings in each record
	 * in {@code waveforms}, where each record {@code recordLength} elements.
	 * Output is null if {@code waveforms==null}, {@code recordLength<=1},
	 * {@code waveforms.length<recordLength}, or if {@code waveforms.length} is
	 * not evenly divisible by {@code recordLength}. A zero-crossing occurs if
	 * {@code waveforms[i]*waveforms[i+1]<0}.
	 *
	 * @param waveforms    one-dimensional array composed of a series of
	 *                     concatenated records, each of size equal to
	 *                     {@code recordLength}
	 * @param recordLength size of each record in {@code waveforms}
	 * @return array containing number of zero-crossings of each input waveform
	 */
	public static int[] execute(double[] waveforms, int recordLength)
	{
		if (waveforms != null && recordLength > 1 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			int[] numberOfZeroCrossings = new int[numRecords];
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// compute number of zero crossings
				double currentValue = waveforms[offset];
				int count = 0;
				for (int j=1; j<recordLength; j++) {
					double newValue = waveforms[offset+j];
					if (currentValue*newValue<0.0f) {
						count++;
					}
					currentValue = newValue;
				}
				numberOfZeroCrossings[i] = count;

			}
			
			return numberOfZeroCrossings;
				
		}
		
		return null;
	}
	
    public void setNPasses(int nPasses) {}
	
}
