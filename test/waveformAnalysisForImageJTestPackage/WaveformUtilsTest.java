package waveformAnalysisForImageJTestPackage;

import waveformAnalysisForImageJ.WaveformUtils;
import java.util.Arrays;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jnm
 */
public class WaveformUtilsTest
{

	public WaveformUtilsTest()
	{
	}

	/**
	 * Test of addScalar method, of class WaveformUtils.
	 */
	@Test
	public void testAddScalar_doubleArr_double()
	{
		System.out.println("addScalar");
		double[] a = new double[]{0.0, 1.0, -2.0};
		double value = 1.0;
		double[] expResult = new double[]{1.0, 2.0, -1.0};
		double[] result = WaveformUtils.addScalar(a, value);
		assertArrayEquals(expResult, result, 0.0);
	}

	/**
	 * Test of addScalar method, of class WaveformUtils.
	 */
	@Test
	public void testAddScalar_floatArr_float()
	{
		System.out.println("addScalar");
		float[] a = new float[]{0.0f, 1.0f, -2.0f};
		float value = 1.0F;
		float[] expResult = new float[]{1.0f, 2.0f, -1.0f};
		float[] result = WaveformUtils.addScalar(a, value);
		assertArrayEquals(expResult, result, 0.0f);
		// TODO review the generated test code and remove the default call to fail.
	}

