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
import java.awt.Graphics2D;
import java.awt.LinearGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * Creates a (Web2.0) vertical mirror effect. The mirror size ratio defines the size of the mirror effect: 1.0 for a mirror the same size than the original image, 0.5 for half its size.
 * The mirror reflect ratio specifies how "strong" the mirror image is (this is used to creates the alpha mask, the lower this value is, the more the fading effect).
 *  
 * @author Rodrigo Reyes
 *
 */
public class VerticalMirror extends Effect 
{
	private float m_mirrorSizeRatio = 0.50f;
	private float m_mirrorReflectRatio = .25f;

	public VerticalMirror()
	{
		
	}
	
	public VerticalMirror(float mirrorSizeRatio, float mirrorReflectRatio)
	{
		m_mirrorReflectRatio = mirrorReflectRatio;
		m_mirrorSizeRatio = mirrorSizeRatio;
	}
	
	public float getMirrorSizeRatio() {
		return m_mirrorSizeRatio;
	}

	public void setMirrorSizeRatio(float mirrorSizeRatio) {
		m_mirrorSizeRatio = mirrorSizeRatio;
	}

	public float getMirrorReflectRatio() {
		return m_mirrorReflectRatio;
	}

	public void setMirrorReflectRatio(float mirrorReflectRatio) {
		m_mirrorReflectRatio = mirrorReflectRatio;
	}

	@Override
	public BufferedImage apply(BufferedImage org) 
	{
		BufferedImage reversed = this.newBuffer(org.getWidth(), org.getHeight());

		// Operate an upside-down mirror effect using AffineTransform
		AffineTransform reverse = new AffineTransform();
		reverse.translate( org.getWidth() / 2.0, org.getHeight() / 2.0 );
		reverse.scale(1.0f, -1.0f);
		reverse.translate( -org.getWidth() / 2.0, -org.getHeight() / 2.0 );
		
		Graphics2D g = this.createInitializedGraphics2D(reversed);
		g.drawImage(org, reverse, null);
	
		Color starter = new Color(0f,0f,0f, m_mirrorReflectRatio);
		LinearGradientPaint gradmask = new LinearGradientPaint(0,0,0,org.getHeight(), new float[] {0f, 1f}, new Color[] {starter, new Color(0,0,0,0)});
		BufferedImage mask = this.newBuffer(reversed.getWidth(), reversed.getHeight());
		Graphics2D gmask = this.createInitializedGraphics2D(mask);
		gmask.setPaint(gradmask);
		gmask.fill(new Rectangle2D.Float(0,0,mask.getWidth(), mask.getHeight()));
		
		reversed = GfxEffects.substituteAlpha(reversed, mask);
		
		float mirrorSize = org.getHeight() * m_mirrorSizeRatio;
		BufferedImage result = this.newBuffer(org.getWidth(), org.getHeight()+(int)mirrorSize);
		Graphics2D gr = this.createInitializedGraphics2D(result);
		gr.drawImage(org, 0, 0, null);

		AffineTransform scaler = new AffineTransform();
		scaler.translate(0, org.getHeight());
		scaler.scale(1f, m_mirrorSizeRatio);
		gr.drawImage(reversed, scaler, null);
		
		return result;
	}

}
