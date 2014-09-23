package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.Skewness;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class SkewnessTest
{
	
	public SkewnessTest()
	{
	}

	/**
	 * Test of execute method, of class Skewness.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		int recordLength = 8;
		boolean isSampleSkewness = false;
		float[] expResult = new float[] {-0.5465406598681920f, 0.5478861364760020f};
		float[] result = Skewness.execute(waveforms, recordLength, isSampleSkewness);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		isSampleSkewness = true;
		expResult = new float[] {-0.43820740653602647f, 0.4392861877835291f};
		result = Skewness.execute(waveforms, recordLength, isSampleSkewness);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
	}

	/**
	 * Test of execute method, of class Skewness.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		int recordLength = 8;
		boolean isSampleSkewness = false;
		double[] expResult = new double[] {-0.5465406598681920, 0.5478861364760020};
		double[] result = Skewness.execute(waveforms, recordLength, isSampleSkewness);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		isSampleSkewness = true;
		expResult = new double[] {-0.43820740653602647, 0.4392861877835291};
		result = Skewness.execute(waveforms, recordLength, isSampleSkewness);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

	}

}
