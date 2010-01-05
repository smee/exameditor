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
import java.awt.image.BufferedImage;

/**
 * Adds some empty margin to an image.
 *  
 * @author Rodrigo Reyes
 *
 */
public class Margin extends Effect 
{
	private int m_top, m_bottom, m_left, m_right;
	private Color m_background = new Color(0,0,0,0);
	
	public Margin(int width)
	{
		m_top = m_bottom = m_left = m_right = width;
	}
	
	public Margin(int left, int right, int top, int bottom)
	{
		m_left = left;
		m_right = right;
		m_top = top;
		m_bottom = bottom;
	}

	@Override
	public BufferedImage apply(BufferedImage org) 
	{
		BufferedImage img = new BufferedImage(org.getWidth()+m_left+m_right, org.getHeight()+m_top+m_bottom, BufferedImage.TYPE_INT_ARGB);
		img.createGraphics().drawImage(org, m_left, m_top, m_background, null);
		return img;
	}

}
