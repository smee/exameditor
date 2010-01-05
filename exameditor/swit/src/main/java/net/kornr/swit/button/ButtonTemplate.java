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
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

import net.kornr.swit.button.effect.Effect;

/**
 * The ButtonTemplate is the basis for all the buttons generator. 
 * <p>
 * Classes extending ButtonTemplate shall implement the following methods:
 *  <ul>
 *  	<li> drawBackground(BufferedImage img, String text): draw the background of the button (or do nothing if there's nothing to draw in the background) </li>
 *  	<li> drawForeground(BufferedImage img, String text): draw the foreground of the button (or do nothing if there's no foreground for this button)</li>
 *  </ul>
 * 
 */
abstract public class ButtonTemplate
{
	final static public int TEXT_TRANSFORM_NONE = 1;
	final static public int TEXT_TRANSFORM_ALLCAPS = 2;

	final static public int TEXT_ALIGN_LEFT = 1;
	final static public int TEXT_ALIGN_CENTER = 2;
	final static public int TEXT_ALIGN_RIGHT = 3;

	private int m_textTransform = TEXT_TRANSFORM_NONE;
	private int m_textAlign = TEXT_ALIGN_CENTER;

	private int m_width = 150;
	private int m_height = 35;
	private float m_smallCapsRatio = 0.75f;
	private boolean m_autoExtend = true;
	private boolean m_shadowDisplayed = false;
	private Font m_font = null;
	private Color m_fontColor = Color.white;
	private List<Effect> m_effects = new LinkedList<Effect>();

	/**
	 * This is the main method that clients should use to create an image with the corresponding text.
	 * 
	 * @param text the text to generate with this button template 
	 * @return an image of the button
	 */
	public BufferedImage getImage(String text)
	{
		int width = m_width;
		int height = m_height;

		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

		Font font = getTextFont(g);
		g.setFont(font);

		FontRenderContext frc = g.getFontRenderContext();
		List<TextElement> elements = buildTextElement(text, font);

		Rectangle2D bounds = getBounds(frc, elements);

		TextPadding padding = null;
		
		if (isAutoExtend())
		{
			int extendedwidth = (int) bounds.getWidth();
			int extendedheight = (int) bounds.getHeight();

			padding = this.getTextPadding(extendedwidth, extendedheight, elements);
			extendedwidth += padding.getLeft()+padding.getRight();
			extendedheight += padding.getTop()+padding.getBottom();

			if (m_shadowDisplayed)
			{
				extendedwidth++;
				extendedheight++;
			}
		
			int newwidth = Math.max(extendedwidth, width);
			int newheight = Math.max(extendedheight, height);
			if (width != newwidth || height != newheight)
				image = new BufferedImage(newwidth, newheight, BufferedImage.TYPE_INT_ARGB);

		}
		else
		{
			padding = this.getTextPadding(width, height, elements);
		}

		BufferedImage replacementImage = null;
		replacementImage = this.drawBackground(image, text);
		if (replacementImage != null)
			image = replacementImage;

		Rectangle2D.Double textBound = new Rectangle2D.Double(padding.getLeft(), padding.getTop(), image.getWidth()-padding.getLeft()-padding.getRight(), image.getHeight()-padding.getTop()-padding.getBottom());
		this.drawText(image, textBound, elements, bounds, g.getFontMetrics(font).getAscent());
		replacementImage = this.drawForeground(image, text);
		if (replacementImage != null)
			image = replacementImage;

		for (Effect e: m_effects)
		{
			image = e.apply(image);
		}

		return image;
	}

	/**
	 * Draw the background of the image.  
	 * @param img The bufferedImage in which to draw the background
	 * @param text the text of the button. This is for information only, as the ButtonTemplate is responsible for drawing the text.
	 * @return null if the changed occurred in the img Image given as parameter, or a new image buffer.
	 */
	abstract protected BufferedImage drawBackground(BufferedImage img, String text);

	/**
	 * Draw the foreground of the image.  
	 * @param img The bufferedImage in which to draw the foreground
	 * @param text the text of the button. This is for information only, as the text is already drawn at the time this method is called.
	 * @return null if the changed occurred in the img Image given as parameter, or a new image buffer.
	 */
	abstract protected BufferedImage drawForeground(BufferedImage img, String text);

	/**
	 * Defines the padding (spaces around the text) to apply on this button. This is required when the buttons need a specific margin to be applied 
	 * around the text to avoid overlapping with graphical elements of the background or foreground.
	 * 
	 * @param width the width of the image containing the text
	 * @param height
	 * @param elements
	 * @return
	 */
	protected TextPadding getTextPadding(int width, int height, List<TextElement> elements)
	{
		return new TextPadding(5,5,5,5);
	}

	protected class TextElement
	{
		public Font font;
		public String text;
		public Color color;
		private double width;

		public TextElement(Font f, String t)
		{
			this(f,t,m_fontColor);
		}

		public TextElement(Font f, String t, Color col)
		{
			this.font = f;
			this.text = t;
			this.color = col;
		}
	}

	protected Rectangle2D drawText(BufferedImage image, Rectangle2D target, List<TextElement> elements, Rectangle2D textBounds, float fontAscent)
	{
		Rectangle2D.Float result = new Rectangle2D.Float();
		Graphics2D g = image.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setColor(m_fontColor);

		float posx, posy;

		switch(m_textAlign)
		{
		case TEXT_ALIGN_LEFT:
			posx = (float)target.getX();
			break;

		case TEXT_ALIGN_RIGHT:
			posx = (float) (target.getX()+ target.getWidth() - textBounds.getWidth() );
			break;

		default:
			posx = (float)target.getX() + (float)(target.getWidth()-textBounds.getWidth())/2f; // center
		break;
		}
		posy = (float)target.getY() + (float)(target.getHeight()-textBounds.getHeight())/2f; // center

		// posy += textBounds.getY();
		posy += fontAscent;



		if (m_shadowDisplayed)
		{
			this.drawShadow(g, elements, posx+1, posy+1);
		}
		this.draw(g, elements, posx, posy);
		
		result.setRect(posx, posy-fontAscent, textBounds.getWidth(), textBounds.getHeight());
		return result;
	}

