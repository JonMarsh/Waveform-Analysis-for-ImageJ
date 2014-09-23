package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.ZeroCrossings;
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


public class ZeroCrossingsTest
{
	
	public ZeroCrossingsTest()
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
	 * Test of execute method, of class ZeroCrossings.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 1.0f, 2.0f, -1.0f, 1.0f, 2.0f, -1.0f, 2.0f};
		int recordLength = 8;
		int[] expResult = new int[] {4};
		int[] result = ZeroCrossings.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);
		
		waveforms = new float[] {1.0f, 1.0f, 2.0f, -1.0f, 1.0f, 2.0f, -1.0f, 2.0f};
		recordLength = 4;
		expResult = new int[] {1, 2};
		result = ZeroCrossings.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);		
		
		waveforms = new float[] {1.0f, 1.0f, 2.0f, 0.0f, 1.0f, 2.0f, 1.0f, 2.0f};
		recordLength = 8;
		expResult = new int[] {0};
		result = ZeroCrossings.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);		
	}

	/**
	 * Test of execute method, of class ZeroCrossings.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 1.0, 2.0, -1.0, 1.0, 2.0, -1.0, 2.0};
		int recordLength = 8;
		int[] expResult = new int[] {4};
		int[] result = ZeroCrossings.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);
		
		waveforms = new double[] {1.0, 1.0, 2.0, -1.0, 1.0, 2.0, -1.0, 2.0};
		recordLength = 4;
		expResult = new int[] {1, 2};
		result = ZeroCrossings.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);
		
		waveforms = new double[] {1.0, 1.0, 2.0, 0.0, 1.0, 2.0, 1.0, 2.0};
		recordLength = 8;
		expResult = new int[] {0};
		result = ZeroCrossings.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result);				
	}

}
