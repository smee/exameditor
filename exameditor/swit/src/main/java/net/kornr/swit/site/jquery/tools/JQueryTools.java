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
package net.kornr.swit.site.jquery.tools;

import net.kornr.swit.site.jquery.JQuery;

import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.PackageResourceStream;

public class JQueryTools extends WebResource
{

	@Override
	public IResourceStream getResourceStream() {
		return new PackageResourceStream(JQueryTools.class, "jquery.tools.min.js");
	}

	static public ResourceReference getReference()
	{
		return new ResourceReference(JQueryTools.class, "js") {
			@Override
			protected Resource newResource() {
				return new JQueryTools();
			}
		};
	}
	
}
