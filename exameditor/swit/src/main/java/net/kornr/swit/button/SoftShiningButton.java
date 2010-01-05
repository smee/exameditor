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
package net.kornr.swit.button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import net.kornr.swit.button.devutil.ButtonFrame;
import net.kornr.swit.button.effect.FactorScale;
import net.kornr.swit.button.effect.Rotate;
import net.kornr.swit.button.effect.RoundBorder;
import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * 
 *
 */

public class SoftShiningButton extends AbstractRoundButton 
{
	private Color m_shineColor, m_softColor;

	/**
	 * Constructor, using a default color.
	 */
	public SoftShiningButton()
	{
		setBaseColor(new Color(0xDFDF77));
	}
	
	/**
	 * Constructs a SoftShiningButton with the base color specified.
	 * @param baseColor a color used for the background
	 */
	public SoftShiningButton(Color baseColor)
	{
		setBaseColor(baseColor);
	}
	
	public void setBaseColor(Color baseColor)
	{
		m_shineColor = GfxEffects.adjustBrightness(baseColor, 1.3f);
		m_softColor = baseColor;		
	}
	
	@Override
	protected BufferedImage drawBackground(BufferedImage img, String text) 
	{
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Color u1 = GfxEffects.adjustBrightness(m_shineColor, 0.9f);
		Color u2 = GfxEffects.adjustBrightness(m_softColor, 0.50f);
		LinearGradientPaint upgradient = new LinearGradientPaint(0,0, 0, img.getHeight(), new float[]{0f,0.20f, 0.40f, 1f}, new Color[]{u1, m_shineColor, m_softColor, u2});

		g.setPaint(upgradient);
		g.fill(new Rectangle2D.Float(0,0, img.getWidth(), img.getHeight()));

		return img;
	}

	public static void main(String[]args) throws IOException
	{
		ButtonTemplate button = new SoftShiningButton(new Color(0xFFFF00));
		button.setWidth(1);
		button.setHeight(24);
		button.setTextAlign(ButtonTemplate.TEXT_ALIGN_LEFT);
		button.setFont(new Font("Verdana", Font.BOLD, 12));
		button.addEffect(new Rotate(3d));
		// button.addEffect(new FactorScale(2d,5d));
		ButtonFrame f = new ButtonFrame(button);
		f.display();
	}
}
