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
		double[][][] expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.8}, {4.0, 0.090909090909090909}, {6.0, 0.0}}};
		double[][][] result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, 2.0f, -0.5f, -1.0f, -0.25f, 2.5f, 0.0f, 1.0f};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.8019907962206931}, {4.0, 0.08724147932651083}, {6.0, 0.0}, {6.0, 0.504938459126981}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);

		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, 2.0f, -0.5f, -1.0f, -0.25f, 2.5f, 0.0f, 1.0f, 0.0f, 2.0f, -0.5f, -1.0f, -0.25f, 2.5f, 0.0f, 1.0f};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.8019907962206931}, {4.0, 0.08724147932651083}, {6.0, 0.0}, {6.0, 0.504938459126981}}, {{0.0, 0.0}, {1.0, 0.8019907962206931}, {4.0, 0.08724147932651083}, {6.0, 0.0}, {6.0, 0.504938459126981}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f};
		recordLength = 15;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.7929222663875938}, {3.0, 0.846618206359578}, {5.0, 0.06266630470291856}, {5.0, 0.9283914914239626}, {7.0, 0.4056260786939184}, {10.0, 0.3817101957059963}, {13.0, 0.2809869989664636}, {13.0, 0.2820127572632537}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 16;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.7929222765174989}, {3.0, 0.8466180947120909}, {5.0, 0.06266721836191635}, {5.0, 0.9283879580339174}, {7.0, 0.40566785217349377}, {10.0, 0.382493632658153}, {15.0, 0.0}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);

		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 17;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);

		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 15;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 0;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = null;
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
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
		double[][][] expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.8}, {4.0, 0.090909090909090909}, {6.0, 0.0}}};
		double[][][] result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, 2.0, -0.5, -1.0, -0.25, 2.5, 0.0, 1.0};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.8019907962206931}, {4.0, 0.08724147932651083}, {6.0, 0.0}, {6.0, 0.504938459126981}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);

		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, 2.0, -0.5, -1.0, -0.25, 2.5, 0.0, 1.0, 0.0, 2.0, -0.5, -1.0, -0.25, 2.5, 0.0, 1.0};
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.8019907962206931}, {4.0, 0.08724147932651083}, {6.0, 0.0}, {6.0, 0.504938459126981}}, {{0.0, 0.0}, {1.0, 0.8019907962206931}, {4.0, 0.08724147932651083}, {6.0, 0.0}, {6.0, 0.504938459126981}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905};
		recordLength = 15;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.7929222826067188}, {3.0, 0.8466182436854719}, {5.0, 0.06266630541668228}, {5.0, 0.9283914914733904}, {7.0, 0.4056260712236802}, {10.0, 0.381710191338657}, {13.0, 0.28099249958110384}, {13.0, 0.28200724341862093}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, double args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 16;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = new double[][][]{{{0.0, 0.0}, {1.0, 0.7929222927366231}, {3.0, 0.846618132037994}, {5.0, 0.06266721907567385}, {5.0, 0.9283879580834553}, {7.0, 0.405667844700292}, {10.0, 0.3824936282695642}, {15.0, 0.0}}};
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		System.out.println(Arrays.toString(result[0]));
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 17;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);

		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 15;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 0;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute, float args, CUBIC_SPLINE");
		waveforms = null;
		recordLength = 8;
		interpolationMethod = waveformAnalysisForImageJ.ZeroCrossingLocations.CUBIC_SPLINE;
		expResult = null;
		result = ZeroCrossingLocations.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
	}

}
