package waveformAnalysisForImageJ;

import waveformAnalysisForImageJ.WaveformUtils;
import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.process.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * This plug-in filter computes the real and imaginary part of the FFT (or its 
 * inverse) of each horizontal line in an image.  It works on arbitrarily-sized 
 * record lengths, by zero-padding each record up to the next power of 2, and 
 * implementing a traditional efficient fft routine.  Both real and imaginary 
 * parts are output, and the input image is left unchanged.
 * 
 * @author Jon N. Marsh
 * @version 2013-11-14
 */

public class FFTComplex implements ExtendedPlugInFilter, DialogListener
{
	private ImagePlus impInput, impRealInput, impImagInput, impRealOutput, impImagOutput;
	private ImageStack stackRealInput, stackImagInput, stackRealOutput, stackImagOutput;
	private int width, height, stackSize, newWidth;
	private String title, transformTitle;
	private int[] useableImageIDs;
	private String[] useableImageTitles;
    private GenericDialog gd;
    private PlugInFilterRunner pfr;
    private static final int REAL_PART = 0, IMAGINARY_PART = 1;
	private static final String[] types = {"Real part", "Imaginary part"};
	private static int typeChoice = REAL_PART;
	private static boolean isForward = true;
	private boolean hasReal, hasImag, inputImageIsReal;
	private static final String NO_CHOICE = "< none >";
	private final int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + KEEP_PREVIEW + FINAL_PROCESSING + NO_CHANGES;
	
    public int setup(String arg, ImagePlus imp) 
    {
        // perform any final processing here
		if (arg.equals("final")) {
			if (impRealOutput != null && impImagOutput != null) {
				impImagOutput.show();
				IJ.resetMinAndMax();
				impRealOutput.show();
				IJ.resetMinAndMax();
			}
			return DONE;
		}

        impInput = imp;
        if (impInput == null) {
            IJ.noImage();
            return DONE;
        }

        width = impInput.getWidth();
        height = impInput.getHeight();
		stackSize = impInput.getStackSize();
		title = impInput.getTitle();
		newWidth = width + WaveformUtils.amountToPadToNextPowerOf2(width);
		
		// create list of images with dimensions that match input image parameters
		useableImageIDs = getUsableImageIDs(impInput);
		useableImageTitles = new String[useableImageIDs.length + 1];
		for (int i=0; i<useableImageIDs.length; i++) {
			useableImageTitles[i] = WindowManager.getImage(useableImageIDs[i]).getTitle();
		}
		useableImageTitles[useableImageTitles.length - 1] = NO_CHOICE;

        return flags;
    }	

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
        this.pfr = pfr;
        gd = new GenericDialog("FFT...");
		gd.addChoice("Input image \""+impInput.getTitle()+"\" is", types, types[typeChoice]);
		gd.addChoice("If complex input, select complementary dataset", useableImageTitles, useableImageTitles[useableImageTitles.length - 1]);
		gd.addCheckbox("Do forward transform (inverse otherwise)", isForward);

        gd.addDialogListener(this);

        gd.showDialog();
        if (gd.wasCanceled()) {
            return DONE;
        }

		transformTitle = title + " FFT_" + (isForward ? "forward" : "inverse");
		
		// create output real and imaginary images and get references to stacks
		impRealOutput = IJ.createImage(transformTitle+", real part", "32-bit", newWidth, height, stackSize);
		impImagOutput = IJ.createImage(transformTitle+", imaginary part", "32-bit", newWidth, height, stackSize);
		stackRealOutput = impRealOutput.getStack();
		stackImagOutput = impImagOutput.getStack();
				
