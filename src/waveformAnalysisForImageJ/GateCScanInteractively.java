package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImageListener;
import ij.ImagePlus;
import ij.ImageStack;
import ij.WindowManager;
import ij.gui.ImageCanvas;
import ij.gui.ImageWindow;
import ij.gui.PolygonRoi;
import ij.gui.Roi;
import ij.plugin.PlugIn;
import ij.plugin.frame.RoiManager;
import ij.process.ImageProcessor;
import ij.plugin.filter.RankFilters;
import ij.plugin.filter.GaussianBlur;
import java.awt.Button;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author jnm
 * @version 2014-09-24
 */
public class GateCScanInteractively implements PlugIn
{
	static int autoStartSearchPoint = 0;
	static int offsetPoint = 0;
	static int gateLength = 10;
	static float threshold = 0.0f;
	static boolean searchBackwards = false;
	static final int MEDIAN = 0;
	static final int GAUSSIAN = 1;
	static final int MEAN = 2;
	static final String[] filters = {"Median", "Gaussian", "Mean"};
	static int filterSelection = MEDIAN;
	static double smoothingRadius = 1.0;
	static boolean outputGatePositions = true;
	static boolean outputGateROIs = true;
	static boolean outputGatedSegments = true;
	static boolean outputGatedWaveforms = true;
	static String pluginTitle = "Create gates for ";
	static GateCScanInteractively instance;

	ImagePlus inputImage, gateImage;
	ImageStack stack;
	ImageCanvas gateCanvas;
	boolean gatesExist = false;
	int recordLength, recordsPerFrame, numberOfFrames, currentSlice, inputImageID;
	double[] globalMinAndMax;
	String title;
	String[] suitableImages;
	int[] suitableImageIDs;
	ImageProcessor gateProcessor;

	/**
	 * Run function required by PlugIn interface
	 */
	public void run(String arg)
	{
		if (instance == null) {
			inputImage = IJ.getImage();
			if (inputImage != null) {
				instance = this;
				inputImageID = inputImage.getID();
				stack = inputImage.getStack();
				title = inputImage.getTitle();
				recordLength = inputImage.getWidth();
				if (recordLength >= Short.MAX_VALUE) {
					IJ.showMessage("This plugin may not behave properly with image widths greather than 2^15 - 1");
				}
				recordsPerFrame = inputImage.getHeight();
				numberOfFrames = inputImage.getImageStackSize();
				currentSlice = inputImage.getSlice();
				globalMinAndMax = WaveformUtils.getGlobalMinAndMax(inputImage);
				gateImage = IJ.createImage(pluginTitle + title, "16-bit", recordsPerFrame, numberOfFrames, 1);
				gateProcessor = gateImage.getProcessor();
				gateCanvas = new ImageCanvas(gateImage);
				suitableImageIDs = getSuitableImageIDs(inputImage, gateImage);

				CreateGatesForCScanDialog createGatesForCScanDialog = new CreateGatesForCScanDialog(gateImage, gateCanvas);
			} else {
				IJ.noImage();
			}
		} else {
			WindowManager.toFront(WindowManager.getFrame(gateImage.getTitle()));
		}
	}

	/**
	 * Inner class for interactive dialog gate creation
	 */
	private class CreateGatesForCScanDialog extends ImageWindow implements ActionListener, ImageListener
	{

		GateCScanInteractivelyControlPanel panel;

		/**
		 * Constructor
		 */
		CreateGatesForCScanDialog(ImagePlus gateImagePlus, ImageCanvas gateImageCanvas)
		{
			super(gateImagePlus, gateImageCanvas);
			addPanel();
			ImagePlus.addImageListener(this);
		}

		/*
		 * Add control panel underneath gate image on user interface window
		 */
		private void addPanel()
		{
			panel = new GateCScanInteractivelyControlPanel();

			panel.searchStartPointTextField.setText("" + autoStartSearchPoint);
			panel.offsetTextField.setText("" + offsetPoint);
			panel.thresholdTextField.setText(IJ.d2s(threshold, 3));
			panel.gateLengthTextField.setText("" + gateLength);
			panel.searchBackwardsCheckbox.setState(searchBackwards);
			panel.createGatesButton.addActionListener(this);
			panel.smoothGatesButton.addActionListener(this);
			for (String filter : filters) {
				panel.filterChoice.add(filter);
			}
			panel.filterChoice.select(filterSelection);
			panel.smoothingRadiusTextField.setText(IJ.d2s(smoothingRadius, 3));
			panel.outputGatePositionsCheckbox.setState(outputGatePositions);
			panel.outputGateROIsCheckbox.setState(outputGateROIs);
			panel.outputGateRegionsCheckbox.setState(outputGatedSegments);
			panel.outputGatedWaveformsCheckbox.setState(outputGatedWaveforms);
			for (int imageID : suitableImageIDs) {
				panel.outputFileChoice.add(WindowManager.getImage(imageID).getTitle());
			}
			panel.cancelButton.addActionListener(this);
			panel.okButton.addActionListener(this);

			add(panel);
			pack();
		}

