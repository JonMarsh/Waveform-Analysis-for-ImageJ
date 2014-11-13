package waveformAnalysisForImageJ;

import ij.*;
import ij.gui.*;
import ij.plugin.filter.*;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.process.*;
import java.awt.*;
import java.util.ArrayList;

/**
 *	Performs the specified element-by-element operation on every record in 
 * {@code waveforms} with the single record {@code waveform}.  Each record in 
 * {@code waveforms} is assumed to be the same length as {@code waveform}.  The
 * results are computed in place.
 * 
 * @author Jon N. Marsh
 * @version 2014-02-10
 */

public class WaveformMath implements ExtendedPlugInFilter, DialogListener
{
	private ImagePlus imp;
	private int width, height;
	private GenericDialog gd;
	private PlugInFilterRunner pfr;
	private int[] usableImageIDs;
	private String[] usableImageTitles;
	private float[] waveform;
	public static final int ADD = 0, SUBTRACT = 1, MULTIPLY = 2, DIVIDE = 3;
	private static final String[] operationNames = {"Add", "Subtract", "Multiply", "Divide"};
	private static int operationChoice = SUBTRACT;
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
		
		usableImageIDs = getUsableImageIDs(imp);
		if (usableImageIDs == null) {
			IJ.error("No suitable waveforms were found");
			return DONE;
		}
		
		usableImageTitles = new String[usableImageIDs.length];
		for (int i=0; i<usableImageIDs.length; i++) {
			usableImageTitles[i] = WindowManager.getImage(usableImageIDs[i]).getTitle();
		}
		
