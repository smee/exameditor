package de.elateportal.editor.components.menu;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.html.resources.CompressedResourceReference;
import org.apache.wicket.model.Model;

/**
 * Simple menu bar based on
 * http://www.dynamicdrive.com/dynamicindex1/chrome/index.htm.
 */
public class ChromeMenu extends Panel implements IHeaderContributor {

    public enum Theme {
        THEME1, THEME2, THEME3, THEME4;

        /**
         * @return
         */
        public ResourceReference getResourceReference() {
            return new CompressedResourceReference(ChromeMenu.class, String.format("css/%s.css", this.name().toLowerCase()));
        }
    }

    private ResourceReference CSS_REFERENCE;

    /**
     * 
     * @param id
     * @param listOfMenus
     */
    public ChromeMenu(final String id, final List<List<LinkVO>> listOfMenus, final Theme cssTheme) {
        super(id);

        if (cssTheme == null) {
            // set default theme
            CSS_REFERENCE = Theme.THEME1.getResourceReference();
        } else {
            CSS_REFERENCE = cssTheme.getResourceReference();
        }

        final ListView<List<LinkVO>> menuView = new ListView<List<LinkVO>>("menuLinkList", listOfMenus) {
            int itemCount = 0;

            @Override
            public void populateItem(final ListItem<List<LinkVO>> item) {

                final LinkVO linkInfo = (item.getModelObject()).get(0);

                item.add(createLink(linkInfo, itemCount, item.getModelObject().size() > 1));
                itemCount++;
            }
        };
        menuView.setReuseItems(true);
        add(menuView);

        final ListView<List<LinkVO>> submenuListView = new ListView<List<LinkVO>>("submenuList", listOfMenus) {
            int submenuCounter = 0;

            @Override
            public void populateItem(final ListItem<List<LinkVO>> item) {
                final List<LinkVO> linkInfoList = item.getModelObject();
                final List<LinkVO> topMenuRemovedList = linkInfoList.subList(1, linkInfoList.size());

                final WebMarkupContainer submenuDiv = new WebMarkupContainer("submenuDiv");
                submenuDiv.add(new AttributeModifier("id", true, new Model("dropmenu" + submenuCounter)));

                final ListView<LinkVO> submenuItem = new ListView<LinkVO>("submenuItem", topMenuRemovedList) {
                    @Override
                    public void populateItem(final ListItem<LinkVO> item) {

                        final LinkVO linkInfo = item.getModelObject();
                        item.add(createLink(linkInfo, submenuCounter, true));
                        item.setRenderBodyOnly(true);
                    }
                };

                submenuDiv.add(submenuItem);
                item.add(submenuDiv);
                item.setRenderBodyOnly(true);
                submenuCounter++;

            }
        };
        submenuListView.setReuseItems(true);
        add(submenuListView);
        setRenderBodyOnly(true);
    }

    private AbstractLink createLink(final LinkVO linkInfo, final int itemCount, final boolean hasOrIsChildren) {
        AbstractLink link = null;
        if (linkInfo.getExternalUrl() != null) {
            link = new ExternalLink("menuLink", Model.of(linkInfo.getExternalUrl()));
        } else {
            link = new Link("menuLink") {
                @Override
                public void onClick() {
                    setResponsePage(linkInfo.getResponsePage());
                }
            };
        }
        if (hasOrIsChildren) {
            link.add(new AttributeModifier("rel", true, Model.of("dropmenu" + itemCount)));
        }

        final Label linkLabel = new Label("linkText", linkInfo.getLinkText());
        linkLabel.setRenderBodyOnly(true);
        link.add(linkLabel);
        return link;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.apache.wicket.markup.html.IHeaderContributor#renderHead(org.apache
     * .wicket.markup.html.IHeaderResponse)
     */
    public void renderHead(final IHeaderResponse response) {
        response.renderJavascriptReference(new CompressedResourceReference(ChromeMenu.class, "js/chrome.js"));
        response.renderCSSReference(CSS_REFERENCE);
    }

}
