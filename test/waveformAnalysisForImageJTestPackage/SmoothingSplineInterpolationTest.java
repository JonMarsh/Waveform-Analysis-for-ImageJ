package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.SmoothingSplineInterpolation;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */


public class SmoothingSplineInterpolationTest
{
	
	public SmoothingSplineInterpolationTest()
	{
	}

	/**
	 * Test of execute method, of class SmoothingSplineInterpolation.
	 */
	@Test
	public void testExecute_5args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {4.893998146057129f, 7.586890697479248f, 10.65810108184814f, 14.98396587371826f, 18.84673118591309f, 19.39882278442383f, 33.77149200439453f, 31.87822532653809f, 32.60565185546875f, 37.95827102661133f, 4.893998146057129f, 7.586890697479248f, 10.65810108184814f, 14.98396587371826f, 18.84673118591309f, 19.39882278442383f, 33.77149200439453f, 31.87822532653809f, 32.60565185546875f, 37.95827102661133f};
		int recordLength = 10;
		int interpolatedRecordLength = 10;
		double smoothingParameter = 1.0;
		double stdev = 1.0;
		float[] expResult = new float[] {4.876516428183352f, 7.562211872033401f, 10.80579397971267f, 14.95426625859855f, 18.04508071068257f, 21.44295007484453f, 31.64573946687982f, 32.55020325380502f, 32.96981029804613f, 37.72957763966635f, 4.876516428183352f, 7.562211872033401f, 10.80579397971267f, 14.95426625859855f, 18.04508071068257f, 21.44295007484453f, 31.64573946687982f, 32.55020325380502f, 32.96981029804613f, 37.72957763966635f};
		float[] result = SmoothingSplineInterpolation.execute(waveforms, recordLength, interpolatedRecordLength, smoothingParameter, stdev);
		assertArrayEquals(expResult, result, Math.ulp(40.0f));

		waveforms = new float[] {2.2f, 3.3f, 1.1f, 0.0f, 5.0f, 4.0f, 2.0f};
		recordLength = 7;
		interpolatedRecordLength = 8;
		smoothingParameter = 0.9;
		stdev = 0.9;
		expResult = new float[] {2.516416117998386f, 2.501236031580607f, 1.634478136280626f, 1.108486786875286f, 2.371336276322567f, 3.982017380172527f, 3.742973813453688f, 2.329251143756413f};
		result = SmoothingSplineInterpolation.execute(waveforms, recordLength, interpolatedRecordLength, smoothingParameter, stdev);
		assertArrayEquals(expResult, result, Math.ulp(5.0f));
}

	/**
	 * Test of execute method, of class SmoothingSplineInterpolation.
	 */
	@Test
	public void testExecute_5args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {4.893998146057129, 7.586890697479248, 10.65810108184814, 14.98396587371826, 18.84673118591309, 19.39882278442383, 33.77149200439453, 31.87822532653809, 32.60565185546875, 37.95827102661133, 4.893998146057129, 7.586890697479248, 10.65810108184814, 14.98396587371826, 18.84673118591309, 19.39882278442383, 33.77149200439453, 31.87822532653809, 32.60565185546875, 37.95827102661133};
		int recordLength = 10;
		int interpolatedRecordLength = 10;
		double smoothingParameter = 1.0;
		double stdev = 1.0;
		double[] expResult = new double[] {4.876516428183352, 7.562211872033401, 10.80579397971267, 14.95426625859855, 18.04508071068257, 21.44295007484453, 31.64573946687982, 32.55020325380502, 32.96981029804613, 37.72957763966635, 4.876516428183352, 7.562211872033401, 10.80579397971267, 14.95426625859855, 18.04508071068257, 21.44295007484453, 31.64573946687982, 32.55020325380502, 32.96981029804613, 37.72957763966635};
		double[] result = SmoothingSplineInterpolation.execute(waveforms, recordLength, interpolatedRecordLength, smoothingParameter, stdev);
		assertArrayEquals(expResult, result, Math.ulp(40.0));
		
		waveforms = new double[] {2.2, 3.3, 1.1, 0, 5, 4, 2};
		recordLength = 7;
		interpolatedRecordLength = 8;
		smoothingParameter = 0.9;
		stdev = 0.9;
		expResult = new double[] {2.516416117998386, 2.501236031580607, 1.634478136280626, 1.108486786875286, 2.371336276322567, 3.982017380172527, 3.742973813453688, 2.329251143756413};
		result = SmoothingSplineInterpolation.execute(waveforms, recordLength, interpolatedRecordLength, smoothingParameter, stdev);
		assertArrayEquals(expResult, result, Math.ulp(5.0));
		
	}
	
}
