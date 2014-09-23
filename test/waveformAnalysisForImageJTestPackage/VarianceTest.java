package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.Variance;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class VarianceTest
{
	
	public VarianceTest()
	{
	}
	
	@BeforeClass
	public static void setUpClass()
	{
	}
	
	@AfterClass
	public static void tearDownClass()
	{
	}
	
	@Before
	public void setUp()
	{
	}
	
	@After
	public void tearDown()
	{
	}

	/**
	 * Test of execute method, of class Variance.
	 */
	@Test
	public void testExecute_3args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1, -1, 3, 2, 5, 9, -11, 4};
		int recordLength = 8;
		boolean useUnbiasedEstimate = false;
		float[] expResult = new float[] {30.0f};
		float[] result = Variance.execute(waveforms, recordLength, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
		
		waveforms = new float[] {1, -1, 3, 2, 5, 9, -11, 4};
		recordLength = 8;
		useUnbiasedEstimate = true;
		expResult = new float[] {34.285714285714285f};
		result = Variance.execute(waveforms, recordLength, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
		
		waveforms = new float[] {1, -1, 3, 2, 5, 9, -11, 4};
		recordLength = 4;
		useUnbiasedEstimate = true;
		expResult = new float[] {2.9166666666666667f, 76.91666666666667f};
		result = Variance.execute(waveforms, recordLength, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
	}

	/**
	 * Test of execute method, of class Variance.
	 */
	@Test
	public void testExecute_3args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1, -1, 3, 2, 5, 9, -11, 4};
		int recordLength = 8;
		boolean useUnbiasedEstimate = false;
		double[] expResult = new double[] {30.0};
		double[] result = Variance.execute(waveforms, recordLength, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(expResult[0]));
		
		waveforms = new double[] {1, -1, 3, 2, 5, 9, -11, 4};
		recordLength = 8;
		useUnbiasedEstimate = true;
		expResult = new double[] {34.285714285714285};
		result = Variance.execute(waveforms, recordLength, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(expResult[0]));
		
		waveforms = new double[] {1, -1, 3, 2, 5, 9, -11, 4};
		recordLength = 4;
		useUnbiasedEstimate = true;
		expResult = new double[] {2.9166666666666667, 76.91666666666667};
		result = Variance.execute(waveforms, recordLength, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(expResult[1]));
	}

}
