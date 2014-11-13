package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import java.awt.AWTEvent;
import java.awt.TextField;

/**
 * Computes the (positive) frequency at which the power spectrum is maximum for
 * each input waveform and returns the result in a new image. Each row in the
 * input image is assumed to represent a single waveform. The spectral maximum
 * frequency value of the {@code i}<SUP>th</SUP> waveform (row) and
 * {@code j}<SUP>th</SUP> slice is displayed in the {@code i}<SUP>th</SUP> row 
 * and {@code j}<SUP>th</SUP> column of the output image. Input waveforms are
 * zero-padded to the next largest power-of-2 length if the recordLength is not
 * already a power of 2, in order to compute the power spectrum using a
 * traditional FFT routine. Frequency values are given in units of
 * {@code 1.0/samplingInterval}.
 *
 * @author Jon N. Marsh
 * @version 2014-02-25
 */

public class SpectralMaximum implements ExtendedPlugInFilter, DialogListener
{
    private ImagePlus imp, resultImp;
	private ImageProcessor resultProcessor;
	private float[] resultPixels;
    private int width, height, stackSize, resultWidth;
	private String title, resultTitle;
    private PlugInFilterRunner pfr;
	private GenericDialog gd;
	private static double deltaT = 1.0;
	private static final String[] windowTypes = WaveformUtils.WindowType.stringValues();
	private static int windowChoice = WaveformUtils.WindowType.RECTANGLE.ordinal();
	private static double windowParameter = 0.5;
	private static TextField windowParameterTextField;
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
		resultTitle = title + " spectral max";
		
		resultImp = IJ.createImage(resultTitle, "32-bit", resultWidth, height, 1);
		resultProcessor = resultImp.getProcessor();
		resultPixels = (float[])resultProcessor.getPixels();

