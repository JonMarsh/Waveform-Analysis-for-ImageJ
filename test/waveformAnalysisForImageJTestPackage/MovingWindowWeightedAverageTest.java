package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MovingWindowWeightedAverage;
import waveformAnalysisForImageJ.WaveformUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MovingWindowWeightedAverageTest
{
	
	public MovingWindowWeightedAverageTest()
	{
	}

	/**
	 * Test of execute method, of class MovingWindowWeightedAverage.
	 */
	@Test
	public void testExecute_5args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		int recordLength = 16;
		int radius = 2;
		WaveformUtils.WindowType windowType = WaveformUtils.WindowType.RECTANGLE;
		double windowParameter = 0.0;
		float[] expResult = new float[] {3f, 3.2f, 2.64f, 2.24f, 2.04f, 2.64f, 2.04f, 1.6f, 2.44f, 1.54f, 0.76f, 1.2f, 1.42f, 1.58f, 1.9f, 2.12f};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		expResult = new float[] {3f, 3.2f, 2.64f, 2.24f, 2.04f, 2.64f, 2.84f, 2.2f, 1.28f, 0.54f, 0.76f, 1.2f, 1.42f, 1.58f, 1.9f, 2.12f};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0f));

		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.HANNING;
		expResult = new float[] {3.16666666666667f, 3.08333333333333f, 2.93333333333333f, 2.38333333333333f, 1.73333333333333f, 1.88333333333333f, 2.85f, 3.5f, 0.53333333333333f, 0.75833333333333f, 1.03333333333333f, 1.09166666666667f, 1.15f, 1.575f, 2.1f, 2.25f};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 0;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		expResult = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(0.0f));

	}

	/**
	 * Test of execute method, of class MovingWindowWeightedAverage.
	 */
	@Test
	public void testExecute_5args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		int recordLength = 16;
		int radius = 2;
		WaveformUtils.WindowType windowType = WaveformUtils.WindowType.RECTANGLE;
		double windowParameter = 0.0;
		double[] expResult = new double[] {3f, 3.2, 2.64, 2.24, 2.04, 2.64, 2.04, 1.6, 2.44, 1.54, 0.76, 1.2, 1.42, 1.58, 1.9, 2.12};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		expResult = new double[] {3, 3.2, 2.64, 2.24, 2.04, 2.64, 2.84, 2.2, 1.28, 0.54, 0.76, 1.2, 1.42, 1.58, 1.9, 2.12};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(3.0));

		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.HANNING;
		expResult = new double[] {3.166666666666667, 3.083333333333333, 2.933333333333333, 2.383333333333333, 1.733333333333333, 1.883333333333333, 2.85, 3.5, 0.533333333333333, 0.758333333333333, 1.033333333333333, 1.091666666666667, 1.15, 1.575, 2.1, 2.25};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 0;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		expResult = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		MovingWindowWeightedAverage.execute(waveforms, recordLength, radius, windowType, windowParameter);
		assertArrayEquals(expResult, waveforms, Math.ulp(0.0));
	}

}
