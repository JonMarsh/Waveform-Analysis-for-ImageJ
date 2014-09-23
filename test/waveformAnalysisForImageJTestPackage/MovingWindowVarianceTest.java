package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.MovingWindowVariance;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class MovingWindowVarianceTest
{
	
	public MovingWindowVarianceTest()
	{
	}

	/**
	 * Test of execute method, of class MovingWindowVariance.
	 */
	@Test
	public void testExecute_4args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		int recordLength = 16;
		int radius = 2;
		boolean useUnbiasedEstimateOfVariance = true;
		float[] expResult = new float[] {3.5f, 3.2f, 2.248f, 4.688f, 3.508f, 5.248f, 6.508f, 7.3f, 5.368f, 5.908f, 2.203f, 2.335f, 1.917f, 2.757f, 1.605f, 1.407f};
		MovingWindowVariance.execute(waveforms, recordLength, radius, useUnbiasedEstimateOfVariance);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 16;
		radius = 2;
		useUnbiasedEstimateOfVariance = false;
		expResult = new float[] {2.8f, 2.56f, 1.7984f, 3.7504f, 2.8064f, 4.1984f, 5.2064f, 5.84f, 4.2944f, 4.7264f, 1.7624f, 1.868f, 1.5336f, 2.2056f, 1.284f, 1.1256f};
		MovingWindowVariance.execute(waveforms, recordLength, radius, useUnbiasedEstimateOfVariance);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0f));
		
		waveforms = new float[] {1.0f, 5.0f, 2.0f, 3.0f, 2.2f, -1.0f, 4.0f, 5.0f, 0.0f, 0.0f, 3.2f, -0.5f, 1.1f, 2.2f, 1.1f, 4.0f};
		recordLength = 8;
		radius = 2;
		useUnbiasedEstimateOfVariance = false;
		expResult = new float[] {2.8f, 2.56f, 1.7984f, 3.7504f, 2.8064f, 4.1984f, 4.5024f, 6.96f, 2.4576f, 1.8064f, 1.7624f, 1.868f, 1.5336f, 2.2056f, 1.284f, 1.1256f};
		MovingWindowVariance.execute(waveforms, recordLength, radius, useUnbiasedEstimateOfVariance);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0f));
		
	}

	/**
	 * Test of execute method, of class MovingWindowVariance.
	 */
	@Test
	public void testExecute_4args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		int recordLength = 16;
		int radius = 2;
		boolean useUnbiasedEstimateOfVariance = true;
		double[] expResult = new double[] {3.5, 3.2, 2.248, 4.688, 3.508, 5.248, 6.508, 7.3, 5.368, 5.908, 2.203, 2.335, 1.917, 2.757, 1.605, 1.407};
		MovingWindowVariance.execute(waveforms, recordLength, radius, useUnbiasedEstimateOfVariance);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 16;
		radius = 2;
		useUnbiasedEstimateOfVariance = false;
		expResult = new double[] {2.8, 2.56, 1.7984, 3.7504, 2.8064, 4.1984, 5.2064, 5.84, 4.2944, 4.7264, 1.7624, 1.868, 1.5336, 2.2056, 1.284, 1.1256};
		MovingWindowVariance.execute(waveforms, recordLength, radius, useUnbiasedEstimateOfVariance);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0));
		
		waveforms = new double[] {1.0, 5.0, 2.0, 3.0, 2.2, -1.0, 4.0, 5.0, 0.0, 0.0, 3.2, -0.5, 1.1, 2.2, 1.1, 4.0};
		recordLength = 8;
		radius = 2;
		useUnbiasedEstimateOfVariance = false;
		expResult = new double[] {2.8, 2.56, 1.7984, 3.7504, 2.8064, 4.1984, 4.5024, 6.96, 2.4576, 1.8064, 1.7624, 1.868, 1.5336, 2.2056, 1.284, 1.1256};
		MovingWindowVariance.execute(waveforms, recordLength, radius, useUnbiasedEstimateOfVariance);
		assertArrayEquals(expResult, waveforms, Math.ulp(10.0));
	}

}
