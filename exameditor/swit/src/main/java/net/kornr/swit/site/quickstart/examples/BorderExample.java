package net.kornr.swit.site.quickstart.examples;

import java.awt.Color;
import net.kornr.swit.wicket.border.TableImageBorder;
import net.kornr.swit.wicket.border.graphics.RoundedBorderMaker;
import org.apache.wicket.markup.html.panel.Panel;

public class BorderExample extends Panel 
{
	static private Long s_border = RoundedBorderMaker.register(12, 6f, Color.black, Color.white);

	public BorderExample(String id) 
	{
		super(id);
		this.add(new TableImageBorder("border", s_border, Color.white));
	}

}
