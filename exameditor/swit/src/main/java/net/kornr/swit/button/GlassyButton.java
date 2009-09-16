package net.kornr.swit.button;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import net.kornr.swit.button.ButtonTemplate.TextElement;
import net.kornr.swit.button.devutil.ButtonFrame;
import net.kornr.swit.button.effect.AutoClip;
import net.kornr.swit.button.effect.GenericRoundShape;
import net.kornr.swit.button.effect.VerticalMirror;
import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * Glassy button, as described in http://jozmak.blogspot.com/2007/07/gimp-tutorial-creating-glassy-buttons.html
 * 
 * @author Rodrigo Reyes
 *
 */
public class GlassyButton extends AbstractRoundButton 
{
	private Color m_baseColor = null;
	private Color m_brightColor = null;
	private Color m_topColor = null;
	private Color m_bottomColor = null;
	
	public GlassyButton()
	{
		setBaseColor(new Color(0x777777));
		this.setFontColor(Color.white);
	}
	
	@Override
	protected BufferedImage drawBackground(BufferedImage img, String text) 
	{
		Graphics2D g = this.initializeGraphics2D(img);
		float width = img.getWidth();
		float height = img.getHeight();
		
		LinearGradientPaint gradient1 = new LinearGradientPaint(width*0.49f,0, width*0.51f, height*1f, new float[] {0f,.2f,.7f,1f}, new Color[] {m_brightColor, m_topColor, m_baseColor, m_bottomColor});
		g.setPaint(gradient1);
		g.fillRect(0, 0, img.getWidth(), img.getHeight());
		
		LinearGradientPaint gradient2 = new LinearGradientPaint(width*0.49f,height*0.2f, width*0.51f, height*1f, new float[] {0f,.2f,.5f,1f}, new Color[] {m_bottomColor, m_baseColor,m_topColor,m_brightColor});
		
		GeneralPath path = new GeneralPath();
		float wstart = -width*0.2f;
		path.moveTo(wstart, height*0.9);
		path.quadTo(width/2, height*-.1f, width*1.33, height*0.6f);
		path.lineTo(width, height);
		path.lineTo(wstart, height);
		g.setPaint(gradient2);
		g.fill(path);

		
		img = GfxEffects.blur(img, null, 3);

		return img;
	}

	
	@Override
	protected Rectangle2D drawText(BufferedImage image, Rectangle2D target, List<TextElement> elements, Rectangle2D textBounds, float ascent) 
	{
		BufferedImage tempbuf = GfxEffects.createSimilar(image);
		Rectangle2D result = super.drawText(tempbuf, target, elements, textBounds, ascent);
		
		AutoClip clipper = new AutoClip(false, true);
		tempbuf = clipper.apply(tempbuf);
		VerticalMirror mirror = new VerticalMirror(0.75f, 0.4f);
		
		tempbuf = mirror.apply(tempbuf);
		Graphics2D g = GfxEffects.createInitializedGraphics2DQuality(image);

		g.drawImage(tempbuf, 0, (int) (image.getHeight()/2 - tempbuf.getHeight()/2 ), null);
		
		return result;
	}
	
	@Override
	protected TextPadding getTextPadding(int width, int height,	List<TextElement> elements) 
	{
		TextPadding padding = super.getTextPadding(width, height, elements);
		
		// Adds the space necessary for the mirror effect
		padding.setBottom((int)(padding.getBottom() + height*0.5f));
		return padding;
	}
	
	public Color getBaseColor() 
	{
		return m_baseColor;
	}

	public void setBaseColor(Color baseColor) 
	{
		m_baseColor = baseColor;
		m_brightColor = GfxEffects.adjustBrightness(m_baseColor, 1.9f);
		m_topColor = GfxEffects.adjustBrightness(m_baseColor, 1.6f);
		m_bottomColor = GfxEffects.adjustBrightness(m_baseColor, 0.4f);
	}

	public static void main(String[]args) throws IOException
	{
		GlassyButton button = new GlassyButton(); // new Color(0x6655AA));
		button.setWidth(1);
		button.setFontColor(Color.white);
		button.setTextAlign(ButtonTemplate.TEXT_ALIGN_LEFT);
		button.setFont(new Font("Verdana", Font.BOLD, 24));
		button.setRoundSize(50f);

		button.setWidth(200);
		button.setFont(new Font("Verdana", Font.BOLD, 24));
		button.setFontColor(Color.white);

		// button.addEffect(new VerticalMirror());
		ButtonFrame f = new ButtonFrame(button);
		f.display();
	}

	
}