		/**
		 * Respond to button press on front panel
		 */
		public void actionPerformed(ActionEvent e)
		{
			Button source = (Button)e.getSource();
			autoStartSearchPoint = Integer.parseInt(panel.searchStartPointTextField.getText());
			offsetPoint = Integer.parseInt(panel.offsetTextField.getText());
			threshold = Float.parseFloat(panel.thresholdTextField.getText());
			gateLength = Integer.parseInt(panel.gateLengthTextField.getText());
			smoothingRadius = Double.parseDouble(panel.smoothingRadiusTextField.getText());
			searchBackwards = panel.searchBackwardsCheckbox.getState();

			if (source == panel.createGatesButton) {
				panel.createGatesButton.setLabel("Working...");
				short[] gatePositions = computeGateStartPositionsForStack(stack, autoStartSearchPoint, offsetPoint, threshold);
				gateProcessor.setPixels(gatePositions);
				gatesExist = true;
				IJ.resetMinAndMax();
				short[] gatePositionsForDisplayedSlice = Arrays.copyOfRange(gatePositions, (currentSlice - 1) * recordsPerFrame, currentSlice * recordsPerFrame);
				drawSingleROI(WindowManager.getImage(inputImageID), gatePositionsForDisplayedSlice, gateLength, searchBackwards);
				panel.createGatesButton.setLabel("Create gates");
			}

			if (source == panel.smoothGatesButton) {
				if (gatesExist) {
					filterSelection = panel.filterChoice.getSelectedIndex();
					switch (filterSelection) {
						case MEDIAN: {
							RankFilters rf = new RankFilters();
							rf.rank(gateProcessor, smoothingRadius, RankFilters.MEDIAN);
							break;
						}
						case GAUSSIAN: {
							GaussianBlur gb = new GaussianBlur();
							gb.blurGaussian(gateProcessor, smoothingRadius, smoothingRadius, 0.01);
							break;
						}
						case MEAN: {
							RankFilters rf = new RankFilters();
							rf.rank(gateProcessor, smoothingRadius, RankFilters.MEAN);
							break;
						}
						default:
					}
					gateImage.updateAndDraw();
					IJ.resetMinAndMax();
					short[] gatePositions = (short[]) gateProcessor.getPixels();
					short[] gatePositionsForDisplayedSlice = Arrays.copyOfRange(gatePositions, (currentSlice - 1) * recordsPerFrame, currentSlice * recordsPerFrame);
					drawSingleROI(WindowManager.getImage(inputImageID), gatePositionsForDisplayedSlice, gateLength, searchBackwards);
				}
			}

			if (source == panel.cancelButton) {
				ImagePlus.removeImageListener(this);
				this.close();
				instance = null;
			}

			if (source == panel.okButton) {
				if (gatesExist) {

					/*
					 * Assign user values to variables used to indicate
					 * checkbox selections -- this is done only to ensure the
					 * static variables are reinitialized to previous values
					 * next time plugin is called
					 */
					searchBackwards = panel.searchBackwardsCheckbox.getState();
					outputGateROIs = panel.outputGateROIsCheckbox.getState();
					outputGatePositions = panel.outputGatePositionsCheckbox.getState();
					outputGatedSegments = panel.outputGateRegionsCheckbox.getState();
					outputGatedWaveforms = panel.outputGatedWaveformsCheckbox.getState();

					int selectedImageID = suitableImageIDs[panel.outputFileChoice.getSelectedIndex()]; // apply gating to the desired image (not necessarily the image the gates were computed with)
					short[] gatePositions = (short[])gateProcessor.getPixels();

					if (outputGateROIs == true) {
						RoiManager rm = RoiManager.getInstance();
						if (rm == null) {
							rm = new RoiManager();
						}
						for (int i = 0; i < WindowManager.getImage(inputImageID).getStackSize(); i++) {
							WindowManager.getImage(inputImageID).setSlice(i + 1);
							short[] gatePositionsForThisSlice = Arrays.copyOfRange(gatePositions, i * recordsPerFrame, (i + 1) * recordsPerFrame);
							PolygonRoi roi = createSingleROI(gatePositionsForThisSlice, gateLength, searchBackwards);
							rm.add(WindowManager.getImage(inputImageID), roi, -1);
						}
					}
					if (outputGatedSegments == true) {
						ImagePlus gatedImage = createGatedImage(WindowManager.getImage(selectedImageID), gatePositions, gateLength);
						gatedImage.show();
						IJ.resetMinAndMax();
					}
					if (outputGatePositions == true) {
						ImagePlus borderImage = new ImagePlus(title + " gate positions", gateProcessor.duplicate());
						borderImage.show();
					}
					if (outputGatedWaveforms == true) {
						ImagePlus gatedWaveformImage = createdGatedWaveformImage(WindowManager.getImage(selectedImageID), gatePositions, gateLength);
						gatedWaveformImage.show();
						IJ.resetMinAndMax();
					}
				}
				ImagePlus.removeImageListener(this);
				this.close();
				instance = null;
			}
		}

