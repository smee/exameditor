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
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;

public class AbstractStrokeBorder extends BorderMaker 
{
	protected Color m_transp = new Color(231,20,189);

	private int m_minimumSize = 32;

	private Color  m_lineColor = new Color(0x27, 0x7d, 0x27); //Color.black;
	private float m_lineWidth = 2.0f;
	private Paint  m_innerColor = new Color(0xcc, 0xe0, 0xcc); // Color.white;

	protected AbstractStrokeBorder(int width, int height, float lineWidth, Color lineColor, Paint innerColor)
	{
		super(width,height);
		this.m_lineWidth = lineWidth;
		this.m_lineColor = lineColor;
		this.m_innerColor = innerColor;		
	}

	@Override
	public ImageMap getImageMap()
	{
//		int[] horizontal = new int[] { 0, (int)m_lineWidth+1, this.getWidth()-((int)m_lineWidth+1), this.getWidth() };
//		int[] vertical = new int[] { 0, (int)m_lineWidth+1, this.getWidth()-((int)m_lineWidth+1), this.getWidth() };
		int[] horizontal = new int[] { 0, (int)m_lineWidth, this.getWidth()-((int)m_lineWidth+1), this.getWidth() };
		int[] vertical = new int[] { 0, (int)m_lineWidth, this.getWidth()-((int)m_lineWidth+1), this.getWidth() };
		
		return new ImageMap(horizontal, vertical);		
	}

	public BufferedImage createImage(Rectangle part)
	{
		int offsetx = part.x;
		int offsety = part.y;

		int lineSizeMargin =  (int)m_lineWidth/2;
		int roundedLineSize = (int)m_lineWidth%2;
		
		// ADD 1 pixel for the anti alias, or it appears truncated
		BufferedImage image = new BufferedImage(part.width, part.height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		g.translate(-(offsetx), -(offsety));

		g.setColor(Color.white); // any color
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1.0f));
		g.fillRect(0,0,image.getWidth(), image.getHeight());
		g.setComposite(AlphaComposite.SrcOver);

		drawBorder(g);

		return image;
	}

	public void drawBorder(Graphics2D g)
	{
	}
		
	public Color getLineColor() {
		return m_lineColor;
	}

	public void setLineColor(Color lineColor) {
		m_lineColor = lineColor;
	}

	public float getLineWidth() {
		return m_lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		m_lineWidth = lineWidth;
	}

	public Paint getInnerColor() {
		return m_innerColor;
	}

	public void setInnerColor(Color innerColor) {
		m_innerColor = innerColor;
	}

	@Override
	public BufferedImage createIndexedImage(Rectangle part) {
		return null;
	}
	
}
