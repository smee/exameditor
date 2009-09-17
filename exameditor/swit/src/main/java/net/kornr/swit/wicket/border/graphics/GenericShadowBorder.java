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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

import net.kornr.swit.button.effect.ShadowBorder;


public class GenericShadowBorder extends MarginBorder 
{
	private int m_xOffset, m_yOffset;
	private float m_blurSize;
	private Color m_shadowColor;
	
	static public Long register(Long parentId, int xoffset, int yoffset, float blurSize, Color background)
	{
		GenericShadowBorder sb = new GenericShadowBorder(parentId, xoffset, yoffset, blurSize, background, Color.black);
		return BorderMaker.register(sb);
	}

	static public Long register(Long parentId, int xoffset, int yoffset, float blurSize, Color background, Color shadow)
	{
		GenericShadowBorder sb = new GenericShadowBorder(parentId, xoffset, yoffset, blurSize, background, shadow);
		return BorderMaker.register(sb);
	}
	
	protected GenericShadowBorder(Long parentId, int xoffset, int yoffset, float blurSize, Color background, Color shadow)
	{
		super(parentId, (yoffset<0?Math.abs(yoffset):0)+(int)blurSize, (yoffset>=0?yoffset:0)+(int)blurSize, (xoffset<0?Math.abs(xoffset):0)+(int)blurSize, (xoffset>=0?xoffset:0)+(int)blurSize, background);
		
		m_xOffset = xoffset;
		m_yOffset = yoffset;
		m_blurSize = blurSize;
		if (m_blurSize <= 0)
			m_blurSize = 1;
		m_shadowColor = shadow;
		
		BorderMaker bm = BorderMaker.get(parentId);
		
		bm.setWidth(bm.getWidth()+Math.abs(xoffset));
		bm.setHeight(bm.getHeight()+Math.abs(yoffset));
		this.setWidth(bm.getWidth()+Math.abs(xoffset));
		this.setHeight(bm.getHeight()+Math.abs(yoffset));
	}
	
	@Override
	public ImageMap getImageMap()
	{
		ImageMap parmap = getParent().getImageMap();
		int[] horizontal = new int[] { 0, Math.abs(m_xOffset)+(int)(m_blurSize)+(int)(m_blurSize/4), (int)(m_blurSize*0.75f), Math.abs(m_xOffset)+(int)(m_blurSize*2) } ;
		int[] vertical= new int[] { 0, Math.abs(m_yOffset)+(int)(m_blurSize)+(int)(m_blurSize/4), (int)(m_blurSize*.75f), Math.abs(m_yOffset)+(int)(m_blurSize*2) };
		return parmap.add(horizontal, vertical);
	}


	protected void drawBackground(BufferedImage original, BufferedImage target)
	{
		super.drawBackground(original, target);
		
		BufferedImage img = new BufferedImage(target.getWidth(), target.getHeight(), BufferedImage.TYPE_INT_ARGB);
		img.createGraphics().drawImage(original, this.getMarginLeft(), this.getMarginTop(), new Color(0,0,0,0), null);
		img = GfxEffects.getAlphaMask(img, 1.0f, m_shadowColor);
		
		BufferedImage blurred = GfxEffects.blurFixed(img, (int)(m_blurSize*1.75f));
		target.createGraphics().drawImage(blurred, m_xOffset, m_yOffset, null);
	}
}
