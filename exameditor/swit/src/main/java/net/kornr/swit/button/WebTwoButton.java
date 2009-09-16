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
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import net.kornr.swit.button.devutil.ButtonFrame;
import net.kornr.swit.button.effect.AutoClip;
import net.kornr.swit.button.effect.ShadowBorder;
import net.kornr.swit.button.effect.VerticalMirror;
import net.kornr.swit.wicket.border.graphics.GenericShadowBorder;
import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * A button that mimics the famous Web2.0 button. Don't forget to add a mirror effect to it, for a revival of the good old times, 
 * when the Web2.0 was the buzz of the year (still trying here to figure out what it's about really).
 *
 */
public class WebTwoButton extends AbstractRoundButton 
{
	static private Color s_defColor = new Color(0x9855AA);
	private Color m_baseColor = s_defColor;
	
	public WebTwoButton()
	{
		
	}

	public WebTwoButton(Color baseColor)
	{
		m_baseColor = baseColor;
	}
	
	public void setBaseColor(Color baseColor)
	{
		m_baseColor = baseColor;
	}
	
	@Override
	protected BufferedImage drawBackground(BufferedImage img, String text) 
	{
		Graphics2D g = this.initializeGraphics2D(img);

		// Draw the background gradient, using the base color
		Color u1 = GfxEffects.adjustBrightness(m_baseColor, 0.75f);
		Color u2 = GfxEffects.adjustBrightness(m_baseColor, 1.80f);
		LinearGradientPaint upgradient = new LinearGradientPaint(0,0,0, img.getHeight(), new float[]{0f,.5f,1f}, new Color[]{u1, m_baseColor, u2});

		g.setPaint(upgradient);
		g.fill(new Rectangle2D.Float(0,0, img.getWidth(), img.getHeight()));
		
		float upShinePadding = this.getLineWidth();
		float upShineSize = (float)this.getRoundSize();
		RoundRectangle2D.Float shineShape = new RoundRectangle2D.Float(upShinePadding, upShinePadding, (float)img.getWidth()-(upShinePadding*2f), upShineSize, upShineSize-upShinePadding,upShineSize-upShinePadding);
		Color u3 = GfxEffects.adjustBrightness(m_baseColor, 3.0f);
		Color u4 = GfxEffects.adjustBrightness(m_baseColor, 2.00f);
		LinearGradientPaint shineGradient = new LinearGradientPaint(0,0,0, upShineSize, new float[]{0f,1f}, new Color[]{new Color(255,255,255,255), new Color(0,0,0,0)});
		BufferedImage shineMask = GfxEffects.createSimilar(img);
		Graphics2D gmask = initializeGraphics2D(shineMask);
		
		Color shiny = new Color(0,00,00,0);
		Color shiny2 = new Color(127,127,127,255);
		LinearGradientPaint shineGradientBottom = new LinearGradientPaint(0,img.getHeight()*0.75f,0, img.getHeight()*1.75f, new float[]{0f,1f}, new Color[]{shiny, shiny2});
		RoundRectangle2D.Float shineShapeBottom = new RoundRectangle2D.Float(upShinePadding, (float)img.getHeight()-upShinePadding-upShineSize, (float)img.getWidth()-(upShinePadding*2f), upShineSize, upShineSize-upShinePadding,upShineSize-upShinePadding);
		
		gmask.setPaint(shineGradient);
		gmask.fill(shineShape);
		gmask.setPaint(shineGradientBottom);
		gmask.fill(shineShapeBottom);

		GfxEffects.mixOn(shineMask,img,0.8f);
		
		return img;
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
		button.addEffect(new ShadowBorder(20,0,0,Color.black));
		button.addEffect(new AutoClip());

		button.setTextAlign(ButtonTemplate.TEXT_ALIGN_LEFT);
		button.setFont(new Font("Verdana", Font.BOLD, 24));
		ButtonFrame f = new ButtonFrame(button);
		f.display();
	}

}
