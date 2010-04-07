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

import java.awt.geom.*;
import java.awt.image.*;


import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import net.kornr.swit.wicket.border.graphics.devutil.BorderUtilSwingFrame;

public class GlossyRoundedBorderMaker extends RoundedBorderMaker 
{

	synchronized static public Long register(int roundSize, float lineWidth, Color outerColor, Color innerColor)
	{
		GlossyRoundedBorderMaker rbi = new GlossyRoundedBorderMaker(roundSize, roundSize*3+(int)lineWidth, roundSize*3+(int)lineWidth, lineWidth, outerColor, innerColor);
		return BorderMaker.register(rbi);		
	}

	public GlossyRoundedBorderMaker(int roundSize, int width, int height, float lineWidth, Color outerColor, Color innerColor) 
	{
		super(roundSize, (int)(width+lineWidth*2f), (int)(height+lineWidth*2f), lineWidth, outerColor, innerColor);
		String outstr = Integer.toHexString(outerColor.getRGB());
		String instr = Integer.toHexString(innerColor.getRGB());
	}

	private Shape getShape(double shift, double round)
	{
		RoundRectangle2D shape = new RoundRectangle2D.Double(shift, shift, this.getWidth()-(shift*2), this.getHeight()-(shift*2), round, round);
		return shape;
	}

	@Override
	public void drawBorder(Graphics2D g)
	{
		int round = getRoundSize()*2;
		double size = 1d;
		String innercol = Integer.toHexString(((Color)getInnerColor()).getRGB());
		String outercol = Integer.toHexString(getLineColor().getRGB());

		g.setStroke(new BasicStroke(1));
		double linew = getLineWidth();
		for (double i=0d; i<linew; i+=0.5)
		{
			Shape shape = getShape(i, round);
			Color c = GfxEffects.getColorTransition(getLineColor(), (Color)getInnerColor(), (float)(i/linew));
			g.setPaint(c);
			g.fill(shape);
		}
		
		//		for (double i = 0.00; i <=size; i += 0.1d)
//		{
//			double multiplier = getLineWidth();
//			Shape shape = getShape(multiplier*i, round);
//			g.setStroke(new BasicStroke((float) (0.1)));
//			Color c = GfxEffects.getColorTransition(getLineColor(), (Color)getInnerColor(), (float)i);
//			String strcol = Integer.toHexString(c.getRGB());
//			g.setPaint(c);
//			g.fill(shape);
//		}
	}

	static public void main(String[] args)
	{
		Long border = register(32, 5f, new Color(0x8899CC), new Color(0xffFFFF));
		
		BorderUtilSwingFrame f = new BorderUtilSwingFrame(border);
		f.setSize(600,300);
		f.setVisible(true);
	}
}
