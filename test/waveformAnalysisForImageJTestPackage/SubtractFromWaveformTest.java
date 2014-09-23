package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.SubtractFromWaveform;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class SubtractFromWaveformTest
{
	
	public SubtractFromWaveformTest()
	{
	}

	/**
	 * Test of execute method, of class SubtractFromWaveform.
	 */
	@Test
	public void testExecute_floatArr()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		int recordLength = 8;
		int operation = SubtractFromWaveform.MEAN;
		SubtractFromWaveform.execute(waveforms, recordLength, operation);
		float[] expResult = new float[] {-1.65f, 2.35f, -0.65f, 0.35f, -0.45f, -3.65f, 1.35f, 2.35f, -1.3875f, -1.3875f, 1.8125f, -1.8875f, -0.2875f, 0.8125f, -0.2875f, 2.6125f};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		operation = SubtractFromWaveform.MEDIAN;
		SubtractFromWaveform.execute(waveforms, recordLength, operation);
		expResult = new float[] {-1.6f, 2.4f, -0.6f, 0.4f, -0.4f, -3.6f, 1.4f, 2.4f, -1.1f, -1.1f, 2.1f, -1.6f, 0.0f, 1.1f, 0.0f, 2.9f};
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		operation = SubtractFromWaveform.LINEAR_FIT;
		SubtractFromWaveform.execute(waveforms, recordLength, operation);
		expResult = new float[] {-1.1f, 2.7428571428571424f, -0.4142857142857146f, 0.42857142857142794f, -0.5285714285714289f, -3.885714285714286f, 0.9571428571428564f, 1.8f, -0.05f, -0.4321428571428576f, 2.3857142857142852f, -1.6964285714285718f, -0.47857142857142887f, 0.2392857142857141f, -1.2428571428571433f, 1.275f};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));
	}

	/**
	 * Test of execute method, of class SubtractFromWaveform.
	 */
	@Test
	public void testExecute_doubleArr()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		int recordLength = 8;
		int operation = SubtractFromWaveform.MEAN;
		SubtractFromWaveform.execute(waveforms, recordLength, operation);
		double[] expResult = new double[] {-1.65, 2.35, -0.65, 0.35, -0.45, -3.65, 1.35, 2.35, -1.3875, -1.3875, 1.8125, -1.8875, -0.2875, 0.8125, -0.2875, 2.6125};
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		operation = SubtractFromWaveform.MEDIAN;
		SubtractFromWaveform.execute(waveforms, recordLength, operation);
		expResult = new double[] {-1.6, 2.4, -0.6, 0.4, -0.4, -3.6, 1.4, 2.4, -1.1, -1.1, 2.1, -1.6, 0.0, 1.1, 0.0, 2.9};
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		operation = SubtractFromWaveform.LINEAR_FIT;
		SubtractFromWaveform.execute(waveforms, recordLength, operation);
		expResult = new double[] {-1.1, 2.7428571428571424, -0.4142857142857146, 0.42857142857142794, -0.5285714285714289, -3.885714285714286, 0.9571428571428564, 1.8, -0.05, -0.4321428571428576, 2.3857142857142852, -1.6964285714285718, -0.47857142857142887, 0.2392857142857141, -1.2428571428571433, 1.275};
		assertArrayEquals(expResult, waveforms, Math.ulp(5.0));
	}
	
}
