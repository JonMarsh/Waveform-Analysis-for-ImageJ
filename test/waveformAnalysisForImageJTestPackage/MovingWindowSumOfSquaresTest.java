package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MovingWindowSumOfSquares;
import waveformAnalysisForImageJ.WaveformUtils;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MovingWindowSumOfSquaresTest
{
	
	public MovingWindowSumOfSquaresTest()
	{
	}

	/**
	 * Test of execute method, of class MovingWindowSumOfSquares.
	 */
	@Test
	public void testExecute_6args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		int recordLength = 16;
		int radius = 2;
		WaveformUtils.WindowType windowType = WaveformUtils.WindowType.RECTANGLE;
		double windowParameter = 0.0;
		boolean logOutput = false;
		float[] expResult = new float[] {2.36f, 2.56f, 1.7536f, 1.7536f, 1.3936f, 2.2336f, 1.8736f, 1.68f, 2.0496f, 1.4196f, 0.468f, 0.6616f, 0.71f, 0.9404f, 0.9788f, 1.124f};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(2.56f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		windowParameter = 0.0;
		logOutput = false;
		expResult = new float[] {2.36f, 2.56f, 1.7536f, 1.7536f, 1.3936f, 2.2336f, 2.5136f, 2.36f, 0.8192f, 0.4196f, 0.468f, 0.6616f, 0.71f, 0.9404f, 0.9788f, 1.124f};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(2.56f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.HANNING;
		windowParameter = 0.0;
		logOutput = false;
		expResult = new float[] {3.29166666666666667f, 3.32638888888888889f, 2.61f, 1.73305555555555556f, 1.30166666666666667f, 1.64972222222222222f, 3.5475f, 4.79166666666666667f, 0.14222222222222222f, 0.64173611111111111f, 1.16180555555555556f, 0.77701388888888889f, 0.53208333333333333f, 0.801875f, 1.45375f, 1.99625f};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(4.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 0;
		windowType = WaveformUtils.WindowType.HANNING;
		windowParameter = 0.0;
		logOutput = false;
		expResult = new float[] {1.0f, 25.0f, 4.0f, 9.0f, 4.84f, 1.0f, 16.0f, 25.0f, 0.0f, 0.0f, 10.24f, 0.25f, 1.21f, 4.84f, 1.21f, 16.0f};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(25.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 16;
		radius = 2;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		windowParameter = 0.0;
		logOutput = true;
		expResult = new float[] {0.3729120029701070f, 0.4082399653118500f, 0.2439305368042750f, 0.2439305368042750f, 0.1441381376635880f, 0.3490054009430670f, 0.2726768777282880f, 0.2253092817258630f, 0.3116691124006110f, 0.1521659906755550f, -0.3297541469258760f, -0.1794045034555100f, -0.1487416512809250f, -0.0266873795470982f, -0.0093060393202484f, 0.0507663112330423f};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0f));
	}

	/**
	 * Test of execute method, of class MovingWindowSumOfSquares.
	 */
	@Test
	public void testExecute_6args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		int recordLength = 16;
		int radius = 2;
		WaveformUtils.WindowType windowType = WaveformUtils.WindowType.RECTANGLE;
		double windowParameter = 0.0;
		boolean logOutput = false;
		double[] expResult = new double[] {2.36, 2.56, 1.7536, 1.7536, 1.3936, 2.2336, 1.8736, 1.68, 2.0496, 1.4196, 0.468, 0.6616, 0.71, 0.9404, 0.9788, 1.124};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(2.56));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		windowParameter = 0.0;
		logOutput = false;
		expResult = new double[] {2.36, 2.56, 1.7536, 1.7536, 1.3936, 2.2336, 2.5136, 2.36, 0.8192, 0.4196, 0.468, 0.6616, 0.71, 0.9404, 0.9788, 1.124};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(2.56));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 2;
		windowType = WaveformUtils.WindowType.HANNING;
		windowParameter = 0.0;
		logOutput = false;
		expResult = new double[] {3.29166666666666667, 3.32638888888888889, 2.61, 1.73305555555555556, 1.30166666666666667, 1.64972222222222222, 3.5475, 4.79166666666666667, 0.14222222222222222, 0.64173611111111111, 1.16180555555555556, 0.77701388888888889, 0.53208333333333333, 0.801875, 1.45375, 1.99625};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(4.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 0;
		windowType = WaveformUtils.WindowType.HANNING;
		windowParameter = 0.0;
		logOutput = false;
		expResult = new double[] {1.0, 25.0, 4.0, 9.0, 4.84, 1.0, 16.0, 25.0, 0.0, 0.0, 10.24, 0.25, 1.21, 4.84, 1.21, 16.0};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(25.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 16;
		radius = 2;
		windowType = WaveformUtils.WindowType.RECTANGLE;
		windowParameter = 0.0;
		logOutput = true;
		expResult = new double[] {0.3729120029701070, 0.4082399653118500, 0.2439305368042750, 0.2439305368042750, 0.1441381376635880, 0.3490054009430670, 0.2726768777282880, 0.2253092817258630, 0.3116691124006110, 0.1521659906755550, -0.3297541469258760, -0.1794045034555100, -0.1487416512809250, -0.0266873795470982, -0.0093060393202484, 0.0507663112330423};
		MovingWindowSumOfSquares.execute(waveforms, recordLength, radius, windowType, windowParameter, logOutput);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0));
		
	}

}