        return flags;
    }
	
    public boolean dialogItemChanged(GenericDialog gd, AWTEvent e)
    {
        typeChoice = gd.getNextChoiceIndex();
		int choiceIndex = gd.getNextChoiceIndex();
		isForward = gd.getNextBoolean();
				
		inputImageIsReal = (typeChoice == REAL_PART);
		if (inputImageIsReal) {
			hasReal = true;
			impRealInput = impInput;
			stackRealInput = impRealInput.getStack();
			hasImag = !useableImageTitles[choiceIndex].equals(NO_CHOICE);
			if (hasImag) {
				impImagInput = WindowManager.getImage(useableImageIDs[choiceIndex]);
				stackImagInput = impImagInput.getStack();
			}
		} else {
			hasImag = true;
			impImagInput = impInput;
			stackImagInput = impImagInput.getStack();
			hasReal = !useableImageTitles[choiceIndex].equals(NO_CHOICE);
			if (hasReal) {
				impRealInput = WindowManager.getImage(useableImageIDs[choiceIndex]);
				stackRealInput = impRealInput.getStack();
			}
		}		

		return true;
    }

    public void run(ImageProcessor ip) 
    {
        int currentSlice = pfr.getSliceNumber();
		float[] pixelsRealInput;
		float[] pixelsImagInput;
		float[] pixelsRealOutput = (float[])(stackRealOutput.getProcessor(currentSlice).getPixels());
		float[] pixelsImagOutput = (float[])(stackImagOutput.getProcessor(currentSlice).getPixels());

		if (inputImageIsReal) {
			pixelsRealInput = (float[])ip.getPixels();
			if (hasImag) {
				pixelsImagInput = (float[])(stackImagInput.getProcessor(currentSlice).getPixels());
			} else {
				pixelsImagInput = new float[pixelsRealInput.length];
			}
		} else {
			pixelsImagInput = (float[])ip.getPixels();
			if (hasReal) {
				pixelsRealInput = (float[])(stackRealInput.getProcessor(currentSlice).getPixels());
			} else {
				pixelsRealInput = new float[pixelsImagInput.length];
			}
		}
		
		float[][] transform = execute(pixelsRealInput, pixelsImagInput, isForward, width);
		
		System.arraycopy(transform[0], 0, pixelsRealOutput, 0, newWidth*height);
		System.arraycopy(transform[1], 0, pixelsImagOutput, 0, newWidth*height);
    }

	/**
	 * Computes FFT of all complex waveforms represented in {@code realWaveforms}
	 * and {@code imagWaveforms}, where each record is of size {@code recordLength} 
	 * (which does not have to be a power of 2).  Input records are zero-padded to 
	 * the next highest power of two if {@code recordLength} is not already a 
	 * power of 2. A forward FFT is performed if {@code isForward} is {@code true} 
	 * (inverse if {@code false}).  The output is returned as a two-dimensional 
	 * array, where the first element comprises the concatenated padded real 
	 * part of the transforms, and the second element is the corresponding
	 * concatenated padded imaginary part of the transforms.  Input arrays are 
	 * left unchanged.  For efficiency, no error checking is performed on array 
	 * dimensions; runtime errors may occur for invalid inputs.
	 * 
	 * @param realWaveforms	one-dimensional array composed of a series of 
	 *						concatenated records, each of size equal to {@code recordLength},
	 *						representing the real part of the input signals
	 * @param imagWaveforms	one-dimensional array composed of a series of 
	 *						concatenated records, each of size equal to {@code recordLength},
	 *						representing the imaginary part of the input signals
	 * @param isForward		{@code true} for forward transform, {@code false} for inverse
	 * @param recordLength	size of each record in {@code realWaveforms} and {@code imagWaveforms} 
	 *						(does not have to be a power of 2)
	 * @return				two-dimensional array of size {@code 2*numberOfRecords*paddedRecordLength}, 
	 *						whose first element comprises the concatenated padded 
	 *						real part of the transforms, and whose second element
	 *						comprises the concatenated padded imaginary part of the transforms
	 *						
	 */
	public static float[][] execute(float[] realWaveforms, float[] imagWaveforms, boolean isForward, int recordLength)
	{
		// compute number of records
		int numRecords = realWaveforms.length/recordLength;
		
		// compute padded record length
		int paddedRecordLength = recordLength + WaveformUtils.amountToPadToNextPowerOf2(recordLength);
		
		// initialize output array
		float[][] output = new float[2][paddedRecordLength*numRecords];
		
		for (int i=0; i<numRecords; i++) {
			
			// compute offsets for input and output arrays
			int offset1 = i*recordLength;
			int offset2 = i*paddedRecordLength;
			
			// initialize temporary real and imaginary waveforms
			double[] ar = new double[paddedRecordLength];
			double[] ai = new double[paddedRecordLength];

			// copy values into current temporary real and imaginary waveforms
			for (int j=0; j<recordLength; j++) {
				ar[j] = realWaveforms[offset1+j];
				ai[j] = imagWaveforms[offset1+j];
			}
			
			// perform FFT
			WaveformUtils.fftComplexPowerOf2(ar, ai, isForward);
			
			// write values into output array
			for (int j=0; j<paddedRecordLength; j++) {
				output[0][offset2+j] = (float)ar[j];
			}
			for (int j=0; j<paddedRecordLength; j++) {
				output[1][offset2+j] = (float)ai[j];
			}
			
		}

		return output;
	}

	/**
	 * Computes FFT of all complex waveforms represented in {@code realWaveforms}
	 * and {@code imagWaveforms}, where each record is of size {@code recordLength} 
	 * (which does not have to be a power of 2).  Input records are zero-padded to 
	 * the next highest power of two if {@code recordLength} is not already a 
	 * power of 2. A forward FFT is performed if {@code isForward} is {@code true} 
	 * (inverse if {@code false}).  The output is returned as a two-dimensional 
	 * array, where the first element comprises the concatenated padded real 
	 * part of the transforms, and the second element is the corresponding
	 * concatenated padded imaginary part of the transforms.  Input arrays are 
	 * left unchanged.  For efficiency, no error checking is performed on array 
	 * dimensions; runtime errors may occur for invalid inputs.
	 * 
	 * @param realWaveforms	one-dimensional array composed of a series of 
	 *						concatenated records, each of size equal to {@code recordLength},
	 *						representing the real part of the input signals
	 * @param imagWaveforms	one-dimensional array composed of a series of 
	 *						concatenated records, each of size equal to {@code recordLength},
	 *						representing the imaginary part of the input signals
	 * @param isForward		{@code true} for forward transform, {@code false} for inverse
	 * @param recordLength	size of each record in {@code realWaveforms} and {@code imagWaveforms} 
	 *						(does not have to be a power of 2)
	 * @return				two-dimensional array of size {@code 2*numberOfRecords*paddedRecordLength}, 
	 *						whose first element comprises the concatenated padded 
	 *						real part of the transforms, and whose second element
	 *						comprises the concatenated padded imaginary part of the transforms
	 *						
	 */
	public static double[][] execute(double[] realWaveforms, double[] imagWaveforms, boolean isForward, int recordLength)
	{
		// compute number of records
		int numRecords = realWaveforms.length/recordLength;
		
		// compute padded record length
		int paddedRecordLength = recordLength + WaveformUtils.amountToPadToNextPowerOf2(recordLength);
		
		// initialize output array
		double[][] output = new double[2][paddedRecordLength*numRecords];
		
		for (int i=0; i<numRecords; i++) {
			
			// compute offsets for input and output arrays
			int offset1 = i*recordLength;
			int offset2 = i*paddedRecordLength;
			
			// initialize temporary real and imaginary waveforms
			double[] ar = new double[paddedRecordLength];
			double[] ai = new double[paddedRecordLength];

			// copy values into current temporary real and imaginary waveforms
			System.arraycopy(realWaveforms, offset1, ar, 0, recordLength);
			System.arraycopy(imagWaveforms, offset1, ai, 0, recordLength);
			
			// perform FFT
			WaveformUtils.fftComplexPowerOf2(ar, ai, isForward);
			
			// write values into output array
			System.arraycopy(ar, 0, output[0], offset2, paddedRecordLength);
			System.arraycopy(ai, 0, output[1], offset2, paddedRecordLength);
			
		}

		return output;
	}	
	
	private int[] getUsableImageIDs(ImagePlus impToMatch)
	{
		// get imageID for all open images
		int[] allIDs = WindowManager.getIDList();
		
		// Populate an ArrayList with all open imageIDs that have the same width, height, and stackSize as input
		ArrayList<Integer> usableArrayList = new ArrayList<Integer>();
		for(int i : allIDs) {
			ImagePlus currentImp = WindowManager.getImage(i);
			if (currentImp.getWidth() == impToMatch.getWidth() && 
					currentImp.getHeight() == impToMatch.getHeight() && 
					currentImp.getStackSize() == impToMatch.getStackSize() && 
					currentImp.getType() == ImagePlus.GRAY32 && 
					i != impToMatch.getID()) {
				
				usableArrayList.add(i);
			}
		}
		
		// return an int array of matches, or null if no matches are found
		if (usableArrayList.isEmpty()) {
			return new int[0];
		} else {
			int[] usableIDs = new int[usableArrayList.size()];
			for (int i=0; i<usableArrayList.size(); i++) {
				usableIDs[i] = usableArrayList.get(i);
			}
			return usableIDs;
		}
	}
	
    public void setNPasses(int nPasses) {}
	
}
