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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import net.kornr.swit.button.ButtonTemplate.TextElement;
import net.kornr.swit.button.devutil.ButtonFrame;
import net.kornr.swit.button.effect.Effect;
import net.kornr.swit.button.effect.FrameBorder;
import net.kornr.swit.button.effect.Margin;
import net.kornr.swit.button.effect.RoundBorder;
import net.kornr.swit.button.effect.ShadowBorder;
import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * A button that somewhat follows the design guidelines defined by some company from Redmond for an operating system they did recently.
 * The button is not unlike the one described here: http://gimp-tutorials.net/gimp-vista-button 
 */
public class VistafarianButton extends ButtonTemplate 
{
	private Color m_upperLayer;
	private Color m_lowerLayer;
	private float m_roundSize = 8.0f;
	private float m_lightBorderRatio = 1.5f;
	private float m_darkBorderRatio = 0.8f;

	public VistafarianButton(Color topColor, Color bottomColor)
	{
		m_upperLayer = topColor;
		m_lowerLayer = bottomColor;
	}
	
	public VistafarianButton(Color baseColor)
	{
		this(GfxEffects.adjustBrightness(baseColor, 1.2f), GfxEffects.adjustBrightness(baseColor, 0.4f));
	}
	
	public VistafarianButton()
	{
		this(new Color(0x999999));
	}
	
	public void setBaseColor(Color col)
	{
		m_upperLayer = GfxEffects.adjustBrightness(col, 1.2f);
		m_lowerLayer = GfxEffects.adjustBrightness(col, 0.4f);
	}
	
	public void setTopColor(Color c)
	{
		m_upperLayer = c;
	}
	
	public void setBottomColor(Color c)
	{
		m_lowerLayer = c;
	}

	@Override
	protected BufferedImage drawBackground(BufferedImage image, String text)
	{
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		int ymid = image.getHeight()/2;

		Color u1 = GfxEffects.adjustBrightness(m_upperLayer, 1.5f);
		Color u2 = GfxEffects.adjustBrightness(m_upperLayer, 0.75f);
		LinearGradientPaint upgradient = new LinearGradientPaint(0,0, 0, image.getHeight()/2, new float[]{0f,0.75f, 0.9f, 1f}, new Color[]{u1, m_upperLayer, u2, m_lowerLayer});
		//g.setPaint(m_upperLayer);
		g.setPaint(upgradient);
		g.fill(new Rectangle2D.Float(0,0, image.getWidth(), ymid));

		g.setPaint(m_lowerLayer);
		g.fill(new Rectangle2D.Float(0, ymid, image.getWidth(), image.getHeight()));

		// Add a radial gradient to the bottom part of the button
		RadialGradientPaint lowergradient = new RadialGradientPaint((float)(image.getWidth()/2), (float)(image.getHeight()), image.getWidth()/2.0f,new float[]{ 0.0f, 1.0f }, new Color[] { u2, m_lowerLayer});
		g.setPaint(lowergradient);
		g.fill(new Rectangle2D.Float(0, ymid, image.getWidth(), image.getHeight()));
		
		return image;
	}

	@Override
	protected BufferedImage drawForeground(BufferedImage image, String text)
	{
		// The lightning in the upper part of the button
		Color lighter = GfxEffects.adjustBrightness(m_upperLayer, m_lightBorderRatio);
		Effect upshine = new RoundBorder(1.0f, m_roundSize, false, lighter);
		upshine.setEffectClipping(new Rectangle2D.Float(0f,0f, (float)image.getWidth(), image.getHeight()/2.0f));
		image = upshine.apply(image);

		// The lightning in the bottom part of the button
		Color darken = GfxEffects.adjustBrightness(m_lowerLayer, m_lightBorderRatio);
		Effect upshine2 = new RoundBorder(1.0f, m_roundSize, false, darken);
		upshine2.setEffectClipping(new Rectangle2D.Float(0f,image.getHeight()/2.0f, (float)image.getWidth(), image.getHeight()));
		image = upshine2.apply(image);
		image = new Margin(1).apply(image);

		// The dark border around the button
		Color darkbordercolor = GfxEffects.adjustBrightness(m_lowerLayer, m_darkBorderRatio);
		image = new RoundBorder(1,m_roundSize, true, darkbordercolor).apply(image);
		
		return image;
	}
	
	@Override
	protected TextPadding getTextPadding(int width, int height, List<TextElement> elements)
	{
		int std = (int)(m_roundSize/2);
		int min = std<5?5:std;
		int minh = m_roundSize<4?4:1;
		return new TextPadding(min, min, minh, minh);
	}
	
	public static void main(String[]args) throws IOException
	{
		VistafarianButton button = new VistafarianButton(); // new Color(0x6655AA));
		button.setFont(new Font("Courier", Font.BOLD, 14));
		button.setFont(new Font("Verdana", Font.BOLD, 18));
		ButtonFrame f = new ButtonFrame(button);
		f.display();
	}

	public float getRoundSize() {
		return m_roundSize;
	}

	public void setRoundSize(float roundSize) {
		m_roundSize = roundSize;
	}
	
}
