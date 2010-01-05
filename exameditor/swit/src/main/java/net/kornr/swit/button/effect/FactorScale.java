/*
 * Copyright 2009 Rodrigo Reyes reyes.rr at gmail dot com
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.kornr.swit.button.effect;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Scales an image by some given factor. 
 * 
 * @author Rodrigo Reyes
 *
 */
public class FactorScale extends Effect 
{
	private double m_xfactor = 1.0d, m_yfactor = 1.0d; 

	/**
	 * Creates a default FactorScale effect, with a default factor of 1, which does nothing really.
	 */
	public FactorScale()
	{
	}

	/**
	 * Creates an object that scales both width and height by the same factor. For instance, factor 2 double the size of the picture, 
	 * while a 0.5 factor returns an image half its original size. 
	 * @param factor
	 */
	public FactorScale(double factor)
	{
		m_xfactor = m_yfactor = factor;
	}

	/**
	 * Creates an object that scales the width and height with distinct factors.
	 * 
	 * @param xfactor
	 * @param yfactor
	 */
	public FactorScale(double xfactor, double yfactor)
	{
		m_xfactor = xfactor;
		m_yfactor = yfactor;
	}
	
	
	@Override
	public BufferedImage apply(BufferedImage org) 
	{
		double newwidth = org.getWidth() * m_xfactor;
		double newheight = org.getHeight() * m_yfactor;
		newwidth = Math.max(newwidth, 1);
		newheight = Math.max(newheight, 1);

		BufferedImage result = this.newBuffer((int)Math.ceil(newwidth), (int)Math.ceil(newheight));
		Graphics2D g = this.createInitializedGraphics2D(result);
		
		AffineTransform transform = new AffineTransform();
		transform.scale(m_xfactor, m_yfactor);
		
		g.drawRenderedImage(org, transform);
		// TODO Auto-generated method stub
		return result;
	}
	
}
