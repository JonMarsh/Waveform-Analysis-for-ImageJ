package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.Kurtosis;
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


public class KurtosisTest
{
	
	public KurtosisTest()
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
	 * Test of execute method, of class Kurtosis.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1, 2, 5, 3, 2, 3, 1};
		int recordLength = 7;
		float[] expResult = new float[] {-0.33045806067816774f};
		float[] result = Kurtosis.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
		
		waveforms = new float[] {1, 2, 5, 3, 2, 3, 1, 1};
		recordLength = 4;
		expResult = new float[] {-1.1542857142857144f, -1.371900826446281f};
		result = Kurtosis.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

	}

	/**
	 * Test of execute method, of class Kurtosis.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1, 2, 5, 3, 2, 3, 1};
		int recordLength = 7;
		double[] expResult = new double[] {-0.33045806067816774};
		double[] result = Kurtosis.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
		
		waveforms = new double[] {1, 2, 5, 3, 2, 3, 1, 1};
		recordLength = 4;
		expResult = new double[] {-1.1542857142857144, -1.371900826446281};
		result = Kurtosis.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
	}

}
