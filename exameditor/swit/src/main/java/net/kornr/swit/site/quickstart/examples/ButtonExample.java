package net.kornr.swit.site.quickstart.examples;

import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.VistafarianButton;
import org.apache.wicket.markup.html.panel.Panel;

public class ButtonExample extends Panel 
{
	static private ButtonTemplate s_buttonTemplate = new VistafarianButton();

	public ButtonExample(String id) 
	{
		super(id);
		this.add(ButtonResource.getImage("button", s_buttonTemplate, "Some Fine Text For My Button"));
	}
}
