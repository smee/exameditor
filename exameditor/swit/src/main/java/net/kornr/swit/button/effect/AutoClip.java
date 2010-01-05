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

import net.kornr.swit.wicket.border.graphics.GfxEffects;

/**
 * If the button generator leaves empty spaces around the buttons, and this annoys you, just use this effect to automatically clip the
 * button to its smallest possible size. It detects the borders, and returns a new, hopefully smaller, image.
 * 
 * @author Rodrigo Reyes
 *
 */
public class AutoClip extends Effect 
{
	private boolean m_clipVertical = true;
	private boolean m_clipHorizontal = true;

	public AutoClip()
	{
		
	}
	
	public AutoClip(boolean horizontalAutoClip, boolean verticalAutoClip)
	{
		m_clipHorizontal = horizontalAutoClip;
		m_clipVertical = verticalAutoClip;
	}
	
	@Override
	public BufferedImage apply(BufferedImage org) 
	{
		int width = org.getWidth();
		int height = org.getHeight();
		int topline = 0, bottomline = height;

		if (m_clipVertical)
		{
			while (isEmptyLine(org, topline) && topline<height)
				topline++;
			while (isEmptyLine(org, bottomline-1) && bottomline>=0)
				bottomline--;
		}

		int leftcol = 0, rightcol=width;

		if (m_clipHorizontal)
		{
			while (isEmptyColumn(org, leftcol) && leftcol<width)
				leftcol++;
			while (isEmptyColumn(org, rightcol-1) && rightcol>0)
				rightcol--;
		}
		int newwidth = rightcol-leftcol;
		int newheight = bottomline-topline;

		if (newwidth>0 && newheight>0)
		{
			return GfxEffects.getClipped(org, leftcol, topline, newwidth, newheight);
		}

		return org;
	}

	private boolean isEmptyLine(BufferedImage img, int line)
	{
		int width = img.getWidth();
		for (int i=0; i<width; i++)
		{
			int c = img.getRGB(i, line);
			if (((c>>24)&0xFF) != 0)
				return false;
		}
		return true;
	}

	private boolean isEmptyColumn(BufferedImage img, int col)
	{
		int height = img.getHeight();
		for (int i=0; i<height; i++)
		{
			int c = img.getRGB(col, i);
			if (((c>>24)&0xFF) != 0)
				return false;
		}
		return true;
	}



}