	/**
	 * Test of addScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testAddScalarInPlace_doubleArr_double()
	{
		System.out.println("addScalarInPlace");
		double[] a = new double[]{0.0, 1.0, -2.0};
		double value = 1.0;
		double[] expResult = new double[]{1.0, 2.0, -1.0};
		WaveformUtils.addScalarInPlace(a, value);
		assertArrayEquals(a, expResult, 0.0);
	}

	/**
	 * Test of addScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testAddScalarInPlace_floatArr_float()
	{
		System.out.println("addScalarInPlace");
		float[] a = new float[]{0.0f, 1.0f, -2.0f};
		float value = 1.0F;
		float[] expResult = new float[]{1.0f, 2.0f, -1.0f};
		WaveformUtils.addScalarInPlace(a, value);
		assertArrayEquals(a, expResult, 0.0f);
	}

	/**
	 * Test of addScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testAddScalarInPlace_4args_1()
	{
		System.out.println("addScalarInPlace");
		double[] a = new double[]{0.0, 1.0, -2.0, -3.0, 4.0, -5.0};
		int from = 3;
		int to = 6;
		double value = 1.0;
		double[] expResult = new double[]{0.0, 1.0, -2.0, -2.0, 5.0, -4.0};
		WaveformUtils.addScalarInPlace(a, from, to, value);
		assertArrayEquals(a, expResult, 0.0);
	}

	/**
	 * Test of addScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testAddScalarInPlace_4args_2()
	{
		System.out.println("addScalarInPlace");
		float[] a = new float[]{0.0f, 1.0f, -2.0f, -3.0f, 4.0f, -5.0f};
		int from = 3;
		int to = 6;
		float value = 1.0F;
		float[] expResult = new float[]{0.0f, 1.0f, -2.0f, -2.0f, 5.0f, -4.0f};
		WaveformUtils.addScalarInPlace(a, from, to, value);
		assertArrayEquals(a, expResult, 0.0f);
	}

	/**
	 * Test of multiplyScalar method, of class WaveformUtils.
	 */
	@Test
	public void testMultiplyScalar_doubleArr_double()
	{
		System.out.println("multiplyScalar");
		double[] a = new double[]{0.0, 1.0, -2.0};
		double value = 1.5;
		double[] expResult = new double[]{0.0, 1.5, -3.0};
		double[] result = WaveformUtils.multiplyScalar(a, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of multiplyScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testMultiplyScalarInPlace_doubleArr_double()
	{
		System.out.println("multiplyScalarInPlace");
		double[] a = new double[]{0.0, 1.0, -2.0};
		double value = 1.5;
		double[] expResult = new double[]{0.0, 1.5, -3.0};
		WaveformUtils.multiplyScalarInPlace(a, value);
		assertArrayEquals(a, expResult, Math.ulp(1.0));
	}

	/**
	 * Test of multiplyScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testMultiplyScalarInPlace_4args_1()
	{
		System.out.println("multiplyScalarInPlace");
		double[] a = new double[]{0.0, 1.0, -2.0, -3.0, 4.0, -5.0};
		int from = 3;
		int to = 6;
		double value = 1.5;
		double[] expResult = new double[]{0.0, 1.0, -2.0, -4.5, 6.0, -7.5};
		WaveformUtils.multiplyScalarInPlace(a, from, to, value);
		assertArrayEquals(a, expResult, Math.ulp(1.0));
	}

	/**
	 * Test of multiplyScalar method, of class WaveformUtils.
	 */
	@Test
	public void testMultiplyScalar_floatArr_float()
	{
		System.out.println("multiplyScalar");
		float[] a = new float[]{0.0f, 1.0f, -2.0f, -3.0f, 4.0f, -5.0f};
		float value = 1.5F;
		float[] expResult = new float[]{0.0f, 1.5f, -3.0f, -4.5f, 6.0f, -7.5f};
		float[] result = WaveformUtils.multiplyScalar(a, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
	}

	/**
	 * Test of multiplyScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testMultiplyScalarInPlace_floatArr_float()
	{
		System.out.println("multiplyScalarInPlace");
		float[] a = new float[]{0.0f, 1.0f, -2.0f, -3.0f, 4.0f, -5.0f};
		float value = 1.5F;
		WaveformUtils.multiplyScalarInPlace(a, value);
		float[] expResult = new float[]{0.0f, 1.5f, -3.0f, -4.5f, 6.0f, -7.5f};
		assertArrayEquals(expResult, a, Math.ulp(1.0f));
	}

	/**
	 * Test of multiplyScalarInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testMultiplyScalarInPlace_4args_2()
	{
		System.out.println("multiplyScalarInPlace");
		float[] a = new float[]{0.0f, 1.0f, -2.0f, -3.0f, 4.0f, -5.0f};
		int from = 3;
		int to = 6;
		float value = 1.5F;
		WaveformUtils.multiplyScalarInPlace(a, from, to, value);
		float[] expResult = new float[]{0.0f, 1.0f, -2.0f, -4.5f, 6.0f, -7.5f};
		assertArrayEquals(expResult, a, Math.ulp(1.0f));
	}

	/**
	 * Test of mean method, of class WaveformUtils.
	 */
	@Test
	public void testMean_3args_1()
	{
		System.out.println("mean");
		double[] a = new double[]{3.0, 4.0, 5.0, 6.0, 1.0, 8.0};
		int from = 3;
		int to = 6;
		double expResult = 5.0;
		double result = WaveformUtils.mean(a, from, to);
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of mean method, of class WaveformUtils.
	 */
	@Test
	public void testMean_doubleArr()
	{
		System.out.println("mean");
		double[] a = new double[]{3.0, 4.0, 5.0};
		double expResult = 4.0;
		double result = WaveformUtils.mean(a);
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of mean method, of class WaveformUtils.
	 */
	@Test
	public void testMean_3args_2()
	{
		System.out.println("mean");
		float[] a = new float[]{3.0f, 4.0f, 5.0f, 6.0f, 1.0f, 8.0f};
		int from = 3;
		int to = 6;
		float expResult = 5.0F;
		float result = WaveformUtils.mean(a, from, to);
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of mean method, of class WaveformUtils.
	 */
	@Test
	public void testMean_floatArr()
	{
		System.out.println("mean");
		float[] a = new float[]{3.0f, 4.0f, 5.0f};
		float expResult = 4.0F;
		float result = WaveformUtils.mean(a);
		assertEquals(expResult, result, 0.0);
	}

	/**
	 * Test of meanAndVariance method, of class WaveformUtils.
	 */
	@Test
	public void testMeanAndVariance_doubleArr_boolean()
	{
		System.out.println("meanAndVariance");
		double[] a = new double[]{3.0, 4.0, 5.0};
		boolean useUnbiasedEstimate = true;
		double[] expResult = new double[]{4.0, 1.0};
		double[] result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		useUnbiasedEstimate = false;
		expResult = new double[]{4.0, 2.0 / 3.0};
		result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of meanAndVariance method, of class WaveformUtils.
	 */
	@Test
	public void testMeanAndVariance_4args_1()
	{
		System.out.println("meanAndVariance");
		double[] a = new double[]{3.0, 4.0, 5.0, 6.0, 1.0, 8.0};
		boolean useUnbiasedEstimate = true;
		int from = 3;
		int to = 6;
		double[] expResult = new double[]{5.0, 13.0};
		double[] result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate, from, to);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		useUnbiasedEstimate = false;
		expResult = new double[]{5.0, 26.0 / 3.0};
		result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate, from, to);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of meanAndVariance method, of class WaveformUtils.
	 */
	@Test
	public void testMeanAndVariance_floatArr_boolean()
	{
		System.out.println("meanAndVariance");
		float[] a = new float[]{3.0f, 4.0f, 5.0f};
		boolean useUnbiasedEstimate = true;
		double[] expResult = new double[]{4.0, 1.0};
		double[] result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		useUnbiasedEstimate = false;
		expResult = new double[]{4.0, 2.0 / 3.0};
		result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of meanAndVariance method, of class WaveformUtils.
	 */
	@Test
	public void testMeanAndVariance_4args_2()
	{
		System.out.println("meanAndVariance");
		float[] a = new float[]{3.0f, 4.0f, 5.0f, 6.0f, 1.0f, 8.0f};
		boolean useUnbiasedEstimate = true;
		int from = 3;
		int to = 6;
		double[] expResult = new double[]{5.0, 13.0};
		double[] result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate, from, to);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		useUnbiasedEstimate = false;
		expResult = new double[]{5.0, 26.0 / 3.0};
		result = WaveformUtils.meanAndVariance(a, useUnbiasedEstimate, from, to);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of median method, of class WaveformUtils.
	 */
	@Test
	public void testMedian_doubleArr()
	{
		System.out.println("median");
		double[] a = new double[]{1.0, 5.0, 3.0, 2.0, 2.2};
		double expResult = 2.2;
		double result = WaveformUtils.median(a);
		assertEquals(expResult, result, 0.0);

		a = new double[]{1.0, 5.0, 3.0, 2.0};
		expResult = 2.5;
		result = WaveformUtils.median(a);
		assertEquals(expResult, result, Math.ulp(expResult));
	}

	/**
	 * Test of median method, of class WaveformUtils.
	 */
	@Test
	public void testMedian_3args_1()
	{
		System.out.println("median");
		double[] a = new double[]{1.0, 5.0, 3.0, 2.0, 2.2, -1.0, 4.4, 10.0, -9.0, 5.0};
		int from = 5;
		int to = 10;
		double expResult = 4.4;
		double result = WaveformUtils.median(a, from, to);
		assertEquals(expResult, result, 0.0);

		a = new double[]{1.0, 5.0, 3.0, 2.0, 2.2, -1.0, 4.4, 10.0};
		from = 4;
		to = 8;
		expResult = 3.3;
		result = WaveformUtils.median(a, from, to);
		assertEquals(expResult, result, Math.ulp(expResult));
	}

	/**
	 * Test of medianAndSort method, of class WaveformUtils.
	 */
	@Test
	public void testMedianAndSort_doubleArr()
	{
		System.out.println("medianAndSort");
		double[] a = new double[]{1.0, 5.0, 3.0, 2.0, 2.2};
		double[] aSorted = Arrays.copyOf(a, a.length);
		Arrays.sort(aSorted);
		double expResult = 2.2;
		double result = WaveformUtils.medianAndSort(a);
		assertEquals(expResult, result, 0.0);
		assertArrayEquals(a, aSorted, 0.0);

		a = new double[]{1.0, 5.0, 3.0, 2.0};
		aSorted = Arrays.copyOf(a, a.length);
		Arrays.sort(aSorted);
		expResult = 2.5;
		result = WaveformUtils.medianAndSort(a);
		assertEquals(expResult, result, Math.ulp(expResult));
		assertArrayEquals(a, aSorted, 0.0);
	}

	/**
	 * Test of median method, of class WaveformUtils.
	 */
	@Test
	public void testMedian_floatArr()
	{
		System.out.println("median");
		float[] a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f};
		float expResult = 2.2F;
		float result = WaveformUtils.median(a);
		assertEquals(expResult, result, 0.0);

		a = new float[]{1.0f, 5.0f, 3.0f, 2.0f};
		expResult = 2.5f;
		result = WaveformUtils.median(a);
		assertEquals(expResult, result, Math.ulp(expResult));
	}

	/**
	 * Test of median method, of class WaveformUtils.
	 */
	@Test
	public void testMedian_3args_2()
	{
		System.out.println("median");
		float[] a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f, -1.0f, 4.4f, 10.0f, -9.0f, 5.0f};
		int from = 5;
		int to = 10;
		float expResult = 4.4F;
		float result = WaveformUtils.median(a, from, to);
		assertEquals(expResult, result, 0.0f);

		a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f, -1.0f, 4.4f, 10.0f};
		from = 4;
		to = 8;
		expResult = 3.3f;
		result = WaveformUtils.median(a, from, to);
		assertEquals(expResult, result, Math.ulp(expResult));
	}

	/**
	 * Test of medianAndSort method, of class WaveformUtils.
	 */
	@Test
	public void testMedianAndSort_floatArr()
	{
		System.out.println("medianAndSort");
		float[] a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f};
		float[] aSorted = Arrays.copyOf(a, a.length);
		Arrays.sort(aSorted);
		float expResult = 2.2F;
		float result = WaveformUtils.medianAndSort(a);
		assertEquals(expResult, result, 0.0);
		assertArrayEquals(a, aSorted, 0.0f);

		a = new float[]{1.0f, 5.0f, 3.0f, 2.0f};
		aSorted = Arrays.copyOf(a, a.length);
		Arrays.sort(aSorted);
		expResult = 2.5f;
		result = WaveformUtils.medianAndSort(a);
		assertEquals(expResult, result, Math.ulp(expResult));
		assertArrayEquals(a, aSorted, 0.0f);
	}

	/**
	 * Test of fftComplexPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testFftComplexPowerOf2_3args()
	{
		System.out.println("fftComplexPowerOf2");
		double[] ar = new double[]{1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 0.0, 0.0};
		double[] ai = new double[ar.length];
		boolean isForward = true;
		WaveformUtils.fftComplexPowerOf2(ar, ai, isForward);
		double[] expResultRe = new double[]{8.0, -1.4142135623730951, 0.0, 1.4142135623730951, 0.0, 1.4142135623730951, 0.0, -1.4142135623730951};
		double[] expResultIm = new double[]{0.0, -3.414213562373095, 0.0, 0.5857864376269049, 0.0, -0.5857864376269049, 0.0, 3.414213562373095};
		assertArrayEquals(expResultRe, ar, Math.ulp(10.0));
		assertArrayEquals(expResultIm, ai, Math.ulp(10.0));

		isForward = false;
		System.arraycopy(expResultRe, 0, ar, 0, ar.length);
		System.arraycopy(expResultIm, 0, ai, 0, ai.length);
		WaveformUtils.fftComplexPowerOf2(ar, ai, isForward);
		expResultRe = new double[]{1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 0.0, 0.0};
		expResultIm = new double[ar.length];
		assertArrayEquals(expResultRe, ar, Math.ulp(10.0));
		assertArrayEquals(expResultIm, ai, Math.ulp(10.0));
	}

	/**
	 * Test of fftComplexPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testFftComplexPowerOf2_5args()
	{
		System.out.println("fftComplexPowerOf2");
		double[] ar = new double[]{7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0, 1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 0.0, 0.0};
		double[] ai = new double[ar.length];
		int from = 8;
		int to = 16;
		boolean isForward = true;
		WaveformUtils.fftComplexPowerOf2(ar, ai, from, to, isForward);
		double[] expResultRe = new double[]{7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0, 8.0, -1.4142135623730951, 0.0, 1.4142135623730951, 0.0, 1.4142135623730951, 0.0, -1.4142135623730951};
		double[] expResultIm = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, -3.414213562373095, 0.0, 0.5857864376269049, 0.0, -0.5857864376269049, 0.0, 3.414213562373095};
		assertArrayEquals(expResultRe, ar, Math.ulp(10.0));
		assertArrayEquals(expResultIm, ai, Math.ulp(10.0));

		isForward = false;
		System.arraycopy(expResultRe, 0, ar, 0, ar.length);
		System.arraycopy(expResultIm, 0, ai, 0, ai.length);
		WaveformUtils.fftComplexPowerOf2(ar, ai, from, to, isForward);
		expResultRe = new double[]{7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0, 0.0, 1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 0.0, 0.0};
		expResultIm = new double[ar.length];
		assertArrayEquals(expResultRe, ar, Math.ulp(10.0));
		assertArrayEquals(expResultIm, ai, Math.ulp(10.0));
	}

	/**
	 * Test of fftRealPowerOf2Forward method, of class WaveformUtils.
	 */
	@Test
	public void testFftRealPowerOf2Forward()
	{
		System.out.println("fftRealPowerOf2Forward");
		double[] ar = new double[]{1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 0.0, 0.0};
		double[] ai = new double[ar.length];
		WaveformUtils.fftRealPowerOf2Forward(ar, ai);
		double[] expResultRe = new double[]{8.0, -1.4142135623730951, 0.0, 1.4142135623730951, 0.0, 1.4142135623730951, 0.0, -1.4142135623730951};
		double[] expResultIm = new double[]{0.0, -3.414213562373095, 0.0, 0.5857864376269049, 0.0, -0.5857864376269049, 0.0, 3.414213562373095};
		assertArrayEquals(expResultRe, ar, Math.ulp(10.0));
		assertArrayEquals(expResultIm, ai, Math.ulp(10.0));
		
		ar = new double[]{1, 4, 3, -2, -2.2, 0, 3, -1};
		ai = new double[ar.length];
		WaveformUtils.fftRealPowerOf2Forward(ar, ai);
		expResultRe = new double[]{5.8, 6.7355339059327379, -7.2, -0.3355339059327377, 3.8, -0.3355339059327377, -7.2, 6.7355339059327379};
		expResultIm = new double[]{0.0, -2.1213203435596428, -7.0, -2.1213203435596428, 0.0, 2.1213203435596428, 7.0, 2.1213203435596428};
		assertArrayEquals(expResultRe, ar, Math.ulp(10.0));
		assertArrayEquals(expResultIm, ai, Math.ulp(10.0));
	}

/**
	 * Test of fftRealPowerOf2Forward method, of class WaveformUtils.
	 */
	@Test
	public void testFftRealPowerOf2Inverse()
	{
		System.out.println("fftRealPowerOf2Inverse");
		double[] ar = new double[]{8.0, -1.4142135623730951, 0.0, 1.4142135623730951, 0.0, 1.4142135623730951, 0.0, -1.4142135623730951};
		double[] ai = new double[]{0.0, -3.414213562373095, 0.0, 0.5857864376269049, 0.0, -0.5857864376269049, 0.0, 3.414213562373095};
		WaveformUtils.fftRealPowerOf2Inverse(ar, ai);
		double[] expResultRe = new double[]{1.0, 1.0, 2.0, 2.0, 1.0, 1.0, 0.0, 0.0};
		double[] expResultIm = new double[ar.length];
		System.out.println(Arrays.toString(ar));
		System.out.println(Arrays.toString(ai));
		assertArrayEquals(expResultRe, ar, Math.ulp(10.0));
		assertArrayEquals(expResultIm, ai, Math.ulp(10.0));
		
	}	
	
	/**
	 * Test of hilbertTransform method, of class WaveformUtils.
	 */
	@Test
	public void testFastHilbertTransformPowerOf2()
	{
		System.out.println("fastHilbertTransformPowerOf2");
		double[] a = new double[]{1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0};
		WaveformUtils.fastHilbertTransformPowerOf2(a, true);
		double[] expResult = new double[]{1.207106781186547, -1.017766952966369, 0.9142135623730954, -0.3964466094067254, -0.207106781186547, 2.517766952966369, -1.914213562373095, -1.103553390593275};
		assertArrayEquals(expResult, a, Math.ulp(10.0));

		a = new double[]{1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0};
		WaveformUtils.fastHilbertTransformPowerOf2(a, false);
		expResult = new double[]{-1.207106781186547, 1.017766952966369, -0.9142135623730954, 0.3964466094067254, 0.207106781186547, -2.517766952966369, 1.914213562373095, 1.103553390593275};
		assertArrayEquals(expResult, a, Math.ulp(10.0));

	}

	/**
	 * Test of isPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testIsPowerOf2()
	{
		System.out.println("isPowerOf2");
		int n = 9;
		boolean expResult = false;
		boolean result = WaveformUtils.isPowerOf2(n);
		assertEquals(expResult, result);

		n = 8;
		expResult = true;
		result = WaveformUtils.isPowerOf2(n);
		assertEquals(expResult, result);

		n = 0;
		expResult = false;
		result = WaveformUtils.isPowerOf2(n);
		assertEquals(expResult, result);
	}

	/**
	 * Test of amountToPadToNextPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testAmountToPadToNextPowerOf2()
	{
		System.out.println("amountToPadToNextPowerOf2");
		int n = 509;
		int expResult = 3;
		int result = WaveformUtils.amountToPadToNextPowerOf2(n);
		assertEquals(expResult, result);

		n = 512;
		expResult = 0;
		result = WaveformUtils.amountToPadToNextPowerOf2(n);
		assertEquals(expResult, result);
	}

	/**
	 * Test of padToPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testPadToPowerOf2_doubleArr_double()
	{
		System.out.println("padToPowerOf2");
		double[] a = {1.0, 5.0, 3.0, 2.0, 2.2};
		double value = 3.3;
		double[] expResult = {1.0, 5.0, 3.0, 2.0, 2.2, 3.3, 3.3, 3.3};
		double[] result = WaveformUtils.padToPowerOf2(a, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = new double[]{1.0, 5.0, 3.0, 2.0, 2.2, 3.3, 3.3, 3.3};
		value = 0.0;
		expResult = new double[]{1.0, 5.0, 3.0, 2.0, 2.2, 3.3, 3.3, 3.3};
		result = WaveformUtils.padToPowerOf2(a, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of padMultipleWaveformsToPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testPadMultipleWaveformsToPowerOf2_3args_1()
	{
		System.out.println("padMultipleWaveformsToPowerOf2");
		double[] a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0};
		int waveformLength = 3;
		double value = 3.3;
		double[] expResult = {1.0, 2.0, 3.0, 3.3, 4.0, 5.0, 6.0, 3.3};
		double[] result = WaveformUtils.padMultipleWaveformsToPowerOf2(a, waveformLength, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		waveformLength = 4;
		value = 3.3;
		expResult = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		result = WaveformUtils.padMultipleWaveformsToPowerOf2(a, waveformLength, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of padToPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testPadToPowerOf2_floatArr_float()
	{
		System.out.println("padToPowerOf2");
		float[] a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f};
		float value = 3.3F;
		float[] expResult = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f, 3.3f, 3.3f, 3.3f};
		float[] result = WaveformUtils.padToPowerOf2(a, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f, 3.3f, 3.3f, 3.3f};
		value = 0.0f;
		expResult = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f, 3.3f, 3.3f, 3.3f};
		result = WaveformUtils.padToPowerOf2(a, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
	}

	/**
	 * Test of padMultipleWaveformsToPowerOf2 method, of class WaveformUtils.
	 */
	@Test
	public void testPadMultipleWaveformsToPowerOf2_3args_2()
	{
		System.out.println("padMultipleWaveformsToPowerOf2");
		float[] a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f};
		int waveformLength = 3;
		float value = 3.3F;
		float[] expResult = new float[]{1.0f, 2.0f, 3.0f, 3.3f, 4.0f, 5.0f, 6.0f, 3.3f};
		float[] result = WaveformUtils.padMultipleWaveformsToPowerOf2(a, waveformLength, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		waveformLength = 4;
		value = 3.3F;
		expResult = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		result = WaveformUtils.padMultipleWaveformsToPowerOf2(a, waveformLength, value);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
	}

	/**
	 * Test of reverseArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testReverseArrayInPlace_doubleArr()
	{
		System.out.println("reverseArrayInPlace");
		double[] a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0};
		double[] expResult = new double[]{7.0, 6.0, 5.0, 4.0, 3.0, 2.0, 1.0};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0);
		
		a = new double[]{1.0, 2.0};
		expResult = new double[]{2.0, 1.0};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0);
		
		a = new double[]{1.0};
		expResult = new double[]{1.0};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0);
		
		a = new double[]{};
		expResult = new double[]{};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0);
		
		a = null;
		expResult = null;
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0);
	}

	/**
	 * Test of reverseArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testReverseArrayInPlace_3args_1()
	{
		System.out.println("reverseArrayInPlace");
		double[] a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		int from = 4;
		int to = 8;
		double[] aReversed = new double[]{1.0, 2.0, 3.0, 4.0, 8.0, 7.0, 6.0, 5.0};
		WaveformUtils.reverseArrayInPlace(a, from, to);
		assertArrayEquals(a, aReversed, 0.0);
	}

	/**
	 * Test of reverseArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testReverseArrayInPlace_floatArr()
	{
		System.out.println("reverseArrayInPlace");
		float[] a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f};
		float[] expResult = new float[]{7.0f, 6.0f, 5.0f, 4.0f, 3.0f, 2.0f, 1.0f};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0f);
		
		a = new float[]{1.0f, 2.0f};
		expResult = new float[]{2.0f, 1.0f};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0f);
		
		a = new float[]{1.0f};
		expResult = new float[]{1.0f};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0f);
		
		a = new float[]{};
		expResult = new float[]{};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0f);
		
		a = null;
		expResult = null;
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a, 0.0f);
	}

	/**
	 * Test of reverseArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testReverseArrayInPlace_3args_2()
	{
		System.out.println("reverseArrayInPlace");
		float[] a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		int from = 4;
		int to = 8;
		float[] aReversed = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 8.0f, 7.0f, 6.0f, 5.0f};
		WaveformUtils.reverseArrayInPlace(a, from, to);
		assertArrayEquals(a, aReversed, 0.0f);
	}

	/**
	 * Test of reverseArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testReverseArrayInPlace_intArr()
	{
		System.out.println("reverseArrayInPlace");
		int[] a = new int[]{1, 2, 3, 4, 5, 6, 7};
		int[] expResult = new int[]{7, 6, 5, 4, 3, 2, 1};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(a, expResult);
		
		a = new int[]{1, 2};
		expResult = new int[]{2, 1};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a);
		
		a = new int[]{1};
		expResult = new int[]{1};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a);
		
		a = new int[]{};
		expResult = new int[]{};
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a);
		
		a = null;
		expResult = null;
		WaveformUtils.reverseArrayInPlace(a);
		assertArrayEquals(expResult, a);
	}

	/**
	 * Test of reverseArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testReverseArrayInPlace_3args_3()
	{
		System.out.println("reverseArrayInPlace");
		int[] a = new int[]{1, 2, 3, 4, 5, 6, 7, 8};
		int from = 4;
		int to = 8;
		int[] aReversed = new int[]{1, 2, 3, 4, 8, 7, 6, 5};
		WaveformUtils.reverseArrayInPlace(a, from, to);
		assertArrayEquals(a, aReversed);
	}

	/**
	 * Test of rotateArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testRotateArrayInPlace_doubleArr_int()
	{
		System.out.println("rotateArrayInPlace");
		double[] a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		int n = 0;
		double[] expResult = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0);

		a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		n = 3;
		expResult = new double[]{6.0, 7.0, 8.0, 1.0, 2.0, 3.0, 4.0, 5.0};
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0);

		a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		n = -3;
		expResult = new double[]{4.0, 5.0, 6.0, 7.0, 8.0, 1.0, 2.0, 3.0};
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0);
		
		a = null;
		n = 3;
		expResult = null;
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0);
	}

	/**
	 * Test of rotateArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testRotateArrayInPlace_4args_1()
	{
		System.out.println("rotateArrayInPlace");
		double[] a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		int n = 3;
		int from = 1;
		int to = 7;
		double[] expResult = new double[]{1.0, 5.0, 6.0, 7.0, 2.0, 3.0, 4.0, 8.0};
		WaveformUtils.rotateArrayInPlace(a, n, from, to);
		assertArrayEquals(a, expResult, 0.0);

		a = new double[]{1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0};
		n = -2;
		from = 1;
		to = 7;
		expResult = new double[]{1.0, 4.0, 5.0, 6.0, 7.0, 2.0, 3.0, 8.0};
		WaveformUtils.rotateArrayInPlace(a, n, from, to);
		assertArrayEquals(a, expResult, 0.0);
		
		a = null;
		n = 3;
		from = 0;
		to = 2;
		expResult = null;
		WaveformUtils.rotateArrayInPlace(a, n, from, to);
		assertArrayEquals(a, expResult, 0.0);
	}

	/**
	 * Test of rotateArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testRotateArrayInPlace_floatArr_int()
	{
		System.out.println("rotateArrayInPlace");
		float[] a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		int n = 0;
		float[] expResult = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0f);

		a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		n = 3;
		expResult = new float[]{6.0f, 7.0f, 8.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f};
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0f);

		a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		n = -3;
		expResult = new float[]{4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 1.0f, 2.0f, 3.0f};
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0f);
		
		a = null;
		n = 3;
		expResult = null;
		WaveformUtils.rotateArrayInPlace(a, n);
		assertArrayEquals(a, expResult, 0.0f);
	}

	/**
	 * Test of rotateArrayInPlace method, of class WaveformUtils.
	 */
	@Test
	public void testRotateArrayInPlace_4args_2()
	{
		System.out.println("rotateArrayInPlace");
		float[] a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		int n = 3;
		int from = 1;
		int to = 7;
		float[] expResult = new float[]{1.0f, 5.0f, 6.0f, 7.0f, 2.0f, 3.0f, 4.0f, 8.0f};
		WaveformUtils.rotateArrayInPlace(a, n, from, to);
		assertArrayEquals(a, expResult, 0.0f);

		a = new float[]{1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f};
		n = -2;
		from = 1;
		to = 7;
		expResult = new float[]{1.0f, 4.0f, 5.0f, 6.0f, 7.0f, 2.0f, 3.0f, 8.0f};
		WaveformUtils.rotateArrayInPlace(a, n, from, to);
		assertArrayEquals(a, expResult, 0.0f);
		
		a = null;
		n = 3;
		from = 0;
		to = 2;
		expResult = null;
		WaveformUtils.rotateArrayInPlace(a, n, from, to);
		assertArrayEquals(a, expResult, 0.0f);
	}

	/**
	 * Test of maxIndex method, of class WaveformUtils.
	 */
	@Test
	public void testMaxIndex_doubleArr()
	{
		System.out.println("maxIndex");
		double[] a = new double[]{1.0, 5.0, 3.0, 2.0, 2.2};
		int expResult = 1;
		int result = WaveformUtils.maxIndex(a);
		assertEquals(expResult, result);

		a = new double[]{1.0, 5.0, 5.0, 2.0, 2.2};
		expResult = 1;
		result = WaveformUtils.maxIndex(a);
		assertEquals(expResult, result);

		a = new double[]{42.99999999999999, 26.999999999999996, 21.0, 13.000000000000004, 4.000000000000007, 13.000000000000004, 21.0, 26.999999999999996};
		expResult = 0;
		result = WaveformUtils.maxIndex(a);
		assertEquals(expResult, result);

	}

	/**
	 * Test of maxIndex method, of class WaveformUtils.
	 */
	@Test
	public void testMaxIndex_floatArr()
	{
		System.out.println("maxIndex");
		float[] a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f};
		int expResult = 1;
		int result = WaveformUtils.maxIndex(a);
		assertEquals(expResult, result);

		a = new float[]{1.0f, 5.0f, 5.0f, 2.0f, 2.2f};
		expResult = 1;
		result = WaveformUtils.maxIndex(a);
		assertEquals(expResult, result);

		a = new float[]{42.99999999999999f, 26.999999999999996f, 21.0f, 13.000000000000004f, 4.000000000000007f, 13.000000000000004f, 21.0f, 26.999999999999996f};
		expResult = 0;
		result = WaveformUtils.maxIndex(a);
		assertEquals(expResult, result);
	}

	/**
	 * Test of minIndex method, of class WaveformUtils.
	 */
	@Test
	public void testMinIndex_doubleArr()
	{
		System.out.println("minIndex");
		double[] a = new double[]{1.0, 5.0, -3.0, 2.0, 2.2};
		int expResult = 2;
		int result = WaveformUtils.minIndex(a);
		assertEquals(expResult, result);

		a = new double[]{1.0, -3.0, -3.0, 2.0, 2.2};
		expResult = 1;
		result = WaveformUtils.minIndex(a);
		assertEquals(expResult, result);
	}

	/**
	 * Test of minIndex method, of class WaveformUtils.
	 */
	@Test
	public void testMinIndex_floatArr()
	{
		System.out.println("minIndex");
		float[] a = new float[]{1.0f, 5.0f, -3.0f, 2.0f, 2.2f};
		int expResult = 2;
		int result = WaveformUtils.minIndex(a);
		assertEquals(expResult, result);

		a = new float[]{1.0f, -3.0f, -3.0f, 2.0f, 2.2f};
		expResult = 1;
		result = WaveformUtils.minIndex(a);
		assertEquals(expResult, result);
	}

	/**
	 * Test of cubicRoots method, of class WaveformUtils.
	 */
	@Test
	public void testCubicRoots()
	{
		System.out.println("cubicRoots");
		double a = 0.0;
		double b = 0.0;
		double c = -27.0;
		double[] expResult = new double[]{3.0};
		double[] result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(3.0));

		System.out.println("cubicRoots");
		a = -51.0;
		b = 867.0;
		c = -4913.0;
		expResult = new double[]{17.0};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(17.0));

		System.out.println("cubicRoots");
		a = -57.0;
		b = 1071.0;
		c = -6647.0;
		expResult = new double[]{17.0, 23.0};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(23.0));

