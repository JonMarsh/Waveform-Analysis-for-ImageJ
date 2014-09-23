package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.AlignWaveforms;
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


public class AlignWaveformsTest
{
	
	public AlignWaveformsTest()
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
	 * Test of execute method, of class AlignWaveforms.
	 */
	@Test
	public void testExecute_4args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {2.0f, 3.0f, 2.0f, 5.0f, 1.0f, 0.0f, 2.0f, 1.0f, 6.0f, 1.0f, 0.0f, 1.0f};
		float[] seedWaveform = new float[] {2.0f, 3.0f, 2.0f, 5.0f, 1.0f, 0.0f};
		int from = 0;
		int to = 6;
		AlignWaveforms.execute(waveforms, seedWaveform, from, to);
		float[] expResult = new float[] {2.0f, 3.0f, 2.0f, 5.0f, 1.0f, 0.0f, 1.0f, 2.0f, 1.0f, 6.0f, 1.0f, 0.0f};
		assertArrayEquals(expResult, waveforms, 0.0f);
	}

	/**
	 * Test of execute method, of class AlignWaveforms.
	 */
	@Test
	public void testExecute_4args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {2.0, 3.0, 2.0, 5.0, 1.0, 0.0, 2.0, 1.0, 6.0, 1.0, 0.0, 1.0};
		double[] seedWaveform = new double[] {2.0, 3.0, 2.0, 5.0, 1.0, 0.0};
		int from = 0;
		int to = 6;
		AlignWaveforms.execute(waveforms, seedWaveform, from, to);
		double[] expResult = new double[] {2.0, 3.0, 2.0, 5.0, 1.0, 0.0, 1.0, 2.0, 1.0, 6.0, 1.0, 0.0};
		assertArrayEquals(expResult, waveforms, 0.0);
	}
}
