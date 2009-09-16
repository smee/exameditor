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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

/**
 * Creates rounded borders around an image. 
 * @author Rodrigo Reyes
 *
 */
public class RoundBorder extends Effect 
{
	private float m_roundSize = 10;
	private float m_lineWidth = 1.0f;
	private boolean m_clipContent = true;
	private Paint m_color = Color.red;

	/**
	 * Creates an object that adds rounded corner. 
	 * @param lineWidth the width of the line to draw around the image
	 * @param roundSize the rounding size
	 * @param clipContent true (the default) to clip the image to the rounded shape. If false, the space left beyond the rounded corners are not emptied.  
	 * @param color the color or gradient to draw the line with.
	 */
	public RoundBorder(float lineWidth, float roundSize, boolean clipContent, Paint color)
	{
		m_roundSize = roundSize;
		m_lineWidth = lineWidth;
		m_clipContent = clipContent;
		m_color = color;
	}
	
	@Override
	public BufferedImage apply(BufferedImage org) 
	{
		int lineWidth = (int) m_lineWidth;
		double dw = (double)m_lineWidth;
		if (dw - Math.ceil(dw)>0.1d)
			lineWidth++;
		
		// BufferedImage img = this.newBuffer(org.getWidth() + (lineWidth*2), org.getHeight() + (lineWidth*2));
		BufferedImage img = this.newBuffer(org.getWidth(), org.getHeight());
		Graphics2D g = this.createInitializedGraphics2D(img);
		RoundRectangle2D clipper = new RoundRectangle2D.Double(m_lineWidth/2, m_lineWidth/2, img.getWidth()-m_lineWidth, img.getHeight()-m_lineWidth, m_roundSize, m_roundSize);
		RoundRectangle2D shape = new RoundRectangle2D.Double(m_lineWidth/2, m_lineWidth/2, img.getWidth()-m_lineWidth, img.getHeight()-m_lineWidth, m_roundSize, m_roundSize);

		this.applyEffectClipping(g);
		g.setPaint(m_color);
		g.fill(shape);
		this.cancelEffectClipping(g);
		
		if (m_clipContent)
			g.setClip(shape);
		// g.drawImage(org, lineWidth, lineWidth, null);
		g.drawImage(org, 0, 0, null);
		
		g.setPaint(m_color);
		g.setStroke(new BasicStroke(m_lineWidth));
		
		this.applyEffectClipping(g);
		g.draw(shape);
		this.cancelEffectClipping(g);

		return img;
	}

}
