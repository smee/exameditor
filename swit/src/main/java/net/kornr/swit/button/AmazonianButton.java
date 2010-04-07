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
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import net.kornr.swit.button.ButtonTemplate.TextElement;
import net.kornr.swit.button.devutil.ButtonFrame;
import net.kornr.swit.button.effect.GenericRoundShape;
import net.kornr.swit.button.effect.Margin;
import net.kornr.swit.button.effect.RoundBorder;
import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * A Button with soft corners that embeds an icon either on the left or on the right. 
 * This button design is based on a two colors gradient and an icon. 
 */
public class AmazonianButton extends ButtonTemplate 
{
	static BufferedImage s_defaultIcon = null;
	static {
		try {
			InputStream iconstream = VistafarianButton.class.getClassLoader().getResourceAsStream("net/kornr/swit/button/right-arrow-symbol.png");
			s_defaultIcon = ImageIO.read(iconstream);
		} catch (Exception exc)
		{
			exc.printStackTrace();
		}
	}
	
	private Color m_innerColor = new Color(0xfce000); 
	private Color m_outerColor = new Color(0xfc8900); 
	private BufferedImage m_icon = null;
	private float m_iconPadding = 4;

	private boolean m_rightHanded = true;

	/*
	 * Constructor with a default color and a default icon.
	 */
	public AmazonianButton()
	{
		this(null);
		setIcon(s_defaultIcon);
	}

	/**
	 * Creates an amazonian button with a specified icon, or with no icon at all if null.
	 * 
	 * @param icon an image used as icon, or null to have no icon drawn
	 */
	public AmazonianButton(BufferedImage icon)
	{
		this.setFontColor(new Color(0x333333));
		this.setShadowDisplayed(false);
		this.setAutoExtend(true);
		this.setSmallCapsRatio(0.8f);
		m_icon = icon;
		m_rightHanded = false;

	}

	/**
	 * The icon to draw on the left or right of the button. 
	 * @param img an image, or null
	 */
	public void setIcon(BufferedImage img)
	{
		m_icon = img;
	}

	/**
	 * The default space around the icon.
	 * @param padding
	 */
	public void setIconPadding(float padding)
	{
		m_iconPadding = padding;
	}

	public Color getInnerColor() {
		return m_innerColor;
	}

	/**
	 * Specifies the first color of the gradient used to draw the background.
	 * 
	 * @param innerColor a color
	 */
	public void setInnerColor(Color innerColor) {
		m_innerColor = innerColor;
	}

	public Color getOuterColor() {
		return m_outerColor;
	}

	/**
	 * Specified the second color of the gradient of this button.
	 * @param outerColor
	 */
	public void setOuterColor(Color outerColor) {
		m_outerColor = outerColor;
	}

	public boolean isRightHanded() {
		return m_rightHanded;
	}

	/**
	 * The default is to have the icon on the left. Set this to true to have it drawn
	 * @param rightHanded
	 */
	public void setRightHanded(boolean rightHanded) {
		m_rightHanded = rightHanded;
	}

	public BufferedImage getIcon() {
		return m_icon;
	}

	public float getIconPadding() {
		return m_iconPadding;
	}


	@Override
	protected BufferedImage drawBackground(BufferedImage img, String text) 
	{
		Graphics2D g = this.initializeGraphics2D(img);

		Color softinner = GfxEffects.adjustBrightness(m_innerColor, 0.9f);
		float mid = img.getHeight()/2;
		GenericRoundShape aShape = null;
		if (m_rightHanded)
			aShape = new GenericRoundShape(mid/2, mid, mid/2, mid);
		else
			aShape = new GenericRoundShape(mid, mid/2, mid, mid/2);

		LinearGradientPaint upgradient = new LinearGradientPaint(0,0, 0, img.getHeight(), new float[]{0f, 0.10f, 0.25f, 0.4f, 1f}, new Color[]{m_outerColor, softinner, m_innerColor, softinner, m_outerColor});
		g.setPaint(upgradient);

		Shape s = aShape.createShape(img.getWidth()-1, img.getHeight()-1);
		g.fill(s);

		Color border = GfxEffects.adjustBrightness(m_outerColor, 0.25f);

		g.translate(0.5, 0.5);
		g.setPaint(border);
		g.setStroke(new BasicStroke(1f));
		g.draw(s);

		g.translate(-0.5, -0.5);
		if (m_icon != null)
		{
			int height = (int)(img.getHeight() - m_iconPadding*2);
			int width = height;
			
			BufferedImage icon = m_icon;
			if (width < m_icon.getWidth() && height < m_icon.getHeight())
			{
				GfxEffects.blur(icon, null, 12);
			}
			
			if (m_rightHanded)
				g.drawImage(icon, img.getWidth() - (int)m_iconPadding - width, (int)m_iconPadding, width, height, null);
			else
				g.drawImage(icon, (int)m_iconPadding, (int)m_iconPadding, width, height, null);
		}

		return img;
	}

	@Override
	protected TextPadding getTextPadding(int width, int height, List<TextElement> elements)
	{
		float mid = height/4;
		if (m_icon != null)
		{
			int iconwidth = (int)(height);
			if (m_rightHanded)
				return new TextPadding((int)(mid), iconwidth+(int)(m_iconPadding)+(int)mid, (int)m_iconPadding, (int)m_iconPadding);
			else
				return new TextPadding(iconwidth+(int)(m_iconPadding)+(int)mid, (int)(mid), (int)m_iconPadding, (int)m_iconPadding);

		}
		return new TextPadding((int)(mid/2), (int)(mid), 3, 3);
	}

	@Override
	protected BufferedImage drawForeground(BufferedImage img, String text) {
		return null;
	}

	public static void main(String[]args) throws IOException
	{
		InputStream iconstream = VistafarianButton.class.getClassLoader().getResourceAsStream("net/kornr/swit/button/right-arrow-symbol.png");
		// InputStream iconstream = VistafarianButton.class.getClassLoader().getResourceAsStream("net/kornr/swit/button/test1.png");
		BufferedImage icon = ImageIO.read(iconstream);

		AmazonianButton button = new AmazonianButton(); // new Color(0x6655AA));
		button.setRightHanded(true);
		button.setWidth(1);
		button.setIcon(icon);
		button.setTextAlign(ButtonTemplate.TEXT_ALIGN_LEFT);
		button.setFont(new Font("Verdana", Font.BOLD, 32));
		ButtonFrame f = new ButtonFrame(button);
		f.display();
	}
}
