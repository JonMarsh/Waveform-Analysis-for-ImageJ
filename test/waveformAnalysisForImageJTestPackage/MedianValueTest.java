package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MedianValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MedianValueTest
{
	
	public MedianValueTest()
	{
	}

	/**
	 * Test of execute method, of class MedianValue.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 3.0f, 2.0f, 2.2f, -1.0f, 4.4f, 10.0f, -9.0f, 5.0f};
		int recordLength = 5;
		float[] expResult = new float[] {2.2f, 4.4f};
		float[] result = MedianValue.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0f);
	}

	/**
	 * Test of execute method, of class MedianValue.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 3.0, 2.0, 2.2, -1.0, 4.4, 10.0, -9.0, 5.0};
		int recordLength = 5;
		double[] expResult = new double[] {2.2, 4.4};
		double[] result = MedianValue.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0);
	}

}
