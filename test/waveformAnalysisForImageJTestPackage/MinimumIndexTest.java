package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MinimumIndex;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MinimumIndexTest
{
	
	public MinimumIndexTest()
	{
	}


	/**
	 * Test of execute method, of class MinimumIndex.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, 1.0f, 4.0f, 5.0f, -1.0f, 0.0f};
		int recordLength = 10;
		int[] expResult = new int[] {8};
		int[] result = MinimumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);

		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, 1.0f, 4.0f, 5.0f, -1.0f, 0.0f};
		recordLength = 5;
		expResult = new int[] {0, 3};
		result = MinimumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);
	}

	/**
	 * Test of execute method, of class MinimumIndex.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, 1.0, 4.0, 5.0, -1.0, 0.0};
		int recordLength = 10;
		int[] expResult = new int[] {8};
		int[] result = MinimumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);

		waveforms = new double[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, 1.0f, 4.0f, 5.0f, -1.0f, 0.0f};
		recordLength = 5;
		expResult = new int[] {0, 3};
		result = MinimumIndex.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);
	}

}