		System.out.println("cubicRoots");
		a = -11.0;
		b = -493.0;
		c = 6647.0;
		expResult = new double[]{-23.0, 17.0};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(23.0));

		System.out.println("cubicRoots");
		a = -143.0;
		b = 5087.0;
		c = -50065.0;
		expResult = new double[]{17, 31.0, 95.0};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(95.0));

		System.out.println("cubicRoots");
		a = 2.0;
		b = 1.0;
		c = 0.0;
		expResult = new double[]{-1.0, 0.0};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		System.out.println("cubicRoots");
		a = 200.0;
		b = -0.000015;
		c = 0.0;
		expResult = new double[]{-200.000000075, 0.0, 0.000000075};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(200.0));

		System.out.println("cubicRoots");
		a = 0.0;
		b = -4.0;
		c = 0.0;
		expResult = new double[]{-2.0, 0.0, 2.0};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(2.0));

		System.out.println("cubicRoots");
		a = -4.0;
		b = 0.0;
		c = 0.0;
		expResult = new double[]{0.0, 4.0};
		result = WaveformUtils.cubicRoots(a, b, c);
		assertArrayEquals(expResult, result, Math.ulp(4.0));
	}

	/**
	 * Test of cubicSplineInterpolant method, of class WaveformUtils.
	 */
	@Test
	public void testCubicSplineInterpolant_DoubleArgs()
	{
		System.out.println("cubicSplineInterpolant");
		double[] x = new double[]{0, 0.1, 0.5, 1, 2};
		double[] y = new double[]{0, 0.3, 0.6, -0.2, 3};
		double[] expResultA = Arrays.copyOf(y, y.length);
		double[] expResultB = new double[]{3.167184154175589, 2.6656316916488216, -1.0750535331905777, -0.4817987152034249, 0.0};
		double[] expResultC = new double[]{0.0, -5.0155246252676635, -4.336188436830836, 5.522698072805138, 0.0};
		double[] expResultD = new double[]{-16.718415417558877, 0.5661134903640226, 6.572591006423982, -1.8408993576017125, 0.0};
		double[][] result = WaveformUtils.cubicSplineInterpolant(x, y);
		assertArrayEquals(expResultA, result[0], Math.ulp(10.0));
		assertArrayEquals(expResultB, result[1], Math.ulp(10.0));
		assertArrayEquals(expResultC, result[2], Math.ulp(10.0));
		assertArrayEquals(expResultD, result[3], Math.ulp(20.0));
	}

	/**
	 * Test of cubicSplineInterpolant method, of class WaveformUtils.
	 */
	@Test
	public void testCubicSplineInterpolant_FloatArgs()
	{
		System.out.println("cubicSplineInterpolant");
		float[] x = new float[]{0.0f, 0.1f, 0.5f, 1.0f, 2.0f};
		float[] y = new float[]{0.0f, 0.3f, 0.6f, -0.2f, 3.0f};
		double[] expResultA = new double[]{0, 0.3, 0.6, -0.2, 3};
		double[] expResultB = new double[]{3.167184154175589, 2.6656316916488216, -1.0750535331905777, -0.4817987152034249, 0.0};
		double[] expResultC = new double[]{0.0, -5.0155246252676635, -4.336188436830836, 5.522698072805138, 0.0};
		double[] expResultD = new double[]{-16.718415417558877, 0.5661134903640226, 6.572591006423982, -1.8408993576017125, 0.0};
		double[][] result = WaveformUtils.cubicSplineInterpolant(x, y);
		assertArrayEquals(expResultA, result[0], Math.ulp(10.0f));
		assertArrayEquals(expResultB, result[1], Math.ulp(10.0f));
		assertArrayEquals(expResultC, result[2], Math.ulp(10.0f));
		assertArrayEquals(expResultD, result[3], Math.ulp(20.0f));
	}

	/**
	 * Test of cubicSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testCubicSplineInterpolantUniformSpacing_DoubleArgs()
	{
		System.out.println("cubicSplineInterpolant");
		double[] y = new double[]{0, 0.3, 0.6, -0.2, 3};
		double[] expResultA = Arrays.copyOf(y, y.length);
		double[] expResultB = new double[]{0.15, 0.6, -0.75, 0.9, 0.0};
		double[] expResultC = new double[]{0., 0.45, -1.8, 3.45, 0.0};
		double[] expResultD = new double[]{0.15, -0.75, 1.75, -1.15, 0.0};
		double[][] result = WaveformUtils.cubicSplineInterpolantUniformSpacing(y, 1.0);
		assertArrayEquals(expResultA, result[0], Math.ulp(1.0));
		assertArrayEquals(expResultB, result[1], Math.ulp(2.0));
		assertArrayEquals(expResultC, result[2], Math.ulp(4.0));
		assertArrayEquals(expResultD, result[3], Math.ulp(2.0));
	}

	/**
	 * Test of cubicSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testCubicSplineInterpolantUniformSpacing_FloatArgs()
	{
		System.out.println("cubicSplineInterpolant");
		float[] y = new float[]{0.0f, 0.3f, 0.6f, -0.2f, 3.0f};
		double[] expResultA = new double[]{0, 0.3, 0.6, -0.2, 3};
		double[] expResultB = new double[]{0.15, 0.6, -0.75, 0.9, 0.0};
		double[] expResultC = new double[]{0., 0.45, -1.8, 3.45, 0.0};
		double[] expResultD = new double[]{0.15, -0.75, 1.75, -1.15, 0.0};
		double[][] result = WaveformUtils.cubicSplineInterpolantUniformSpacing(y, 1.0);
		assertArrayEquals(expResultA, result[0], Math.ulp(1.0f));
		assertArrayEquals(expResultB, result[1], Math.ulp(2.0f));
		assertArrayEquals(expResultC, result[2], Math.ulp(4.0f));
		assertArrayEquals(expResultD, result[3], Math.ulp(2.0f));
	}

	/**
	 * Test of cubicSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testCubicSplineInterpolantUniformSpacingFromTo_DoubleArgs()
	{
		System.out.println("cubicSplineInterpolant");
		double[] y = new double[]{-10.0, 0, 0.3, 0.6, -0.2, 3.0, 100.0};
		double[] expResultA = Arrays.copyOfRange(y, 1, 6);
		double[] expResultB = new double[]{0.15, 0.6, -0.75, 0.9, 0.0};
		double[] expResultC = new double[]{0., 0.45, -1.8, 3.45, 0.0};
		double[] expResultD = new double[]{0.15, -0.75, 1.75, -1.15, 0.0};
		double[][] result = WaveformUtils.cubicSplineInterpolantUniformSpacing(y, 1, 6, 1.0);
		assertArrayEquals(expResultA, result[0], Math.ulp(1.0));
		assertArrayEquals(expResultB, result[1], Math.ulp(2.0));
		assertArrayEquals(expResultC, result[2], Math.ulp(4.0));
		assertArrayEquals(expResultD, result[3], Math.ulp(2.0));
	}

	/**
	 * Test of cubicSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testCubicSplineInterpolantUniformSpacingFromTo_FloatArgs()
	{
		System.out.println("cubicSplineInterpolant");
		float[] y = new float[]{-10.0f, 0.0f, 0.3f, 0.6f, -0.2f, 3.0f, 100.0f};
		double[] expResultA = new double[]{0.0f, 0.3f, 0.6f, -0.2f, 3.0f};
		double[] expResultB = new double[]{0.15, 0.6, -0.75, 0.9, 0.0};
		double[] expResultC = new double[]{0., 0.45, -1.8, 3.45, 0.0};
		double[] expResultD = new double[]{0.15, -0.75, 1.75, -1.15, 0.0};
		double[][] result = WaveformUtils.cubicSplineInterpolantUniformSpacing(y, 1, 6, 1.0);
		assertArrayEquals(expResultA, result[0], Math.ulp(1.0f));
		assertArrayEquals(expResultB, result[1], Math.ulp(2.0f));
		assertArrayEquals(expResultC, result[2], Math.ulp(4.0f));
		assertArrayEquals(expResultD, result[3], Math.ulp(2.0f));
	}

	/**
	 * Test of smoothingSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testSmoothingSplineInterpolantUniformSpacing_4args_1()
	{
		System.out.println("smoothingSplineInterpolantUniformSpacing");
		double[] y = {1.0, 5.0, 2.0, 3.0, 4.0, 4.0, 6.0, 2.0, 1.0};
		double standardDeviation = 0.25;
		double dx = 1.0;
		double smoothingParameter = 1.0;
		double[] expResultACoeffs = new double[]{1.136359892260138, 4.634599008583634, 2.330867544288061, 2.913262910415026, 3.904007714784649, 4.26150968998688, 5.624971170757583, 2.264905713415565, 0.9295163555084647};
//		double[] expResultBCoeffs = new double[expResultACoeffs.length];
//		double[] expResultCCoeffs = new double[expResultACoeffs.length];
//		double[] expResultDCoeffs = new double[expResultACoeffs.length];
		double[][] result = WaveformUtils.smoothingSplineInterpolantUniformSpacing(y, standardDeviation, dx, smoothingParameter);
		double[] resultACoeffs = result[0];
		double[] resultBCoeffs = result[1];
		double[] resultCCoeffs = result[2];
		double[] resultDCoeffs = result[3];
		assertArrayEquals(expResultACoeffs, resultACoeffs, Math.ulp(10.0));
//		assertArrayEquals(expResultBCoeffs, resultBCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultCCoeffs, resultCCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultDCoeffs, resultDCoeffs, Math.ulp(1.0));
	}

	/**
	 * Test of smoothingSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testSmoothingSplineInterpolantUniformSpacing_6args_1()
	{
		System.out.println("smoothingSplineInterpolantUniformSpacing");
		double[] y = {2.0, 1.0, 2.0, 1.0, 2.0, 1.0, 2.0, 1.0, 1.0, 5.0, 2.0, 3.0, 4.0, 4.0, 6.0, 2.0, 1.0};
		int from = 8;
		int to = 17;
		double standardDeviation = 0.25;
		double dx = 1.0;
		double smoothingParameter = 1.0;
		double[] expResultACoeffs = new double[]{1.136359892260138, 4.634599008583634, 2.330867544288061, 2.913262910415026, 3.904007714784649, 4.26150968998688, 5.624971170757583, 2.264905713415565, 0.9295163555084647};
//		double[] expResultBCoeffs = new double[expResultACoeffs.length];
//		double[] expResultCCoeffs = new double[expResultACoeffs.length];
//		double[] expResultDCoeffs = new double[expResultACoeffs.length];
		double[][] result = WaveformUtils.smoothingSplineInterpolantUniformSpacing(y, from, to, standardDeviation, dx, smoothingParameter);
		double[] resultACoeffs = result[0];
		double[] resultBCoeffs = result[1];
		double[] resultCCoeffs = result[2];
		double[] resultDCoeffs = result[3];
		assertArrayEquals(expResultACoeffs, resultACoeffs, Math.ulp(10.0));
//		assertArrayEquals(expResultBCoeffs, resultBCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultCCoeffs, resultCCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultDCoeffs, resultDCoeffs, Math.ulp(1.0));
	}

	/**
	 * Test of smoothingSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testSmoothingSplineInterpolantUniformSpacing_4args_2()
	{
		System.out.println("smoothingSplineInterpolantUniformSpacing");
		float[] y = {1.0f, 5.0f, 2.0f, 3.0f, 4.0f, 4.0f, 6.0f, 2.0f, 1.0f};
		double standardDeviation = 0.25;
		double dx = 1.0;
		double smoothingParameter = 1.0;
		double[] expResultACoeffs = new double[]{1.136359892260138, 4.634599008583634, 2.330867544288061, 2.913262910415026, 3.904007714784649, 4.26150968998688, 5.624971170757583, 2.264905713415565, 0.9295163555084647};
//		double[] expResultBCoeffs = new double[expResultACoeffs.length];
//		double[] expResultCCoeffs = new double[expResultACoeffs.length];
//		double[] expResultDCoeffs = new double[expResultACoeffs.length];
		double[][] result = WaveformUtils.smoothingSplineInterpolantUniformSpacing(y, standardDeviation, dx, smoothingParameter);
		double[] resultACoeffs = result[0];
		double[] resultBCoeffs = result[1];
		double[] resultCCoeffs = result[2];
		double[] resultDCoeffs = result[3];
		assertArrayEquals(expResultACoeffs, resultACoeffs, Math.ulp(10.0));
//		assertArrayEquals(expResultBCoeffs, resultBCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultCCoeffs, resultCCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultDCoeffs, resultDCoeffs, Math.ulp(1.0));
	}

	/**
	 * Test of smoothingSplineInterpolantUniformSpacing method, of class
	 * WaveformUtils.
	 */
	@Test
	public void testSmoothingSplineInterpolantUniformSpacing_6args_2()
	{
		System.out.println("smoothingSplineInterpolantUniformSpacing");
		float[] y = {2.0f, 1.0f, 2.0f, 1.0f, 2.0f, 1.0f, 2.0f, 1.0f, 1.0f, 5.0f, 2.0f, 3.0f, 4.0f, 4.0f, 6.0f, 2.0f, 1.0f};
		int from = 8;
		int to = 17;
		double standardDeviation = 0.25;
		double dx = 1.0;
		double smoothingParameter = 1.0;
		double[] expResultACoeffs = new double[]{1.136359892260138, 4.634599008583634, 2.330867544288061, 2.913262910415026, 3.904007714784649, 4.26150968998688, 5.624971170757583, 2.264905713415565, 0.9295163555084647};
//		double[] expResultBCoeffs = new double[expResultACoeffs.length];
//		double[] expResultCCoeffs = new double[expResultACoeffs.length];
//		double[] expResultDCoeffs = new double[expResultACoeffs.length];
		double[][] result = WaveformUtils.smoothingSplineInterpolantUniformSpacing(y, from, to, standardDeviation, dx, smoothingParameter);
		double[] resultACoeffs = result[0];
		double[] resultBCoeffs = result[1];
		double[] resultCCoeffs = result[2];
		double[] resultDCoeffs = result[3];
		assertArrayEquals(expResultACoeffs, resultACoeffs, Math.ulp(10.0));
//		assertArrayEquals(expResultBCoeffs, resultBCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultCCoeffs, resultCCoeffs, Math.ulp(1.0));
//		assertArrayEquals(expResultDCoeffs, resultDCoeffs, Math.ulp(1.0));
	}

	/**
	 * Test of simpleDerivative method, of class WaveformUtils.
	 */
	@Test
	public void testSimpleDerivative_floatArr_float()
	{
		System.out.println("simpleDerivative");
		float[] a = new float[]{1.0f, 5.0f, 3.0f, 2.0f, 2.2f};
		float dx = 2.0F;
		float[] expResult = new float[]{2.0f, 0.5f, -0.75f, -0.2f, 0.1f};
		float[] result = WaveformUtils.simpleDerivative(a, dx);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		a = new float[]{1.0f, 5.0f};
		dx = 1.0f;
		expResult = new float[]{4.0f, 4.0f};
		result = WaveformUtils.simpleDerivative(a, dx);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		a = new float[]{1.0f};
		dx = 1.0f;
		expResult = new float[]{Float.NaN};
		result = WaveformUtils.simpleDerivative(a, dx);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));
	}

	/**
	 * Test of simpleDerivative method, of class WaveformUtils.
	 */
	@Test
	public void testSimpleDerivative_doubleArr_double()
	{
		System.out.println("simpleDerivative");
		double[] a = new double[]{1.0, 5.0, 3.0, 2.0, 2.2};
		double dx = 2.0;
		double[] expResult = new double[]{2.0, 0.5, -0.75, -0.2, 0.1};
		double[] result = WaveformUtils.simpleDerivative(a, dx);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = new double[]{1.0, 5.0};
		dx = 1.0;
		expResult = new double[]{4.0, 4.0};
		result = WaveformUtils.simpleDerivative(a, dx);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = new double[]{1.0};
		dx = 1.0;
		expResult = new double[]{Double.NaN};
		result = WaveformUtils.simpleDerivative(a, dx);
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}

	/**
	 * Test of windowFunction method, of class WaveformUtils.
	 */
	@Test
	public void testWindowFunction()
	{
		System.out.println("windowFunction");
		WaveformUtils.WindowType windowType = WaveformUtils.WindowType.BLACKMAN;
		int n = 0;
		double windowParam = 0.0;
		boolean normalize = false;
		double[] expResult = new double[]{};
		double[] result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN;
		n = 1;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.13, 0.63, 1.0, 0.63, 0.13};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.20077014326253048, 0.84922985673746942, 0.84922985673746942, 0.20077014326253048};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN_HARRIS;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0049, 0.134845, 0.632395, 1.0, 0.632395, 0.134845};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN_HARRIS;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0049, 0.20538826815436156, 0.85023673184563830, 0.85023673184563830, 0.20538826815436156};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN_NUTTALL;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0003628, 0.0613345, 0.5292298, 1.0, 0.5292298, 0.0613345};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(10.0));

		windowType = WaveformUtils.WindowType.BLACKMAN_NUTTALL;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0003628, 0.11051525304987181, 0.79825809695012817, 0.79825809695012817, 0.11051525304987181};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BOHMAN;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.10899778104422932, 0.60899778104422930, 1.0, 0.60899778104422930, 0.10899778104422932};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BOHMAN;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.17912389370628382, 0.83431145225768566, 0.83431145225768566, 0.17912389370628382};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.COSINE_TAPERED;
		n = 6;
		windowParam = 0.9;
		normalize = false;
		expResult = new double[]{0.0, 0.5, 1.0, 1.0, 0.5, 0.0};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.COSINE_TAPERED;
		n = 5;
		windowParam = 0.9;
		normalize = false;
		expResult = new double[]{0.0, 0.5, 1.0, 0.5, 0.0};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.EXACT_BLACKMAN;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.00687876182287185, 0.13988607050730861, 0.63644668959587258, 1.0, 0.63644668959587258, 0.13988607050730861};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(10.0));

		windowType = WaveformUtils.WindowType.EXACT_BLACKMAN;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.00687876182287185, 0.21097316584368608, 0.85206423742372572, 0.85206423742372572, 0.21097316584368608};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.EXPONENTIAL;
		n = 6;
		windowParam = 0.2;
		normalize = false;
		expResult = new double[]{1.0, 0.72477966367769553, 0.52530556088075353, 0.38073078774317576, 0.27594593229224301, 0.2};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.EXPONENTIAL;
		n = 5;
		windowParam = 0.2;
		normalize = false;
		expResult = new double[]{1.0, 0.66874030497642201, 0.44721359549995798, 0.29906975624424414, 0.2};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.FLAT_TOP;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{-0.000421053, -0.051263158, 0.198210528, 1.0, 0.198210528, -0.051263158};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		windowType = WaveformUtils.WindowType.FLAT_TOP;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{-0.000421053, -0.06771425407621184, 0.60687215057621169, 0.60687215057621169, -0.06771425407621184};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		windowType = WaveformUtils.WindowType.GAUSSIAN;
		n = 6;
		windowParam = 0.2;
		normalize = false;
		expResult = new double[]{0.10066889977289781, 0.36044778859782112, 0.77483742888324936, 1.0, 0.77483742888324936, 0.36044778859782112};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.GAUSSIAN;
		n = 5;
		windowParam = 0.2;
		normalize = false;
		expResult = new double[]{0.11416176000968695, 0.45783336177161427, 0.91685535573202892, 0.91685535573202892, 0.45783336177161427};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.HAMMING;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.08, 0.31, 0.77, 1.0, 0.77, 0.31};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(10.0));

		windowType = WaveformUtils.WindowType.HAMMING;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.08, 0.39785218258752419, 0.91214781741247586, 0.91214781741247586, 0.39785218258752419};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.HANNING;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.25, 0.75, 1.0, 0.75, 0.25};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(10.0));

		windowType = WaveformUtils.WindowType.HANNING;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.34549150281252627, 0.90450849718747373, 0.90450849718747373, 0.34549150281252627};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.KAISER;
		n = 6;
		windowParam = 10.0;
		normalize = false;
		expResult = new double[]{0.00035514937472410, 0.09120953428221423, 0.58181016242804617, 1.0, 0.58181016242804617, 0.09120953428221423};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		windowType = WaveformUtils.WindowType.KAISER;
		n = 5;
		windowParam = 10.0;
		normalize = false;
		expResult = new double[]{0.00035514937472410, 0.15184912835301759, 0.82568130216971236, 0.82568130216971236, 0.15184912835301759};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		windowType = WaveformUtils.WindowType.MODIFIED_BARTLETT_HANNING;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.27, 0.73, 1.0, 0.73, 0.27};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.MODIFIED_BARTLETT_HANNING;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.35857354213751996, 0.87942645786247997, 0.87942645786247997, 0.35857354213751996};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.PARZEN;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.07407407407407407, 0.55555555555555556, 1.0, 0.55555555555555556, 0.07407407407407407};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.PARZEN;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.128, 0.808, 0.808, 0.128};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.RECTANGLE;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 1.0, 1.0, 1.0, 1.0, 1.0};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.RECTANGLE;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.TRIANGLE;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.33333333333333333, 0.66666666666666667, 1.0, 0.66666666666666667, 0.3333333333333333};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.TRIANGLE;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.4, 0.8, 0.8, 0.4};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.WELCH;
		n = 6;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.55555555555555556, 0.88888888888888889, 1.0, 0.88888888888888889, 0.55555555555555556};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.WELCH;
		n = 5;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{0.0, 0.64, 0.96, 0.96, 0.64};
		result = WaveformUtils.windowFunction(windowType, n, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

	}

	/**
	 * Test of windowFunctionSingleSided method, of class WaveformUtils.
	 */
	@Test
	public void testWindowFunctionSingleSided()
	{
		System.out.println("windowFunctionSingleSided");
		WaveformUtils.WindowType windowType = WaveformUtils.WindowType.BLACKMAN;
		int radius = 0;
		double windowParam = 0.0;
		boolean normalize = false;
		double[] expResult = new double[]{1.0};
		double[] result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.63, 0.13};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN_HARRIS;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.34401, 0.0049};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.BLACKMAN_NUTTALL;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.2269824, 0.0003628};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(10.0));

		windowType = WaveformUtils.WindowType.BOHMAN;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.60899778104422930, 0.10899778104422932};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.COSINE_TAPERED;
		radius = 2;
		windowParam = 0.9;
		normalize = false;
		expResult = new double[]{1.0, 0.75, 0.25};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.EXACT_BLACKMAN;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.34974204643164219, 0.00687876182287185};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.EXPONENTIAL;
		radius = 2;
		windowParam = 0.2;
		normalize = false;
		expResult = new double[]{1.0, 0.44721359549995798, 0.2};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.FLAT_TOP;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, -0.054736842, -0.000421053};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		windowType = WaveformUtils.WindowType.GAUSSIAN;
		radius = 2;
		windowParam = 0.2;
		normalize = false;
		expResult = new double[]{1.0, 0.60653065971263342, 0.13533528323661273};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.HAMMING;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.54, 0.08};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.HANNING;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.75, 0.25};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(10.0));

		windowType = WaveformUtils.WindowType.KAISER;
		radius = 2;
		windowParam = 10.0;
		normalize = false;
		expResult = new double[]{1.0, 0.28205962082270342, 0.00035514937472410};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0f));

		windowType = WaveformUtils.WindowType.MODIFIED_BARTLETT_HANNING;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.73, 0.27};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.PARZEN;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.55555555555555556, 0.07407407407407407};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.RECTANGLE;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 1.0, 1.0};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.TRIANGLE;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.66666666666666667, 0.3333333333333333};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		windowType = WaveformUtils.WindowType.WELCH;
		radius = 2;
		windowParam = 0.0;
		normalize = false;
		expResult = new double[]{1.0, 0.88888888888888889, 0.55555555555555556};
		result = WaveformUtils.windowFunctionSingleSided(windowType, radius, windowParam, normalize);
		assertArrayEquals(expResult, result, Math.ulp(1.0));

	}

	/**
	 * Test of log2 method, of class WaveformUtils.
	 */
	@Test
	public void testLog2()
	{
		System.out.println("log2");
		double x = 1.0;
		double expResult = 0.0;
		double result = WaveformUtils.log2(x);
		assertEquals(expResult, result, Math.ulp(expResult));

		x = 2.0;
		expResult = 1.0;
		result = WaveformUtils.log2(x);
		assertEquals(expResult, result, Math.ulp(expResult));

		x = 5.0;
		expResult = 2.321928094887362;
		result = WaveformUtils.log2(x);
		assertEquals(expResult, result, Math.ulp(expResult));
	}

	/**
	 * Test of pow method, of class WaveformUtils.
	 */
	@Test
	public void testPow()
	{
		System.out.println("pow");
		double a = 0.0;
		int b = 0;
		double expResult = 1.0;
		double result = WaveformUtils.pow(a, b);
		assertEquals(expResult, result, 0.0);

		a = 2.0;
		b = 5;
		expResult = 32.0;
		result = WaveformUtils.pow(a, b);
		assertEquals(expResult, result, Math.ulp(expResult));

		a = -2.0;
		b = 5;
		expResult = -32.0;
		result = WaveformUtils.pow(a, b);
		assertEquals(expResult, result, Math.ulp(expResult));

		a = 2.0;
		b = -5;
		expResult = 0.03125;
		result = WaveformUtils.pow(a, b);
		assertEquals(expResult, result, Math.ulp(expResult));

	}

	/**
	 * Test of quadraticRoots method, of class WaveformUtils.
	 */
	@Test
	public void testQuadraticRoots()
	{
		System.out.println("quadraticRoots");
		
		double a = 0.0;
		double b = 0.0;
		double c = 0.0;
		double[] expResult = new double[0];
		double[] result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 4.0;
		b = -20.0;
		c = 26.0;
		expResult = new double[0];
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 4.0;
		b = -20.0;
		c = 25.0;
		expResult = new double[]{2.5};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 4.0;
		b = -20.0;
		c = 21.0;
		expResult = new double[]{1.5, 3.5};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 4.0;
		b = 7.0;
		c = 0.0;
		expResult = new double[]{-1.75, 0.0};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 5.0;
		b = 0.0;
		c = -20.0;
		expResult = new double[]{-2.0, 2.0};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 0.0;
		b = 3.0;
		c = -21.0;
		expResult = new double[]{7.0};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 0.0;
		b = 0.0;
		c = 1.0;
		expResult = new double[0];
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 1.0;
		b = 0.0;
		c = 0.0;
		expResult = new double[]{0.0};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 1.0;
		b = 200.0;
		c = -0.000015;
		expResult = new double[]{-200.000000075, 0.000000075};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 1.0;
		b = -1.786737601482363;
		c = 2.054360090947453e-8;
		expResult = new double[]{1.149782767465722e-8, 1.786737589984535};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(2.0));

		a = 94906265.625;
		b = -189812534.0;
		c = 94906268.375;
		expResult = new double[]{1.0, 1.000000028975958};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));

		a = 94906266.375;
		b = -189812534.75;
		c = 94906268.375;
		expResult = new double[]{1.0, 1.000000021073424};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));
		
		a = 1027;
		b = -5922;
		c = 8537;
		expResult = new double[]{2.878587715910591, 2.8877219238167706};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(3.0));
		
		a = 1028;
		b = -5924;
		c = 8534;
		expResult = new double[]{2.859746037925274, 2.9028998764716136};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(3.0));
		
		a = 1.0;
		b = 2.000000000000002;
		c = 1.0;
		expResult = new double[]{-1.00000004472136055, -0.999999955278641450};
		result = WaveformUtils.quadraticRoots(a, b, c);
		System.out.println("a="+a+", b="+b+", c="+c+", expected roots: "+Arrays.toString(expResult));
		assertArrayEquals(expResult, result, Math.ulp(1.0));
	}
}
