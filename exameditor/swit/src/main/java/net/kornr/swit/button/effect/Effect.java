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

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;

/**
 * Base class for the effects. An effect basically takes an image and returns another image, or null if no change is applied.
 * 
 * @author Rodrigo Reyes
 *
 */
abstract public class Effect
{
	private Shape m_effectClipping = null;
	
	/**
	 * Takes an image, return another image. Simple.
	 * @param org an image
	 * @return an image, or null if no effect can be applied on the image.
	 */
	abstract public BufferedImage apply(BufferedImage org);
	
	protected BufferedImage newBuffer(int width, int height)
	{
		return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	}
	
	protected Graphics2D createInitializedGraphics2D(BufferedImage img)
	{
		Graphics2D g = img.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		g.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
		g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

		return g;
	}

	protected void applyEffectClipping(Graphics2D g)
	{
		g.setClip(m_effectClipping);
	}
	
	protected void cancelEffectClipping(Graphics2D g)
	{
		g.setClip(null);
	}

	public Shape getEffectClipping() {
		return m_effectClipping;
	}

	public void setEffectClipping(Shape effectClipping) {
		m_effectClipping = effectClipping;
	}

}