	private void draw(Graphics2D g, List<TextElement> elements, float posx, float posy)
	{
		for (TextElement el : elements)
		{
			g.setFont(el.font);
			g.setColor(el.color);
			g.drawString(el.text, posx, posy);
			posx += el.width;
		}
	}

	private void drawShadow(Graphics2D g, List<TextElement> elements, float posx, float posy)
	{
		for (TextElement el : elements)
		{
			g.setFont(el.font);
			g.setColor(Color.black);
			g.drawString(el.text, posx, posy);
			posx += el.width;
		}
	}

	private Rectangle2D getBounds(FontRenderContext frc, List<TextElement> elements)
	{
		double width = 0;
		double maxascent = 0;
		double maxheight = 0;
		for (TextElement el : elements)
		{
			Rectangle2D bounds = el.font.getStringBounds(el.text, frc);
			el.width = bounds.getWidth();
			width += el.width;
			LineMetrics metrics = el.font.getLineMetrics(el.text, frc);
			maxascent = Math.max(maxascent, metrics.getAscent());
			maxheight = Math.max(maxheight, metrics.getAscent()+metrics.getDescent()+metrics.getLeading());
		}

		return new Rectangle2D.Double(0,maxascent,width,Math.ceil(maxheight));
	}

	/**
	 * Create a list of TextElement objects that represent the text to display. 
	 * This methods manages the parsing and creation of TextElement. 
	 *  
	 * @param text a String object that represents the text of the button
	 * @param defaultFont the default font to use, if no font is specified in the text
	 * @return alist of TextElement object. If the string is empty, returns an empty list.
	 */
	protected List<TextElement> buildTextElement(String text, Font defaultFont)
	{
		List<TextElement> els = splitWords(text, defaultFont);
		switch(m_textTransform)
		{
		case ButtonTemplate.TEXT_TRANSFORM_ALLCAPS:
			return makeAllCaps(els);
		default:
			return els;
		}
	}

	private List<TextElement> splitWords(String text, Font defaultFont)
	{
		LinkedList<TextElement> res = new LinkedList<TextElement>();
		String[] split = text.split(" ");
		boolean firstword = true;
		Font font = m_font;
		if (m_font == null)
			font = defaultFont;
		for (String s:split)
		{
			if (!firstword)
				res.add(new TextElement(font, " "));
			else
				firstword = false;

			if (s.trim().length()>0)
				res.add(new TextElement(font, s));
		}
		return res;
	}

	private List<TextElement> makeAllCaps(List<TextElement> elements)
	{
		LinkedList<TextElement> res = new LinkedList<TextElement>();
		for (TextElement el : elements)
		{
			String s = el.text;
			if (s.length()>1)
			{
				TextElement e1 = new TextElement(el.font, el.text.substring(0, 1).toUpperCase()); 
				res.add(e1);
				float fs = el.font.getSize();
				fs *= m_smallCapsRatio;
				Font smaller = el.font.deriveFont(fs);
				TextElement e2 = new TextElement(smaller, el.text.substring(1).toUpperCase());
				res.add(e2);
			}
			else
			{
				el.text = el.text.toUpperCase();
				res.add(el);
			}
		}
		return res;
	}

	public Font getFont() {
		return m_font;
	}

	public void setFont(Font font) {
		m_font = font;
	}

	public int getWidth() {
		return m_width;
	}

	public void setWidth(int width) {
		m_width = width;
	}

	public int getHeight() {
		return m_height;
	}

	public void setHeight(int height) {
		m_height = height;
	}

	public boolean isAutoExtend() {
		return m_autoExtend;
	}

	public void setAutoExtend(boolean autoExtend) {
		m_autoExtend = autoExtend;
	}

	public Color getFontColor() {
		return m_fontColor;
	}

	public void setFontColor(Color fontColor) {
		m_fontColor = fontColor;
	}

	public int getTextTransform() {
		return m_textTransform;
	}

	public void setTextTransform(int textTransform) {
		m_textTransform = textTransform;
	}

	public float getSmallCapsRatio() {
		return m_smallCapsRatio;
	}

	public void setSmallCapsRatio(float smallCapsRatio) {
		m_smallCapsRatio = smallCapsRatio;
	}

	public boolean isShadowDisplayed() {
		return m_shadowDisplayed;
	}

	public void setShadowDisplayed(boolean shadowDisplayed) {
		m_shadowDisplayed = shadowDisplayed;
	}

	public int getTextAlign() {
		return m_textAlign;
	}

	public void setTextAlign(int textAlign) {
		m_textAlign = textAlign;
	}

	protected Graphics2D initializeGraphics2D(BufferedImage img)
	{
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		return g;
	}

	/**
	 * Add an effect to apply on the button. Effects are applied in the same order they are added.
	 * @param effect
	 * @return
	 */
	public ButtonTemplate addEffect(Effect effect)
	{
		m_effects.add(effect);
		return this;
	}

	public List<Effect> getEffects() {
		return m_effects;
	}

	public void setEffects(List<Effect> effects) {
		m_effects = effects;
	}

	protected Font getTextFont(Graphics2D g)
	{
		Font font = m_font;
		if (font == null)
			font = g.getFont();
		return font;
	}
}
