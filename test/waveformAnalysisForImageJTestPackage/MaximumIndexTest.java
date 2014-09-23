package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MaximumIndex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MaximumIndexTest
{
	
	/**
	 * Test of execute method, of class MaximumIndex.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f};
		int recordLength = 10;
		int[] expResult = new int[] {1};
		int[] result = MaximumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);

		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f};
		recordLength = 5;
		expResult = new int[] {1, 2};
		result = MaximumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of execute method, of class MaximumIndex.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0};
		int recordLength = 10;
		int[] expResult = new int[] {1};
		int[] result = MaximumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);

		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0};
		recordLength = 5;
		expResult = new int[] {1, 2};
		result = MaximumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);
	}

}
