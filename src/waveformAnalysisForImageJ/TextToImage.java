package waveformAnalysisForImageJ;

import ij.IJ;
import ij.ImagePlus;
import ij.gui.DialogListener;
import ij.gui.GenericDialog;
import ij.gui.NonBlockingGenericDialog;
import ij.plugin.filter.ExtendedPlugInFilter;
import ij.plugin.filter.PlugInFilterRunner;
import ij.process.ImageProcessor;
import java.awt.AWTEvent;
import java.awt.Panel;
import java.awt.TextArea;
import ij.util.Tools;

/**
 * This plugin converts text input to a 32-bit grayscale image. Rows are
 * delimited by carriage returns or line-feeds, and columns are delimited with
 * spaces, tabs, or commas.
 *
 * @author Jon N. Marsh
 * @version 2015-03-17
 */
public class TextToImage implements ExtendedPlugInFilter, DialogListener
{

	private ImagePlus imp;
	private int width, height;
	private NonBlockingGenericDialog gd;
	private static String input;
	private final int flags = DOES_ALL + NO_IMAGE_REQUIRED + NO_CHANGES;

	public int setup(String arg, ImagePlus imp)
	{
		return flags;
	}

	public int showDialog(ImagePlus imp, String command, PlugInFilterRunner pfr)
	{
		gd = new NonBlockingGenericDialog("Convert Text to Image");
		gd.addMessage("Enter delimited text below:");
		Panel panel = new Panel();
		TextArea textArea = new TextArea(input, 16, 80, TextArea.SCROLLBARS_BOTH);
		panel.add(textArea);
		gd.addPanel(panel);
		gd.addDialogListener(this);

		gd.showDialog();
		if (gd.wasCanceled()) {
			return DONE;
		}

		input = textArea.getText();

		return flags;
	}

	public boolean dialogItemChanged(GenericDialog gd, AWTEvent e)
	{
		return true;
	}

	public void run(ImageProcessor ip)
	{
		String[] parsedRows = Tools.split(input, "\n\r");
		height = parsedRows.length;
		String[][] entries = new String[height][];
		int maxWidth = 0;
		for (int i = 0; i < height; i++) {
			entries[i] = Tools.split(parsedRows[i], " ,\t");
			if (entries[i].length > maxWidth) {
				maxWidth = entries[i].length;
			}
		}
		width = maxWidth;
		ImagePlus textImage = IJ.createImage("Text image", "32-bit", width, height, 1);
		ImageProcessor textProcessor = textImage.getProcessor();
		float[] pixels = (float[]) textProcessor.getPixels();
		for (int i = 0; i < height; i++) {
			int offset = i * width;
			for (int j = 0; j < entries[i].length; j++) {
				pixels[offset + j] = (float) Tools.parseDouble(entries[i][j]);
			}
		}
		textImage.show();
	}

	public void setNPasses(int nPasses)
	{
	}

}
