package waveformAnalysisForImageJ;


import ij.ImagePlus;
import ij.ImageStack;
import ij.process.ImageProcessor;
import java.util.Arrays;

/**
 * Static utility methods for waveform analysis in ImageJ plugins.
 *
 * @author Jon N. Marsh
 * @version 2014-09-23
 */
public class WaveformUtils
{
	/**
	 * Private constructor
	 */
	private WaveformUtils()
	{
	}

	/**
	 * Window functions
	 */
	public static enum WindowType
	{
		BLACKMAN ("Blackman", false, ""), 
		BLACKMAN_HARRIS ("Blackman-Harris", false, ""), 
		BLACKMAN_NUTTALL ("Blackman-Nuttall", false, "") , 
		BOHMAN ("Bohman", false, ""), 
		COSINE_TAPERED ("Cosine tapered (Tukey)", true, "Tapered section ratio"), 
		EXACT_BLACKMAN ("Exact Blackman", false, ""),
		EXPONENTIAL ("Exponential", true, "Endpoint weight value"), 
		FLAT_TOP ("Flat top", false, ""),
		GAUSSIAN ("Gaussian", true, "Standard deviation"),
		HAMMING ("Hamming", false, ""),
		HANNING ("Hanning", false, ""),
		KAISER ("Kaiser-Bessel", true, "Attenuation parameter (beta)"),
		MODIFIED_BARTLETT_HANNING ("Modified Bartlett-Hanning", false, ""),
		PARZEN ("Parzen", false, ""),
		RECTANGLE ("Rectangle", false, ""),
		TRIANGLE ("Triangle", false, ""), 
		WELCH ("Welch", false, "");
		
		private final String stringValue;
		private final boolean takesParameter;
		private final String paramDescription;
		
		private WindowType(String stringValue, boolean takesParameter, String paramDescription) 
		{
			this.stringValue = stringValue;
			this.takesParameter = takesParameter;
			this.paramDescription = paramDescription;
		}
		
		/**
		 * Static method for retrieving array of nicely formatted names of all
		 * elements.
		 *
		 * @return array of nicely formatted name strings of all window
		 *         functions in the enumeration
		 */
		public static String[] stringValues()
		{
		    WindowType[] w = WindowType.values();
		    String[] s = new String[w.length];
		    
		    for (int i=0; i<w.length; i++) {
		        s[i] = w[i].toString();
		    }
		    
		    return s;
		}
		
		/**
		 *
		 * @return {@code true} if the window function takes a parameter,
		 *         {@code false} otherwise
		 */
		public boolean usesParameter()
		{
			return takesParameter;
		}

		/**
		 *
		 * @return brief description of the parameter the window function takes
		 *         (empty {@code String} if no parameter is used)
		 */
		public String getParameterDescription()
		{
			return paramDescription;
		}

