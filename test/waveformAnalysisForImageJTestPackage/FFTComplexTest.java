package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.FFTComplex;
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


public class FFTComplexTest
{
	
	public FFTComplexTest()
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
	 * Test of execute method, of class FFTComplex.
	 */
	@Test
	public void testExecute_4args_1()
	{
		System.out.println("execute");
		float[] realWaveforms = new float[] {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		float[] imagWaveforms = new float[] {7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f, 0.0f};
		boolean isForward = true;
		int recordLength = 8;
		float[] expResultRe = new float[] {36.0f, 5.6568542494923797f, 0.0f, -2.3431457505076194f, -4.0f, -5.6568542494923806f, -8.0f, -13.6568542494923797f};
		float[] expResultIm = new float[] {28.0f, 13.6568542494923797f, 8.0f, 5.6568542494923806f, 4.0f, 2.3431457505076194f, 0.0f, -5.6568542494923797f};
		float[][] result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(1.0f));
		assertArrayEquals(expResultIm, result[1], Math.ulp(1.0f));
		
		realWaveforms = new float[] {36.0f, 5.6568542494923797f, 0.0f, -2.3431457505076194f, -4.0f, -5.6568542494923806f, -8.0f, -13.6568542494923797f};
		imagWaveforms = new float[] {28.0f, 13.6568542494923797f, 8.0f, 5.6568542494923806f, 4.0f, 2.3431457505076194f, 0.0f, -5.6568542494923797f};
		isForward = false;
		recordLength = 8;
		expResultRe = new float[] {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		expResultIm = new float[] {7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f, 0.0f};
		result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(1.0f));
		assertArrayEquals(expResultIm, result[1], Math.ulp(1.0f));
		
		realWaveforms = new float[] {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		imagWaveforms = new float[] {7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f, 0.0f};
		isForward = true;
		recordLength = 4;
		expResultRe = new float[] {10.0f, 0.0f, -2.0f, -4.0f, 26.0f, 0.0f, -2.0f, -4.0f};
		expResultIm = new float[] {22.0f, 4.0f, 2.0f, 0.0f, 6.0f, 4.0f, 2.0f, 0.0f};
		result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(1.0f));
		assertArrayEquals(expResultIm, result[1], Math.ulp(1.0f));
		
		realWaveforms = new float[] {10.0f, 0.0f, -2.0f, -4.0f, 26.0f, 0.0f, -2.0f, -4.0f};
		imagWaveforms = new float[] {22.0f, 4.0f, 2.0f, 0.0f, 6.0f, 4.0f, 2.0f, 0.0f};
		isForward = false;
		recordLength = 4;
		expResultRe = new float[] {1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		expResultIm = new float[] {7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f, 0.0f};
		result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(1.0f));
		assertArrayEquals(expResultIm, result[1], Math.ulp(1.0f));		
	}

	/**
	 * Test of execute method, of class FFTComplex.
	 */
	@Test
	public void testExecute_4args_2()
	{
		System.out.println("execute");
		double[] realWaveforms = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		double[] imagWaveforms = new double[] {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0};
		boolean isForward = true;
		int recordLength = 8;
		double[] expResultRe = new double[] {36.0, 5.6568542494923797, 0.0, -2.3431457505076194, -4.0, -5.6568542494923806, -8.0, -13.6568542494923797};
		double[] expResultIm = new double[] {28.0, 13.6568542494923797, 8.0, 5.6568542494923806, 4.0, 2.3431457505076194, 0.0, -5.6568542494923797};
		double[][] result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(36.0));
		assertArrayEquals(expResultIm, result[1], Math.ulp(28.0));
		
		realWaveforms = new double[] {36.0, 5.6568542494923797, 0.0, -2.3431457505076194, -4.0, -5.6568542494923806, -8.0, -13.6568542494923797};
		imagWaveforms = new double[] {28.0, 13.6568542494923797, 8.0, 5.6568542494923806, 4.0, 2.3431457505076194, 0.0, -5.6568542494923797};
		isForward = false;
		recordLength = 8;
		expResultRe = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		expResultIm = new double[] {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0};
		result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(8.0));
		assertArrayEquals(expResultIm, result[1], Math.ulp(7.0));
		
		realWaveforms = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		imagWaveforms = new double[] {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0};
		isForward = true;
		recordLength = 4;
		expResultRe = new double[] {10.0, 0.0, -2.0, -4.0, 26.0, 0.0, -2.0, -4.0};
		expResultIm = new double[] {22.0, 4.0, 2.0, 0.0, 6.0, 4.0, 2.0, 0.0};
		result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(26.0));
		assertArrayEquals(expResultIm, result[1], Math.ulp(22.0));
		
		realWaveforms = new double[] {10.0, 0.0, -2.0, -4.0, 26.0, 0.0, -2.0, -4.0};
		imagWaveforms = new double[] {22.0, 4.0, 2.0, 0.0, 6.0, 4.0, 2.0, 0.0};
		isForward = false;
		recordLength = 4;
		expResultRe = new double[] {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		expResultIm = new double[] {7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0};
		result = FFTComplex.execute(realWaveforms, imagWaveforms, isForward, recordLength);
		assertArrayEquals(expResultRe, result[0], Math.ulp(1.0));
		assertArrayEquals(expResultIm, result[1], Math.ulp(1.0));		
	}
	
}
