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
package net.kornr.swit.site.widget;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormSubmitBehavior;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.util.value.ValueMap;

public class AjaxImageRadio extends Image 
{
	private String m_radioId;
	
	public AjaxImageRadio(String id, ResourceReference resourceReference,ValueMap resourceParameters, Form form, Radio radio) 
	{
		super(id, resourceReference, resourceParameters);
		
		radio.setOutputMarkupId(true);
		m_radioId = radio.getMarkupId();
		
		this.add(new AjaxFormSubmitBehavior(form, "onclick") {
			@Override
			protected void onError(AjaxRequestTarget arg0) {
			}
			@Override
			protected void onSubmit(AjaxRequestTarget target) {
				AjaxImageRadio.this.onSubmit(target);
			}
			@Override
			protected CharSequence getEventHandler() {
				String seq = "$('#"+m_radioId+"').attr('checked',true);";
				return seq + super.getEventHandler();
			}
		});
	}

	protected void onSubmit(AjaxRequestTarget target) {
	}

}
