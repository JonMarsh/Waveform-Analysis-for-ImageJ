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
 * This plugin filter moves a sliding gate along each waveform (horizontal line)
 * in an image, computes the median within the window, and then returns the
 * median as the new value at that index. The input data are overwritten by the
 * computed values. The length of the moving window is equal to
 * {@code 2*radius+1}. At positions where portions of the moving window lie
 * outside the bounds of the waveform, the waveform values are mirrored about
 * the end points. This plugin utilizes a fast median-filtering algorithm
 * adapted from "Smoothing Scatterplots" by J.H. Friedman and W. Stuetzle
 * (Project Orion, Department of Statistics, Stanford University, 1982).
 * Computation complexity for this algorithm is O({@code n*k}), where {@code n}
 * is the number of records and {@code k} is the window length.
 *
 * @author Jon N. Marsh
 * @version 2014-01-16
 */

public class MovingWindowMedian implements ExtendedPlugInFilter, DialogListener
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
        gd = new GenericDialog("Moving Window Median...");
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
	 * record is of length {@code recordLength}, computes the median in the
	 * window, and replaces the value at the central point of the window with
	 * that median. Input data is overwritten by median-filtered data. The
	 * moving window is of length {@code 2*radius+1}. Input waveforms are left
	 * unchanged if the array representing them is null,
	 * {@code 2*radius+1>recordLength}, {@code radius<=0}, or
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * At positions where a part of the moving window lies outside the bounds of
	 * the waveform, the waveform values are mirrored around the appropriate end
	 * point. This method is adapted from an algorithm described in "Smoothing
	 * Scatterplots" by J.H. Friedman and W. Stuetzle (Project Orion, Department
	 * of Statistics, Stanford University, 1982).
	 *
	 * @param waveforms	                    one-dimensional array composed of a
	 *                                      series of concatenated records, each
	 *                                      of size equal to
	 *                                      {@code recordLength}
	 * @param recordLength                  size of each record in
	 *                                      {@code waveforms}
	 * @param radius                        radius of moving window; length of two-sided window function
	 *                                      is equal to {@code 2*radius+1}
	 */
	public static final void execute(float[] waveforms, int recordLength, int radius)
	{
		int windowLength = 2*radius + 1;
				
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius > 0) {
								
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// create temporary copy of current waveform
				float[] currentWaveformCopy = Arrays.copyOfRange(waveforms, offset, offset+recordLength);
				
				// compute median at first point
				float[] segment = new float[windowLength];
				segment[radius] = currentWaveformCopy[0];
				for (int j=1; j<=radius; j++) {
					segment[radius+j] = currentWaveformCopy[j];
					segment[radius-j] = currentWaveformCopy[j];
				}
				Arrays.sort(segment);
				float currentMedian = segment[radius];
				float newMedian = currentMedian;
				waveforms[offset] = currentMedian;
				
				// perform computation at each point in the waveform
				for (int j=1; j<recordLength; j++) {
					
					int indexIn = j + radius;
					int indexOut = j - (radius+1);
					float yIn = elementAt(currentWaveformCopy, recordLength, indexIn);
					float yOut = elementAt(currentWaveformCopy, recordLength, indexOut);
					
					// below are the only cases for which the median needs to be recomputed
					if (yIn > currentMedian) {
						if (yOut <= currentMedian) {
						    int kPlus = 0;
						    float minPosDiffFromMedian = Float.POSITIVE_INFINITY;
						    float smallestValueGreaterThanMedian = Float.POSITIVE_INFINITY;
						    if (yOut < currentMedian) {
						    	for (int k=-radius; k<=radius; k++) {
						    		float currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
						    		if (currentValue > currentMedian) {
						    		    kPlus++;
						    		    if (currentValue - currentMedian < minPosDiffFromMedian) {
						    		        minPosDiffFromMedian = currentValue - currentMedian;
						    		        smallestValueGreaterThanMedian = currentValue;
						    		    }
						    		}
						    	}
						    	if (kPlus >= radius+1) {
						    	    newMedian = smallestValueGreaterThanMedian;
						    	}
						    } else { // yOut == currentMedian
						        boolean currentMedianExistsInNewSpan = false;
    						    for (int k=-radius; k<=radius; k++) {
    						        float currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
    						        if (currentValue >= currentMedian) {
    						        	if (currentValue > currentMedian) {
    						        		kPlus++;
    						        		if (currentValue - currentMedian < minPosDiffFromMedian) {
						    		        	minPosDiffFromMedian = currentValue - currentMedian;
						    		        	smallestValueGreaterThanMedian = currentValue;
						    		    	}
    						        	} else {
    						        	    currentMedianExistsInNewSpan = true;
    						        	}
    						        }
    						    }
    						    if (kPlus == radius+1) {
    						        newMedian = smallestValueGreaterThanMedian;
    						    } else {
    						        newMedian = currentMedianExistsInNewSpan ? currentMedian : smallestValueGreaterThanMedian;
    						    }
    						}
						} 
					} else if (yIn < currentMedian) {
					    if (yOut >= currentMedian) {
					        int kMinus = 0;
						    float minPosDiffFromMedian = Float.POSITIVE_INFINITY;
						    float largestValueSmallerThanMedian = Float.NEGATIVE_INFINITY;
						    if (yOut > currentMedian) {
						        for (int k=-radius; k<=radius; k++) {
						            float currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
						            if (currentValue < currentMedian) {
						                kMinus++;
						                if (currentMedian - currentValue < minPosDiffFromMedian) {
						                    minPosDiffFromMedian = currentMedian - currentValue;
						                    largestValueSmallerThanMedian = currentValue;
						                }
						            }
						        }
						        if (kMinus >= radius+1) {
						            newMedian = largestValueSmallerThanMedian;
						        }
						    } else { // yOut == currentMedian
						        boolean currentMedianExistsInNewSpan = false;
						        for (int k=-radius; k<=radius; k++) {
						            float currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
						            if (currentValue <= currentMedian) {
						                if (currentValue < currentMedian) {
						                    kMinus++;
						                    if (currentMedian - currentValue < minPosDiffFromMedian) {
						                        minPosDiffFromMedian = currentMedian - currentValue;
						                        largestValueSmallerThanMedian = currentValue;
						                    }
						                } else {
						                    currentMedianExistsInNewSpan = true;
						                }
						            }
						        }
						        if (kMinus == radius+1) {
						            newMedian = largestValueSmallerThanMedian;
						        } else {
						            newMedian = currentMedianExistsInNewSpan ? currentMedian : largestValueSmallerThanMedian;
						        }
						    }
					    }
					} 

					waveforms[offset+j] = newMedian;
					currentMedian = newMedian;
					
				}

			}
			
		}
		
	}
	
	/**
	 * Applies a moving window to each record in {@code waveforms}, where each
	 * record is of length {@code recordLength}, computes the median in the
	 * window, and replaces the value at the central point of the window with
	 * that median. Input data is overwritten by median-filtered data. The
	 * moving window is of length {@code 2*radius+1}. Input waveforms are left
	 * unchanged if the array representing them is null,
	 * {@code 2*radius+1>recordLength}, {@code radius<=0}, or
	 * {@code waveforms.length} is not evenly divisible by {@code recordLength}.
	 * At positions where a part of the moving window lies outside the bounds of
	 * the waveform, the waveform values are mirrored around the appropriate end
	 * point. This method is adapted from an algorithm described in "Smoothing
	 * Scatterplots" by J.H. Friedman and W. Stuetzle (Project Orion, Department
	 * of Statistics, Stanford University, 1982).
	 *
	 * @param waveforms	                    one-dimensional array composed of a
	 *                                      series of concatenated records, each
	 *                                      of size equal to
	 *                                      {@code recordLength}
	 * @param recordLength                  size of each record in
	 *                                      {@code waveforms}
	 * @param radius                        radius of moving window; length of two-sided window function
	 *                                      is equal to {@code 2*radius+1}
	 */
	public static final void execute(double[] waveforms, int recordLength, int radius)
	{
		int windowLength = 2*radius + 1;
				
		if (waveforms != null && recordLength > windowLength && waveforms.length%recordLength == 0 && radius > 0) {
								
			// compute number of records
			int numRecords = waveforms.length/recordLength;
			
			// loop over all records
			for (int i=0; i<numRecords; i++) {
				
				// compute row offset
				int offset = i*recordLength;
				
				// create temporary copy of current waveform
				double[] currentWaveformCopy = Arrays.copyOfRange(waveforms, offset, offset+recordLength);
				
				// compute median at first point
				double[] segment = new double[windowLength];
				segment[radius] = currentWaveformCopy[0];
				for (int j=1; j<=radius; j++) {
					segment[radius+j] = currentWaveformCopy[j];
					segment[radius-j] = currentWaveformCopy[j];
				}
				Arrays.sort(segment);
				double currentMedian = segment[radius];
				double newMedian = currentMedian;
				waveforms[offset] = currentMedian;
				
				// perform computation at each point in the waveform
				for (int j=1; j<recordLength; j++) {
					
					int indexIn = j + radius;
					int indexOut = j - (radius+1);
					double yIn = elementAt(currentWaveformCopy, recordLength, indexIn);
					double yOut = elementAt(currentWaveformCopy, recordLength, indexOut);
					
					// below are the only cases for which the median needs to be recomputed
					if (yIn > currentMedian) {
						if (yOut <= currentMedian) {
						    int kPlus = 0;
						    double minPosDiffFromMedian = Double.POSITIVE_INFINITY;
						    double smallestValueGreaterThanMedian = Double.POSITIVE_INFINITY;
						    if (yOut < currentMedian) {
						    	for (int k=-radius; k<=radius; k++) {
						    		double currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
						    		if (currentValue > currentMedian) {
						    		    kPlus++;
						    		    if (currentValue - currentMedian < minPosDiffFromMedian) {
						    		        minPosDiffFromMedian = currentValue - currentMedian;
						    		        smallestValueGreaterThanMedian = currentValue;
						    		    }
						    		}
						    	}
						    	if (kPlus >= radius+1) {
						    	    newMedian = smallestValueGreaterThanMedian;
						    	}
						    } else { // yOut == currentMedian
						        boolean currentMedianExistsInNewSpan = false;
    						    for (int k=-radius; k<=radius; k++) {
    						        double currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
    						        if (currentValue >= currentMedian) {
    						        	if (currentValue > currentMedian) {
    						        		kPlus++;
    						        		if (currentValue - currentMedian < minPosDiffFromMedian) {
						    		        	minPosDiffFromMedian = currentValue - currentMedian;
						    		        	smallestValueGreaterThanMedian = currentValue;
						    		    	}
    						        	} else {
    						        	    currentMedianExistsInNewSpan = true;
    						        	}
    						        }
    						    }
    						    if (kPlus == radius+1) {
    						        newMedian = smallestValueGreaterThanMedian;
    						    } else {
    						        newMedian = currentMedianExistsInNewSpan ? currentMedian : smallestValueGreaterThanMedian;
    						    }
    						}
						} 
					} else if (yIn < currentMedian) {
					    if (yOut >= currentMedian) {
					        int kMinus = 0;
						    double minPosDiffFromMedian = Double.POSITIVE_INFINITY;
						    double largestValueSmallerThanMedian = Double.NEGATIVE_INFINITY;
						    if (yOut > currentMedian) {
						        for (int k=-radius; k<=radius; k++) {
						            double currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
						            if (currentValue < currentMedian) {
						                kMinus++;
						                if (currentMedian - currentValue < minPosDiffFromMedian) {
						                    minPosDiffFromMedian = currentMedian - currentValue;
						                    largestValueSmallerThanMedian = currentValue;
						                }
						            }
						        }
						        if (kMinus >= radius+1) {
						            newMedian = largestValueSmallerThanMedian;
						        }
						    } else { // yOut == currentMedian
						        boolean currentMedianExistsInNewSpan = false;
						        for (int k=-radius; k<=radius; k++) {
						            double currentValue = elementAt(currentWaveformCopy, recordLength, j+k);
						            if (currentValue <= currentMedian) {
						                if (currentValue < currentMedian) {
						                    kMinus++;
						                    if (currentMedian - currentValue < minPosDiffFromMedian) {
						                        minPosDiffFromMedian = currentMedian - currentValue;
						                        largestValueSmallerThanMedian = currentValue;
						                    }
						                } else {
						                    currentMedianExistsInNewSpan = true;
						                }
						            }
						        }
						        if (kMinus == radius+1) {
						            newMedian = largestValueSmallerThanMedian;
						        } else {
						            newMedian = currentMedianExistsInNewSpan ? currentMedian : largestValueSmallerThanMedian;
						        }
						    }
					    }
					} 

					waveforms[offset+j] = newMedian;
					currentMedian = newMedian;
					
				}

			}
			
		}
		
	}	

	// Simple array indexing method that replicates out-of-bound indices with mirrored values around endpoints
	private static float elementAt(float[] array, int arrayLength, int index)
	{
		int i = index;
		if (i < 0) {
			return array[-index];
		} else if (i > arrayLength-1) {
			return array[2*(arrayLength-1) - index];
		} else {
			return array[index];
		}
	}
	
	// Simple array indexing method that replicates out-of-bound indices with mirrored values around endpoints
	private static double elementAt(double[] array, int arrayLength, int index)
	{
		int i = index;
		if (i < 0) {
			return array[-index];
		} else if (i > arrayLength-1) {
			return array[2*(arrayLength-1) - index];
		} else {
			return array[index];
		}
	}	
		
    public void setNPasses(int nPasses) {}
	
}
