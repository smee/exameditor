package net.kornr.swit.site.quickstart;

import java.io.InputStream;

import net.kornr.swit.site.jquery.JQuery;
import net.kornr.swit.site.jquery.tools.JQueryTools;
import net.kornr.swit.site.util.shjs.Shjs;
import net.kornr.swit.util.StringUtils;

import org.apache.wicket.PageParameters;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

public class SourcePage extends WebPage implements IHeaderContributor
{
	private String m_type;
	
	static public PageParameters getParameters(String className, String type)
	{
		PageParameters params = new PageParameters();
		params.put("class", className);
		params.put("type", type);
		return params;
	}

	static public WebMarkupContainer createIFrame(String id, Class src, String type)
	{
		WebMarkupContainer srcframe = new WebMarkupContainer(id);
		String srcurl = RequestCycle.get().urlFor(null, SourcePage.class, SourcePage.getParameters(src.getCanonicalName(), type)).toString();
		srcframe.add(new SimpleAttributeModifier("src", srcurl));
		return srcframe;
	}
	
	public SourcePage(PageParameters params)
	{
		String clazz = params.getString("class", "");
		String type = params.getString("type", "");
		if (!clazz.startsWith("net.kornr.swit.site") && (type.equals("java") || type.equals("html")))
		{
			this.add(new Label("source", clazz+"."+type+"? ORLY?"));
			return;
		}

		m_type = type;
		
		try {
			Class base = Class.forName(clazz);
			String path = base.getSimpleName();
			InputStream srcin = base.getResourceAsStream(path+"." + type);
			if (srcin == null && "java".equals(type))
				srcin = base.getResourceAsStream(path+".src");

			Label src = new Label("source", StringUtils.load(srcin));
			src.setEscapeModelStrings(false);
			src.add(new SimpleAttributeModifier("class", (m_type.equals("java")?"sh_java":"sh_html")));
			this.add(src);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}	
	}
	

	public void renderHead(IHeaderResponse response) {
		response.renderJavascriptReference(JQuery.getReference());
		Shjs.include(response, "java".equals(m_type), "html".equals(m_type));
		String shscript = JQuery.getOnReadyScript("sh_highlightDocument();");
		response.renderJavascript(shscript, shscript);
	}

}
