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
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Creates a rectangular border around the image, using provided width and colors.
 * 
 * @author Rodrigo Reyes
 *
 */
public class FrameBorder extends Effect 
{
	private float m_width;
	private Paint m_color;
	
	/**
	 * Constructor
	 * @param width the size of the space around the image
	 * @param color the color or gradient of the space
	 */
	public FrameBorder(float width, Paint color)
	{
		m_width = width;
		m_color = color;
	}
	
	@Override
	public BufferedImage apply(BufferedImage org) 
	{
		int lineWidth = (int) m_width;
		double dw = (double)m_width;
		if (dw - Math.ceil(dw)>0.1d)
			lineWidth++;

		
		BufferedImage img = new BufferedImage(org.getWidth() + (lineWidth*2), org.getHeight() + (lineWidth*2), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = img.createGraphics();
		g.drawImage(org, lineWidth, lineWidth, null);
		g.setPaint(m_color);
		g.setStroke(new BasicStroke(m_width));
		g.draw(new Rectangle2D.Float(m_width/2, m_width/2, img.getWidth()-m_width, img.getHeight()-m_width));
		
		return img;
	}

}
