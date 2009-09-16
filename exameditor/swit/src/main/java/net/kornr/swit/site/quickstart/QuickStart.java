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
package net.kornr.swit.site.quickstart;

import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.model.Model;

import net.kornr.swit.site.BasePage;
import net.kornr.swit.site.jquery.JQuery;
import net.kornr.swit.site.jquery.tools.JQueryTools;
import net.kornr.swit.site.quickstart.examples.BorderExample;
import net.kornr.swit.site.quickstart.examples.BorderExample2;
import net.kornr.swit.site.quickstart.examples.ButtonExample;
import net.kornr.swit.site.quickstart.examples.ButtonExample2;
import net.kornr.swit.site.quickstart.examples.ThreeColumnExample;
import net.kornr.swit.site.quickstart.examples.ThreeColumnExample2;
import net.kornr.swit.wicket.border.graphics.BorderMaker;

public class QuickStart extends BasePage implements IHeaderContributor
{
	public QuickStart()
	{
		this.innerAdd(new ExampleDisplay("button-example", new Model<Class>(ButtonExample.class)));
		this.innerAdd(new ExampleDisplay("button-example2", new Model<Class>(ButtonExample2.class)));
		
		this.innerAdd(BorderMaker.getImage("border-example-image", this.getOuterBorder(), "full", false));
		this.innerAdd(new ExampleDisplay("border-example1", new Model<Class>(BorderExample.class)));
		this.innerAdd(new ExampleDisplay("border-example2", new Model<Class>(BorderExample2.class)));

		this.innerAdd(new ExampleDisplay("layout-example1", new Model<Class>(ThreeColumnExample.class)));
		this.innerAdd(new ExampleDisplay("layout-example2", new Model<Class>(ThreeColumnExample2.class)));
	}

	@Override
	public void renderHead(IHeaderResponse response) 
	{
		super.renderHead(response);

		response.renderJavascriptReference(JQuery.getReference());
		response.renderJavascriptReference(JQueryTools.getReference());
		response.renderJavascript(JQuery.getOnReadyScript("$('ul.tabs').tabs('div.panes > div');"), null);
	}
	
	
}
