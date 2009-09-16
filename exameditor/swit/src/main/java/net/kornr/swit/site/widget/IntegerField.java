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

import net.kornr.swit.site.jquery.JQuery;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.model.IModel;
import org.apache.wicket.resource.ContextRelativeResource;


public class IntegerField extends FormComponentPanel<Double> implements IHeaderContributor 
{
	private static final long serialVersionUID = 1L;

	private static String s_enablenumeric = "$('#NUMBERFIELD').numeric();";
	private static String s_enablenumeric_decimal = "$('#NUMBERFIELD').numeric({allow:'.'});";
	private static String s_stepfn = "$(document).ready(function(){ $('#IMAGEID').bind('click', function() { $('#NUMBERFIELD').val(new Number($('#NUMBERFIELD').val()) + STEP); });});";
	private static String s_stepfnminmax = "$(document).ready(function(){ $('#IMAGEID').bind('click', function() { var v = new Number($('#NUMBERFIELD').val()); v+= STEP; if ((v*100)%100 >0)v=v.toFixed(2); if (v<MIN) v=MIN; if (v>MAX)v=MAX; $('#NUMBERFIELD').val(v); });});";

	private TextField<Double> m_numbertf;
	private Image m_inc, m_dec;
	private Number m_step;
	private Number m_max = null;
	private Number m_min = null;
	private boolean m_allowDecimal;

	static public class IntegerAdaptor implements IModel<Double>
	{
		private IModel<Integer> m_org;
		public IntegerAdaptor(IModel<Integer> model)
		{
			m_org = model;
		}

		public Double getObject() {
			return (double)m_org.getObject();
		}

		public void setObject(Double object) {
			m_org.setObject(object.intValue());
		}

		public void detach() {
			m_org.detach();
		}
	}

	static public class FloatAdaptor implements IModel<Double>
	{
		private IModel<Float> m_org;
		
		public FloatAdaptor(IModel<Float> model)
		{
			m_org = model;
		}

		public Double getObject() {
			return (double)m_org.getObject();
		}

		public void setObject(Double object) {
			m_org.setObject(object.floatValue());
		}

		public void detach() {
			m_org.detach();
		}
	}

	
	public IntegerField(String id, IModel<Double> model, Number step, boolean allowDecimal, Double min, Double max) {
		this(id, model, step, allowDecimal);
		m_min = Math.min(max, min);
		m_max = Math.max(max, min);
	}

	public IntegerField(String id, IModel<Double> model, Number step)
	{
		this(id,model,step,false);
	}

	public IntegerField(String id, IModel<Double> model, Number step, boolean allowDecimal) {
		super(id, model);
		m_step = step;
		m_allowDecimal = allowDecimal;

		m_numbertf = new TextField<Double>("number", model, Double.class) {
			@Override
			protected void convertInput() {
				String s = this.getInput();
				Double d = new Double(IntegerField.this.getValue(s));
				setConvertedInput(d);
			}

			@Override
			protected String getModelValue() {
				if (m_allowDecimal)
				{
					double d = this.getModelObject();
					int rest = (int)((d*100)%100);
					if (rest==0)
						return Integer.toString((int)d);
					else
						return Double.toString(d);						
				}
				else
					return Integer.toString(this.getModelObject().intValue());
			}

		};
		m_numbertf.setOutputMarkupId(true);
		add(m_numbertf);

		m_inc = new Image("inc", new ResourceReference(IntegerField.class, "small-arrow-up.png"));
		m_dec = new Image("dec", new ResourceReference(IntegerField.class, "small-arrow-down.png"));
		m_inc.setOutputMarkupId(true);
		m_dec.setOutputMarkupId(true);
		this.add(m_inc);
		this.add(m_dec);

	}

	@Override
	protected void convertInput() {
		Double d = m_numbertf.getConvertedInput();
		this.setConvertedInput(d);
	}

	@Override
	public void updateModel() {
		Double d = m_numbertf.getConvertedInput();
		this.setModelObject(d);
	}

	private double getValue(String str)
	{
		str = str.replace(" ", "");
		str = str.replace(",", ".");
		return Double.parseDouble(str);
	}

	public void renderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JQuery.getReference());

		String stepcode = s_stepfn;
		if (m_min != null && m_max != null)
			stepcode = s_stepfnminmax;
		String javascriptcode =  varreplace(stepcode, -m_step.floatValue(), m_dec.getMarkupId())
		+ varreplace(stepcode, m_step.floatValue(), m_inc.getMarkupId()); 

		response.renderOnLoadJavascript(javascriptcode);

		String upurl = this.urlFor(new ResourceReference(IntegerField.class, "small-arrow-up.png")).toString();
		String uphoverurl = this.urlFor(new ResourceReference(IntegerField.class, "small-arrow-up-hover.png")).toString();
		String downurl = this.urlFor(new ResourceReference(IntegerField.class, "small-arrow-down.png")).toString();
		String downhoverurl = this.urlFor(new ResourceReference(IntegerField.class, "small-arrow-down-hover.png")).toString();
		
		String str = "$('#IMAGEID').mouseenter(function(){$('#IMAGEID').attr('src', '"+uphoverurl+"');});";
		str += "$('#IMAGEID').mouseleave(function(){$('#IMAGEID').attr('src', '"+upurl+"');});";
		response.renderOnLoadJavascript(varreplace(str,0,m_inc.getMarkupId()));
		str = "$('#IMAGEID').mouseenter(function(){$('#IMAGEID').attr('src', '"+downhoverurl+"');});";
		str += "$('#IMAGEID').mouseleave(function(){$('#IMAGEID').attr('src', '"+downurl+"');});";
		response.renderOnLoadJavascript(varreplace(str,0,m_dec.getMarkupId()));
		
		String chgevent_inc = "$('#"+m_inc.getMarkupId()+"').bind('click', function(){$('#"+m_numbertf.getMarkupId()+"').change()});";
		String chgevent_dec = "$('#"+m_dec.getMarkupId()+"').bind('click', function(){$('#"+m_numbertf.getMarkupId()+"').change()});";
		response.renderJavascript(JQuery.getOnReadyScript(chgevent_inc), null);
		response.renderJavascript(JQuery.getOnReadyScript(chgevent_dec), null);
	} 

	public String getTextFieldMarkupId()
	{
		return m_numbertf.getMarkupId();
	}
	
	private String varreplace(String str, float nb, String imgid)
	{
		return str.replace("IMAGEID", imgid).replace("NUMBERFIELD", m_numbertf.getMarkupId()).replace("STEP", Float.toString(nb)).replace("MIN", m_min!=null?m_min.toString():"").replace("MAX", m_min!=null?m_max.toString():"");
	}

}
