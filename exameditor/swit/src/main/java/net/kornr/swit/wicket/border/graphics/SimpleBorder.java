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
package net.kornr.swit.wicket.border.graphics;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

public class SimpleBorder extends BorderMaker 
{
	private float m_lineWidth = 1; 
	private Color m_lineColor = Color.black;
	private Color m_innerColor = Color.yellow;
	
	
	synchronized static public Long register(float lineWidth, Color lineColor, Color innerColor)
	{
		SimpleBorder rbi = new SimpleBorder(lineWidth, lineColor, innerColor);
		return BorderMaker.register(rbi);		
	}
	
	public SimpleBorder(float lineWidth, Color lineColor, Color innerColor)
	{
		super(3+(int)(lineWidth*2),3+(int)(lineWidth*2));
		m_lineWidth = lineWidth;
		m_lineColor = lineColor;
		m_innerColor = innerColor;
	}

	@Override
	public BufferedImage createImage(Rectangle part) 
	{
		if (part.width<=0)
			part.width = 1;
		if (part.height <=0)
			part.height = 1;
		
		int offsetx = part.x;
		int offsety = part.y;

		int lineSizeMargin =  (int)m_lineWidth/2;
		int roundedLineSize = (int)m_lineWidth%2;

		// ADD 1 pixel for the anti alias, or it appears truncated
		BufferedImage image = new BufferedImage(part.width+1, part.height+1, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g.translate(-(offsetx), -(offsety));
		g.setColor(Color.white);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 0.5f));
		g.fillRect(0,0,image.getWidth(), image.getHeight());

		Rectangle2D rect = new Rectangle2D.Double(lineSizeMargin, lineSizeMargin, this.getWidth()-m_lineWidth, this.getHeight()-m_lineWidth);
		g.setStroke(new BasicStroke(m_lineWidth));
		g.setColor(m_innerColor);
		g.setComposite(AlphaComposite.SrcOver);
		g.fill(rect);
		g.setColor(m_lineColor);
		g.draw(rect);

		// drawGlow(image, rect);
		
		return image;
	}

	private void drawGlow3(BufferedImage img, Shape s)
	{
		Graphics2D g2 = img.createGraphics();

		for (int i=(int)m_lineWidth; i >= 1; i-=1) 
		{
		        float pct = (float)i/m_lineWidth; 

		        Color col = GfxEffects.getColorTransition(m_lineColor, m_innerColor, pct);
		        g2.setColor(col);
		        // See my "Java 2D Trickery: Soft Clipping" entry for more
		        // on why we use SRC_ATOP here
		      //  g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, pct));
		        //g2.setStroke(new BasicStroke(i));
		        g2.draw(s);
		    }
	}

	private void drawGlow(BufferedImage img, Shape s)
	{
		int glowWidth = (int)10;
		int gw = glowWidth*2;

		Color clrGlowInnerHi = m_innerColor;
		Color clrGlowInnerLo = GfxEffects.adjustBrightness(m_innerColor, 0.8f);
		Color clrGlowOuterHi = m_lineColor;
		Color clrGlowOuterLo = GfxEffects.adjustBrightness(m_lineColor, 0.8f);
		
		Graphics2D g2 = img.createGraphics();
		
		for (int i=gw; i >= 2; i-=2) {
		        float pct = (float)(gw - i) / (gw - 1);

		        Color mixHi = GfxEffects.getColorTransition(clrGlowInnerHi, clrGlowOuterHi, 1.0f - pct);
		        Color mixLo = GfxEffects.getColorTransition(clrGlowInnerLo, clrGlowOuterLo, 1.0f - pct);
		        //Color col = GfxEffects.getColorTransition(m_lineColor, m_innerColor, pct);
		        GradientPaint grad = new GradientPaint(0.0f, this.getHeight()*0.25f,  mixHi, 0.0f, this.getHeight(), mixLo);
		        g2.setPaint(grad);
//		        g2.setColor(col);
		        // See my "Java 2D Trickery: Soft Clipping" entry for more
		        // on why we use SRC_ATOP here
		      //  g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, pct));
		        g2.setStroke(new BasicStroke(i));
		        g2.draw(s);
		    }
	}

	@Override
	public BufferedImage createIndexedImage(Rectangle part) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ImageMap getImageMap() 
	{
		int[] horizontal = new int[] { 0, (int)m_lineWidth+1, this.getWidth()-((int)m_lineWidth+1), this.getWidth() };
		int[] vertical = new int[] { 0, (int)m_lineWidth+1, this.getWidth()-((int)m_lineWidth+1), this.getWidth() };
		
		return new ImageMap(horizontal, vertical);		
	}

}
