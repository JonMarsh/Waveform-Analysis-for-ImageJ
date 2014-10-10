package waveformAnalysisForImageJ;

import ij.*;
import ij.process.*;
import ij.gui.*;
import java.awt.*;
import ij.plugin.filter.*;
import java.util.Arrays;

/**
 * Subtracts the specified quantity of each waveform in an image from every 
 * element in the waveform.  Each row is assumed to represent a single waveform. 
 * Results are computed in place.
 * 
 * @author Jon N. Marsh
 * @version 2014-02-12
 */


public class SubtractFromWaveform implements ExtendedPlugInFilter, DialogListener
{
	private ImagePlus imp;
	private int width;
	private GenericDialog gd;
	private static final String[] operationNames = {"Mean", "Median", "Linear fit"};
	public static final int MEAN = 0, MEDIAN = 1, LINEAR_FIT = 2;
	private static int operationChoice = MEAN;
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
		gd = new GenericDialog("Subtract From Waveform...");
		gd.addChoice("Value to subtract:", operationNames, operationNames[operationChoice]);
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
		operationChoice = gd.getNextChoiceIndex();

		return true;
	}
		
	public void run(ImageProcessor ip) 
	{
		float[] pixels = (float[])ip.getPixels();

		execute(pixels, width, operationChoice);
	}
	
	/**
	 * Subtracts the specified quantity of each waveform in {@code waveforms}
	 * from every element in the waveform. {@code waveforms} is a
	 * one-dimensional array composed of a series of concatenated records, each
	 * of length {@code recordLength}. The operation is carried out on 
	 * {@code waveforms} in place.
	 *
	 * @param waveforms	   array of concatenated waveforms, assumed to be of length
	 *                     {@code recordLength}
	 * @param recordLength length of each record in {@code waveforms}
	 * @param operation    {@code MEAN} subtracts the mean value of each
	 *                     waveform from all its elements; {@code MEDIAN}
	 *                     subtracts the median value of each waveform all its
	 *                     elements; {@code LINEAR_FIT} computes the best fit
	 *                     line to the entire waveform and subtracts from each
	 *                     point the value of the line at that point
	 */
	public static final void execute(float[] waveforms, int recordLength, int operation)
	{
		if (waveforms != null && waveforms.length >= recordLength && recordLength > 0 && waveforms.length % recordLength == 0) {

			// compute number of records
			int numRecords = waveforms.length / recordLength;

			switch (operation) {

				case MEAN: {
					for (int i = 0; i < numRecords; i++) {

						// offset to current record
						int offset = i * recordLength;

						// compute mean for current record
						float mean = WaveformUtils.mean(waveforms, offset, offset + recordLength);

						// subtract mean from current record in place
						WaveformUtils.addScalarInPlace(waveforms, offset, offset + recordLength, -mean);
					}

					break;
				}

				case MEDIAN: {
					for (int i = 0; i < numRecords; i++) {

						// offset to current record
						int offset = i * recordLength;

						// compute median for current record
						float[] temp = Arrays.copyOfRange(waveforms, offset, offset + recordLength);
						float median = WaveformUtils.medianAndSort(temp);

						// subtract mean from current record in place
						WaveformUtils.addScalarInPlace(waveforms, offset, offset + recordLength, -median);
					}

					break;
				}

				case LINEAR_FIT: {
					// precompute global parameters for linear fit
					double n = (double) recordLength;
					double sumX = 0.5 * n * (n - 1.0);
					double sumXsumX = sumX * sumX;
					double sumXX = sumX * (2.0 * n - 1.0) / 3.0;

					for (int i = 0; i < numRecords; i++) {

						// offset to current record
						int offset = i * recordLength;

						// compute linear fit parameters for current record
						double sumY = 0.0;
						double sumXY = 0.0;
						for (int j = 0; j < recordLength; j++) {
							sumY += waveforms[offset + j];
							sumXY += j * waveforms[offset + j];
						}
						double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumXsumX);
						double intercept = (sumY - slope * sumX) / n;

						// subtract median from current record in place
						double value = intercept;
						for (int j = 0; j < recordLength; j++) {
							waveforms[offset + j] -= (float)value;
							value += slope;
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
	
	public static final void execute(double[] waveforms, int recordLength, int operation)
	{
		if (waveforms != null && waveforms.length >= recordLength && recordLength > 0 && waveforms.length % recordLength == 0) {

			// compute number of records
			int numRecords = waveforms.length / recordLength;

			switch (operation) {

				case MEAN: {
					for (int i = 0; i < numRecords; i++) {

						// offset to current record
						int offset = i * recordLength;

						// compute mean for current record
						double mean = WaveformUtils.mean(waveforms, offset, offset + recordLength);

						// subtract mean from current record in place
						WaveformUtils.addScalarInPlace(waveforms, offset, offset + recordLength, -mean);
					}

					break;
				}

				case MEDIAN: {
					for (int i = 0; i < numRecords; i++) {

						// offset to current record
						int offset = i * recordLength;

						// compute median for current record
						double[] temp = Arrays.copyOfRange(waveforms, offset, offset + recordLength);
						double median = WaveformUtils.medianAndSort(temp);

						// subtract mean from current record in place
						WaveformUtils.addScalarInPlace(waveforms, offset, offset + recordLength, -median);
					}

					break;
				}

				case LINEAR_FIT: {
					// precompute global parameters for linear fit
					double n = (double) recordLength;
					double sumX = 0.5 * n * (n - 1.0);
					double sumXsumX = sumX * sumX;
					double sumXX = sumX * (2.0 * n - 1.0) / 3.0;

					for (int i = 0; i < numRecords; i++) {

						// offset to current record
						int offset = i * recordLength;

						// compute linear fit parameters for current record
						double sumY = 0.0;
						double sumXY = 0.0;
						for (int j = 0; j < recordLength; j++) {
							sumY += waveforms[offset + j];
							sumXY += j * waveforms[offset + j];
						}
						double slope = (n * sumXY - sumX * sumY) / (n * sumXX - sumXsumX);
						double intercept = (sumY - slope * sumX) / n;

						// subtract median from current record in place
						double value = intercept;
						for (int j = 0; j < recordLength; j++) {
							waveforms[offset + j] -= value;
							value += slope;
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
	
	private static void subtractMean(float[] waveforms, int recordLength)
	{
		// compute number of records
		int numRecords = waveforms.length/recordLength;
		
		// peform computations on a row-by-row basis
		for (int i=0; i<numRecords; i++) {

			// offset to current record
			int offset = i*recordLength;
			
			// compute mean for current record
			float mean = WaveformUtils.mean(waveforms, offset, offset+recordLength);
			
			// subtract mean from current record in place
			WaveformUtils.addScalarInPlace(waveforms, offset, offset+recordLength, -mean);			
		}
	}
	
	private static void subtractMedian(float[] waveforms, int recordLength)
	{
		// compute number of records
		int numRecords = waveforms.length/recordLength;
		
		// peform computations on a row-by-row basis
		for (int i=0; i<numRecords; i++) {

			// offset to current record
			int offset = i*recordLength;
			
			// compute mean for current record
			float[] temp = Arrays.copyOfRange(waveforms, offset, offset+recordLength);
			float median = WaveformUtils.medianAndSort(temp);
			
			// subtract median from current record in place
			WaveformUtils.addScalarInPlace(waveforms, offset, offset+recordLength, -median);
		}		
	}
	
	private static void subtractLinearFit(float[] waveforms, int recordLength)
	{
		// compute number of records
		int numRecords = waveforms.length/recordLength;
		
		// precompute global parameters for linear fit
		double n = (double)recordLength;
		double sumX = 0.5*n*(n-1.0);
		double sumXsumX = sumX*sumX;
		double sumXX = sumX*(2.0*n-1.0)/3.0;
		
		// peform computations on a row-by-row basis
		for (int i=0; i<numRecords; i++) {

			// offset to current record
			int offset = i*recordLength;
			
			// compute linear fit parameters for current record
			double sumY = 0.0;
			double sumXY = 0.0;
			for (int j=0; j<recordLength; j++) {
				sumY += waveforms[offset+j];
				sumXY += j*waveforms[offset+j];
			}
			double slope = (n*sumXY - sumX*sumY)/(n*sumXX - sumXsumX);
			double intercept = (sumY - slope*sumX)/n;
					
			// subtract median from current record in place
			double value = intercept;
			for (int j=0; j<recordLength; j++) {
				value += slope;
				waveforms[offset+j] -= (float)value;
			}
		}		
	}

	public void setNPasses(int nPasses) {}
}
