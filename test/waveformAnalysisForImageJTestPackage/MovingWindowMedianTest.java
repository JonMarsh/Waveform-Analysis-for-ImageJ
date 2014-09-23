package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MovingWindowMedian;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MovingWindowMedianTest
{
	
	public MovingWindowMedianTest()
	{
	}

	/**
	 * Test of execute method, of class MovingWindowMedian.
	 */
	@Test
	public void testExecute_3args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		int recordLength = 16;
		int radius = 2;
		float[] expResult = new float[] {2.0f, 3.0f, 2.2f, 2.2f, 2.2f, 3.0f, 2.2f, 0.0f, 3.2f, 0.0f, 0.0f, 1.1f, 1.1f, 1.1f, 1.1f, 2.2f};
		MovingWindowMedian.execute(waveforms, recordLength, radius);
		assertArrayEquals(expResult, waveforms, 0.0f);
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 2;
		expResult = new float[] {2.0f, 3.0f, 2.2f, 2.2f, 2.2f, 3.0f, 4.0f, 4.0f, 0.0f, 0.0f, 0.0f, 1.1f, 1.1f, 1.1f, 1.1f, 2.2f};
		MovingWindowMedian.execute(waveforms, recordLength, radius);
		assertArrayEquals(expResult, waveforms, 0.0f);
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 0;
		expResult = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		MovingWindowMedian.execute(waveforms, recordLength, radius);
		assertArrayEquals(expResult, waveforms, 0.0f);
	}

	/**
	 * Test of execute method, of class MovingWindowMedian.
	 */
	@Test
	public void testExecute_3args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		int recordLength = 16;
		int radius = 2;
		double[] expResult = new double[] {2.0, 3.0, 2.2, 2.2, 2.2, 3.0, 2.2, 0.0, 3.2, 0.0, 0.0, 1.1, 1.1, 1.1, 1.1, 2.2};
		MovingWindowMedian.execute(waveforms, recordLength, radius);
		assertArrayEquals(expResult, waveforms, 0.0);
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 2;
		expResult = new double[] {2.0, 3.0, 2.2, 2.2, 2.2, 3.0, 4.0, 4.0, 0.0, 0.0, 0.0, 1.1, 1.1, 1.1, 1.1, 2.2};
		MovingWindowMedian.execute(waveforms, recordLength, radius);
		assertArrayEquals(expResult, waveforms, 0.0);
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 0;
		expResult = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		MovingWindowMedian.execute(waveforms, recordLength, radius);
		assertArrayEquals(expResult, waveforms, 0.0);
	}

}
