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
import java.awt.image.BufferedImage;

public class SizedBorder extends BorderMaker 
{
	private BorderMaker m_parent;
	
	static public Long register(Long parentId, int width, int height)
	{
		SizedBorder sb = new SizedBorder(parentId, width, height);
		return BorderMaker.register(sb);
	}

	protected SizedBorder(Long parentId, int width, int height)
	{
		super(width,height);
		m_parent = BorderMaker.get(parentId);
		m_parent.setWidth(width);
		m_parent.setHeight(height);
	}
	
	@Override
	public BufferedImage createImage(Rectangle part) {
		return m_parent.createImage(part);
	}

	@Override
	public BufferedImage createIndexedImage(Rectangle part) {
		return m_parent.createIndexedImage(part);
	}

	@Override
	public ImageMap getImageMap() {
		return m_parent.getImageMap();
	}
}