		/**
		 * Respond to image being closed
		 */
		public void imageClosed(ImagePlus image)
		{
			if (image.getID() == inputImageID) {
				IJ.error("Source image closed!");
				ImagePlus.removeImageListener(this);
				this.close();
				instance = null;
			} else if (image.getID() == instance.gateImage.getID()) {
				ImagePlus.removeImageListener(this);
				this.close();
				instance = null;
			} else {
				suitableImageIDs = getSuitableImageIDs(inputImage, gateImage);
				panel.outputFileChoice.removeAll();
				for (int imageID : suitableImageIDs) {
					panel.outputFileChoice.add(WindowManager.getImage(imageID).getTitle());
				}
			}
		}

		/**
		 * Respond to image being opened
		 */
		public void imageOpened(ImagePlus image)
		{
			suitableImageIDs = getSuitableImageIDs(inputImage, gateImage);
			panel.outputFileChoice.removeAll();
			for (int imageID : suitableImageIDs) {
				panel.outputFileChoice.add(WindowManager.getImage(imageID).getTitle());
			}
		}

		/**
		 * Respond to current image being updated by scrolling through frames
		 */
		public void imageUpdated(ImagePlus image)
		{
			if (image.getID() == inputImageID) {
				if (image.getSlice() != currentSlice) {
					currentSlice = image.getSlice();
					if (gatesExist) {
						short[] gatePositions = (short[])gateProcessor.getPixels();
						short[] gatePositionsForDisplayedSlice = Arrays.copyOfRange(gatePositions, (currentSlice - 1) * recordsPerFrame, currentSlice * recordsPerFrame);
						drawSingleROI(image, gatePositionsForDisplayedSlice, gateLength, searchBackwards);
					}
				}
			}
		}

	}

	/**
	 * Create new ImagePlus from gated segments of input image
	 */
	private ImagePlus createGatedImage(ImagePlus inputImage, short[] gateStartPositions, int gateLength)
	{
		int recordLength = inputImage.getWidth();
		int recordsPerFrame = inputImage.getHeight();
		int stackSize = inputImage.getStackSize();
		ImagePlus gatedImage = IJ.createImage(inputImage.getTitle() + "_gatedSegments", "32-bit", gateLength, recordsPerFrame, stackSize);
		ImageStack gatedStack = gatedImage.getStack();
		ImageStack inputStack = inputImage.getStack();
		for (int slice = 1; slice <= stackSize; slice++) {
			float[] inputPixels = (float[]) inputStack.getPixels(slice);
			float[] gatedPixels = (float[]) gatedStack.getPixels(slice);
			for (int i = 0; i < recordsPerFrame; i++) {
				int gateIndex = ((slice - 1) * recordsPerFrame) + i;
				if (gateStartPositions[gateIndex] < 0) { // gate position set to -1, so no boundary detected; just set start of gate to 0
					System.arraycopy(inputPixels, i * recordLength, gatedPixels, i * gateLength, gateLength);
				} else if (gateStartPositions[gateIndex] + gateLength >= recordLength) { // end of gate set past end of waveform, so just pad with zeroes
					System.arraycopy(inputPixels, (i * recordLength) + gateStartPositions[gateIndex], gatedPixels, i * gateLength, recordLength - gateStartPositions[i]);
				} else {	// gate is within waveform limits
					System.arraycopy(inputPixels, (i * recordLength) + gateStartPositions[gateIndex], gatedPixels, i * gateLength, gateLength);
				}
			}
		}

		return gatedImage;
	}

