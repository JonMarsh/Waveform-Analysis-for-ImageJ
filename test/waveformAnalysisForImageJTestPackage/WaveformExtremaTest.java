
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

import org.junit.Test;
import waveformAnalysisForImageJ.WaveformExtrema;
import static org.junit.Assert.*;

/**
 *
 * @author Jon N. Marsh
 */
public class WaveformExtremaTest
{
	
	public WaveformExtremaTest()
	{
	}

	/**
	 * Test of execute method, of class WaveformExtrema.
	 */
	@Test
	public void testExecute_3args_1()
	{
		System.out.println("execute null test, floats");
		float[] waveforms = null;
		int recordLength = 0;
		int interpolationMethod = 0;
		WaveformExtrema.SignalExtrema[] expResult = null;
		WaveformExtrema.SignalExtrema[] result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute single waveform test, no interpolation, floats");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 16;
		interpolationMethod = WaveformExtrema.NONE;
		double[] expectedMaxPositions = new double[]{3.0, 5.0, 8.0, 10.0, 13.0};
		double[] expectedMaxValues = new double[]{0.95, -0.02, 0.32, 0.5, -0.1};
		double[] expectedMinPositions = new double[]{1.0, 4.0, 7.0, 9.0, 12.0, 14.0};
		double[] expectedMinValues = new double[]{-0.5, -0.14, -0.21, 0.25, -1.44, -0.38414905};
		result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		double[] resultMaxPositions = result[0].getMaximaPositionsAsPrimitiveDoubleArray();
		double[] resultMinPositions = result[0].getMinimaPositionsAsPrimitiveDoubleArray();
		double[] resultMaxValues = result[0].getMaximaValuesAsPrimitiveDoubleArray();
		double[] resultMinValues = result[0].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0f));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0f));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0f));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0f));

		System.out.println("execute single waveform test, spline interpolation, floats");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 16;
		interpolationMethod = WaveformExtrema.CUBIC_SPLINE;
		expectedMaxPositions = new double[]{2.8823663597110563, 5.482198857390193, 8.137102338000433, 9.775890250444817, 13.196401185358635};
		expectedMaxValues = new double[]{0.9723900454128153, 0.060214419025921284, 0.33362322308909464, 0.5751884356947412, -0.0441734367693049};
		expectedMinPositions = new double[]{0.95371089760149, 4.306857594041816, 6.808537324972403, 8.834617590717327, 11.630217564783145, 14.141961639219403};
		expectedMinValues = new double[]{-0.5018018456421943, -0.2409861463974189, -0.23524004590020162, 0.22970330208229084, -1.6229919745087433, -0.4015458376988517};
		result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		resultMaxPositions = result[0].getMaximaPositionsAsPrimitiveDoubleArray();
		resultMinPositions = result[0].getMinimaPositionsAsPrimitiveDoubleArray();
		resultMaxValues = result[0].getMaximaValuesAsPrimitiveDoubleArray();
		resultMinValues = result[0].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0f));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0f));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0f));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0f));

		System.out.println("execute multiple waveform test, spline interpolation, doubles");
		waveforms = new float[]{0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f, 0.0f, -0.5f, 0.23f, 0.95f, -0.14f, -0.02f, -0.02f, -0.21f, 0.32f, 0.25f, 0.5f, -1.13f, -1.44f, -0.1f, -0.38414905f, 0.0f};
		recordLength = 16;
		interpolationMethod = WaveformExtrema.CUBIC_SPLINE;
		expectedMaxPositions = new double[]{2.8823663597110563, 5.482198857390193, 8.137102338000433, 9.775890250444817, 13.196401185358635};
		expectedMaxValues = new double[]{0.9723900454128153, 0.060214419025921284, 0.33362322308909464, 0.5751884356947412, -0.0441734367693049};
		expectedMinPositions = new double[]{0.95371089760149, 4.306857594041816, 6.808537324972403, 8.834617590717327, 11.630217564783145, 14.141961639219403};
		expectedMinValues = new double[]{-0.5018018456421943, -0.2409861463974189, -0.23524004590020162, 0.22970330208229084, -1.6229919745087433, -0.4015458376988517};
		result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		resultMaxPositions = result[0].getMaximaPositionsAsPrimitiveDoubleArray();
		resultMinPositions = result[0].getMinimaPositionsAsPrimitiveDoubleArray();
		resultMaxValues = result[0].getMaximaValuesAsPrimitiveDoubleArray();
		resultMinValues = result[0].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0f));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0f));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0f));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0f));
		resultMaxPositions = result[1].getMaximaPositionsAsPrimitiveDoubleArray();
		resultMinPositions = result[1].getMinimaPositionsAsPrimitiveDoubleArray();
		resultMaxValues = result[1].getMaximaValuesAsPrimitiveDoubleArray();
		resultMinValues = result[1].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0f));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0f));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0f));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0f));

	}

	/**
	 * Test of execute method, of class WaveformExtrema.
	 */
	@Test
	public void testExecute_3args_2()
	{
		System.out.println("execute null test, doubles");
		double[] waveforms = null;
		int recordLength = 0;
		int interpolationMethod = 0;
		WaveformExtrema.SignalExtrema[] expResult = null;
		WaveformExtrema.SignalExtrema[] result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		assertArrayEquals(expResult, result);
		
		System.out.println("execute single waveform test, no interpolation, doubles");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 16;
		interpolationMethod = WaveformExtrema.NONE;
		double[] expectedMaxPositions = new double[]{3.0, 5.0, 8.0, 10.0, 13.0};
		double[] expectedMaxValues = new double[]{0.95, -0.02, 0.32, 0.5, -0.1};
		double[] expectedMinPositions = new double[]{1.0, 4.0, 7.0, 9.0, 12.0, 14.0};
		double[] expectedMinValues = new double[]{-0.5, -0.14, -0.21, 0.25, -1.44, -0.38414905};
		result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		double[] resultMaxPositions = result[0].getMaximaPositionsAsPrimitiveDoubleArray();
		double[] resultMinPositions = result[0].getMinimaPositionsAsPrimitiveDoubleArray();
		double[] resultMaxValues = result[0].getMaximaValuesAsPrimitiveDoubleArray();
		double[] resultMinValues = result[0].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0));

		System.out.println("execute single waveform test, spline interpolation, doubles");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 16;
		interpolationMethod = WaveformExtrema.CUBIC_SPLINE;
		expectedMaxPositions = new double[]{2.8823663597110563, 5.482198857390193, 8.137102338000433, 9.775890250444817, 13.196401185358635};
		expectedMaxValues = new double[]{0.9723900454128153, 0.060214419025921284, 0.33362322308909464, 0.5751884356947412, -0.0441734367693049};
		expectedMinPositions = new double[]{0.95371089760149, 4.306857594041816, 6.808537324972403, 8.834617590717327, 11.630217564783145, 14.141961639219403};
		expectedMinValues = new double[]{-0.5018018456421943, -0.2409861463974189, -0.23524004590020162, 0.22970330208229084, -1.6229919745087433, -0.4015458376988517};
		result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		resultMaxPositions = result[0].getMaximaPositionsAsPrimitiveDoubleArray();
		resultMinPositions = result[0].getMinimaPositionsAsPrimitiveDoubleArray();
		resultMaxValues = result[0].getMaximaValuesAsPrimitiveDoubleArray();
		resultMinValues = result[0].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0));

		System.out.println("execute multiple waveform test, spline interpolation, doubles");
		waveforms = new double[]{0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0, 0.0, -0.5, 0.23, 0.95, -0.14, -0.02, -0.02, -0.21, 0.32, 0.25, 0.5, -1.13, -1.44, -0.1, -0.38414905, 0.0};
		recordLength = 16;
		interpolationMethod = WaveformExtrema.CUBIC_SPLINE;
		expectedMaxPositions = new double[]{2.8823663597110563, 5.482198857390193, 8.137102338000433, 9.775890250444817, 13.196401185358635};
		expectedMaxValues = new double[]{0.9723900454128153, 0.060214419025921284, 0.33362322308909464, 0.5751884356947412, -0.0441734367693049};
		expectedMinPositions = new double[]{0.95371089760149, 4.306857594041816, 6.808537324972403, 8.834617590717327, 11.630217564783145, 14.141961639219403};
		expectedMinValues = new double[]{-0.5018018456421943, -0.2409861463974189, -0.23524004590020162, 0.22970330208229084, -1.6229919745087433, -0.4015458376988517};
		result = WaveformExtrema.execute(waveforms, recordLength, interpolationMethod);
		resultMaxPositions = result[0].getMaximaPositionsAsPrimitiveDoubleArray();
		resultMinPositions = result[0].getMinimaPositionsAsPrimitiveDoubleArray();
		resultMaxValues = result[0].getMaximaValuesAsPrimitiveDoubleArray();
		resultMinValues = result[0].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0));
		resultMaxPositions = result[1].getMaximaPositionsAsPrimitiveDoubleArray();
		resultMinPositions = result[1].getMinimaPositionsAsPrimitiveDoubleArray();
		resultMaxValues = result[1].getMaximaValuesAsPrimitiveDoubleArray();
		resultMinValues = result[1].getMinimaValuesAsPrimitiveDoubleArray();
		assertArrayEquals(expectedMaxPositions, resultMaxPositions, Math.ulp(13.0));
		assertArrayEquals(expectedMaxValues, resultMaxValues, Math.ulp(1.0));
		assertArrayEquals(expectedMinPositions, resultMinPositions, Math.ulp(14.0));
		assertArrayEquals(expectedMinValues, resultMinValues, Math.ulp(1.0));
	}
	
}
