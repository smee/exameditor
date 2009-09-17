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
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

public class RoundedBorderMaker extends AbstractStrokeBorder 
{
	private int m_roundSize = 16;

	synchronized static public Long register(int roundSize, float lineWidth, Color lineColor, Paint innerColor)
	{
		RoundedBorderMaker rbi = new RoundedBorderMaker(roundSize, roundSize*3+(int)lineWidth+8, roundSize*3+(int)lineWidth+8, lineWidth, lineColor, innerColor);
		return BorderMaker.register(rbi);		
	}
	
	public RoundedBorderMaker(int roundSize, int width, int height, float lineWidth, Color lineColor, Paint innerColor)
	{
		super(width,height,lineWidth,lineColor,innerColor);
		m_roundSize = roundSize;
	}
	
	@Override
	public ImageMap getImageMap()
	{
		int[] horizontal = new int[] { 0, m_roundSize+(int)getLineWidth(), this.getWidth()-(m_roundSize+(int)getLineWidth()), this.getWidth() };
		int[] vertical = new int[] { 0, m_roundSize+(int)getLineWidth(), this.getHeight()-(m_roundSize+(int)getLineWidth()), this.getHeight() }; 
		return new ImageMap(horizontal, vertical);
	}

	@Override
	public void drawBorder(Graphics2D g)
	{
		int lineSizeMargin =  (int)this.getLineWidth()/2;
		int roundedLineSize = (int)this.getLineWidth()%2;

		int round = m_roundSize*2;
		RoundRectangle2D shape = new RoundRectangle2D.Double(lineSizeMargin, lineSizeMargin, this.getWidth()-this.getLineWidth()-1, this.getHeight()-this.getLineWidth()-1, round, round);

		g.setStroke(new BasicStroke(this.getLineWidth()));
		g.setPaint(this.getInnerColor());
		g.setComposite(AlphaComposite.SrcOver);
		g.fill(shape);

		g.setColor(this.getLineColor());
		g.draw(shape);
	}

	public int getRoundSize() {
		return m_roundSize;
	}

	public void setRoundSize(int roundSize) {
		m_roundSize = roundSize;
	}


	
}
