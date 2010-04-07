package net.kornr.swit.site.jquery.colorpicker;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;

public class ColorPickerField extends TextField<String> implements IHeaderContributor 
{
	private IModel<String> m_model;
	private Component m_sample;
	
	public ColorPickerField(String id, IModel<String> model, Component sample) 
	{
		super(id, model);

		if (sample != null)
		{
			m_sample = sample;
			sample.setOutputMarkupId(true);
			
		}
		else
		{
			m_sample = null;
		}
	}

	public void renderHead(IHeaderResponse response) 
	{
		ColorPicker.renderHead(response, this, m_sample);
	}
	
}
