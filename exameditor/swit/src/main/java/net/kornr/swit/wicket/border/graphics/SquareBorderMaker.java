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
import java.awt.geom.Rectangle2D;

public class SquareBorderMaker extends AbstractStrokeBorder 
{

	synchronized static public Long register(float lineWidth, Color lineColor, Paint innerColor)
	{
		SquareBorderMaker sq = new SquareBorderMaker(3+(int)lineWidth+8, 3+(int)lineWidth+8, lineWidth, lineColor, innerColor);
		return BorderMaker.register(sq);		
	}

	protected SquareBorderMaker(int width, int height, float lineWidth, Color lineColor, Paint innerColor) {
		super(width, height, lineWidth, lineColor, innerColor);
	}

	@Override
	public void drawBorder(Graphics2D g)
	{
		int lineSizeMargin =  (int)this.getLineWidth()/2;

		Rectangle2D shape = new Rectangle2D.Double(lineSizeMargin, lineSizeMargin, this.getWidth()-this.getLineWidth()-1, this.getHeight()-this.getLineWidth()-1);

		g.setStroke(new BasicStroke(this.getLineWidth()));
		g.setPaint(this.getInnerColor());
		g.setComposite(AlphaComposite.SrcOver);
		g.fill(shape);

		g.setColor(this.getLineColor());
		g.draw(shape);
	}

}
