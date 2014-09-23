package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.ImageStack;
import ij.gui.GenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import java.awt.FlowLayout;
import java.awt.Panel;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

/**
 * This plugin creates a scaled sector scan image from an input image consisting
 * of a sequence of waveforms. Each row is assumed to represent a single
 * waveform. User inputs include:
 * <ul>
 * <li> "radial length", which is the desired final length (in pixels) of
 * displayed data for each line of the sector
 * <li> "blanked length", which is the length (in pixels) of any non-displayed
 * area within the range from the vertex to the start of the displayed data
 * <li> "maximum half-angle", which is half the span of the sector (in degrees)
 * </ul>
 *
 * @author Jon N. Marsh
 * @version 2014-01-24
 */
public class SectorScanConvert implements ExtendedPlugInFilter 
{
	ImagePlus imp, resizedImp;
	ImageStack stack, resizedStack;
	static int convertedRadialLength = 256;
	static int blankedLength = 50;
	static double maxHalfAngleInDegrees = 45;
	static boolean rotateRight = false;
	static BufferedImage scanConversionIllustration;
	static Panel panel;
	static JLabel illustrationLabel;
	static int illustrationWidth = 225;
	double maxHalfAngleInRadians;
	int recordLength, height, stackSize, rMin, rMax, trueXMin, trueXMax, trueYMin, trueYMax, convertedWidth, convertedHeight;
	String name;
	PlugInFilterRunner pfr;
	int flags = DOES_32 + DOES_STACKS + PARALLELIZE_STACKS + FINAL_PROCESSING;

