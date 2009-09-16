package net.kornr.swit.site.util.shjs;

import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderResponse;

public class Shjs 
{
	static public void include(IHeaderResponse response, boolean java, boolean html)
	{
		response.renderJavascriptReference(new ResourceReference(Shjs.class, "sh_main.min.js"));
		
		if (java)
			response.renderJavascriptReference(new ResourceReference(Shjs.class, "sh_java.min.js"));
		
		if (html)
			response.renderJavascriptReference(new ResourceReference(Shjs.class, "sh_html.min.js"));
		
		response.renderCSSReference(new ResourceReference(Shjs.class, "sh_style.min.css"));
	}
}
