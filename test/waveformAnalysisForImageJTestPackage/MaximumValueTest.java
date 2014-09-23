package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MaximumValue;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MaximumValueTest
{
	
	public MaximumValueTest()
	{
	}

	/**
	 * Test of execute method, of class MaximumValue.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f};
		int recordLength = 10;
		float[] expResult = new float[] {5.0f};
		float[] result = MaximumValue.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0f);
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f};
		recordLength = 5;
		expResult = new float[] {5.0f, 5.0f};
		result = MaximumValue.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0f);
	}

	/**
	 * Test of execute method, of class MaximumValue.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0};
		int recordLength = 10;
		double[] expResult = new double[] {5.0};
		double[] result = MaximumValue.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0);
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0};
		recordLength = 5;
		expResult = new double[] {5.0, 5.0};
		result = MaximumValue.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0);
	}
	
}
