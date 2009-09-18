package de.elateportal.editor.pages;

import java.awt.Color;

import net.kornr.swit.wicket.layout.ColumnPanel;
import net.kornr.swit.wicket.layout.LayoutInfo;
import net.kornr.swit.wicket.layout.ThreeColumnsLayoutManager;
import net.kornr.swit.wicket.layout.threecol.ThreeColumnsLayoutResource;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

import de.elateportal.editor.pages.OverviewPage;

/**
 * @author sdienst
 */
public class TwoColumnLayoutTest extends Panel {
    static private LayoutInfo s_layout = new LayoutInfo(LayoutInfo.UNIT_PERCENTAGE, 25, 25);
    static {
        ThreeColumnsLayoutResource.register(s_layout);
        s_layout.setMiddleColor(new Color(0xFE, 0xFE, 0xFE));
    }

    public TwoColumnLayoutTest(final String id) {
        super(id);

        final ThreeColumnsLayoutManager layoutmanager = new ThreeColumnsLayoutManager("layout", s_layout);
        add(layoutmanager);

        final ColumnPanel leftcolumn = layoutmanager.getLeftColumn();
        leftcolumn.addContent(new Label(leftcolumn.getContentId(),
                "Any component can be added on the sides, not just panels"));

        layoutmanager.add(new Link("linkToHome") {

            @Override
            public void onClick() {
                setResponsePage(OverviewPage.class);
            }

        });
    }

}
