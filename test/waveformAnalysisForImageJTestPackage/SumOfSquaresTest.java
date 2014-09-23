package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.SumOfSquares;
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


public class SumOfSquaresTest
{
	
	public SumOfSquaresTest()
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
	 * Test of execute method, of class SumOfSquares.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1, 2, -1, -8, 4, 5, -2, 0};
		int recordLength = 8;
		float[] expResult = new float[] {115.0f};
		float[] result = SumOfSquares.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(expResult[0]));

		waveforms = new float[] {1, 2, -1, -8, 4, 5, -2, 0};
		recordLength = 4;
		expResult = new float[] {70.0f, 45.0f};
		result = SumOfSquares.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(expResult[0]));
	}

	/**
	 * Test of execute method, of class SumOfSquares.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1, 2, -1, -8, 4, 5, -2, 0};
		int recordLength = 8;
		double[] expResult = new double[] {115.0};
		double[] result = SumOfSquares.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(expResult[0]));

		waveforms = new double[] {1, 2, -1, -8, 4, 5, -2, 0};
		recordLength = 4;
		expResult = new double[] {70.0, 45.0};
		result = SumOfSquares.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(expResult[0]));
	}

}