		return flags;
	}	
	
	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
	{
		this.pfr = pfr;
		gd = new GenericDialog("Waveform Math...");
		gd.addMessage("Input image: "+imp.getTitle());
		gd.addChoice("Waveform operand:", usableImageTitles, usableImageTitles[0]);
		gd.addChoice("Operation:", operationNames, operationNames[operationChoice]);
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
		int id = gd.getNextChoiceIndex();
		operationChoice = gd.getNextChoiceIndex();
		
		ImagePlus waveformImage = WindowManager.getImage(usableImageIDs[id]);
		waveform = (float[])(waveformImage.getProcessor().getPixelsCopy());

		return true;
	}
	
	public void run(ImageProcessor ip) 
	{
		float[] pixels = (float[])ip.getPixels();

		execute(pixels, waveform, operationChoice);
	}
	
	/**
	 * Performs the specified element-by-element operation on every record in
	 * {@code waveforms} with the single record {@code waveform}.
	 * {@code waveforms} is a one-dimensional array composed of a series of
	 * concatenated records, each of length equal to {@code waveform.length}.
	 * The operation is carried out on {@code waveforms} in place. No action is
	 * performed if either {@code waveforms} or {@code waveform} is
	 * {@code null}, or if {@code waveforms.length<waveform.length}, or if the
	 * length of {@code waveforms} is not evenly divisible by
	 * {@code waveform.length}
	 *
	 * @param waveforms array of concatenated waveforms, assumed to be of length
	 *                  {@code waveform.length}
	 * @param waveform	single waveform with which to perform desired operation
	 * @param operation {@code ADD} adds {@code waveform} to every record in
	 *                  {@code waveforms} on an element-by-element basis;
	 *                  {@code SUBTRACT} subtracts {@code waveform} from every
	 *                  record in {@code waveforms} on an element-by-element
	 *                  basis; {@code MULTIPLY} multiplies {@code waveform} with
	 *                  every record in {@code waveforms} on an
	 *                  element-by-element basis; {@code DIVIDE} divides each
	 *                  record in {@code waveforms} by {@code waveform} on an
	 *                  element-by-element basis
	 */
	public static final void execute(float[] waveforms, float[] waveform, int operation)
	{
		if (waveforms != null && waveform != null) {

			int recordLength = waveform.length;

			if (recordLength > 0) {

				if (waveforms.length % recordLength == 0 && waveforms.length >= recordLength) {

					// compute number of records
					int numRecords = waveforms.length / recordLength;

					int offset;

					switch (operation) {

						case ADD: {
							// peform computations on a row-by-row basis
							for (int i = 0; i < numRecords; i++) {

								// offset to current record
								offset = i * recordLength;

								// perform operation in place
								for (int j = 0; j < recordLength; j++) {
									waveforms[offset + j] += waveform[j];
								}

							}
							break;
						}

						case SUBTRACT: {
							// peform computations on a row-by-row basis
							for (int i = 0; i < numRecords; i++) {

								// offset to current record
								offset = i * recordLength;

								// perform operation in place
								for (int j = 0; j < recordLength; j++) {
									waveforms[offset + j] -= waveform[j];
								}

							}
							break;
						}

						case MULTIPLY: {
							// peform computations on a row-by-row basis
							for (int i = 0; i < numRecords; i++) {

								// offset to current record
								offset = i * recordLength;

								// perform operation in place
								for (int j = 0; j < recordLength; j++) {
									waveforms[offset + j] *= waveform[j];
								}

							}
							break;
						}

						case DIVIDE: {
							// peform computations on a row-by-row basis
							for (int i = 0; i < numRecords; i++) {

								// offset to current record
								offset = i * recordLength;

								// perform operation in place
								for (int j = 0; j < recordLength; j++) {
									waveforms[offset + j] /= waveform[j];
								}

							}
							break;
						}

						default: {
							break;
						}

					}
				}
			}
		}
	}
		
	/**
	 * Performs the specified element-by-element operation on every record in
	 * {@code waveforms} with the single record {@code waveform}.
	 * {@code waveforms} is a one-dimensional array composed of a series of
	 * concatenated records, each of length equal to {@code waveform.length}.
	 * The operation is carried out on {@code waveforms} in place. No action is
	 * performed if either {@code waveforms} or {@code waveform} is
	 * {@code null}, or if {@code waveforms.length<waveform.length}, or if the
	 * length of {@code waveforms} is not evenly divisible by
	 * {@code waveform.length}
	 *
	 * @param waveforms array of concatenated waveforms, assumed to of length
	 *                  {@code waveform.length}
	 * @param waveform	single waveform with which to perform desired operation
	 * @param operation {@code ADD} adds {@code waveform} to every record in
	 *                  {@code waveforms} on an element-by-element basis;
	 *                  {@code SUBTRACT} subtracts {@code waveform} from every
	 *                  record in {@code waveforms} on an element-by-element
	 *                  basis; {@code MULTIPLY} multiplies {@code waveform} with
	 *                  every record in {@code waveforms} on an
	 *                  element-by-element basis; {@code DIVIDE} divides each
	 *                  record in {@code waveforms} by {@code waveform} on an
	 *                  element-by-element basis
	 */
	public static final void execute(double[] waveforms, double[] waveform, int operation)
	{
		int recordLength = waveform.length;
		
		// compute number of records
		int numRecords = waveforms.length/recordLength;
		
		int offset;
		
		switch (operation) {
			
			case ADD: {
				// peform computations on a row-by-row basis
				for (int i=0; i<numRecords; i++) {
					
					// offset to current record
					offset = i*recordLength;
					
					// perform operation in place
					for (int j=0; j<recordLength; j++) {
						waveforms[offset+j] += waveform[j];
					}
					
				}
				break;
			}
				
			case SUBTRACT: {
				// peform computations on a row-by-row basis
				for (int i=0; i<numRecords; i++) {
					
					// offset to current record
					offset = i*recordLength;
					
					// perform operation in place
					for (int j=0; j<recordLength; j++) {
						waveforms[offset+j] -= waveform[j];
					}
					
				}
				break;
			}
				
			case MULTIPLY: {
				// peform computations on a row-by-row basis
				for (int i=0; i<numRecords; i++) {
					
					// offset to current record
					offset = i*recordLength;
					
					// perform operation in place
					for (int j=0; j<recordLength; j++) {
						waveforms[offset+j] *= waveform[j];
					}
					
				}
				break;
			}
				
			case DIVIDE: {
				// peform computations on a row-by-row basis
				for (int i=0; i<numRecords; i++) {
					
					// offset to current record
					offset = i*recordLength;
					
					// perform operation in place
					for (int j=0; j<recordLength; j++) {
						waveforms[offset+j] /= waveform[j];
					}
					
				}
				break;
			}
				
			default: {
				break;
			}
			
		}
	}
		
	private int[] getUsableImageIDs(ImagePlus impToMatch)
	{
		// get imageID for all open images
		int[] allIDs = WindowManager.getIDList();
		
		// Populate an ArrayList with all open imageIDs that are a single waveform of the same length as the input waveforms
		ArrayList<Integer> usableArrayList = new ArrayList<Integer>();
		for(int i : allIDs) {
			ImagePlus currentImp = WindowManager.getImage(i);
			if (currentImp.getWidth() == impToMatch.getWidth() && 
					currentImp.getHeight() == 1 && 
					currentImp.getStackSize() == 1 && 
					currentImp.getType() == ImagePlus.GRAY32 && 
					i != impToMatch.getID()) {
				
				usableArrayList.add(i);
			}
		}
		
		// return an int array of matches, or null if no matches are found
		if (usableArrayList.isEmpty()) {
			return null;
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
