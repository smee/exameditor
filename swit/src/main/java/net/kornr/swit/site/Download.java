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
package net.kornr.swit.site;

import java.io.InputStream;
import net.kornr.swit.util.StringUtils;
import org.apache.wicket.markup.html.basic.Label;

public class Download extends BasePage 
{
	private static String s_history = null;
	private static String s_license = null;
	
	public Download()
	{
		if (s_history == null)
			s_history = StringUtils.load(Download.class, "/HISTORY", null);
		
		if (s_license == null)
			s_license = StringUtils.load(Download.class, "/SHORT-LICENSE", null);
		
		this.innerAdd(new Label("history", s_history));
		this.innerAdd(new Label("shortlicense", s_license));
	}
	
}
