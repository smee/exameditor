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
package net.kornr.swit.wicket.border;

import java.net.URL;

import net.kornr.swit.wicket.border.graphics.BorderMaker;

import org.apache.wicket.behavior.AttributeAppender;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.model.Model;


public class FixedImageWebContainer extends WebMarkupContainer 
{
	public FixedImageWebContainer(String id, Long borderimage) 
	{
		super(id);
//		String url = BorderMaker.getUrl(borderimage, "full", false);
//		String val = "background-repeat: no-repeat;background-image: url('" + url + "')";
//		this.add(new AttributeAppender("style", true, new Model<String>(val), ";"));
		
		this.add(getAttributeAppender(borderimage));
	}
	
	static public AttributeAppender getAttributeAppender(Long borderId)
	{
		String url = BorderMaker.getUrl(borderId, "full", false);
		String val = "background-repeat: no-repeat;background-image: url('" + url + "')";
		
		return new AttributeAppender("style", true, new Model<String>(val), ";");		
	}

}
