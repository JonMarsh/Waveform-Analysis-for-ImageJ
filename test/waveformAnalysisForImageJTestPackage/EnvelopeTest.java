package waveformAnalysisForImageJTestPackage;


import waveformAnalysisForImageJ.Envelope;
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


public class EnvelopeTest
{
	
	public EnvelopeTest()
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
	 * Test of execute method, of class Envelope.
	 */
	@Test
	public void testExecute_3args_1()
	{
		System.out.println("execute");
		float[] waveforms = new float[] {1.0f, -1.0f, 2.0f, -2.0f, 3.0f, -2.0f, -1.0f, 1.0f};
		int recordLength = 8;
		Envelope.execute(waveforms, recordLength, false);
		float[] expResult = new float[] {1.567516088485718f, 1.426831960678101f, 2.199042081832886f, 2.03891396522522f, 3.007140398025513f, 3.215454816818237f, 2.159678936004639f, 1.489238142967224f};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));

		waveforms = new float[] {1.0f, -1.0f, 2.0f, -2.0f, 3.0f, -2.0f, -1.0f, 1.0f};
		recordLength = 8;
		Envelope.execute(waveforms, recordLength, true);
		expResult = new float[] {1.4908828864758450f, 1.5170611624290058f, 2.0860037002907990f, 2.1616648477759197f, 2.8824500375224984f, 3.2946586210789053f, 2.2203239768946097f, 1.4083519041382770f};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));

		waveforms = new float[] {1.0f, -1.0f, 2.0f, -2.0f, 3.0f, -2.0f, -1.0f, 1.0f};
		recordLength = 8;
		Envelope.execute(waveforms, recordLength, false);
		expResult = new float[] {1.567516088485718f, 1.426831960678101f, 2.199042081832886f, 2.03891396522522f, 3.007140398025513f, 3.215454816818237f, 2.159678936004639f, 1.489238142967224f};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));
		
		waveforms = new float[] {1.0f, -1.0f, 2.0f, -2.0f, 3.0f, -2.0f, -1.0f, 1.0f, 1.0f, -1.0f, 2.0f, -2.0f, 3.0f, -2.0f, -1.0f, 1.0f};
		recordLength = 8;
		Envelope.execute(waveforms, recordLength, false);
		expResult = new float[] {1.567516088485718f, 1.426831960678101f, 2.199042081832886f, 2.03891396522522f, 3.007140398025513f, 3.215454816818237f, 2.159678936004639f, 1.489238142967224f, 1.567516088485718f, 1.426831960678101f, 2.199042081832886f, 2.03891396522522f, 3.007140398025513f, 3.215454816818237f, 2.159678936004639f, 1.489238142967224f};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));

		
		waveforms = new float[] {1.0f, -1.0f, 2.0f, -2.0f, 3.0f, -2.0f, -1.0f, 1.0f, -1.0f, 2.0f};
		recordLength = 10;
		Envelope.execute(waveforms, recordLength, false);
		expResult = new float[] {1.5578115082694171f, 1.4727930412398540f, 2.1807600916290708f, 2.0072751180434869f, 3.0484247110291984f, 3.7383969927501579f, 2.8952565090260505f, 1.2518840334714916f, 1.5578115082694171f, 2.0716339192295456f};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));
	}

	/**
	 * Test of execute method, of class Envelope.
	 */
	@Test
	public void testExecute_3args_2()
	{
		System.out.println("execute");
		double[] waveforms = new double[] {1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0};
		int recordLength = 8;
		Envelope.execute(waveforms, recordLength, false);
		double[] expResult = new double[] {1.567516088485718, 1.426831960678101, 2.199042081832886, 2.03891396522522, 3.007140398025513, 3.215454816818237, 2.159678936004639, 1.489238142967224};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));

		waveforms = new double[] {1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0};
		recordLength = 8;
		Envelope.execute(waveforms, recordLength, true);
		expResult = new double[] {1.4908828864758450, 1.5170611624290058, 2.0860037002907990, 2.1616648477759197, 2.8824500375224984, 3.2946586210789053, 2.2203239768946097, 1.4083519041382770};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));

		waveforms = new double[] {1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0};
		recordLength = 8;
		Envelope.execute(waveforms, recordLength, false);
		expResult = new double[] {1.567516088485718, 1.426831960678101, 2.199042081832886, 2.03891396522522, 3.007140398025513, 3.215454816818237, 2.159678936004639, 1.489238142967224};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));
		
		waveforms = new double[] {1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0, 1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0};
		recordLength = 8;
		Envelope.execute(waveforms, recordLength, false);
		expResult = new double[] {1.567516088485718, 1.426831960678101, 2.199042081832886, 2.03891396522522, 3.007140398025513, 3.215454816818237, 2.159678936004639, 1.489238142967224, 1.567516088485718, 1.426831960678101, 2.199042081832886, 2.03891396522522, 3.007140398025513, 3.215454816818237, 2.159678936004639, 1.489238142967224};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));

		
		waveforms = new double[] {1.0, -1.0, 2.0, -2.0, 3.0, -2.0, -1.0, 1.0, -1.0, 2.0};
		recordLength = 10;
		Envelope.execute(waveforms, recordLength, false);
		expResult = new double[] {1.5578115082694171, 1.4727930412398540, 2.1807600916290708, 2.0072751180434869, 3.0484247110291984, 3.7383969927501579, 2.8952565090260505, 1.2518840334714916, 1.5578115082694171, 2.0716339192295456};
		assertArrayEquals(expResult, waveforms, Math.ulp(1.0f));
	}


}