	public int setup(String arg, ImagePlus imp) 
	{
		if (arg.equals("final")) {	// do final processing here
			if (resizedImp != null) {
				imp.setStack(resizedStack);	// set original image to scan-converted image
				imp.updateAndDraw();
				IJ.resetMinAndMax();
			}
			return DONE;
		}

		this.imp = imp;
		if (imp == null) {
			IJ.noImage();
			return DONE;
		}

		recordLength = imp.getWidth();
		height = imp.getHeight();
		stackSize = imp.getStackSize();
		name = imp.getTitle();
		
		// Create a panel to show an illustration above the plugin input fields
		panel = new Panel();
		panel.setLayout(new FlowLayout());
		URL illustrationResource = this.getClass().getResource("/Resources/sector_scan_illustration.png");
		if (illustrationResource != null) {
			try {
				scanConversionIllustration = ImageIO.read(illustrationResource);
				int w = scanConversionIllustration.getWidth();
				int h = scanConversionIllustration.getHeight();
				int scaledWidth = illustrationWidth;
				int scaledHeight = (int)(scaledWidth*((double)h/(double)w));
				illustrationLabel = new JLabel(new ImageIcon((new ImageIcon(scanConversionIllustration)).getImage().getScaledInstance(scaledWidth, scaledHeight, java.awt.Image.SCALE_SMOOTH)));
				panel.add(illustrationLabel);
			} catch (IOException ex) {

			}
		}
		
			
		return flags;
	}

	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr) 
	{
		this.pfr = pfr;

		GenericDialog gd = new GenericDialog("Scan Conversion...");
		gd.addPanel(panel);
		gd.addNumericField("Radial_length_in_pixels", convertedRadialLength, 0);
		gd.addNumericField("Blanked_length_in_pixels", blankedLength, 0);
		gd.addNumericField("Max_half_angle_in_degrees", maxHalfAngleInDegrees, 3);
		gd.addCheckbox("Rotate_right_90_degrees", rotateRight);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return DONE;
		}
			
		convertedRadialLength = (int)gd.getNextNumber();
		blankedLength = (int)gd.getNextNumber();
		maxHalfAngleInDegrees = gd.getNextNumber();
		rotateRight = gd.getNextBoolean();
		maxHalfAngleInRadians = Math.toRadians((double)maxHalfAngleInDegrees);

		rMin = blankedLength;
		rMax = rMin + convertedRadialLength;
		if (Math.abs(maxHalfAngleInRadians) >= 0.5*Math.PI) {
			trueXMin = (int)((double)rMax*Math.cos(maxHalfAngleInRadians));
			trueXMax = rMax;
			trueYMin = -rMax;
			trueYMax = rMax;
		}
		else {
			trueXMin = (int)((double)rMin*Math.cos(maxHalfAngleInRadians));
			trueXMax = rMax;
			trueYMin = (int)((double)rMax*Math.sin(-maxHalfAngleInRadians));
			trueYMax = -trueYMin;
		}
		convertedWidth = trueXMax - trueXMin;
		convertedHeight = trueYMax - trueYMin;

		if (rotateRight) {
			resizedImp = IJ.createImage(name+"-scan_converted", "32-bit", convertedHeight, convertedWidth, stackSize);
		} else {
			resizedImp = IJ.createImage(name+"-scan_converted", "32-bit", convertedWidth, convertedHeight, stackSize);
		}
		resizedStack = resizedImp.getStack();

		return flags;
	}

	public void run(ImageProcessor ip) 
	{
		ImageProcessor resized_ip;
		int currentSlice = pfr.getSliceNumber();
		int trueX;
		int trueY;
		double radiusIndex;
		double angleIndex;
		float[] pixels;
		float[] converted_pixels;

		// scan conversion of the resized image adds processing time but gives nicer results
		ip.setInterpolationMethod(ImageProcessor.BILINEAR);
		resized_ip = ip.resize(convertedWidth, convertedHeight);
		pixels = (float[])resized_ip.getPixels();
		converted_pixels = new float[pixels.length];
		for (int y=0; y<convertedHeight; y++) {
			trueY = y + trueYMin;
			for (int x=0; x<convertedWidth; x++) {
				trueX = x + trueXMin;
				radiusIndex = getRadiusIndex(trueX, trueY, rMin, rMax, convertedWidth);
				angleIndex = getAngleIndex(trueX, trueY, convertedHeight, maxHalfAngleInRadians);
				if ((radiusIndex < (double)convertedWidth) && (radiusIndex >= 0.0) && (angleIndex < (double)convertedHeight) && (angleIndex >= 0.0)) {
					converted_pixels[ (y*convertedWidth) + x ] = (float)resized_ip.getInterpolatedValue(radiusIndex, angleIndex);
				}
			}
		}

		float[] temp = (float[])resizedStack.getProcessor(currentSlice).getPixels();
		if (rotateRight) {
			for (int i=0; i<convertedHeight; i++) {
				int offset = i*convertedWidth;
				for (int j=0; j<convertedWidth; j++) {
					temp[j*convertedHeight+(convertedHeight-i-1)] = converted_pixels[offset+j];
				}
			}	
		} else {
			System.arraycopy(converted_pixels, 0, temp, 0, pixels.length);
		}
		
	}

	public void setNPasses(int nPasses)
	{
	}

	// Returns fractional row (i.e. "angle") of rotational scan point corresponding to real-world (integer) cartesian point [x,y]
	private double getAngleIndex(int x, int y, int numberOfAngles, double maxHalfAngleInRadians) 
	{
		double angleInRadians = Math.atan2((double)y, (double)x);
		double a = (double)numberOfAngles/2.0;
		return ((a*angleInRadians/maxHalfAngleInRadians) + a);
	} 
	
	// Returns fractional column (i.e. "radius") of rotational scan point corresponding to real-world (integer) cartesian point [x,y]
	private double getRadiusIndex(int x, int y, int rMin, int rMax, int maxRowIndex) 
	{
		double radius = Math.sqrt(x*x + y*y);
		double rRatio = (radius-(double)rMin)/(double)(rMax-rMin);
		return (rRatio*(double)maxRowIndex);
	} 

}
