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
package net.kornr.swit.site.jquery;

import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.PackageResourceStream;

public class JQuery extends WebResource
{
	private static boolean s_referenceAdded = false;

	public JQuery()
	{
		this.setCacheable(true);
	}
	
	@Override
	protected int getCacheDuration() {
		return 60*60*24; // 24h
	}

	@Override
	public IResourceStream getResourceStream() 
	{
		return new PackageResourceStream(JQuery.class, "jquery.js");
	}
	
	public static ResourceReference getReference()
	{
		if (s_referenceAdded == false)
		{
			s_referenceAdded = true;

			Class c = JQuery.class;
			String name = "/jquery-1.3.2.js";
			WebResource instance = new JQuery();
			
			WebApplication.get().getSharedResources().add(c, "js", null, null, instance);
			ResourceReference ref = new ResourceReference(c, "js");
			WebApplication.get().mountSharedResource(name, ref.getSharedResourceKey());
		}

		return new ResourceReference(JQuery.class, "js");
	}

	public static String getOnReadyScript(String script)
	{
		return "$(document).ready(function(){" + script + ";});";
	}
	
}
