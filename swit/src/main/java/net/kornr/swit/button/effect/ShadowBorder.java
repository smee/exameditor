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

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.io.IOException;

import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.WebTwoButton;
import net.kornr.swit.button.devutil.ButtonFrame;
import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * Adds a shadow to an image.
 * 
 * @author Rodrigo Reyes
 *
 */
public class ShadowBorder extends Effect 
{
	private float m_blurSize = 8;
	private int m_xOffset = 0, m_yOffset = 0;
	private Color m_shadowColor = Color.black;
	
	/**
	 * Adds a shadow or glow effect to an image. For a glow effect, just let xoffet and yoffset to 0, or specify non-0 values for a drop shadow effect.
	 * @param size Size of the shadow.
	 * @param xoffset horizontal offset of the shadow, can be negative
	 * @param yoffset vertical offset of the shadow, can be negative
	 * @param shadowColor the color of the shadow (black is generally the best option, but it's possible to create glowing effects by using some other colors).
	 */
	public ShadowBorder(float size, int xoffset, int yoffset, Color shadowColor)
	{
		m_blurSize = size*2;
		m_xOffset = xoffset;
		m_yOffset = yoffset;
		m_shadowColor = shadowColor;
	}
	
	@Override
	public BufferedImage apply(BufferedImage org)
	{
		int newwidth = org.getWidth() + (int)(m_blurSize) + (int)Math.abs(m_xOffset);
		int newheight = org.getHeight() + (int)(m_blurSize) + (int)Math.abs(m_yOffset);

		int midshadow = (int)(m_blurSize/2);
		int xoffsetshadow = m_xOffset;
		int yoffsetshadow = m_yOffset;
		int xoffsetimg = 0;
		int yoffsetimg = 0;
		
		if (xoffsetshadow < 0)
		{
			xoffsetimg -= xoffsetshadow;
			xoffsetshadow = 0;
		}
		if (yoffsetshadow < 0)
		{
			yoffsetimg -= yoffsetshadow;
			yoffsetshadow = 0;
		}
		
		if (xoffsetshadow < midshadow)
		{
			int diff = midshadow - xoffsetshadow;
			xoffsetshadow += diff;
			xoffsetimg += diff;
		}

		if (yoffsetshadow < midshadow)
		{
			int diff = midshadow - yoffsetshadow;
			yoffsetshadow += diff;
			yoffsetimg += diff;
		}
		
		BufferedImage img = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_ARGB);
		
		img.createGraphics().drawImage(org, midshadow, midshadow, new Color(0,0,0,0), null);
		img = GfxEffects.getAlphaMask(img, 1.0f, m_shadowColor);
		BufferedImage blurred = GfxEffects.blurFixed(img, (int)m_blurSize);

		img.createGraphics().drawImage(blurred, xoffsetshadow-midshadow, yoffsetshadow-midshadow, null);

		img.createGraphics().drawImage(org, xoffsetimg, yoffsetimg, new Color(0,0,0,0), null);
		
		return img;
	}

	public BufferedImage blur(BufferedImage org, BufferedImage target, int size)
	{
		int size2 = size*size;
		float[] matrix = new float[size2];
		for (int i = 0; i < (size2); i++)
			matrix[i] = (1.0f/(float)size2);
		
		ConvolveOp op = new ConvolveOp(new Kernel(size,size,matrix), ConvolveOp.EDGE_ZERO_FILL, null);

		return op.filter(org, target);
	}

	public static void main(String[]args) throws IOException
	{
		// WebTwoButton button = new WebTwoButton(new Color(0x4444DD));
		WebTwoButton button = new WebTwoButton(new Color(0x9855AA));
		button.setRoundSize(30);
		button.setLineWidth(3);
		button.setWidth(1);
		button.setHeight(60);
		button.setShadowDisplayed(true);
		
		//button.addEffect(new VerticalMirror());
		button.addEffect(new ShadowBorder(5,1,1, new Color(0x555555)));
		
		button.setTextAlign(ButtonTemplate.TEXT_ALIGN_LEFT);
		button.setFont(new Font("Verdana", Font.BOLD, 24));
		ButtonFrame f = new ButtonFrame(button);
		f.display();
	}

}
