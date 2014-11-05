/*
 * Copyright 2014 Jon N. Marsh.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package waveformAnalysisForImageJTestPackage;

import java.util.Arrays;
import org.junit.Test;
import waveformAnalysisForImageJ.ZeroCrossingLocations;
import static org.junit.Assert.*;

/**
 *
 * @author Jon N. Marsh
 */
public class ZeroCrossingLocationsTest
{
	
	public ZeroCrossingLocationsTest()
	{
	}


	/**
	 * Test of execute method, of class ZeroCrossingLocations.
	 */
	@Test
	public void testExecuteFloatArgs()
	{
		System.out.println("execute, float args, LINEAR");
		float[] waveforms = new float[]{0.0f, 2.0f, -0.5f, -1.0f, -0.25f, 2.5f, 0.0f, 1.0f};
		int recordLength = 8;
		int interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.LINEAR;
		double[][] expResult = new double[][]{{0.0, 1.8, 4.090909090909091, 6.0}};
		double[][] result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, 2.0f, -0.5f, -1.0f, -0.25f, 2.5f, 0.0f, 1.0f};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.8019907962206931, 4.087241479326511, 6.0, 6.504938459126981}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);

		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, 2.0f, -0.5f, -1.0f, -0.25f, 2.5f, 0.0f, 1.0f, 0.0f, 2.0f, -0.5f, -1.0f, -0.25f, 2.5f, 0.0f, 1.0f};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.8019907962206931, 4.087241479326511, 6.0, 6.504938459126981}, {0.0, 1.8019907962206931, 4.087241479326511, 6.0, 6.504938459126981}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f};
		recordLength = 15;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.7929222663875937, 3.8466182063595777, 5.062666304702919, 5.928391491423962, 7.405626078693919, 10.381710195705995, 13.280986998966464, 13.282012757263253}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 16;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.792922276517499, 3.846618094712091, 5.062667218361916, 5.928387958033918, 7.405667852173494, 10.382493632658154, 15.0}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
	}
		

	/**
	 * Test of execute method, of class ZeroCrossingLocations.
	 */
	@Test
	public void testExecuteDoubleArgs()
	{
		System.out.println("execute, double args, LINEAR");
		double[] waveforms = new double[]{0.0, 2.0, -0.5, -1.0, -0.25, 2.5, 0.0, 1.0};
		int recordLength = 8;
		int interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.LINEAR;
		double[][] expResult = new double[][]{{0.0, 1.8, 4.090909090909091, 6.0}};
		double[][] result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, 2.0, -0.5, -1.0, -0.25, 2.5, 0.0, 1.0};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.8019907962206931, 4.087241479326511, 6.0, 6.504938459126981}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);

		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, 2.0, -0.5, -1.0, -0.25, 2.5, 0.0, 1.0, 0.0, 2.0, -0.5, -1.0, -0.25, 2.5, 0.0, 1.0};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.8019907962206931, 4.087241479326511, 6.0, 6.504938459126981}, {0.0, 1.8019907962206931, 4.087241479326511, 6.0, 6.504938459126981}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905};
		recordLength = 15;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.7929222826067188, 3.846618243685472, 5.062666305416682, 5.92839149147339, 7.40562607122368, 10.381710191338657, 13.280992499581103, 13.28200724341862}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 16;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][]{{0.0, 1.792922292736623, 3.846618132037994, 5.062667219075674, 5.928387958083455, 7.405667844700292, 10.382493628269565, 15.0}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
	}

	
}
