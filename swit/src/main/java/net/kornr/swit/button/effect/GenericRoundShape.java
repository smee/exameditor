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

import java.awt.Shape;
import java.awt.geom.GeneralPath;

/**
 * Not an effect, this class is mainly used to created a rounded shape with arbitrary round size at each corner.
 *  
 * @author Rodrigo Reyes
 *
 */
public class GenericRoundShape 
{
	private float m_roundTopLeft, m_roundTopRight, m_roundBottomLeft, m_roundBottomRight;
	
	public GenericRoundShape(float rTopLeft, float rTopRight, float rBottomLeft, float rBottomRight)
	{
		m_roundTopLeft = rTopLeft;
		m_roundTopRight = rTopRight;
		m_roundBottomLeft = rBottomLeft;
		m_roundBottomRight = rBottomRight;
	}
	
	public Shape createShape(float width, float height)
	{
		GeneralPath path = new GeneralPath();
		
		// Top Left curve
		float x1 = 0;
		float y1 = m_roundTopLeft;
		path.moveTo(x1, y1);
		
		float x2 = m_roundTopLeft;
		float y2 = 0;
		path.quadTo(0, 0, x2, y2);
		
		// Top Right Curve
		float x3 = width - m_roundTopRight;
		float y3 = 0;
		path.lineTo(x3, y3);
		
		float x4 = width;
		float y4 = m_roundTopRight;
		path.quadTo(width, 0, x4, y4);
		
		// Bottom Right curve
		float x5 = width;
		float y5 = height - m_roundBottomRight;
		path.lineTo(x5, y5);
		
		float x6 = width - m_roundBottomRight;
		float y6 = height;
		path.quadTo(width, height, x6, y6);
		
		// Bottom Left curve
		float x7 = m_roundTopLeft;
		float y7 = height;
		path.lineTo(x7, y7);
		
		float x8 = 0;
		float y8 = height - m_roundTopLeft;
		path.quadTo(0, height, x8, y8);
		
		path.lineTo(x1, y1);
		
		return path;
	}
}
