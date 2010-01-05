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

import java.awt.Color;

public class BackgroundColorBorder extends MarginBorder 
{
	public static Long register(Long parentId, Color background)
	{
		BackgroundColorBorder sb = new BackgroundColorBorder(parentId, background);
		return BorderMaker.register(sb);
	}
	
	protected BackgroundColorBorder(Long parentId, Color background) 
	{
		super(parentId, 0,0,0,0, background);
	}
}
