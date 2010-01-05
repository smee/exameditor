package net.kornr.swit.site.quickstart.examples;

import java.awt.Color;
import net.kornr.swit.wicket.layout.ColumnPanel;
import net.kornr.swit.wicket.layout.LayoutInfo;
import net.kornr.swit.wicket.layout.ThreeColumnsLayoutManager;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutResource;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

public class ThreeColumnExample2 extends Panel 
{
	static private LayoutInfo s_layout = new LayoutInfo(LayoutInfo.UNIT_PERCENTAGE, 33, 0);
	static {
		ThreeColumnsLayoutResource.register(s_layout);
		s_layout.setLeftColor(Color.yellow);
		s_layout.setMiddleColor(Color.lightGray);
	}
	
	public ThreeColumnExample2(String id) {
		super(id);

		ThreeColumnsLayoutManager layoutmanager = new ThreeColumnsLayoutManager("layout", s_layout);
        this.add(layoutmanager);
        
        ColumnPanel leftcolumn = layoutmanager.getLeftColumn();
        leftcolumn.addContent(new Label(leftcolumn.getContentId(), "This left side should be yellow and take 33% of the space"));
	}

}
