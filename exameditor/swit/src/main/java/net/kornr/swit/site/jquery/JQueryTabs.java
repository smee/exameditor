package net.kornr.swit.site.jquery;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;

import net.kornr.swit.util.MappedString;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.Link;

public class JQueryTabs implements Serializable
{
	private LinkedHashMap<String, String> m_linkToPane = new LinkedHashMap<String, String>();
	
	public void add(AbstractLink l, Component comp)
	{
		m_linkToPane.put(l.getMarkupId(), comp.getMarkupId());
		l.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
		comp.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
	}
	
	public void configure(IHeaderResponse response)
	{
		StringBuffer buffer = new StringBuffer();
		boolean first = true;
		for (String s: m_linkToPane.keySet())
		{
			String curpaneid = m_linkToPane.get(s);
			buffer.append("$('#" + s + "').unbind('click');");
			buffer.append("$('#" + s + "').click(function(){");
			for (String v: m_linkToPane.values())
			{
				if (!curpaneid.equals(v))
				{
					buffer.append("$('#" + v + "').hide();");
				}
				else
				{
					buffer.append("$('#" + v + "').show();");
				}
			}
			buffer.append("return false;");
			buffer.append("});\n");
			if (first)
			{
				first = false;
			}
			else
			{
				buffer.append("$('#"+curpaneid+"').hide();");
			}
		}
		response.renderJavascriptReference(JQuery.getReference());
		response.renderJavascript(JQuery.getOnReadyScript(buffer.toString()), null);
	}
	
}