		/**
		 *
		 * @return nicely formatted {@code String} representation of the window
		 *         function name
		 */
		@Override
		public String toString()
		{
			return stringValue;
		}
		
	}
	
	
	//--------------------addScalar Methods-----------------------------------//
	/**
	 * Adds the specified value to each element in the input array and returns
	 * the result as a new array.
	 *
	 * @param a	    input array
	 * @param value	value to add to each element of input array
	 * @return new array with {@code value} added to each element of {@code a}
	 */
	public static final double[] addScalar(double[] a, double value)
	{
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i] + value;
		}
		return result;
	}

	/**
	 * Adds the specified value to each element in the input array and returns
	 * the result as a new array.
	 *
	 * @param a     input array
	 * @param value	value to add to each element of input array
	 * @return new array with {@code value} added to each element of {@code a}
	 */
	public static final float[] addScalar(float[] a, float value)
	{
		float[] result = new float[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i] + value;
		}
		return result;
	}

	/**
	 * Adds the specified value to each element of the input array in place.
	 *
	 * @param a	    input array
	 * @param value	value to add to each element of input array
	 */
	public static final void addScalarInPlace(double[] a, double value)
	{
		addScalarInPlace(a, 0, a.length, value);
	}

	/**
	 * Adds the specified value to each element of the input array in place.
	 *
	 * @param a     input array
	 * @param value	value to add to each element of input array
	 */
	public static final void addScalarInPlace(float[] a, float value)
	{
		addScalarInPlace(a, 0, a.length, value);
	}

	/**
	 * Adds the specified value to each element of the input array in place,
	 * within the specified range of indices. No error checking is performed on
	 * range limits; if the values are negative or outside the range of the
	 * array, a runtime exception may be thrown.
	 *
	 * @param a     input array
	 * @param from  initial index of the range to perform the addition,
	 *              inclusive
	 * @param to    final index of the range to perform the addition, exclusive
	 * @param value	value to add to each element of input array
	 */
	public static final void addScalarInPlace(double[] a, int from, int to, double value)
	{
		for (int i = from; i < to; i++) {
			a[i] += value;
		}
	}

	/**
	 * Adds the specified value to each element of the input array in place,
	 * within the specified range of indices. No error checking is performed on
	 * range limits; if the values are negative or outside the range of the
	 * array, a runtime exception may be thrown.
	 *
	 * @param a     input array
	 * @param from  initial index of the range to perform the addition,
	 *              inclusive
	 * @param to    final index of the range to perform the addition, exclusive
	 * @param value value to add to each element of input array
	 */
	public static final void addScalarInPlace(float[] a, int from, int to, float value)
	{
		for (int i = from; i < to; i++) {
			a[i] += value;
		}
	}

	//--------------------multiplyScalar Methods------------------------------//
	/**
	 * Multiplies the specified value with each element in the input array and
	 * returns the result as a new array.
	 *
	 * @param a     input array
	 * @param value	value to multiply with each element of input array
	 * @return new array with each element of {@code a} multiplied by
	 *         {@code value}
	 */
	public static final double[] multiplyScalar(double[] a, double value)
	{
		double[] result = new double[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i] * value;
		}
		return result;
	}

	/**
	 * Multiplies the specified value with each element of the input array and
	 * returns the result in place.
	 *
	 * @param a     input array
	 * @param value	value to multiply with each element of input array
	 */
	public static final void multiplyScalarInPlace(double[] a, double value)
	{
		multiplyScalarInPlace(a, 0, a.length, value);
	}

	/**
	 * Multiplies the specified value with each element of the input array in
	 * place, within the specified range of indices. No error checking is
	 * performed on range limits; if the values are negative or outside the
	 * range of the array, a runtime exception may be thrown.
	 *
	 * @param a     input array
	 * @param from  initial index of the range to perform the multiplication,
	 *              inclusive
	 * @param to    final index of the range to perform the multiplication,
	 *              exclusive
	 * @param value	value to multiply with each element of input array {@code a}
	 */
	public static final void multiplyScalarInPlace(double[] a, int from, int to, double value)
	{
		for (int i = from; i < to; i++) {
			a[i] *= value;
		}
	}

	/**
	 * Multiplies the specified value with each element in the input array and
	 * returns the result as a new array.
	 *
	 * @param a     input array
	 * @param value	value to multiply with each element of input array
	 * @return new array with each element of {@code a} multiplied by
	 *         {@code value}
	 */
	public static final float[] multiplyScalar(float[] a, float value)
	{
		float[] result = new float[a.length];
		for (int i = 0; i < a.length; i++) {
			result[i] = a[i] * value;
		}
		return result;
	}

	/**
	 * Multiplies the specified value with each element of the input array and
	 * returns the result in place.
	 *
	 * @param a     input array
	 * @param value	value to multiply with each element of input array
	 */
	public static final void multiplyScalarInPlace(float[] a, float value)
	{
		multiplyScalarInPlace(a, 0, a.length, value);
	}

	/**
	 * Multiplies the specified value with each element of the input array in
	 * place, within the specified range of indices. No error checking is
	 * performed on range limits; if the values are negative or outside the
	 * range of the array, a runtime exception may be thrown.
	 *
	 * @param a     input array
	 * @param from  initial index of the range to perform the multiplication,
	 *              inclusive
	 * @param to    final index of the range to perform the multiplication,
	 *              exclusive
	 * @param value	value to multiply with each element of input array {@code a}
	 */
	public static final void multiplyScalarInPlace(float[] a, int from, int to, float value)
	{
		for (int i = from; i < to; i++) {
			a[i] *= value;
		}
	}

	//--------------------mean Methods----------------------------------------//
	/**
	 * Computes the mean value in the specified range of an array.	No error
	 * checking is performed on range limits; if the values are negative or
	 * outside the range of the array, a runtime exception may be thrown.
	 *
	 * @param a    input array
	 * @param from initial index of the range to compute the mean, inclusive
	 * @param to   final index of the range to compute the mean, exclusive
	 * @return	mean value in the specified range of input array
	 */
	public static final double mean(double[] a, int from, int to)
	{
		double sum = 0.0;
		for (int i = from; i < to; i++) {
			sum += a[i];
		}
		return sum / (double) (to - from);
	}

	/**
	 * Computes the mean value of an array.
	 *
	 * @param a	input array
	 * @return	mean value of input array
	 */
	public static final double mean(double[] a)
	{
		return mean(a, 0, a.length);
	}

	/**
	 * Computes the mean value in the specified range of an array.	No error
	 * checking is performed on range limits; if the values are negative or
	 * outside the range of the array, a runtime exception may be thrown.
	 *
	 * @param a    input array
	 * @param from initial index of the range to compute the mean, inclusive
	 * @param to   final index of the range to compute the mean, exclusive
	 * @return mean value in the specified range of input array
	 */
	public static final float mean(float[] a, int from, int to)
	{
		double sum = 0.0;
		for (int i = from; i < to; i++) {
			sum += a[i];
		}
		return (float)(sum / (to - from));
	}

	/**
	 * Computes the mean value of an array.
	 *
	 * @param a	input array
	 * @return mean value of input array
	 */
	public static final float mean(float[] a)
	{
		return mean(a, 0, a.length);
	}

	//--------------------meanAndVariance Methods-----------------------------//
	
	/**
	 * Computes the mean and variance of the input array and returns the result
	 * as a two-element double array: {@code {mean, variance}}, using a
	 * numerically stable algorithm described by 
	 * <a href="http://www.jstor.org/stable/1266577">Welford</a>.
	 *
	 * @param a	                  input array
	 * @param useUnbiasedEstimate set to true to return unbiased estimate of
	 *                            variance
	 * @return two-element array whose first value is the mean of the input
	 *         array and second value is the variance
	 */
	public static final double[] meanAndVariance(double[] a, boolean useUnbiasedEstimate)
	{
		return meanAndVariance(a, useUnbiasedEstimate, 0, a.length);
	}

	/**
	 * Computes the mean and variance of the specified range of an array and
	 * returns the result as a two-element double array:
	 * {@code {mean, variance}}, using a numerically stable algorithm described
	 * by <a href="http://www.jstor.org/stable/1266577">Welford</a>. No 
	 * error checking is performed on range limits; if the values are negative 
	 * or outside the range of the array, unexpected results may occur or a 
	 * runtime exception may be thrown.
	 *
	 * @param a	                  input array
	 * @param useUnbiasedEstimate set to true to return unbiased estimate of variance
	 * @param from                initial index of the range to compute the mean
	 *                            and variance, inclusive
	 * @param to                  final index of the range to compute the mean
	 *                            and variance, exclusive
	 * @return two-element array whose first value is the mean within the
	 *         specified range of input array and second value is the variance
	 *         for the specified range
	 */
	public static final double[] meanAndVariance(double[] a, boolean useUnbiasedEstimate, int from, int to)
	{
		long n = 0;
		double mean = 0.0;
		double m2 = 0.0;

		for (int i = from; i < to; i++) {
			n++;
			double x = a[i];
			double delta = x - mean;
			mean += delta / n;
			m2 += delta * (x - mean);
		}

		double norm = useUnbiasedEstimate ? 1.0/(n-1) : 1.0/n ;
		return new double[]{mean, m2*norm};
	}

	/**
	 * Computes the mean and variance of the input array and returns the result
	 * as a two-element double array: {@code {mean, variance}}, using a
	 * numerically stable algorithm described by 
	 * <a href="http://www.jstor.org/stable/1266577">Welford</a>.
	 *
	 * @param a	                  input array
	 * @param useUnbiasedEstimate set to true to return unbiased estimate of
	 *                            variance
	 * @return two-element array whose first value is the mean of the input
	 *         array and second value is the variance
	 */

	public static final double[] meanAndVariance(float[] a, boolean useUnbiasedEstimate)
	{
		return meanAndVariance(a, useUnbiasedEstimate, 0, a.length);
	}

	/**
	 * Computes the mean and variance of the specified range of an array and
	 * returns the result as a two-element double array:
	 * {@code {mean, variance}}, using a numerically stable algorithm described
	 * by <a href="http://www.jstor.org/stable/1266577">Welford</a>. No 
	 * error checking is performed on range limits; if the values are negative 
	 * or outside the range of the array, unexpected results may occur or a 
	 * runtime exception may be thrown.
	 *
	 * @param a	                  input array
	 * @param useUnbiasedEstimate normalize by {@code n-1} instead of {@code n}
	 * @param from                initial index of the range to compute the mean
	 *                            and variance, inclusive
	 * @param to                  final index of the range to compute the mean
	 *                            and variance, exclusive
	 * @return two-element array whose first value is the mean within the
	 *         specified range of input array and second value is the variance
	 *         for the specified range
	 */
	public static final double[] meanAndVariance(float[] a, boolean useUnbiasedEstimate, int from, int to)
	{
		long n = 0;
		double mean = 0.0;
		double m2 = 0.0;

		for (int i = from; i < to; i++) {
			n++;
			double x = a[i];
			double delta = x - mean;
			mean += delta / n;
			m2 += delta * (x - mean);
		}

		double norm = useUnbiasedEstimate ? 1.0/(n-1) : 1.0/n ;
		return new double[]{mean, m2*norm};
	}

	//--------------------median Methods--------------------------------------//
	/**
	 * Computes the median value of an array. If the array length is even, the
	 * value returned is equal to the average of the middle two values of the
	 * sorted array. The input array is left unchanged. {@code null} input
	 * returns {@code Double.NaN}.
	 *
	 * @param a	input array
	 * @return median value in the specified range of input array; if array is
	 *         zero length, method returns {@code NaN}
	 */
	public static final double median(double[] a)
	{
		return median(a, 0, a.length);
	}

	/**
	 * Computes the median value in the specified range of an array. If the
	 * array length is even, the value returned is equal to the average of the
	 * middle two values of the sorted array. No error checking is performed on
	 * range limits; if the values are negative or outside the range of the
	 * array, unexpected results may occur or a runtime exception may be thrown.
	 * The input array is left unchanged.
	 *
	 * @param a    input array
	 * @param from initial index of the range to compute the median, inclusive
	 * @param to   final index of the range to compute the median, exclusive
	 * @return median value in the specified range of input array; if array
	 *         range is zero, method returns {@code NaN}
	 */
	public static final double median(double[] a, int from, int to)
	{
		int n = to - from;
		if (n > 1) {
			int halfN = n / 2;
			final double[] temp = new double[n];
			System.arraycopy(a, from, temp, 0, n);
			Arrays.sort(temp);
			if (n % 2 == 0) {
				return (0.5 * (temp[halfN] + temp[halfN - 1]));
			} else {
				return temp[halfN];
			}
		} else if (n == 1) {
			return a[from];
		} else {
			return Double.NaN;
		}
	}

	/**
	 * Returns the median value of input array and sorts array in place. If the
	 * array length is even, the value returned is equal to the average of the
	 * middle two values of the sorted array. {@code null} input returns
	 * {@code Double.NaN}. Because no array copying is performed, this may yield
	 * slight performance improvements over {@link #median(double[]) median} if the input array
	 * reordering is unimportant.
	 *
	 * @param a	input array; array is sorted in place
	 * @return median value of {@code a} or {@code NaN} if {@code a} is
	 *         {@code null}
	 */
	public static final double medianAndSort(double[] a)
	{
		if (a != null) {
			int n = a.length;
			if (n > 1) {
				int halfN = n / 2;
				Arrays.sort(a);
				if (n % 2 == 0) {
					return (0.5 * (a[halfN] + a[halfN - 1]));
				} else {
					return a[halfN];
				}
			} else {
				return a[0];
			}
		} else {
			return Double.NaN;
		}
	}

	/**
	 * Computes the median value of an array. If the array length is even, the
	 * value returned is equal to the average of the middle two values of the
	 * sorted array. The input array is left unchanged. {@code null} input
	 * returns {@code Double.NaN}.
	 *
	 * @param a	input array
	 * @return	median value in the specified range of input array; if array is
	 *         zero length, method returns {@code NaN}
	 */
	public static final float median(float[] a)
	{
		return median(a, 0, a.length);
	}

	/**
	 * Computes the median value in the specified range of an array. If the
	 * array length is even, the value returned is equal to the average of the
	 * middle two values of the sorted array. No error checking is performed on
	 * range limits; if the values are negative or outside the range of the
	 * array, unexpected results may occur or a runtime exception may be thrown.
	 * The input array is left unchanged. {@code null} input returns
	 * {@code Double.NaN}.
	 *
	 * @param a		  input array
	 * @param from	initial index of the range to compute the median, inclusive
	 * @param to		 final index of the range to compute the median, exclusive
	 * @return	median value in the specified range of input array; if array
	 *         range is zero, method returns {@code NaN}
	 */
	public static final float median(float[] a, int from, int to)
	{
		int n = to - from;
		if (n > 1) {
			int halfN = n / 2;
			final float[] temp = new float[n];
			System.arraycopy(a, from, temp, 0, n);
			Arrays.sort(temp);
			if (n % 2 == 0) {
				return (0.5f * (temp[halfN] + temp[halfN - 1]));
			} else {
				return temp[halfN];
			}
		} else if (n == 1) {
			return a[from];
		} else {
			return Float.NaN;
		}
	}

	/**
	 * Returns the median value of input array and sorts array in place. If the
	 * array length is even, the value returned is equal to the average of the
	 * middle two values of the sorted array. {@code null} input returns
	 * {@code Double.NaN}. Because no array copying is performed, this may yield
	 * slight performance improvements over {@link #median(float[]) median} if the input array
	 * reordering is unimportant.
	 *
	 * @param a	input array; array is sorted in place
	 * @return median value of {@code a} or {@code NaN} if {@code a} is
	 *         {@code null}
	 */
	public static final float medianAndSort(float[] a)
	{
		if (a != null) {
			int n = a.length;
			if (n > 1) {
				int halfN = n / 2;
				Arrays.sort(a);
				if (n % 2 == 0) {
					return (0.5f * (a[halfN] + a[halfN - 1]));
				} else {
					return a[halfN];
				}
			} else {
				return a[0];
			}
		} else {
			return Float.NaN;
		}
	}

	//--------------------FFT Methods-----------------------------------------//
	/**
	 * Computes complex FFT of real and imaginary input arrays (in place).	For
	 * efficiency, no error checking is performed on real and imaginary input
	 * array lengths. The length of the input array range <b>must</b> be
	 * identical and equal to a power of 2, otherwise a runtime exception may be
	 * thrown. Normalization is consistent with NI LabVIEW&reg FFT
	 * implementation.
	 *
	 * @param	ar		      input array containing the real part of the waveform
	 * @param	ai		      input array containing the imaginary part of the
	 *                  waveform
	 * @param	isForward	true for forward FFT, false for inverse FFT
	 */
	public static final void fftComplexPowerOf2(double ar[], double ai[], boolean isForward)
	{
		fftComplexPowerOf2(ar, ai, 0, ar.length, isForward);
	}

	/**
	 * Computes complex FFT of real and imaginary input arrays (in place) within
	 * the specified range. For efficiency, no error checking is performed on
	 * input data range limits or length of real and imaginary input arrays. The
	 * length of the input array range <b>must</b> be identical and equal to a
	 * power of 2, otherwise a runtime exception may be thrown.	Normalization is
	 * consistent with NI LabVIEW&reg FFT implementation.
	 *
	 *
	 * @param ar        input array containing the real part of the waveform
	 * @param ai        input array containing the imaginary part of the
	 *                  waveform
	 * @param from      initial index of the range to compute the FFT, inclusive
	 * @param to        final index of the range to compute the FFT, exclusive
	 * @param isForward	true for forward FFT, false for inverse FFT
	 */
	public static final void fftComplexPowerOf2(double ar[], double ai[], int from, int to, boolean isForward)
	{
		int n = to - from;
		double scale = 1.0;
		double c = -Math.PI;
		if (!isForward) {
			scale /= n;
			c *= -1;
		}
		int i, j;
		for (i = j = 0; i < n; ++i) {
			if (j >= i) {
				int ii = i + from;
				int jj = j + from;
				double tempr = ar[jj] * scale;
				double tempi = ai[jj] * scale;
				ar[jj] = ar[ii] * scale;
				ai[jj] = ai[ii] * scale;
				ar[ii] = tempr;
				ai[ii] = tempi;
			}
			int m = n / 2;
			while (m >= 1 && j >= m) {
				j -= m;
				m /= 2;
			}
			j += m;
		}

		int mmax, istep;
		double delta, alpha, beta, temp, cosMDelta, cosMDeltaPlusOne, sinMDelta, sinMDeltaPlusOne, tr, ti;
		for (mmax = 1, istep = 2 * mmax; mmax < n; mmax = istep, istep = 2 * mmax) {

			// Set up trig recursions
			delta = c / (double) mmax;
			beta = Math.sin(delta);
			temp = Math.sin(delta / 2.0);
			alpha = 2.0 * temp * temp;
			cosMDelta = 1.0;
			sinMDelta = 0.0;

			for (int m = 0; m < mmax; ++m) {
				for (i = m; i < n; i += istep) {
					j = i + mmax;
					int ii = i + from;
					int jj = j + from;
					tr = cosMDelta * ar[jj] - sinMDelta * ai[jj];
					ti = cosMDelta * ai[jj] + sinMDelta * ar[jj];
					ar[jj] = ar[ii] - tr;
					ai[jj] = ai[ii] - ti;
					ar[ii] += tr;
					ai[ii] += ti;
				}
				cosMDeltaPlusOne = cosMDelta - (alpha * cosMDelta + beta * sinMDelta);
				sinMDeltaPlusOne = sinMDelta - (alpha * sinMDelta - beta * cosMDelta);
				cosMDelta = cosMDeltaPlusOne;
				sinMDelta = sinMDeltaPlusOne;
			}
		}
	}

	public static final double[] fftRealPowerOf2Forward(double ar[])	
	{
		int nn = ar.length;
		int n = nn/2;
		
		// initialize real and imaginary arrays with even and odd element indices of input real array
		double[] re = new double[n];
		double[] im = new double[n];
		for (int i=0; i<n; i++) {
			int j = 2*i;
			re[i] = ar[j];
			im[i] = ar[j + 1];
		}
		
		// perform forward FFT using first half of real and imaginary parts
		fftComplexPowerOf2(re, im, true);
		
		// initialize output imaginary array
		double[] ai = new double[nn];
		
		// sort
		double delta = Math.PI/n;
		double arg = delta;
		for (int i=1; i<n; i++) {
			double c = Math.cos(arg);
			double s = Math.sin(arg);
			double a1 = re[i] + re[n - i];
			double a2 = re[i] - re[n - i];
			double b1 = im[i] + im[n - i];
			double b2 = im[i] - im[n - i];
			
			ar[i] = 0.5*(a1 + c*b1 - s*a2);
			ar[nn - i] = ar[i];
			ai[i] = 0.5*(b2 - s*b1 - c*a2);
			ai[nn - i] = -ai[i];
			
			arg += delta;
		}
		ar[n] = ar[0] = re[0] + im[0];
		ai[n] = ai[0] = 0.0;
		
		return ai;
	}
	
	//--------------------hilbertTransform Methods----------------------------//
	/**
	 * Computes discrete Hilbert transform of the input array (in place) using
	 * FFTs. The transform operation is computed in place. For efficiency, no
	 * error checking is performed on input data range limits or length of input
	 * array. The input array length <b>must</b> be equal to a power of 2,
	 * otherwise a runtime exception may be thrown.
	 *
	 * @param a         input array
	 * @param isForward true for forward Hilbert transform, false for inverse
	 *                  Hilbert transform
	 */
	public static final void fastHilbertTransformPowerOf2(double[] a, boolean isForward)
	{
		int n = a.length;
		int nOver2 = n/2;
		double c = isForward ? 1.0 : -1.0;
		
		// create zero-valued imaginary component
		double[] aIm = new double[n];
		
		// perform FFT
		fftComplexPowerOf2(a, aIm, true);
		
		// zero out DC and Nyquist components
		a[0] = aIm[0] = a[nOver2] = aIm[nOver2] = 0.0;
		
		// multiply positive frequency components by -I
		for (int i=1; i<nOver2; i++) {
			double temp = a[i];
			a[i] = c*aIm[i];
			aIm[i] = -c*temp;
		}
		
		// multiply negative frequency components by I
		for (int i=nOver2+1; i<n; i++) {
			double temp = a[i];
			a[i] = -c*aIm[i];
			aIm[i] = c*temp;
		}
		
		fftComplexPowerOf2(a, aIm, false);
	}
	
	//--------------------power-of-2 Methods----------------------------------//
	/**
	 * Determines if an integer is a positive integral power of 2.
	 *
	 * @param n	input integer
	 * @return	true if {@code n} is greater than zero and a power of 2, false
	 *         otherwise
	 */
	public static final boolean isPowerOf2(int n)
	{
		return ((n != 0) && ((n & (n - 1)) == 0));
	}

	/**
	 * Computes the number required to add to the input to yield the next
	 * highest positive integral power of 2. If input {@code n} is negative,
	 * output is {@code -n}.
	 *
	 * @param n input integer
	 * @return	number required to add to the input integer to yield the next
	 *         highest positive integral power of 2
	 */
	public static final int amountToPadToNextPowerOf2(int n)
	{
		int highestOneBit = Integer.highestOneBit(n);
		return (n - highestOneBit == 0 ? 0 : highestOneBit * 2 - n);
	}

	//--------------------padArray Methods------------------------------------//
	/**
	 * Returns copy of input array padded to the next highest power-of-2 length
	 * with specified value. If the original array length is already a power of
	 * two, the returned array is just a copy of the original array. If input
	 * array is zero-length, output is a new zero-length array.
	 *
	 * @param	a		   input array
	 * @param	value	value used to pad input array
	 * @return	copy of input array padded with {@code value}
	 */
	public static final double[] padToPowerOf2(double[] a, double value)
	{
		return padMultipleWaveformsToPowerOf2(a, a.length, value);
	}

	/**
	 * Returns copy of input waveforms padded to the next highest power-of-2
	 * length with specified value. This method assumes the input array is
	 * composed of an integral number of waveforms with length
	 * {@code waveformLength}. If {@code waveformLength} is already a power of
	 * 2, the returned array is just a copy of the original array. If either
	 * {@code waveformLength>a.length},
	 * {@code waveformLength<=0}, {@code a.length=0}, or {@code a.length} is not
	 * evenly divisible by {@code waveformLength}, a zero-length array is
	 * returned.
	 *
	 * @param	a				          input array
	 * @param	waveformLength	length of individual waveforms within input array
	 * @param	value			       value used to pad input array
	 * @return	array consisting of concatenated copies of input waveforms, each
	 *         padded to the next highest power of 2 with {@code value} (unless
	 *         {@code waveformLength} is itself a power of 2, in which case the
	 *         returned array is just a copy of the input array)
	 */
	public static final double[] padMultipleWaveformsToPowerOf2(double[] a, int waveformLength, double value)
	{
		// If input array length is zero or if waveformLength is zero, return a zero-length array
		if (waveformLength > a.length || a.length == 0 || waveformLength <= 0 || a.length % waveformLength != 0) {
			return new double[0];
		}

		int newLength = waveformLength + amountToPadToNextPowerOf2(waveformLength);

		// If original waveforms' lengths are already a power of 2, just return a copy of original input
		if (newLength == waveformLength) {
			return Arrays.copyOf(a, a.length);
		}

		int numberOfRows = a.length / waveformLength;

		double[] paddedArrays = new double[newLength * numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			int offset1 = i * waveformLength;
			int offset2 = i * newLength;
			System.arraycopy(a, offset1, paddedArrays, offset2, waveformLength);
			if (value != 0.0) { // paddedArrays initialized with zeros already, so don't need next step if zero padding.
				Arrays.fill(paddedArrays, offset2 + waveformLength, offset2 + newLength, value);
			}
		}

		return paddedArrays;
	}

	/**
	 * Returns copy of input array padded to the next highest power-of-2 length
	 * with specified value. If the original array length is already a power of
	 * two, the returned array is just a copy of the original array. If input
	 * array is zero-length, output is a new zero-length array.
	 *
	 * @param a     input array
	 * @param value value used to pad input array
	 * @return copy of input array padded with {@code value}
	 */
	public static final float[] padToPowerOf2(float[] a, float value)
	{
		return padMultipleWaveformsToPowerOf2(a, a.length, value);
	}

	/**
	 * Returns copy of input waveforms padded to the next highest power-of-2
	 * length with specified value. This method assumes the input array is
	 * composed of an integral number of waveforms with length
	 * {@code waveformLength}. If {@code waveformLength} is already a power of
	 * 2, the returned array is just a copy of the original array. If either
	 * {@code waveformLength>a.length},
	 * {@code waveformLength<=0}, {@code a.length=0}, or {@code a.length} is not
	 * evenly divisible by {@code waveformLength}, a zero-length array is
	 * returned.
	 *
	 * @param a              input array
	 * @param waveformLength length of individual waveforms within input array
	 * @param value          value used to pad input array
	 * @return array consisting of concatenated copies of input waveforms, each
	 *         padded to the next highest power of 2 with {@code value} (unless
	 *         {@code waveformLength} is itself a power of 2, in which case the
	 *         returned array is just a copy of the input array)
	 */
	public static final float[] padMultipleWaveformsToPowerOf2(float[] a, int waveformLength, float value)
	{
		// If input array length is zero or if waveformLength is zero, return a zero-length array
		if (waveformLength > a.length || a.length == 0 || waveformLength <= 0 || a.length % waveformLength != 0) {
			return new float[0];
		}

		int newLength = waveformLength + amountToPadToNextPowerOf2(waveformLength);

		// If original waveforms' lengths are already a power of 2, just return a copy of original input
		if (newLength == waveformLength) {
			return Arrays.copyOf(a, a.length);
		}

		int numberOfRows = a.length / waveformLength;

		float[] paddedArrays = new float[newLength * numberOfRows];
		for (int i = 0; i < numberOfRows; i++) {
			int offset1 = i * waveformLength;
			int offset2 = i * newLength;
			System.arraycopy(a, offset1, paddedArrays, offset2, waveformLength);
			if (value != 0.0f) { // paddedArrays initialized with zeros already, so don't need next step if zero padding.
				Arrays.fill(paddedArrays, offset2 + waveformLength, offset2 + newLength, value);
			}
		}

		return paddedArrays;
	}

