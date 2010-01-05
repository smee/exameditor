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
package net.kornr.swit.wicket.border.graphics;

import java.awt.Rectangle;
import java.io.Serializable;

public class ImageMap implements Serializable
{
	private int[] m_horizontal;
	private int[] m_vertical;

	static public final String TOP_LEFT = "tl";
	static public final String TOP_RIGHT = "tr";
	static public final String BOTTOM_LEFT = "bl";
	static public final String BOTTOM_RIGHT = "br";
	static public final String TOP = "t";
	static public final String BOTTOM = "b";
	static public final String LEFT = "l";
	static public final String RIGHT = "r";
	
	public ImageMap(int[] horizontal, int[] vertical)
	{
		m_horizontal = horizontal;
		m_vertical = vertical;
	}
	
	public ImageMap add(int[]h, int[]v)
	{
		for (int i=0; i<m_horizontal.length; i++)
			m_horizontal[i] += h[i];
		
		for (int i=0; i<m_vertical.length; i++)
			m_vertical[i] += v[i];
		
		return this;
	}
	
	public Rectangle getZone(String name)
	{
		int posx=0,posy=0;
		
		if ("tl".equalsIgnoreCase(name))
		{
			posx = posy = 0;
		}
		else if ("t".equalsIgnoreCase(name))
		{
			posx = 1; posy=0;
		}
		else if ("tr".equalsIgnoreCase(name))
		{
			posx = 2; posy=0;
		}
		else if ("r".equalsIgnoreCase(name))
		{
			posx = 2; posy=1;
		}
		else if ("l".equalsIgnoreCase(name))
		{
			posx = 0; posy=1;
		}
		else if ("bl".equalsIgnoreCase(name))
		{
			posx = 0; posy=2;
		}
		else if ("b".equalsIgnoreCase(name))
		{
			posx = 1; posy=2;
		}
		else if ("br".equalsIgnoreCase(name))
		{
			posx = 2; posy=2;
		}
		else if ("full".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[0], m_horizontal[3] - m_horizontal[0], m_vertical[3] - m_vertical[0]);
		}
		else if ("line1".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[0], m_horizontal[3] - m_horizontal[0], m_vertical[1] - m_vertical[0]);
		}
		else if ("line2".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[1], m_horizontal[3] - m_horizontal[0], m_vertical[2] - m_vertical[1]);
		}
		else if ("line3".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[2], m_horizontal[3] - m_horizontal[0], m_vertical[3] - m_vertical[2]);
		}
		else if ("line12".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[0], m_horizontal[3] - m_horizontal[0], m_vertical[2] - m_vertical[0]);
		}
		else if ("line23".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[1], m_horizontal[3] - m_horizontal[0], m_vertical[3] - m_vertical[1]);
		}
		else if ("row1".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[0], m_horizontal[1] - m_horizontal[0], m_vertical[3] - m_vertical[0]);
		}
		else if ("row2".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[1], m_vertical[0], m_horizontal[2] - m_horizontal[1], m_vertical[3] - m_vertical[0]);
		}
		else if ("row3".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[2], m_vertical[0], m_horizontal[3] - m_horizontal[2], m_vertical[3] - m_vertical[0]);
		}
		else if ("row12".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[0], m_horizontal[2] - m_horizontal[0], m_vertical[3] - m_vertical[0]);
		}
		else if ("row23".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[1], m_vertical[0], m_horizontal[3] - m_horizontal[1], m_vertical[3] - m_vertical[0]);
		}
		else if ("cell2356".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[1], m_vertical[0], m_horizontal[3] - m_horizontal[1], m_vertical[2] - m_vertical[0]);
		}
		else if ("cell89".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[1], m_vertical[2], m_horizontal[3] - m_horizontal[1], m_vertical[3] - m_vertical[2]);
		}
		else if ("cell14".equalsIgnoreCase(name))
		{
			return new Rectangle(m_horizontal[0], m_vertical[0], m_horizontal[1] - m_horizontal[0], m_vertical[2] - m_vertical[0]);
		}
		
		return new Rectangle(m_horizontal[posx], m_vertical[posy], m_horizontal[posx+1] - m_horizontal[posx], m_vertical[posy+1] - m_vertical[posy]);

	}

}
