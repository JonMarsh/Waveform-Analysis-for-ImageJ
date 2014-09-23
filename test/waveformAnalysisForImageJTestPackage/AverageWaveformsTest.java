package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.AverageWaveforms;
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


public class AverageWaveformsTest
{
	
	public AverageWaveformsTest()
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
	 * Test of execute method, of class AverageWaveforms.
	 */
	@Test
	public void testExecute_floatArr_int()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		int recordLength = 4;
		float[] expResult = new float[] {3.0f, 4.0f, 5.0f, 6.0f};
		float[] result = AverageWaveforms.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0f);
	}

	/**
	 * Test of execute method, of class AverageWaveforms.
	 */
	@Test
	public void testExecute_doubleArr_int()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		int recordLength = 4;
		double[] expResult = new double[] {3.0, 4.0, 5.0, 6.0};
		double[] result = AverageWaveforms.execute(waveforms, recordLength);
		assertArrayEquals(expResult, result, 0.0);
	}

	
}
