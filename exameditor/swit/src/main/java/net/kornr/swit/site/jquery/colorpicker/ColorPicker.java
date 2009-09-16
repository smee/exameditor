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
package net.kornr.swit.site.jquery.colorpicker;

import net.kornr.swit.site.jquery.JQuery;

import org.apache.wicket.Component;
import org.apache.wicket.Resource;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebResource;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.resources.PackagedResourceReference;
import org.apache.wicket.util.resource.IResourceStream;
import org.apache.wicket.util.resource.PackageResourceStream;


public class ColorPicker extends WebResource
{
	@Override
	public IResourceStream getResourceStream() {
		return new PackageResourceStream(ColorPicker.class, "js/colorpicker.js");
	}

	public static ResourceReference getReference()
	{
		return new ResourceReference(ColorPicker.class, "js") {
			@Override
			protected Resource newResource() {
				return new ColorPicker();
			}
		};
	}

	public static void renderHead(IHeaderResponse response, TextField<String> field, Component sample)
	{
		field.setOutputMarkupId(true);
		String id = field.getMarkupId();
		
		

		String options = null;
		if (sample == null)
			options = "color: '#0000ff', onBeforeShow: function () { $(this).ColorPickerSetColor(this.value);}, onShow: function (colpkr) { $(colpkr).fadeIn(50); return false; },"
				+ "onHide: function (colpkr) { $(colpkr).fadeOut(50); return false; },"
				+ "onChange: function (hsb, hex, rgb) { $('#"+id+"').val(hex); $('#"+id+" ~ span.colorpickersample').css('backgroundColor', '#' + hex); $('#"+id+" ~ .colorpickersample').css('backgroundColor', '#' + hex); }";
		else
			options = "color: '#0000ff', onBeforeShow: function () { $(this).ColorPickerSetColor(this.value);}, onShow: function (colpkr) { $(colpkr).fadeIn(50); return false; },"
			+ "onHide: function (colpkr) { $(colpkr).fadeOut(50); return false; },"
			+ "onChange: function (hsb, hex, rgb) { $('#"+id+"').val(hex); $('#"+sample.getMarkupId()+"').css('backgroundColor', '#' + hex); }";
		
		
		response.renderJavascriptReference(JQuery.getReference());
		response.renderJavascriptReference(ColorPicker.getReference());
		response.renderCSSReference(new ResourceReference(ColorPicker.class, "css/colorpicker.css"));
//		response.renderCSSReference(new ResourceReference(ColorPicker.class, "css/layout.css"));
		response.renderJavascript(JQuery.getOnReadyScript("$('#"+id+"').ColorPicker({ " + options + "});"), null);
	}
	
}