//--------------------reverseArray Methods---------------------------------//
	/**
	 * Reverses input array in place.
	 *
	 * @param a input array
	 */
	public static final void reverseArrayInPlace(double[] a)
	{
		reverseArrayInPlace(a, 0, a.length);
	}

	/**
	 * Reverses elements of input array between indices {@code from} (inclusive)
	 * and {@code to} (exclusive) in place. No error checking is performed on
	 * range limits; if input parameters are invalid, runtime errors may occur.
	 *
	 * @param a input array
	 * @param from initial index of the range in which to reverse elements, inclusive
	 * @param to final index of the range in which to reverse elements, exclusive
	 */
	public static final void reverseArrayInPlace(double[] a, int from, int to)
	{
		int n = to - from;
		int halfN = n / 2;
		for (int i = 0; i < halfN; i++) {
			double tmp = a[from + i];
			a[from + i] = a[from + (n - 1 - i)];
			a[from + (n - 1 - i)] = tmp;
		}

	}

	/**
	 * Reverses input array in place.
	 *
	 * @param a input array
	 */
	public static final void reverseArrayInPlace(float[] a)
	{
		reverseArrayInPlace(a, 0, a.length);
	}

	/**
	 * Reverses elements of input array between indices {@code from} (inclusive)
	 * and {@code to} (exclusive) in place. No error checking is performed on
	 * range limits; if input parameters are invalid, runtime errors may occur.
	 *
	 * @param a input array
	 * @param from initial index of the range in which to reverse elements, inclusive
	 * @param to final index of the range in which to reverse elements, exclusive
	 */
	public static final void reverseArrayInPlace(float[] a, int from, int to)
	{
		int n = to - from;
		int halfN = n / 2;
		for (int i = 0; i < halfN; i++) {
			float tmp = a[from + i];
			a[from + i] = a[from + (n - 1 - i)];
			a[from + (n - 1 - i)] = tmp;
		}

	}

	/**
	 * Reverses input array in place.
	 *
	 * @param a input array
	 */
	public static final void reverseArrayInPlace(int[] a)
	{
		reverseArrayInPlace(a, 0, a.length);
	}

	/**
	 * Reverses elements of input array between indices {@code from} (inclusive)
	 * and {@code to} (exclusive) in place. No error checking is performed on
	 * range limits; if input parameters are invalid, runtime errors may occur.
	 *
	 * @param a input array
	 * @param from initial index of the range in which to reverse elements, inclusive
	 * @param to final index of the range in which to reverse elements, exclusive
	 */
	public static final void reverseArrayInPlace(int[] a, int from, int to)
	{
		int n = to - from;
		int halfN = n / 2;
		for (int i = 0; i < halfN; i++) {
			int tmp = a[from + i];
			a[from + i] = a[from + (n - 1 - i)];
			a[from + (n - 1 - i)] = tmp;
		}

	}

	//--------------------rotateArray Methods---------------------------------//
	/**
	 * Rotates input array in place by specified number of points.{@code n>0}
	 * corresponds to shift to the right. {@code n<0} corresponds to shift to
	 * the left. If {@code abs(n) >= a.length}, the elements are rotated by
	 * {@code abs(n)%a.length} in the appropriate direction. No action is
	 * performed if {@code a} is {@code null}.
	 *
	 * @param a	input array
	 * @param n	number of places to shift
	 */
	public static final void rotateArrayInPlace(double[] a, int n)
	{
		rotateArrayInPlace(a, n, 0, a.length);
	}

	/**
	 * Rotates portion of input array in specified range in place by specified
	 * number of points. {@code n>0} corresponds to shift to the right,
	 * {@code n<0} corresponds to shift to the left. No error checking is
	 * performed on range limits; if the values are negative or outside the
	 * range of the array, a runtime exception may be thrown. No action is
	 * performed if {@code a} is {@code null} or zero-length, if
	 * {@code to-from<=0}, or if {@code abs(n)<0}.
	 *
	 * @param a	input array
	 * @param n number of places to shift
	 * @param from initial index of the range in which to rotate elements, inclusive
	 * @param to final index of the range in which to rotate elements, exclusive
	 */
	public static final void rotateArrayInPlace(double[] a, int n, int from, int to)
	{
		int absN = Math.abs(n);
		int size = to - from;

		if (a != null && size > 0 && absN > 0 && a.length > 0) {

			if (absN > size) {
				absN = absN % size;
			}

			if (n > 0) {
				reverseArrayInPlace(a, from, from + size);
			}

			reverseArrayInPlace(a, from, from + absN);
			reverseArrayInPlace(a, from + absN, from + size);

			if (n < 0) {
				reverseArrayInPlace(a, from, from + size);
			}
		}
	}

	/**
	 * Rotates input array in place by specified number of points.{@code n>0}
	 * corresponds to shift to the right. {@code n<0} corresponds to shift to
	 * the left. If {@code abs(n) >= a.length}, the elements are rotated by
	 * {@code abs(n)%a.length} in the appropriate direction.
	 *
	 * @param a	input array
	 * @param n	number of places to shift
	 */
	public static final void rotateArrayInPlace(float[] a, int n)
	{
		rotateArrayInPlace(a, n, 0, a.length);
	}

	/**
	 * Rotates portion of input array in specified range in place by specified
	 * number of points.{@code n>0} corresponds to shift to the right,
	 * {@code n<0} corresponds to shift to the left. No error checking is
	 * performed on range limits; if the values are negative or outside the
	 * range of the array, a runtime exception may be thrown. No action is
	 * performed if {@code a} is {@code null} or zero-length, if
	 * {@code to-from<=0}, or if {@code abs(n)<0}.
	 *
	 * @param a	   input array
	 * @param n	   number of places to shift
	 * @param from initial index of the range in which to rotate elements,
	 *             inclusive
	 * @param to   final index of the range in which to rotate elements,
	 *             exclusive
	 */
	public static final void rotateArrayInPlace(float[] a, int n, int from, int to)
	{
		int absN = Math.abs(n);
		int size = to - from;

		if (a != null && size > 0 && absN > 0 && a.length > 0) {

			if (absN > size) {
				absN = absN % size;
			}

			if (n > 0) {
				reverseArrayInPlace(a, from, from + size);
			}

			reverseArrayInPlace(a, from, from + absN);
			reverseArrayInPlace(a, from + absN, from + size);

			if (n < 0) {
				reverseArrayInPlace(a, from, from + size);
			}
		}
	}

	//--------------------freqDomainFilter Methods----------------------------//
	/**
	 * Filters input array within specified range by Fourier transforming the
	 * segment of the input array, multiplying the real and imaginary parts of
	 * the transformed segment by {@code filterCoefficients} element-by-element,
	 * and inverse Fourier transforming the result. The output is the filtered
	 * segment; input array is unchanged. The length of the segment to be
	 * filtered <b>must</b> be a power-of-2, otherwise unexpected results or
	 * runtime errors may occur. No error checking is performed on range limits;
	 * if the values are negative or outside the range of the array, a runtime
	 * exception may be thrown.
	 *
	 * @param a                  input array (assumed to be real-valued)
	 * @param from               initial index of the range of elements to
	 *                           filter
	 * @param to                 final index of the range of elements to filter
	 * @param filterCoefficients frequency-domain filter coefficients; if length
	 *                           of array is not equal to {@code from-to},
	 *                           return an unmodified copy of the input array
	 *                           segment
	 * @return filtered array segment
	 */
	public static final double[] freqDomainFilter(double[] a, int from, int to, double[] filterCoefficients)
	{
		int n = to - from;

		double[] re = Arrays.copyOfRange(a, from, to);
		if (filterCoefficients.length != n) {
			return re;
		}

		double[] im = new double[n];

		fftComplexPowerOf2(re, im, true);
		for (int i = 0; i < n; i++) {
			re[i] *= filterCoefficients[i];
			im[i] *= filterCoefficients[i];
		}
		fftComplexPowerOf2(re, im, false);

		return re;
	}

	/**
	 * Filters input array by Fourier transform, multiplying the real and
	 * imaginary parts by {@code filterCoefficients} element-by-element, and
	 * inverse Fourier transforming the result. The output is the filtered
	 * array; input array is unchanged. The length of the array to be filtered
	 * <b>must</b> be a power of 2, otherwise unexpected results or runtime
	 * errors may occur.
	 *
	 * @param a	                 input array (assumed to be real-valued)
	 * @param filterCoefficients frequency-domain filter coefficients; if length
	 *                           of array is not equal to {@code a.length},
	 *                           return an unmodified copy of the {@code a}
	 * @return filtered array
	 */
	public static final double[] freqDomainFilter(double[] a, double[] filterCoefficients)
	{
		return freqDomainFilter(a, 0, a.length, filterCoefficients);
	}

	/**
	 * Filters multiple real-valued waveforms (assumed to be concatenated
	 * sequentially in {@code re} in the frequency domain by Fourier
	 * transforming each waveform, multiplying the resulting real and imaginary
	 * portions by {@code filterCoefficients} element-by-element, and inverse
	 * Fourier transforming. Results are returned in place.
	 * {@code waveformLength} <b>must</b> be a power of 2, otherwise unexpected
	 * results or runtime errors may occur. If
	 * {@code filterCoefficients.length!=waveformLength}, input array is left
	 * unchanged.
	 *
	 * @param re
	 * @param waveformLength
	 * @param filterCoefficients
	 */
	public static final void freqDomainFilterMultipleWaveformsInPlace(double[] re, int waveformLength, double[] filterCoefficients)
	{
		if (filterCoefficients.length != waveformLength) {
			return;
		}

		int numRecords = re.length / waveformLength;
		double[] im = new double[waveformLength * numRecords];

		for (int i = 0; i < numRecords; i++) {
			int offset = i * waveformLength;
			fftComplexPowerOf2(re, im, offset, offset + waveformLength, true);
			for (int j = 0; j < waveformLength; j++) {
				re[offset + j] *= filterCoefficients[j];
				im[offset + j] *= filterCoefficients[j];
			}
			fftComplexPowerOf2(re, im, offset, offset + waveformLength, false);
		}
	}

	//--------------------maxIndex/minIndex Methods---------------------------//
	/**
	 * Returns the index of the maximum value in array a. If {@code a==null} or
	 * {@code a.length==0}, the return value is {@code -1}. If several elements
	 * have the same value equal to the maximum value of {@code a}, the returned
	 * value is the index of the first instance of the maximum value.
	 *
	 * @param a
	 * @return index of maximum value or {@code -1} if {@code a.length==0} or
	 *         {@code a==null}
	 */
	public static final int maxIndex(double[] a)
	{
		int index = -1;

		if (a != null && a.length != 0) {
			double max = a[0];
			index = 0;
			for (int i = 1; i < a.length; i++) {
				if (a[i] > max) {
					max = a[i];
					index = i;
				}
			}
		}

		return index;
	}

	/**
	 * Returns the index of the maximum value in array a. If {@code a==null} or
	 * {@code a.length==0}, the return value is {@code -1}. If several elements
	 * have the same value equal to the maximum value of {@code a}, the returned
	 * value is the index of the first instance of the maximum value.
	 *
	 * @param a
	 * @return index of maximum value or {@code -1} if {@code a.length==0} or
	 *         {@code a==null}
	 */
	public static final int maxIndex(float[] a)
	{
		int index = -1;

		if (a != null && a.length != 0) {
			float max = a[0];
			index = 0;
			for (int i = 1; i < a.length; i++) {
				if (a[i] > max) {
					max = a[i];
					index = i;
				}
			}
		}

		return index;
	}

	/**
	 * Returns the index of the minimum value in array a. If {@code a==null} or
	 * {@code a.length==0}, the return value is {@code -1}. If several elements
	 * have the same value equal to the minimum value of {@code a}, the returned
	 * value is the index of the first instance of the minimum value.
	 *
	 * @param a
	 * @return index of minimum value or {@code -1} if {@code a.length==0} or
	 *         {@code a==null}
	 */
	public static final int minIndex(double[] a)
	{
		int index = -1;

		if (a != null && a.length != 0) {
			double min = a[0];
			index = 0;
			for (int i = 1; i < a.length; i++) {
				if (a[i] < min) {
					min = a[i];
					index = i;
				}
			}
		}

		return index;
	}

	/**
	 * Returns the index of the minimum value in array a. If {@code a==null} or
	 * {@code a.length==0}, the return value is {@code -1}. If several elements
	 * have the same value equal to the minimum value of {@code a}, the returned
	 * value is the index of the first instance of the minimum value.
	 *
	 * @param a
	 * @return index of minimum value or {@code -1} if {@code a.length==0} or
	 *         {@code a==null}
	 */
	public static final int minIndex(float[] a)
	{
		int index = -1;

		if (a != null && a.length != 0) {
			float min = a[0];
			index = 0;
			for (int i = 1; i < a.length; i++) {
				if (a[i] < min) {
					min = a[i];
					index = i;
				}
			}
		}

		return index;
	}

	//--------------------smoothingSpline Methods-----------------------------//

	/**
	 * Computes interpolant values for input values {@code y[]} evaluated at
	 * points {@code x[]} using the method described by Reinsch in Numerische
	 * Mathematik 10, 177-183 (1967). Ordinal values in {@code x[]} must in
	 * order of increasing value, but can be spaced irregularly. This method is
	 * called once, and the resulting interpolant values can be used to compute
	 * interpolated values at any fractional index between the first and last
	 * values of {@code x[]}. The spline is a natural spline, e.g. the second
	 * derivative is zero at the endpoints. The smoothing spline reverts to a
	 * simple cubic spline when {@code smoothingParameter=0}. For smoothing
	 * splines, setting {@code smoothingParameter=1.0} is usually a good choice.
	 * Very large values for {@code smoothingParameter} yield a straight-line
	 * fit to the input data. {@code standardDeviation} is typically a measure
	 * of the standard deviation of the system noise or the variation of the
	 * part of the signal that should be "smoothed out" by the smoothing spline
	 * algorithm.
	 *
	 * @param x                  ordinal values in increasing order
	 * @param y                  waveform values evaluated at points given in
	 *                           {@code x[]}
	 * @param standardDeviation  standard deviation of the noisy parts of
	 *                           {@code y[]}
	 * @param smoothingParameter positive value set to {@code 0} for no
	 *                           smoothing, {@code 1.0} for "typical" smoothing;
	 *                           negative value will result in zero values for
	 *                           all interpolant coefficients
	 * @return a two-dimensional array of dimension {@code [4][y.length]}, where
	 *         the first element is an array of smoothed values of {@code y[]},
	 *         and whose remaining elements are (in order) the first, second,
	 *         and third derivatives of {@code y[]} evaluated at the points in
	 *         {@code x[]}
	 */
	public static final double[][] smoothingSplineInterpolant(double[] x, double[] y, double standardDeviation, double smoothingParameter)
	{
		int i, n1, n2, m1, m2, n;
		double e, f, f2, g, h, p, s;
		double[] shiftedX, shiftedY, r, r1, r2, t, t1, u, v, a, b, c, d;
		double[][] output;

		n = x.length;
		n1 = 1;
		n2 = n;

		s = smoothingParameter*n; // This is to make Reinsch's "S" correspond with Igor Pro's "s"

		shiftedX = new double[n + 2];
		System.arraycopy(x, 0, shiftedX, 1, n);
		shiftedY = new double[n + 2];
		System.arraycopy(y, 0, shiftedY, 1, n);

		r = new double[n + 2];
		r1 = new double[n + 2];
		r2 = new double[n + 2];
		t = new double[n + 2];
		t1 = new double[n + 2];
		u = new double[n + 2];
		v = new double[n + 2];
		a = new double[n + 2];
		b = new double[n + 2];
		c = new double[n + 2];
		d = new double[n + 2];

		m1 = n1 - 1;
		m2 = n2 + 1;
		r[m1] = 0;
		r[n1] = 0;
		r1[n2] = 0;
		r2[n2] = 0;
		r2[m2] = 0;
		u[m1] = 0;
		u[n1] = 0;
		u[n2] = 0;
		u[m2] = 0;
		g = p = 0;

		m1 = n1 + 1;
		m2 = n2 - 1;
		h = shiftedX[m1] - shiftedX[n1];
		f = (shiftedY[m1] - shiftedY[n1])/h;

		for (i=m1; i<=m2; i++) {
			g = h;
			h = shiftedX[i+1] - shiftedX[i];
			e = f;
			f = (shiftedY[i+1] - shiftedY[i])/h;
			a[i] = f - e;
			t[i] = 2.0*(g+h)/3.0;
			t1[i] = h/3.0;
			r2[i] = standardDeviation/g;
			r[i] = standardDeviation/h;
			r1[i] = -standardDeviation/g - standardDeviation/h;
		}
		
		for (i=m1; i<=m2; i++) {
			b[i] = r[i]*r[i] + r1[i]*r1[i] + r2[i]*r2[i];
			c[i] = r[i]*r1[i+1] + r1[i]*r2[i+1];
			d[i] = r[i]*r2[i+2];
		}

		f2 = -s;

		for (;;) {
			for (i=m1; i<=m2; i++) {
				r1[i-1] = f*r[i-1];
				r2[i-2] = g*r[i-2];
				r[i] = 1.0/(p*b[i]+t[i]-f*r1[i-1]-g*r2[i-2]);
				u[i] = a[i]-r1[i-1]*u[i-1]-r2[i-2]*u[i-2];
				f = p*c[i]+t1[i]-h*r1[i-1];
				g = h;
				h = d[i]*p;
			}
			for (i=m2; i>=m1; i--) {
				u[i] = r[i]*u[i]-r1[i]*u[i+1]-r2[i]*u[i+2];
			}
			e = 0;
			h = 0;
			for (i=n1; i<=m2; i++) {
				g = h;
				h = (u[i+1]-u[i])/(shiftedX[i+1]-shiftedX[i]);
				v[i] = (h-g)*standardDeviation*standardDeviation;
				e += v[i]*(h-g);
			}
			g = v[n2] = -h*standardDeviation*standardDeviation;
			e -= g*h;
			g = f2;
			f2 = e*p*p;

			if (f2 >= s || f2 <= g)
				break;

			f = 0;
			h = (v[m1]-v[n1])/(shiftedX[m1]-shiftedX[n1]);
			for (i=m1; i<=m2; i++) {
				g = h;
				h = (v[i+1]-v[i])/(shiftedX[i+1]-shiftedX[i]);
				g = h-g-r1[i-1]*r[i-1]-r2[i-2]*r[i-2];
				f += g*r[i]*g;
				r[i] = g;
			}
			h = e-p*f;
			
			if (h<=0.0)
				break;
	
			p += (s-f2)/((Math.sqrt(s/e)+p)*h);
		}
		
		for (i=n1; i<=n2; i++) {
			a[i] = shiftedY[i]-p*v[i];
			c[i] = u[i];
		}
		for (i=n1; i<=m2; i++) {
			h = shiftedX[i+1] - shiftedX[i];
			d[i] = (c[i+1]-c[i])/(3.0*h);
			b[i] = (a[i+1]-a[i])/h - (h*d[i]+c[i])*h;
		}

		output = new double[4][n];
		System.arraycopy(a, 1, output[0], 0, n);
		System.arraycopy(b, 1, output[1], 0, n);
		System.arraycopy(c, 1, output[2], 0, n);
		System.arraycopy(d, 1, output[3], 0, n);

		return output;
	}

	/**
	 * Computes interpolant values for input values {@code y[]} evaluated at
	 * points {@code x[]} using the method described by Reinsch in Numerische
	 * Mathematik 10, 177-183 (1967). Ordinal values in {@code x[]} must in
	 * order of increasing value, but can be spaced irregularly. This method is
	 * called once, and the resulting interpolant values can be used to compute
	 * interpolated values at any fractional index between the first and last
	 * values of {@code x[]}. The spline is a natural spline, e.g. the second
	 * derivative is zero at the endpoints. The smoothing spline reverts to a
	 * simple cubic spline when {@code smoothingParameter=0}. For smoothing
	 * splines, setting {@code smoothingParameter=1.0} is usually a good choice.
	 * Very large values for {@code smoothingParameter} yield a straight-line
	 * fit to the input data. {@code standardDeviation} is typically a measure
	 * of the standard deviation of the system noise or the variation of the
	 * part of the signal that should be "smoothed out" by the smoothing spline
	 * algorithm.
	 *
	 * @param x                  ordinal values in increasing order
	 * @param y                  waveform values evaluated at points given in
	 *                           {@code x[]}
	 * @param standardDeviation  standard deviation of the noisy parts of
	 *                           {@code y[]}
	 * @param smoothingParameter positive value set to {@code 0} for no
	 *                           smoothing, {@code 1.0} for "typical" smoothing;
	 *                           negative value will result in zero values for
	 *                           all interpolant coefficients
	 * @return a two-dimensional array of dimension {@code [4][y.length]}, where
	 *         the first element is an array of smoothed values of {@code y[]},
	 *         and whose remaining elements are (in order) the first, second,
	 *         and third derivatives of {@code y[]} evaluated at the points in
	 *         {@code x[]}
	 */
	public static final double[][] smoothingSplineInterpolant(float[] x, float[] y, double standardDeviation, double smoothingParameter)
	{
		int i, n1, n2, m1, m2, n;
		double e, f, f2, g, h, p, s;
		double[] shiftedX, shiftedY, r, r1, r2, t, t1, u, v, a, b, c, d;
		double[][] output;

		n = x.length;
		n1 = 1;
		n2 = n;

		s = smoothingParameter*n; // This is to make Reinsch's "S" correspond with Igor Pro's "s"

		shiftedX = new double[n + 2];
		shiftedY = new double[n + 2];
		for (int j=0; j<n; j++) {
			shiftedX[j+1] = x[j];
			shiftedY[j+1] = y[j];
		}

		r = new double[n + 2];
		r1 = new double[n + 2];
		r2 = new double[n + 2];
		t = new double[n + 2];
		t1 = new double[n + 2];
		u = new double[n + 2];
		v = new double[n + 2];
		a = new double[n + 2];
		b = new double[n + 2];
		c = new double[n + 2];
		d = new double[n + 2];

		m1 = n1 - 1;
		m2 = n2 + 1;
		r[m1] = 0;
		r[n1] = 0;
		r1[n2] = 0;
		r2[n2] = 0;
		r2[m2] = 0;
		u[m1] = 0;
		u[n1] = 0;
		u[n2] = 0;
		u[m2] = 0;
		g = p = 0;

		m1 = n1 + 1;
		m2 = n2 - 1;
		h = shiftedX[m1] - shiftedX[n1];
		f = (shiftedY[m1] - shiftedY[n1])/h;

		for (i=m1; i<=m2; i++) {
			g = h;
			h = shiftedX[i+1] - shiftedX[i];
			e = f;
			f = (shiftedY[i+1] - shiftedY[i])/h;
			a[i] = f - e;
			t[i] = 2.0*(g+h)/3.0;
			t1[i] = h/3.0;
			r2[i] = standardDeviation/g;
			r[i] = standardDeviation/h;
			r1[i] = -standardDeviation/g - standardDeviation/h;
		}
		
		for (i=m1; i<=m2; i++) {
			b[i] = r[i]*r[i] + r1[i]*r1[i] + r2[i]*r2[i];
			c[i] = r[i]*r1[i+1] + r1[i]*r2[i+1];
			d[i] = r[i]*r2[i+2];
		}

		f2 = -s;

		for (;;) {
			for (i=m1; i<=m2; i++) {
				r1[i-1] = f*r[i-1];
				r2[i-2] = g*r[i-2];
				r[i] = 1.0/(p*b[i]+t[i]-f*r1[i-1]-g*r2[i-2]);
				u[i] = a[i]-r1[i-1]*u[i-1]-r2[i-2]*u[i-2];
				f = p*c[i]+t1[i]-h*r1[i-1];
				g = h;
				h = d[i]*p;
			}
			for (i=m2; i>=m1; i--) {
				u[i] = r[i]*u[i]-r1[i]*u[i+1]-r2[i]*u[i+2];
			}
			e = 0;
			h = 0;
			for (i=n1; i<=m2; i++) {
				g = h;
				h = (u[i+1]-u[i])/(shiftedX[i+1]-shiftedX[i]);
				v[i] = (h-g)*standardDeviation*standardDeviation;
				e += v[i]*(h-g);
			}
			g = v[n2] = -h*standardDeviation*standardDeviation;
			e -= g*h;
			g = f2;
			f2 = e*p*p;

			if (f2 >= s || f2 <= g)
				break;

			f = 0;
			h = (v[m1]-v[n1])/(shiftedX[m1]-shiftedX[n1]);
			for (i=m1; i<=m2; i++) {
				g = h;
				h = (v[i+1]-v[i])/(shiftedX[i+1]-shiftedX[i]);
				g = h-g-r1[i-1]*r[i-1]-r2[i-2]*r[i-2];
				f += g*r[i]*g;
				r[i] = g;
			}
			h = e-p*f;
			
			if (h<=0.0)
				break;
	
			p += (s-f2)/((Math.sqrt(s/e)+p)*h);
		}
		
		for (i=n1; i<=n2; i++) {
			a[i] = shiftedY[i]-p*v[i];
			c[i] = u[i];
		}
		for (i=n1; i<=m2; i++) {
			h = shiftedX[i+1] - shiftedX[i];
			d[i] = (c[i+1]-c[i])/(3.0*h);
			b[i] = (a[i+1]-a[i])/h - (h*d[i]+c[i])*h;
		}

		output = new double[4][n];
		System.arraycopy(a, 1, output[0], 0, n);
		System.arraycopy(b, 1, output[1], 0, n);
		System.arraycopy(c, 1, output[2], 0, n);
		System.arraycopy(d, 1, output[3], 0, n);

		return output;
	}
	
	/**
	 * Computes interpolant values for input array {@code y[]} using
	 * the method described by Reinsch in Numerische Mathematik 10, 177-183
	 * (1967). This method is called once, and the resulting interpolant values
	 * can be used to compute interpolated points of {@code y[]} at any fractional
	 * index between {@code 0} and {@code y.length}. The spline is a natural
	 * spline, e.g. the second derivative is zero at the endpoints. The
	 * smoothing spline reverts to a simple cubic spline when
	 * {@code smoothingParameter=0}. For smoothing splines, setting
	 * {@code smoothingParameter=1.0} is usually a good choice. Very large
	 * values for {@code smoothingParameter} yield a straight-line fit to the
	 * input data. {@code standardDeviation} is typically a measure of the
	 * standard deviation of the system noise or the variation of the part of
	 * the signal that should be "smoothed out" by the smoothing spline
	 * algorithm. {@code y[]} is assumed to have uniform spacing between elements,
	 * given by {@code dx}.
	 *
	 * @param y	                 input array
	 * @param standardDeviation	 standard deviation of the noisy parts of
	 *                           {@code y[]}
	 * @param dx                 inter-element spacing
	 * @param smoothingParameter positive value set to {@code 0} for no
	 *                           smoothing, {@code 1.0} for "typical" smoothing;
	 *                           negative value will result in zero values for
	 *                           all interpolant coefficients
	 * @return a two-dimensional array of dimension {@code [4][y.length]}, where
	 *         the first element is an array of smoothed values of
	 *         {@code y[]}, and whose remaining elements are (in order) the
	 *         first, second, and third derivatives of {@code y[]}
	 *         evaluated at the indices between {@code 0} and {@code y.length}
	 */
	public static final double[][] smoothingSplineInterpolantUniformSpacing(double[] y,
			double standardDeviation,
			double dx,
			double smoothingParameter)
	{
		return smoothingSplineInterpolantUniformSpacing(y, 0, y.length, standardDeviation, dx, smoothingParameter);
	}

	/**
	 * Computes interpolant values for segment of input array {@code y[]} within
	 * index range {@code from} and {@code to} using the method described by
	 * Reinsch in Numerische Mathematik 10, 177-183 (1967). This method is
	 * called once for a particular waveform, and the resulting interpolant
	 * values can be used to compute interpolated waveform points at any
	 * fractional index between {@code from} and {@code to}. The spline is a
	 * natural spline, e.g. the second derivative is zero at the endpoints. The
	 * smoothing spline reverts to a simple cubic spline when
	 * {@code smoothingParameter=0}. For smoothing splines, setting
	 * {@code smoothingParameter=1.0} is usually a good choice. Very large
	 * values for {@code smoothingParameter} yield a straight-line fit to the
	 * input data. {@code standardDeviation} is typically a measure of the
	 * standard deviation of the system noise or the variation of the part of
	 * the signal that should be "smoothed out" by the smoothing spline
	 * algorithm. Waveforms in {@code y[]} are assumed to have uniform spacing
	 * between elements, given by {@code dx}. No error checking is performed on
	 * range limits; if the values are negative or outside the range of the
	 * array, a runtime exception may be thrown.
	 *
	 * @param y	                 input array
	 * @param from               initial index of the range of {@code y[]} over
	 *                           which to compute the spline, inclusive
	 * @param to                 final index of the range of {@code y[]} over
	 *                           which to compute the spline, exclusive
	 * @param standardDeviation	 standard deviation of the noisy parts of
	 *                           {@code y[]}
	 * @param dx                 inter-element spacing
	 * @param smoothingParameter positive value set to {@code 0} for no
	 *                           smoothing, {@code 1.0} for "typical" smoothing;
	 *                           negative value will result in zero values for
	 *                           all interpolant coefficients
	 * @return a two-dimensional array of dimension {@code [4][to-from]}, where
	 *         the first element is an array of smoothed values of
	 *         {@code y[]} between {@code from} and {@code to}, and whose
	 *         remaining elements are (in order) the first, second, and third
	 *         derivatives of {@code y[]} evaluated at the indices between
	 *         {@code from} and {@code to}
	 */
	public static final double[][] smoothingSplineInterpolantUniformSpacing(double[] y,
			int from,
			int to,
			double standardDeviation,
			double dx,
			double smoothingParameter)
	{
		int i;
		int n = to - from;
		double e, f, f2, g, h, p;
		double s = smoothingParameter * n;
		double dxInverse = 1.0 / dx;
		double dyOverDx = standardDeviation * dxInverse;
		double dd = dyOverDx * dyOverDx;
		double cc = -4.0 * dd;
		double bb = 6.0 * dd;
		double t1 = dx / 3.0;
		double t = 4.0 * t1;
		double variance = standardDeviation * standardDeviation;
		double[][] coeffs = new double[4][n];
		double[] r = new double[n + 2];
		double[] r1 = new double[n + 2];
		double[] r2 = new double[n + 2];
		double[] u = new double[n + 2];
		double[] v = new double[n + 2];

		if (smoothingParameter >= 0) {

			f = (y[from + 1] - y[from]) * dxInverse;
			for (i = 0; i < n - 2; i++) {
				e = f;
				f = (y[from + i + 2] - y[from + i + 1]) * dxInverse;
				coeffs[0][i] = f - e;
			}

			f2 = -s;
			h = dx;
			g = p = 0.0;

			for (;;) {
				for (i = 2; i < n; i++) {
					r1[i - 1] = f * r[i - 1];
					r2[i - 2] = g * r[i - 2];
					r[i] = 1.0 / (p * bb + t - f * r1[i - 1] - g * r2[i - 2]);
					u[i] = coeffs[0][i - 2] - r1[i - 1] * u[i - 1] - r2[i - 2] * u[i - 2];
					f = p * cc + t1 - h * r1[i - 1];
					g = h;
					h = dd * p;
				}
				for (i = n - 1; i > 1; i--) {
					u[i] = r[i] * u[i] - r1[i] * u[i + 1] - r2[i] * u[i + 2];
				}
				e = 0.0;
				h = 0.0;
				for (i = 1; i < n; i++) {
					g = h;
					h = (u[i + 1] - u[i]) * dxInverse;
					v[i] = (h - g) * variance;
					e += v[i] * (h - g);
				}
				g = v[n] = -h * variance;
				e -= g * h;
				g = f2;
				f2 = e * p * p;

				if (f2 >= s || f2 <= g) {
					break;
				}

				f = 0.0;
				h = (v[2] - v[1]) * dxInverse;
				for (i = 2; i < n; i++) {
					g = h;
					h = (v[i + 1] - v[i]) * dxInverse;
					g = h - g - r1[i - 1] * r[i - 1] - r2[i - 2] * r[i - 2];
					f += g * r[i] * g;
					r[i] = g;
				}
				h = e - p * f;

				if (h <= 0.0) {
					break;
				}

				p += (s - f2) / ((Math.sqrt(s / e) + p) * h);
			}

			for (i = 0; i < n; i++) {
				coeffs[0][i] = y[from + i] - p * v[i + 1];
				coeffs[2][i] = u[i + 1];
			}
			for (i = 0; i < n - 1; i++) {
				coeffs[3][i] = (coeffs[2][i + 1] - coeffs[2][i]) / (3.0 * dx);
				coeffs[1][i] = (coeffs[0][i + 1] - coeffs[0][i]) * dxInverse - (dx * coeffs[3][i] + coeffs[2][i]) * dx;
			}

		}

		return coeffs;

	}

	/**
	 * Computes interpolant values for input array {@code y[]} using
	 * the method described by Reinsch in Numerische Mathematik 10, 177-183
	 * (1967). This method is called once, and the resulting interpolant values
	 * can be used to compute interpolated points of {@code y[]} at any fractional
	 * index between {@code 0} and {@code y.length}. The spline is a natural
	 * spline, e.g. the second derivative is zero at the endpoints. The
	 * smoothing spline reverts to a simple cubic spline when
	 * {@code smoothingParameter=0}. For smoothing splines, setting
	 * {@code smoothingParameter=1.0} is usually a good choice. Very large
	 * values for {@code smoothingParameter} yield a straight-line fit to the
	 * input data. {@code standardDeviation} is typically a measure of the
	 * standard deviation of the system noise or the variation of the part of
	 * the signal that should be "smoothed out" by the smoothing spline
	 * algorithm. {@code y[]} is assumed to have uniform spacing between elements,
	 * given by {@code dx}.
	 *
	 * @param y	                 input array
	 * @param standardDeviation	 standard deviation of the noisy parts of
	 *                           {@code y[]}
	 * @param dx                 inter-element spacing
	 * @param smoothingParameter positive value set to {@code 0} for no
	 *                           smoothing, {@code 1.0} for "typical" smoothing;
	 *                           negative value will result in zero values for
	 *                           all interpolant coefficients
	 * @return a two-dimensional array of dimension {@code [4][y.length]}, where
	 *         the first element is an array of smoothed values of
	 *         {@code y[]}, and whose remaining elements are (in order) the
	 *         first, second, and third derivatives of {@code y[]}
	 *         evaluated at the indices between {@code 0} and {@code y.length}
	 */
	public static final double[][] smoothingSplineInterpolantUniformSpacing(float[] y,
			double standardDeviation,
			double dx,
			double smoothingParameter)
	{
		return smoothingSplineInterpolantUniformSpacing(y, 0, y.length, standardDeviation, dx, smoothingParameter);
	}

	/**
	 * Computes interpolant values for segment of input array {@code y[]} within
	 * index range {@code from} and {@code to} using the method described by
	 * Reinsch in Numerische Mathematik 10, 177-183 (1967). This method is
	 * called once for a particular waveform, and the resulting interpolant
	 * values can be used to compute interpolated waveform points at any
	 * fractional index between {@code from} and {@code to}. The spline is a
	 * natural spline, e.g. the second derivative is zero at the endpoints. The
	 * smoothing spline reverts to a simple cubic spline when
	 * {@code smoothingParameter=0}. For smoothing splines, setting
	 * {@code smoothingParameter=1.0} is usually a good choice. Very large
	 * values for {@code smoothingParameter} yield a straight-line fit to the
	 * input data. {@code standardDeviation} is typically a measure of the
	 * standard deviation of the system noise or the variation of the part of
	 * the signal that should be "smoothed out" by the smoothing spline
	 * algorithm. Waveforms in {@code y[]} are assumed to have uniform spacing
	 * between elements, given by {@code dx}. No error checking is performed on
	 * range limits; if the values are negative or outside the range of the
	 * array, a runtime exception may be thrown. Internal computations are
	 * performed with double precision.
	 *
	 * @param y	                 input array
	 * @param from               initial index of the range of {@code y[]} over
	 *                           which to compute the spline, inclusive
	 * @param to                 final index of the range of {@code y[]} over
	 *                           which to compute the spline, exclusive
	 * @param standardDeviation	 standard deviation of the noisy parts of
	 *                           {@code y[]}
	 * @param dx                 inter-element spacing
	 * @param smoothingParameter positive value set to {@code 0} for no
	 *                           smoothing, {@code 1.0} for "typical" smoothing;
	 *                           negative value will result in zero values for
	 *                           all interpolant coefficients
	 * @return	a two-dimensional array of dimension {@code [4][to-from]}, where
	 *         the first element is an array of smoothed values of
	 *         {@code y[]} between {@code from} and {@code to}, and whose
	 *         remaining elements are (in order) the first, second, and third
	 *         derivatives of {@code y[]} evaluated at the indices between
	 *         {@code from} and {@code to}
	 */
	public static final double[][] smoothingSplineInterpolantUniformSpacing(float[] y,
			int from,
			int to,
			double standardDeviation,
			double dx,
			double smoothingParameter)
	{
		int i;
		int n = to - from;
		double e, f, f2, g, h, p;
		double s = smoothingParameter * n;
		double dxInverse = 1.0 / dx;
		double dyOverDx = standardDeviation * dxInverse;
		double dd = dyOverDx * dyOverDx;
		double cc = -4.0 * dd;
		double bb = 6.0 * dd;
		double t1 = dx / 3.0;
		double t = 4.0 * t1;
		double variance = standardDeviation * standardDeviation;
		double[][] coeffs = new double[4][n];
		double[] r = new double[n + 2];
		double[] r1 = new double[n + 2];
		double[] r2 = new double[n + 2];
		double[] u = new double[n + 2];
		double[] v = new double[n + 2];

		if (smoothingParameter >= 0) {

			f = (y[from + 1] - y[from]) * dxInverse;
			for (i = 0; i < n - 2; i++) {
				e = f;
				f = (y[from + i + 2] - y[from + i + 1]) * dxInverse;
				coeffs[0][i] = f - e;
			}

			f2 = -s;
			h = dx;
			g = p = 0.0;

			for (;;) {
				for (i = 2; i < n; i++) {
					r1[i - 1] = f * r[i - 1];
					r2[i - 2] = g * r[i - 2];
					r[i] = 1.0 / (p * bb + t - f * r1[i - 1] - g * r2[i - 2]);
					u[i] = coeffs[0][i - 2] - r1[i - 1] * u[i - 1] - r2[i - 2] * u[i - 2];
					f = p * cc + t1 - h * r1[i - 1];
					g = h;
					h = dd * p;
				}
				for (i = n - 1; i > 1; i--) {
					u[i] = r[i] * u[i] - r1[i] * u[i + 1] - r2[i] * u[i + 2];
				}
				e = 0.0;
				h = 0.0;
				for (i = 1; i < n; i++) {
					g = h;
					h = (u[i + 1] - u[i]) * dxInverse;
					v[i] = (h - g) * variance;
					e += v[i] * (h - g);
				}
				g = v[n] = -h * variance;
				e -= g * h;
				g = f2;
				f2 = e * p * p;

				if (f2 >= s || f2 <= g) {
					break;
				}

				f = 0.0;
				h = (v[2] - v[1]) * dxInverse;
				for (i = 2; i < n; i++) {
					g = h;
					h = (v[i + 1] - v[i]) * dxInverse;
					g = h - g - r1[i - 1] * r[i - 1] - r2[i - 2] * r[i - 2];
					f += g * r[i] * g;
					r[i] = g;
				}
				h = e - p * f;

				if (h <= 0.0) {
					break;
				}

				p += (s - f2) / ((Math.sqrt(s / e) + p) * h);
			}

			for (i = 0; i < n; i++) {
				coeffs[0][i] = y[from + i] - p * v[i + 1];
				coeffs[2][i] = u[i + 1];
			}
			for (i = 0; i < n - 1; i++) {
				coeffs[3][i] = (coeffs[2][i + 1] - coeffs[2][i]) / (3.0 * dx);
				coeffs[1][i] = (coeffs[0][i + 1] - coeffs[0][i]) * dxInverse - (dx * coeffs[3][i] + coeffs[2][i]) * dx;
			}

		}

		return coeffs;

	}

	//--------------------simpleDerivative Methods----------------------------//
	/**
	 * Computes simple derivative of input array with input spacing given by
	 * {@code dx}. The returned array is the same length as the input array.
	 * Derivative at index {@code i} is given by {@code (a[i+1]-a[i-1])/(2*dx)},
	 * with initial value approximated by {@code (a[1]-a[0])/dx}, and the final
	 * value computed similarly. If {@code a} is {@code null}, a {@code null}
	 * array is returned. If {@code a.length==2}, the returned array is the same
	 * length with both values given by {@code (a[1]-a[0])/dx}. If
	 * {@code a.length==1}, the return array has length one with value
	 * {@code [Float.NaN]}. If {@code a.length==0}, the return array is
	 * zero-length.
	 *
	 * @param a		input array
	 * @param dx	spacing between elements of input array
	 * @return	simple derivative of input array
	 */
	public static final float[] simpleDerivative(float[] a, float dx)
	{
		if (a == null) {
			return null;
		}

		float[] deriv;

		if (a.length > 2) {

			// initialize output array
			deriv = new float[a.length];

			float oneOverDx = 1.0f / dx;
			float oneOver2Dx = oneOverDx * 0.5f;

			// approximate derivative at index zero
			deriv[0] = oneOverDx * (a[1] - a[0]);

			// compute derivative at intermediate points
			for (int i = 1; i < (deriv.length - 1); i++) {
				deriv[i] = oneOver2Dx * (a[i + 1] - a[i - 1]);
			}

			// approximate slope at final point
			deriv[a.length - 1] = oneOverDx * (a[a.length - 1] - a[a.length - 2]);

		} else if (a.length == 2) {

			float slope = (a[1] - a[0]) / dx;
			deriv = new float[]{slope, slope};

		} else if (a.length == 1) {

			deriv = new float[]{Float.NaN};

		} else {

			deriv = new float[0];

		}

		return deriv;
	}

	/**
	 * Computes simple derivative of input array with input spacing given by
	 * {@code dx}. The returned array is the same length as the input array.
	 * Derivative at index {@code i} is given by {@code (a[i+1]-a[i-1])/(2*dx)},
	 * with initial value approximated by {@code (a[1]-a[0])/dx}, and the final
	 * value computed similarly. If {@code a} is {@code null}, a {@code null}
	 * array is returned. If {@code a.length==2}, the returned array is the same
	 * length with both values given by {@code (a[1]-a[0])/dx}. If
	 * {@code a.length==1}, the return array has length one with value
	 * {@code [Float.NaN]}. If {@code a.length==0}, the return array is
	 * zero-length.
	 *
	 * @param a		input array
	 * @param dx	spacing between elements of input array
	 * @return	simple derivative of input array
	 */
	public static final double[] simpleDerivative(double[] a, double dx)
	{
		if (a == null) {
			return null;
		}

		double[] deriv;

		if (a.length > 2) {

			// initialize output array
			deriv = new double[a.length];

			double oneOverDx = 1.0 / dx;
			double oneOver2Dx = oneOverDx * 0.5;

			// approximate derivative at index zero
			deriv[0] = oneOverDx * (a[1] - a[0]);

			// compute derivative at intermediate points
			for (int i = 1; i < (deriv.length - 1); i++) {
				deriv[i] = oneOver2Dx * (a[i + 1] - a[i - 1]);
			}

			// approximate slope at final point
			deriv[a.length - 1] = oneOverDx * (a[a.length - 1] - a[a.length - 2]);

		} else if (a.length == 2) {

			double slope = (a[1] - a[0]) / dx;
			deriv = new double[]{slope, slope};

		} else if (a.length == 1) {

			deriv = new double[]{Double.NaN};

		} else {

			deriv = new double[0];

		}

		return deriv;
	}

	
	//--------------------unbox Array Methods---------------------------------//
	/**
	 * Returns array of unboxed primitive {@code byte}s equivalent to input
	 * {@code Byte} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Byte}s
	 * @return unboxed array of {@code byte}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final byte[] unboxArray(Byte[] a)
	{
		byte[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new byte[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	/**
	 * Returns array of unboxed primitive {@code int}s equivalent to input
	 * {@code Integer} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Integer}s
	 * @return unboxed array of {@code int}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final int[] unboxArray(Integer[] a)
	{
		int[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new int[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	/**
	 * Returns array of unboxed primitive {@code short}s equivalent to input
	 * {@code Short} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Short}s
	 * @return unboxed array of {@code short}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final short[] unboxArray(Short[] a)
	{
		short[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new short[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	/**
	 * Returns array of unboxed primitive {@code long}s equivalent to input
	 * {@code Long} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Long}s
	 * @return unboxed array of {@code long}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final long[] unboxArray(Long[] a)
	{
		long[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new long[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	/**
	 * Returns array of unboxed primitive {@code float}s equivalent to input
	 * {@code Float} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Float}s
	 * @return unboxed array of {@code float}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final float[] unboxArray(Float[] a)
	{
		float[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new float[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	/**
	 * Returns array of unboxed primitive {@code double}s equivalent to input
	 * {@code Double} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Double}s
	 * @return unboxed array of {@code double}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final double[] unboxArray(Double[] a)
	{
		double[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new double[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	/**
	 * Returns array of unboxed primitive {@code char}s equivalent to input
	 * {@code Character} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Character}s
	 * @return unboxed array of {@code char}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final char[] unboxArray(Character[] a)
	{
		char[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new char[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	/**
	 * Returns array of unboxed primitive {@code boolean}s equivalent to input
	 * {@code Boolean} array. Output is {@code null} if input is {@code null}.
	 *
	 * @param a input array of {@code Boolean}s
	 * @return unboxed array of {@code boolean}s, unless input is {@code null}, in
	 *         which case the output is also {@code null}
	 */
	public static final boolean[] unboxArray(Boolean[] a)
	{
		boolean[] unboxedArray;
		
		if (a != null) {
			unboxedArray = new boolean[a.length];
			for (int i=0; i<a.length; i++) {
				unboxedArray[i] = a[i];
			}
		} else {
			unboxedArray = null;
		}
		
		return unboxedArray;
	}
	
	
	//--------------------global minAndMax Methods----------------------------//

	/**
	 * Compute the global minimum and maximum pixel values of an input ImageJ
	 * {@code Imageplus} object. The min and max values are computed over all
	 * slices of an image. If {@code image} is null, the return value is null.
	 *
	 * @param image
	 * @return two-element {@code double} array whose first value is the global
	 *         minimum pixel value and whose second value is the global maximum
	 *         pixel value of {@code image}
	 */
		public static final double[] getGlobalMinAndMax(ImagePlus image)
	{
		double[] minAndMax = null;
		
		if (image != null) {
			double globalMin = Double.POSITIVE_INFINITY;
			double globalMax = Double.NEGATIVE_INFINITY;
			int stackSize = image.getStackSize();
			ImageStack stack = image.getStack();
			for (int slice=1; slice<=stackSize; slice++) {
				ImageProcessor processor = stack.getProcessor(slice);
				double min = processor.getMin();
				double max = processor.getMax();
				if (min < globalMin) {
					globalMin = min;
				}
				if (max > globalMax) {
					globalMax = max;
				}
			}
			minAndMax = new double[] {globalMin, globalMax};
		}
		
		return minAndMax;
	}
	
	//--------------------window Methods--------------------------------------//
	/**
	 * Returns an array of window function values for a given length. Window
	 * functions that require a parameter use the value {@code windowParam}. The
	 * functions follow the asymmetric convention, in that the first value is
	 * (typically) zero but the last value is nonzero. The window functions are
	 * defined over the indices {@code i=0} to {@code i=n-1}, with the following
	 * formulae:
	 * <ul>
	 * <li> {@code BOHMAN: w[i]=((1-(abs(i-n/2)/(n/2)))*cos(PI*(abs(i-n/2)/(n/2))) + (1/PI)*sin(PI*(abs(i-n/2)/(n/2))), i=0,1,...,n-1 } </li>
	 * <li> {@code BLACKMAN: w[i]=0.42-0.5*cos(i*2*PI/n)+0.08*cos(2*i*2*PI/n), i=0,1,...,n-1 } </li>
	 * <li> {@code BLACKMAN_NUTTALL: w[i]=0.3635819-0.4891775*cos(i*2*PI/n)+0.1365995cos(2*i*2*PI/n)-0.0106411cos(3*i*2*PI/n), i=0,1,...,n-1 } </li>
	 * <li> {@code BLACKMAN_HARRIS: w[i]=0.42323-0.49755*cos(i*2*PI/n)+0.07922*cos(2*i*2*PI/n), i=0,1,...,n-1 } </li>
	 * <li> {@code COSINE_TAPERED: w[i]=0.5*(1-cos(PI*i/m)), i=0,1,...,m-1; w[i]=0.5*(1-cos(PI*(n-i-1)/m)), i=n-m,...,n-1; w[i] = 1.0} elsewhere; {@code m=floor(n*windowParam/2.0), 0.0<=windowParam<=1.0 } </li>
	 * <li>	{@code EXACT_BLACKMAN: w[i]=(7938-9240*cos(i*2*PI/n)+1430*cos(2*i*2*PI/n))/18608, i=0,1,...,n-1} </li>
	 * <li> {@code EXPONENTIAL: w[i]=exp(a*i), a=ln(windowParam)/(n-1), i=0,1,...,n-1} </li>
	 * <li>	{@code FLAT_TOP: w[i]=0.21557895-0.41663158*cos(i*2*PI/n)+0.277263158*cos(2*i*2*PI/n)-0.083578947*cos(3*i*2*PI/n)+0.006947368*cos(4*i*2*PI/n), i=0,1,...,n-1} </li>
	 * <li>	{@code GAUSSIAN: w[i]=exp(-(i-(n/2))*(i-(n/2))/(2*windowParam*windowParam*(n+1)*(n+1))), i=0,1,...,n-1} </li>
	 * <li>	{@code HAMMING: w[i]=0.54-0.46*cos(2*PI*i/n), i=0,1,...,n-1} </li>
	 * <li>	{@code HANNING: w[i]=0.5*(1.0-cos(2*PI*i/n)), i=0,1,...,n-1} </li>
	 * <li>	{@code KAISER: w[i]=besselI0(windowParam*sqrt(1-a*a))/besselI0(windowParam), a=(i-k)/k, k=0.5*n, i=0,1,...,n-1, besselI0(x)} is the modified bessel function I<sub>0</sub>(x) </li>
	 * <li> {@code MODIFIED_BARTLETT_HANNING: w[i]=0.62-0.48*abs((i/n)-0.5)+0.38*cos(2*PI*((i/n)-0.5))}; </li>
	 * <li> {@code PARZEN: w[i]=1.0-6.0*c*c+6.0*c*c*c for 0<=c<=0.5, and w[i]=2.0*(1.0-c)^3 for 0.5<c<1, c=abs(i-0.5*n)/(0.5*n), i=0,1,...,n-1} </li>
	 * <li> {@code TRIANGLE: w[i]=1.0-abs((2.0*i - n)/n), i=0,1,...,n-1} </li>
	 * <li> {@code WELCH: w[i] = 1.0-((i - 0.5*n)/(0.5*n))*((i - 0.5*n)/(0.5*n)), i=0,1,...,n-1} </li>
	 * </ul>
	 * The default value is the unit array.
	 *
	 * @param windowType  window function enumerated type constant as given
	 *                    above
	 * @param n           length of window
	 * @param windowParam used only for window functions that require it.
	 * <ul>
	 * <li> {@code COSINE_TAPERED}: {@code windowParam} refers to the ﻿ratio of
	 * the length of the tapered section to the length of the entire signal, and
	 * can have values from {@code 0.0} to {@code 1.0} </li>
	 * <li> {@code EXPONENTIAL}: {@code windowParam} refers to the weighting
	 * value of the last point of the window, and can be any nonzero positive
	 * value </li>
	 * <li> {@code GAUSSIAN}: {@code windowParam} refers to the
	 * length-normalized standard deviation, and can be any number greater than
	 * or equal to zero </li>
	 * <li> {@code KAISER}: {@code windowParam} is proportional to the side-lobe
	 * attenuation, and can be any real number </li>
	 * </ul>
	 * This value is ignored for other types of windows.
	 * @param normalize   set to true to normalize weights so that the sum of
	 *                    all elements is equal to {@code 1.0}
	 * @return an array of length {@code n} with weights computed via the
	 *         formulae above. If {@code n==1}, the returned array has a single
	 *         element equal to unity. If {@code n==0}, an empty array is
	 *         returned. {@code Null} is returned if {@code n<0}. If
	 *         {@code windowParam} is not within a valid range for a window
	 *         function that requires it, the return array is filled with zeros.
	 */
	public static final double[] windowFunction(WindowType windowType, int n, double windowParam, boolean normalize)
	{
		if (n > 1) {

			double[] w = new double[n];

			switch (windowType) {

				case BLACKMAN: {
					double c = 2.0 * Math.PI / n;
					for (int i = 0; i < n; i++) {
						w[i] = 0.42 - 0.5 * Math.cos(i * c) + 0.08 * Math.cos(2 * i * c);
					}
					break;
				}

				case BLACKMAN_HARRIS: {
					double c = 2.0 * Math.PI / n;
					for (int i = 0; i < n; i++) {
						w[i] = 0.42323 - 0.49755 * Math.cos(i * c) + 0.07922 * Math.cos(2 * i * c);
					}
					break;
				}

				case BLACKMAN_NUTTALL: {
					double c = 2.0 * Math.PI / n;
					for (int i = 0; i < n; i++) {
						w[i] = 0.3635819 - 0.4891775 * Math.cos(i * c) + 0.1365995 * Math.cos(2 * i * c) - 0.0106411 * Math.cos(3 * i * c);
					}
					break;
				}
				
				case BOHMAN: {
					double nOver2 = 0.5 * n;
					for (int i = 0; i < n; i++) {
						double fraction = Math.abs(i - nOver2) / nOver2;
						w[i] = ((1.0 - fraction) * Math.cos(Math.PI * fraction) + (1.0 / Math.PI) * (Math.sin(Math.PI * fraction)));
					}
					break;
				}

				case COSINE_TAPERED: {
					int m = (int) Math.floor(0.5 * n * windowParam);
					if (windowParam>=0.0 && windowParam<=1.0) {
						for (int i=0; i<m; i++) {
							w[i] = 0.5*(1.0-Math.cos(Math.PI*i/(double)m));
						}
						for (int i=m; i<n-m; i++) {
							w[i] = 1.0;
						}
						for (int i=n-m; i<n; i++) {
							w[i] = 0.5*(1.0-Math.cos(Math.PI*(n-i-1)/(double)m));
						}
					}
					break;
				}
				
				case EXACT_BLACKMAN: {
					double c = 2.0*Math.PI/n;
					for (int i=0; i<n; i++) {
						w[i] = (7938.0 - 9240.0*Math.cos(i*c) + 1430.0*Math.cos(2*i*c))/18608.0;
					}
					break;
				}
				
				case EXPONENTIAL: {
					if (windowParam > 0) {
					    w[0] = 1.0;
					    if (n > 1) {
    						double a = Math.log(windowParam)/(n-1.0);
    						for (int i=1; i<n; i++) {
    							w[i] = Math.exp(a*i);
    						}
						}
					}
					break;
				}
				
				case FLAT_TOP: {
					double a0 = 0.21557895, a1 = 0.41663158, a2 = 0.277263158, a3 = 0.083578947, a4 = 0.006947368;
					double c = 2.0*Math.PI/n;
					for (int i=0; i<n; i++) {
						w[i] = a0 - a1*Math.cos(i*c) + a2*Math.cos(2*i*c) - a3*Math.cos(3*i*c) + a4*Math.cos(4*i*c);
					}
					break;
				}
				
				case GAUSSIAN: {
					if (windowParam >= 0.0) {
						double m = 0.5*n;
						double c = 1.0/(2.0*windowParam*windowParam*(n+1)*(n+1));
						for (int i=0; i<n; i++) {
							w[i] = Math.exp(-(i-m)*(i-m)*c);
						}
					}
					break;
				}
				
				case HAMMING: {
					double c = 2.0*Math.PI/n;
					for (int i=0; i<n; i++) {
						w[i] = 0.54 - 0.46*Math.cos(i*c);
					}
					break;
				}
				
				case HANNING: {
					double c = 2.0*Math.PI/n;
					for (int i=0; i<n; i++) {
						w[i] = 0.5*(1.0 - Math.cos(i*c));
					}
					break;
				}
				
				case KAISER: {
					double k = 0.5*n;
					for (int i=0; i<n; i++) {
						double a = (i-k)/k;
						w[i] = besselI0(windowParam*Math.sqrt(1.0 - a*a))/besselI0(windowParam);
					}
					break;
				}
				
				case MODIFIED_BARTLETT_HANNING: {
					for (int i=0; i<n; i++) {
						double c = (double)i/(double)n - 0.5;
						w[i] = 0.62 - 0.48*Math.abs(c) + 0.38*Math.cos(2.0*Math.PI*(c));
					}
					break;
				}
				
				case PARZEN: {
					for (int i=0; i<n; i++) {
						double c = Math.abs(i-0.5*n)/(0.5*n);
						if (c <= 0.5) {
							w[i] = 1.0 - 6.0*c*c + 6.0*c*c*c;
						} else {
							w[i] = 2.0*WaveformUtils.pow(1.0 - c, 3);
						}
					}
					break;
				}
				
				case RECTANGLE: {
					for (int i=0; i<n; i++) {
						w[i] = 1.0;
					}
					break;
				}
				
				case TRIANGLE: {
					for (int i=0; i<n; i++) {
						w[i] = 1.0 - Math.abs((2.0*i - n)/n);
					}
					break;
				}
				
				case WELCH: {
					for (int i=0; i<n; i++) {
						double c = (i - 0.5*n)/(0.5*n);
						w[i] = 1.0 - c*c;
					}
					break;
				}
				
				default: {
					Arrays.fill(w, 1.0);
				}
			}

			if (normalize) {
				double sum = 0.0;
				for (int i=0; i<n; i++) {
					sum += w[i];
				}
				multiplyScalarInPlace(w, 1.0/sum);
			}
			
			return w;

		} else if (n == 1) {
			
			return new double[] {1.0};
			
		} else if (n == 0) {
			
			return new double[] {};
			
		} else {

			return null;

		}
	}

	/**
	 * Returns an array of values for the desired window function of odd length
	 * centered at index {@code 0}. The returned array is equivalent to positive
	 * x-axis side of the symmetrical window function of length
	 * {@code 2*radius+1} when the window is centered at the origin (i.e., when
	 * the maximum value of {@code 1.0} is at index {@code 0} of the array). All
	 * values in the returned array are nonzero; e.g., for a {@code TRIANGLE}
	 * window function given an input of {@code radius=1}, the output is a
	 * 2-element array the returned array is {@code {1.0, 0.5}} (non-normalized).
		 * 
	 * @param windowType  window function enumerated type constant
	 * @param radius      number of nonzero-valued points for this window
	 *                    function on the right side of the maximum value of
	 *                    {@code 1.0} at index {@code 0}.
	 * @param windowParam used only for window functions that require it.
	 * <ul>
	 * <li> {@code COSINE_TAPERED}: {@code windowParam} refers to the ﻿ratio of
	 * the length of the tapered section to the length of the entire signal, and
	 * can have values from {@code 0.0} to {@code 1.0} </li>
	 * <li> {@code EXPONENTIAL}: {@code windowParam} refers to the weighting
	 * value of the last point of the window, and can be any nonzero positive
	 * value </li>
	 * <li> {@code GAUSSIAN}: {@code windowParam} refers to the
	 * length-normalized standard deviation, and can be any number greater than
	 * or equal to zero </li>
	 * <li> {@code KAISER}: {@code windowParam} is proportional to the side-lobe
	 * attenuation, and can be any real number </li>
	 * </ul>
	 * This value is ignored for other types of windows.
	 * @param normalize set to true to normalize weights so that the sum of all 
	 *					elements of the equivalent <b>two-sided</b> window 
	 *					function is equal to {@code 1.0}
	 * @return an array of window function values of length {@code radius+1}
	 *         whose first element is always 1.0 and last element is nonzero. If
	 *         {@code radius==0}, the returned array has a single element with
	 *         value 1.0. If {@code radius<0}, the return value is {@code null}.
	 */
	public static final double[] windowFunctionSingleSided(WindowType windowType, int radius, double windowParam, boolean normalize)
	{
		if (radius > 0) {

			double[] w = new double[radius+1];
			w[0] = 1.0;
			int n = 2*(radius+1);

			switch (windowType) {

				case BLACKMAN: {
					double c = 2.0*Math.PI/n;
					for (int i = 1, j=i+radius+1; i <= radius; i++, j++) {
						w[i] = 0.42 - 0.5 * Math.cos(j * c) + 0.08 * Math.cos(2 * j * c);
					}
					break;
				}

				case BLACKMAN_HARRIS: {
					double c = Math.PI/radius;
					for (int i = 1, j=i+radius; i <= radius; i++, j++) {
						w[i] = 0.42323 - 0.49755 * Math.cos(j * c) + 0.07922 * Math.cos(2 * j * c);
					}
					break;
				}

				case BLACKMAN_NUTTALL: {
					double c = Math.PI/radius;
					for (int i = 1, j=i+radius; i <= radius; i++, j++) {
						w[i] = 0.3635819 - 0.4891775 * Math.cos(j * c) + 0.1365995 * Math.cos(2 * j * c) - 0.0106411 * Math.cos(3 * j * c);
					}
					break;
				}
				
				case BOHMAN: {
					double nOver2 = 0.5 * n;
					for (int i = 1, j=i+radius+1; i <= radius; i++, j++) {
						double fraction = Math.abs(j - nOver2) / nOver2;
						w[i] = ((1.0 - fraction) * Math.cos(Math.PI * fraction) + (1.0 / Math.PI) * (Math.sin(Math.PI * fraction)));
					}
					break;
				}

				case COSINE_TAPERED: {
					if (windowParam>0.0 && windowParam<=1.0) {
					   	int m = (int) Math.floor(radius * (1.0-windowParam));
						for (int i=0; i<m; i++)  {
							w[i] = 1.0;
						}
						for (int i=m; i<=radius; i++) {
							w[i] = 0.5*(1.0+Math.cos(Math.PI*(m-i)/(double)(radius+1-m)));
						}
					} 
					break;
				}
				
				case EXACT_BLACKMAN: {
					double c = 2.0*Math.PI/(2*radius);
					for (int i = 0, j=i+radius; i <= radius; i++, j++) {
						w[i] = (7938.0 - 9240.0*Math.cos(j*c) + 1430.0*Math.cos(2*j*c))/18608.0;
					}
					break;
				}
				
				case EXPONENTIAL: {
					if (windowParam > 0) {
						double a = Math.log(windowParam)/radius;
						for (int i=1; i<=radius; i++) {
							w[i] = Math.exp(a*i);
						}						
					}
					break;
				}
				
				case FLAT_TOP: {
					double a0 = 0.21557895, a1 = 0.41663158, a2 = 0.277263158, a3 = 0.083578947, a4 = 0.006947368;
					double c = 2.0*Math.PI/(2*radius);
					for (int i = 1, j=i+radius; i <= radius; i++, j++) {
						w[i] = a0 - a1*Math.cos(j*c) + a2*Math.cos(2*j*c) - a3*Math.cos(3*j*c) + a4*Math.cos(4*j*c);
					}
					break;
				}
				
				case GAUSSIAN: {
					if (windowParam >= 0.0) {
						double c = 1.0/(2.0*windowParam*windowParam*(n-1)*(n-1));
						for (int i = 1; i <= radius; i++) {
							w[i] = Math.exp(-(i*i)*c);
						}
					}
					break;
				}
				
				case HAMMING: {
					double c = Math.PI/radius;
					for (int i = 1, j=i+radius; i <= radius; i++, j++) {
						w[i] = 0.54 - 0.46*Math.cos(j*c);
					}   
					break;
				}
				
				case HANNING: {
					double c = Math.PI/(radius+1);
					for (int i = 1, j=i+radius+1; i <= radius; i++, j++) {
						w[i] = 0.5*(1.0 - Math.cos(j*c));
					}
					break;
				}
				
				case KAISER: {
					double k = radius;
					for (int i = 1, j=i+radius; i <= radius; i++, j++) {
						double a = (j-k)/k;
						w[i] = besselI0(windowParam*Math.sqrt(1.0 - a*a))/besselI0(windowParam);
					}
					break;
				}
				
				case MODIFIED_BARTLETT_HANNING: {
					for (int i = 1, j=i+radius+1; i <= radius; i++, j++) {
						double c = (double)j/(double)n - 0.5;
						w[i] = 0.62 - 0.48*c + 0.38*Math.cos(2.0*Math.PI*(c));
					}
					break;
				}
				
				case PARZEN: {
					for (int i = 1, j=i+radius+1; i <= radius; i++, j++) {
						double c = Math.abs(j-0.5*n)/(0.5*n);
						if (c <= 0.5) {
							w[i] = 1.0 - 6.0*c*c + 6.0*c*c*c;
						} else {
							w[i] = 2.0*WaveformUtils.pow(1.0 - c, 3);
						}
					}
					break;
				}
				
				case RECTANGLE: {
					for (int i=1; i<=radius; i++) {
						w[i] = 1.0;
					}
					break;
				}
				
				case TRIANGLE: {
					for (int i = 1, j=i+radius+1; i <= radius; i++, j++) {
						w[i] = 1.0 - Math.abs((2.0*j - n)/n);
					}
					break;
				}
				
				case WELCH: {
					for (int i = 1, j=i+radius+1; i <= radius; i++, j++) {
						double c = (j - 0.5*n)/(0.5*n);
						w[i] = 1.0 - c*c;
					}
					break;
				}
				
				default: {
					Arrays.fill(w, 1.0);
				}
			}

			if (normalize) {
				double sum = 1.0;
				for (int i=1; i<=radius; i++) {
					sum += 2.0*w[i];
				}
				multiplyScalarInPlace(w, 1.0/sum);
			}
			
			return w;

		} else if (radius == 0) {
			
			return new double[] {1.0};
			
		} else {

			return null;

		}
		
	}
	
	
	//--------------------Miscellaneous Methods-------------------------------//
	/**
	 * Returns the base-2 logarithm of the input.
	 *
	 * @param	x	input value
	 * @return	base-2 logarithm of the input
	 */
	public static final double log2(double x)
	{
		return (3.321928094887362 * Math.log10(x));
	}

	/**
	 * Computes {@code a^b} for integer exponents. Works for both positive and
	 * negative values of the exponent {@code b}.
	 *
	 * @param	a	base for exponentiation
	 * @param	b	integral value of exponent
	 * @return	{@code a} raised to the {@code b}<sup>th</sup> power
	 */
	public static final double pow(double a, int b)
	{
		if (b < 0.0) {
			a = 1.0 / a;
			b *= -1;
		}
		double result = 1.0;
		while (b != 0) {
			if ((b & 1) == 1) {
				result *= a;
			}
			b >>= 1;
			a *= a;
		}

		return result;
	}

	/**
	 * Returns the modified Bessel function I0(x) for any real x value.
	 * 
	 * @param x 
	 */
	private static double besselI0(double x)
	{
		double ax, ans, y;
		
		if ((ax = Math.abs(x)) < 3.75) {
			y = x/3.75;
			y *= y;
			ans = 1.0 + y*(3.5156229 + y*(3.0899424 + y*(1.2067492 + y*(0.2659732 + y*(0.360768e-1 + y*0.45813e-2)))));
		} else {
			y = 3.75/ax;
			ans = (Math.exp(ax)/Math.sqrt(ax))*(0.39894228 + y*(0.1328592e-1 + y*(0.225319e-2 + y*(-0.157565e-2 + y*(0.916281e-2 + y*(-0.2057706e-1 + y*(0.2635537e-1 + y*(-0.1647633e-1 + y*0.392377e-2))))))));
		}
		
		return ans;
	}
}
