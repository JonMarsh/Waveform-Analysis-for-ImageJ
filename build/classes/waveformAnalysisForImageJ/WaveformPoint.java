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
package waveformAnalysisForImageJ;

/**
 * A simple container for describing the position (x-value) and y-value of a single point in a waveform. This class
 * is primarily useful for maintaining precision in the value of an interpolated position
 * of a point on a uniformly sampled waveform, by separately specifying the closest integer index
 * less than the actual position, and the remaining fractional index (less than {@code 1.0}).  The y-value
 * of the point is also stored.
 * 
 * @author Jon N. Marsh
 * @version 2014-11-13
 */
public final class WaveformPoint
{

	private int baseIndex;
	private double fractionalIndex;
	private double value;

	/**
	 * Creates an instance with {@code baseIndex=0}, {@code fractionalIndex=0.0},
	 * and {@code value=0.0}.
	 */
	public WaveformPoint()
	{
		baseIndex = 0;
		fractionalIndex = 0.0;
		value = 0.0;
	}

	/**
	 * Initializes an instance with the specified values.
	 * 
	 * @param baseIndex closest integer value less than the actual index.
	 * @param fractionalIndex actual index - baseIndex (should {@code >=0.0} and {@code >1.0})
	 * @param value waveform y-value at this index
	 */
	public WaveformPoint(int baseIndex, double fractionalIndex, double value)
	{
		this.baseIndex = baseIndex;
		this.fractionalIndex = fractionalIndex;
		this.value = value;
	}
	
	/**
	 * Initializes an instance with {@code baseIndex=Math.floor(x)}, {@code fractionalIndex=x-baseIndex}, and
	 * {@code value=y}.
	 * 
	 * @param x ordinal
	 * @param y value
	 */
	public WaveformPoint(double x, double y)
	{
		this.baseIndex = (int)Math.floor(x);
		this.fractionalIndex = x - baseIndex;
		this.value = y;
	}

	public int getBaseIndex()
	{
		return baseIndex;
	}

	public void setBaseIndex(int baseIndex)
	{
		this.baseIndex = baseIndex;
	}

	public double getFractionalIndex()
	{
		return fractionalIndex;
	}

	public void setFractionalIndex(double fractionalIndex)
	{
		this.fractionalIndex = fractionalIndex;
	}
	
	/**
	 * @return {@code baseIndex+fractionalIndex}.
	 */
	public double getFullIndex()
	{
		return (baseIndex+fractionalIndex);
	}

	public double getValue()
	{
		return value;
	}

	public void setValue(double value)
	{
		this.value = value;
	}
}
