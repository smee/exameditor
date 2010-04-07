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
import java.awt.image.BufferedImage;
import java.util.List;

import net.kornr.swit.button.ButtonTemplate.TextElement;
import net.kornr.swit.button.effect.RoundBorder;

/**
 * An abstract class that can be used as a basis for implementing buttons that contain 
 * a rounded border of variable line width and color. Extending classes need to draw
 * the background, the rounded border is added automatically in the drawForeground()
 * method, and an appropriate text padding is specified. 
 * 
 */
abstract public class AbstractRoundButton extends ButtonTemplate 
{
	private float m_roundSize = 15f;
	private float m_lineWidth = 1.0f;
	private Color m_lineColor = Color.black;

	@Override
	final protected BufferedImage drawForeground(BufferedImage img, String text) 
	{
		img = new RoundBorder(m_lineWidth, m_roundSize, true, m_lineColor).apply(img);
		return img;
	}

	public float getRoundSize() {
		return m_roundSize;
	}

	public void setRoundSize(float roundSize) {
		m_roundSize = roundSize;
	}

	public float getLineWidth() {
		return m_lineWidth;
	}

	public void setLineWidth(float lineWidth) {
		m_lineWidth = lineWidth;
	}

	public Color getLineColor() {
		return m_lineColor;
	}

	public void setLineColor(Color lineColor) {
		m_lineColor = lineColor;
	}

	@Override
	protected TextPadding getTextPadding(int width, int height, List<TextElement> elements)
	{
		int std = (int)(m_roundSize/2);
		int min = std<5?5:std;
		int minh = m_roundSize<4?4:1;
		return new TextPadding(min, min, minh, minh);
	}

}