        return flags;
    }	

    public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
    {
		this.pfr = pfr;
		gd = new GenericDialog("Spectral Maximum...");
		gd.addNumericField("Sampling interval", deltaT, 4);
		gd.addChoice("Weight function", windowTypes, windowTypes[windowChoice]);
		gd.addNumericField("Window parameter", windowParameter, 4);
		windowParameterTextField = (TextField)(gd.getNumericFields().get(1));
		windowParameterTextField.setEnabled(WaveformUtils.WindowType.values()[windowChoice].usesParameter());
		gd.addDialogListener(this);
		
        gd.showDialog();
        if (gd.wasCanceled()) {
            return DONE;
        }
		
		return flags;
    }

    public boolean dialogItemChanged(GenericDialog gd, AWTEvent e)
    {
		deltaT = gd.getNextNumber();
		windowChoice = gd.getNextChoiceIndex();
		windowParameter = gd.getNextNumber();

		windowParameterTextField.setEnabled(WaveformUtils.WindowType.values()[windowChoice].usesParameter());
		
		return (deltaT > 0.0 && !gd.invalidNumber());
    }

	public void run(ImageProcessor ip) 
    {
        int currentSlice = pfr.getSliceNumber();
        float[] pixels = (float[])ip.getPixels();

		float[] spectralMaxValues = execute(pixels, width, deltaT, WaveformUtils.WindowType.values()[windowChoice], windowParameter);
		for (int i=0; i<height; i++) {
			resultPixels[i*resultWidth+(currentSlice-1)] = spectralMaxValues[i];
		}
    }

	/**
	 * Returns an array representing the value at which the power spectrum is
	 * largest for each record in {@code waveforms}, where each record has
	 * {@code recordLength} elements. Output is null if {@code waveforms==null},
	 * {@code recordLength<=0}, {@code waveforms.length<recordLength}, or if
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * Input waveforms are zero-padded to the next largest power-of-2 length if
	 * the recordLength is not already a power of 2, in order to compute the
	 * power spectrum using a traditional FFT routine. Frequency values are
	 * given in units of {@code 1.0/deltaT}. Input waveforms are windowed with 
	 * user-selected window function prior to padding and FFT.
	 *
	 * @param waveforms       one-dimensional array composed of a series of
	 *                        concatenated records, each of size equal to
	 *                        {@code recordLength}
	 * @param recordLength    size of each record in {@code waveforms}
	 * @param deltaT          sampling interval
	 * @param windowType      window function
	 * @param windowParameter used only for window functions that require it,
	 *                        ignored otherwise
	 * @return array of frequency values at which power spectrum is largest for
	 *         each input waveform
	 *
	 */
	public static float[] execute(float[] waveforms, int recordLength, double deltaT, WaveformUtils.WindowType windowType, double windowParameter)
	{
		if (waveforms != null && recordLength > 0 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			float[] spectralMaxValues = new float[numRecords];
			
			// determine padded record length
			int paddedLength = recordLength + WaveformUtils.amountToPadToNextPowerOf2(recordLength);
			
			// generate window function values
			double[] window = WaveformUtils.windowFunction(windowType, recordLength, windowParameter, false);
			
			// compute frequency spacing
			double deltaF = 1.0/(paddedLength*deltaT);
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// initialize temporary arrays
				double[] realPadded = new double[paddedLength];
				double[] imagPadded = new double[paddedLength];
				for (int j=0; j<recordLength; j++) {
					realPadded[j] = waveforms[offset+j]*window[j];
				}
				
				// compute FFT
				WaveformUtils.fftComplexPowerOf2(realPadded, imagPadded, true);
				
				// find frequency of the power spectrum max value
				double magSqrd = realPadded[i]*realPadded[0] + imagPadded[i]*imagPadded[0];
				double freq = 0.0;
				double maxValue = magSqrd;
				double maxFreq = freq;
				for (int j=1; j<(paddedLength/2)+1; j++) {
					magSqrd = realPadded[j]*realPadded[j] + imagPadded[j]*imagPadded[j];
					freq += deltaF;
					if (magSqrd > maxValue) {
						maxValue = magSqrd;
						maxFreq = freq;
					}
				}
				spectralMaxValues[i] = (float)maxFreq;

			}
			
			return spectralMaxValues;
				
		}
		
		return null;
	}
	
	/**
	 * Returns an array representing the value at which the power spectrum is
	 * largest for each record in {@code waveforms}, where each record has
	 * {@code recordLength} elements. Output is null if {@code waveforms==null},
	 * {@code recordLength<=0}, {@code waveforms.length<recordLength}, or if
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * Input waveforms are zero-padded to the next largest power-of-2 length if
	 * the recordLength is not already a power of 2, in order to compute the
	 * power spectrum using a traditional FFT routine. Frequency values are
	 * given in units of {@code 1.0/deltaT}. Input waveforms are windowed with 
	 * user-selected window function prior to padding and FFT.
	 *
	 * @param waveforms       one-dimensional array composed of a series of
	 *                        concatenated records, each of size equal to
	 *                        {@code recordLength}
	 * @param recordLength    size of each record in {@code waveforms}
	 * @param deltaT          sampling interval
	 * @param windowType      window function
	 * @param windowParameter used only for window functions that require it,
	 *                        ignored otherwise
	 * @return array of frequency values at which power spectrum is largest for
	 *         each input waveform
	 *
	 */
	public static double[] execute(double[] waveforms, int recordLength, double deltaT, WaveformUtils.WindowType windowType, double windowParameter)
	{
		if (waveforms != null && recordLength > 0 && waveforms.length >= recordLength && waveforms.length%recordLength == 0) {
			
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// allocate output array
			double[] spectralMaxValues = new double[numRecords];
			
			// determine padded record length
			int paddedLength = recordLength + WaveformUtils.amountToPadToNextPowerOf2(recordLength);
			
			// generate window function values
			double[] window = WaveformUtils.windowFunction(windowType, recordLength, windowParameter, false);
			
			// compute frequency spacing
			double deltaF = 1.0/(paddedLength*deltaT);
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// initialize temporary arrays
				double[] realPadded = new double[paddedLength];
				double[] imagPadded = new double[paddedLength];
				for (int j=0; j<recordLength; j++) {
					realPadded[j] = waveforms[offset+j]*window[j];
				}
				
				// compute FFT
				WaveformUtils.fftComplexPowerOf2(realPadded, imagPadded, true);
				
				// find frequency of the power spectrum max value
				double magSqrd = realPadded[i]*realPadded[0] + imagPadded[i]*imagPadded[0];
				double freq = 0.0;
				double maxValue = magSqrd;
				double maxFreq = freq;
				for (int j=1; j<(paddedLength/2)+1; j++) {
					magSqrd = realPadded[j]*realPadded[j] + imagPadded[j]*imagPadded[j];
					freq += deltaF;
					if (magSqrd > maxValue) {
						maxValue = magSqrd;
						maxFreq = freq;
					}
				}
				spectralMaxValues[i] = maxFreq;

			}
			
			return spectralMaxValues;
				
		}
		
		return null;
	}
	
    public void setNPasses(int nPasses) {}
	
}