	/**
	 * Create new ImagePlus with gated full-length waveforms (zero-padded)
	 */
	private ImagePlus createdGatedWaveformImage(ImagePlus inputImage, short[] gateStartPositions, int gateLength)
	{
		int recordLength = inputImage.getWidth();
		int recordsPerFrame = inputImage.getHeight();
		int stackSize = inputImage.getStackSize();
		ImagePlus gatedImage = IJ.createImage(inputImage.getTitle() + "_gatedWaveforms", "32-bit", recordLength, recordsPerFrame, stackSize);
		ImageStack gatedStack = gatedImage.getStack();
		ImageStack inputStack = inputImage.getStack();
		for (int slice = 1; slice <= stackSize; slice++) {
			float[] inputPixels = (float[]) inputStack.getPixels(slice);
			float[] gatedPixels = (float[]) gatedStack.getPixels(slice);
			for (int i = 0; i < recordsPerFrame; i++) {
				int gateIndex = ((slice - 1) * recordsPerFrame) + i;
				if (gateStartPositions[gateIndex] < 0) { // gate position set to -1, so no boundary detected; just set start of gate to 0
					System.arraycopy(inputPixels, i * recordLength, gatedPixels, i * gateLength, gateLength);
				} else if (gateStartPositions[gateIndex] + gateLength >= recordLength) { // end of gate set past end of waveform, so just pad with zeroes
					System.arraycopy(inputPixels, (i * recordLength) + gateStartPositions[gateIndex], gatedPixels, (i * recordLength) + gateStartPositions[gateIndex], recordLength - gateStartPositions[i]);
				} else {	// gate is within waveform limits
					System.arraycopy(inputPixels, (i * recordLength) + gateStartPositions[gateIndex], gatedPixels, (i * recordLength) + gateStartPositions[gateIndex], gateLength);
				}
			}
		}

		return gatedImage;
	}

	/**
	 * Substitute nearest valid gate start position for all positions where no
	 * valid gates were detected. Employs Bresenham's circle algorithm, adapted
	 * from implementation by Alois Zingl at
	 * http://members.chello.at/easyfilter/bresenham.html
	 */
	private void nearestNeighborInterpolate(short[] gateStartPositions, int recordsPerFrame, int stackSize)
	{
		short[] interpolatedGateStartPositions = Arrays.copyOf(gateStartPositions, gateStartPositions.length);
		int rMax = (int) Math.sqrt(recordsPerFrame * recordsPerFrame + stackSize * stackSize);
		for (int y0 = 0; y0 < stackSize; y0++) {
			int offset = y0 * recordsPerFrame;
			for (int x0 = 0; x0 < recordsPerFrame; x0++) {
				int position = gateStartPositions[offset + x0];
				if (position < 0) {
					// look for closest point that is not equal to -1 using Bresenham's circle algorithm
					for (int radius = 1; radius <= rMax; radius++) {
						int sum = 0, nValid = 0, r = radius, x = -r, y = 0, err = 2 - 2*r;
						int[] px = new int[4];
						int[] py = new int[4];
						do {
							px[0] = x0-x;
							py[0] = y0+y;
							px[1] = x0-y;
							py[1] = y0-x;
							px[2] = x0+x;
							py[2] = y0-y;
							px[3] = x0+y;
							py[3] = y0+x;
							for (int i=0; i<px.length; i++) {
								if (px[i] >= 0 && px[i] < recordsPerFrame && py[i] >= 0 && py[i] < stackSize) {
									short gateValue = gateStartPositions[py[i]*recordsPerFrame + px[i]];
									if (gateValue >= 0) {
										sum += gateValue;
										nValid++;
									}
								}
							}
							r = err;
							if (r <= y) {
								err += ++y * 2 + 1;
							}
							if (r > x || err > y) {
								err += ++x * 2 + 1;
							}
						} while (x < 0 && nValid == 0);
						if (nValid > 0) {
							interpolatedGateStartPositions[offset + x0] = (short) (sum / nValid);
							break;
						}
					}
				}
			}
		}
		System.arraycopy(interpolatedGateStartPositions, 0, gateStartPositions, 0, gateStartPositions.length);
	}
	
	/**
	 * Computes gate start positions for entire stack
	 */
	private short[] computeGateStartPositionsForStack(ImageStack stack, int searchStartPoint, int offsetPoint, float threshold)
	{
		int recordLength = stack.getWidth();
		int recordsPerFrame = stack.getHeight();
		int stackSize = stack.getSize();
		short[] gateStartPositions = new short[recordsPerFrame * stackSize];
		float[] pixelValues;

		// check to make sure there is a possibility of detecting a peak above the specified threshold
		if (threshold < globalMinAndMax[1]) {
			for (int slice = 1; slice <= stackSize; slice++) {
				pixelValues = (float[]) stack.getProcessor(slice).convertToFloat().getPixels();
				short[] sliceGates = computeGateStartPositions(pixelValues, recordLength, recordsPerFrame, searchStartPoint, offsetPoint, threshold);
				System.arraycopy(sliceGates, 0, gateStartPositions, (slice - 1) * recordsPerFrame, recordsPerFrame);
			}

			// substitute nearest valid gate start position for all positions where no valid gates were detected
			nearestNeighborInterpolate(gateStartPositions, recordsPerFrame, stackSize);
		}

		return gateStartPositions;
	}

