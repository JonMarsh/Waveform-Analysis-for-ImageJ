package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.Range;
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


public class RangeTest
{
	
	public RangeTest()
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
	 * Test of execute method, of class Range.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1, 2, -1, 5, 7, 8};
		int recordLength = 6;
		float[] expResult = new float[] {9.0f};
		float[] result = Range.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0f);
		
		waveforms = new float[] {1, 2, -1, 5, 7, 8};
		recordLength = 3;
		expResult = new float[] {3.0f, 3.0f};
		result = Range.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0f);
	}

	/**
	 * Test of execute method, of class Range.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1, 2, -1, 5, 7, 8};
		int recordLength = 6;
		double[] expResult = new double[] {9.0};
		double[] result = Range.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0);
		
		waveforms = new double[] {1, 2, -1, 5, 7, 8};
		recordLength = 3;
		expResult = new double[] {3.0, 3.0};
		result = Range.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0f);		
	}
	
}
