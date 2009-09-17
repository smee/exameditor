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
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

public class MarginBorder extends BorderMaker {

	private int m_marginTop, m_marginBottom, m_marginLeft, m_marginRight;
	private BorderMaker m_parent;
	private Color m_background = null;
	
	static public Long register(Long parentId, int top, int bottom, int left, int right, Color background)
	{
		MarginBorder sb = new MarginBorder(parentId, top, bottom, left, right, background);
		return BorderMaker.register(sb);
	}
	
	protected MarginBorder(Long parentId, int top, int bottom, int left, int right, Color background)
	{
		super(left+right,top+bottom);
		m_marginTop = top;
		m_marginBottom = bottom;
		m_marginLeft = left;
		m_marginRight = right;
		m_background = background;
		m_parent = BorderMaker.get(parentId);
	}
	
	@Override
	public BufferedImage createImage(Rectangle part) 
	{		
		Rectangle fullorg = m_parent.getImageMap().getZone("full");
		BufferedImage fullimg = m_parent.createImage(fullorg);
		BufferedImage fullresult = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);

		drawBackground(fullimg, fullresult);
		fullresult.createGraphics().drawImage(fullimg, m_marginLeft, m_marginTop, new Color(0,0,0,0), null);
		drawForeground(fullimg, fullresult);
		
		if (part.width<=0)
			part.width=1;
		if (part.height<=0)
			part.height=1;
		
		BufferedImage result = new BufferedImage(part.width, part.height, BufferedImage.TYPE_INT_ARGB);
		result.createGraphics().drawImage(fullresult, -part.x, -part.y, new Color(0,0,0,0), null);

		return result;
	}

	protected void drawBackground(BufferedImage original, BufferedImage target)
	{
		if (m_background != null)
		{
			Graphics2D fg = target.createGraphics();
			fg.setColor(m_background);
			fg.fillRect(0, 0, target.getWidth(), target.getHeight());
		}
	}

	protected void drawForeground(BufferedImage original, BufferedImage target)
	{
	}
	
	@Override
	public BufferedImage createIndexedImage(Rectangle part) 
	{
		return null;
	}

	@Override
	public ImageMap getImageMap()
	{
		ImageMap parmap = m_parent.getImageMap();

		int[] horizontal = new int[] { 0, m_marginLeft, m_marginLeft, m_marginLeft+m_marginRight };
		int[] vertical = new int[] { 0, m_marginTop, m_marginTop, m_marginTop+m_marginBottom };
		
		return parmap.add(horizontal, vertical);
	}


	@Override
	public int getHeight() {
		return m_parent.getHeight() + m_marginTop + m_marginBottom;
	}

	@Override
	public int getWidth() {
		return m_parent.getWidth() + m_marginLeft + m_marginRight;
	}

	public int getMarginTop() {
		return m_marginTop;
	}

	public void setMarginTop(int marginTop) {
		m_marginTop = marginTop;
	}

	public int getMarginBottom() {
		return m_marginBottom;
	}

	public void setMarginBottom(int marginBottom) {
		m_marginBottom = marginBottom;
	}

	public int getMarginLeft() {
		return m_marginLeft;
	}

	public void setMarginLeft(int marginLeft) {
		m_marginLeft = marginLeft;
	}

	public int getMarginRight() {
		return m_marginRight;
	}

	public void setMarginRight(int marginRight) {
		m_marginRight = marginRight;
	}

	public BorderMaker getParent() {
		return m_parent;
	}

	public void setParent(BorderMaker parent) {
		m_parent = parent;
	}

	public Color getBackground() {
		return m_background;
	}

	public void setBackground(Color background) {
		m_background = background;
	}
	
}