	/**
	 * Computes gate start positions for individual slice, assumed to be
	 * represented by data in 'pixels'
	 */
	private short[] computeGateStartPositions(float[] pixels, int recordLength, int numberOfRecords, int searchStartPoint, int offsetPoint, float threshold)
	{
		float[] tempArray = new float[recordLength];
		short[] gateStartPositions = new short[numberOfRecords];

		for (int i = 0; i < numberOfRecords; i++) {
			if (searchBackwards) {
				for (int j = 0, k = ((i + 1) * recordLength) - 1 - searchStartPoint; k >= i * recordLength; j++, k--) {
					tempArray[j] = pixels[k];
				}
				int gateStart = peakDetect(tempArray, threshold);
				gateStartPositions[i] = (gateStart >= 0) ? (short)(recordLength - (gateStart + offsetPoint + searchStartPoint) - 1) : (short) (-1);
			} else {
				System.arraycopy(pixels, (i * recordLength) + searchStartPoint, tempArray, 0, recordLength - searchStartPoint);
				int gateStart = peakDetect(tempArray, threshold);
				gateStartPositions[i] = (gateStart >= 0) ? (short)(gateStart + offsetPoint + searchStartPoint) : (short) (-1);
			}

		}

		return gateStartPositions;
	}

	/**
	 * Draw single ROI onto current slice of input image
	 */
	private void drawSingleROI(ImagePlus image, short[] gateStartPositions, int gateLength, boolean reverseSearch)
	{
		image.setRoi(createSingleROI(gateStartPositions, gateLength, reverseSearch));
	}

	/**
	 * Create single ROI for current slice
	 */
	private PolygonRoi createSingleROI(short[] gateStartPositions, int gateLength, boolean reverseSearch)
	{
		int direction = reverseSearch ? -1 : 1;
		int length = gateStartPositions.length;
		int[] xPoints = new int[length * 2];
		int[] yPoints = new int[length * 2];
		for (int i = 0; i < length; i++) {
			xPoints[i] = gateStartPositions[i];
			xPoints[length + i] = gateStartPositions[(length - 1) - i] + direction * gateLength;
			yPoints[i] = i;
			yPoints[length + i] = (length - 1) - i;
		}
		return new PolygonRoi(new Polygon(xPoints, yPoints, length * 2), Roi.POLYGON);
	}

	/**
	 * Returns the index of the first peak value found which exceeds the
	 * specified threshold. Returns -1 if no peaks above threshold are detected.
	 */
	private int peakDetect(float[] a, float threshold)
	{
		int i;

		for (i = 1; i < a.length - 1; i++) {
			if (a[i] > threshold && a[i - 1] < a[i] && a[i + 1] < a[i]) {
				break;
			}
		}

		return (i >= a.length - 2) ? -1 : i;
	}
	
	private int[] getSuitableImageIDs(ImagePlus impToCheckAgainst, ImagePlus gateImage)
	{
		int width = impToCheckAgainst.getWidth();
		int height = impToCheckAgainst.getHeight();
		int depth = impToCheckAgainst.getStackSize();
		int channels = impToCheckAgainst.getProcessor().getNChannels();
		int thisID = impToCheckAgainst.getID();
		int[] fullList = WindowManager.getIDList();
		ArrayList<Integer> suitableIDs = new ArrayList<Integer>(); //will hold image ID's of suitable images
		suitableIDs.add(thisID); // make sure the first element is the image we're comparing everything to
		for (int imageIDtoCheck : fullList) {
			ImagePlus impFromList = WindowManager.getImage(imageIDtoCheck);
			if (impFromList.getWidth() == width && impFromList.getHeight() == height && impFromList.getProcessor().getNChannels() == channels && impFromList.getStackSize() == depth && imageIDtoCheck != thisID && imageIDtoCheck != gateImage.getID()) {
				if (!suitableIDs.contains(imageIDtoCheck)) {
					suitableIDs.add(imageIDtoCheck); // enter only if a different image than the one we're checking against
				}
			}
		}
		int[] id = new int[suitableIDs.size()];
		for (int i = 0; i < suitableIDs.size(); i++) {
			id[i] = suitableIDs.get(i);
		}

		return id;
	}
}
