package de.elateportal.editor.pages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.Model;
import org.codesmell.wicket.cssmenu.Constants;
import org.codesmell.wicket.cssmenu.LabeledMenuItem;
import org.codesmell.wicket.cssmenu.MenuItem;
import org.codesmell.wicket.cssmenu.quickmenu.dropshadow.QuickMenu;

import de.elateportal.editor.components.menu.LinkVO;
import de.elateportal.editor.components.menu.LinkVO.Create;
import de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.MappingSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.McSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.PaintSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.TextSubTaskDef;

public class OverviewPage extends WebPage {

    /** Add components to be displayed on page. */
    public OverviewPage() {
        // add(new DataStyleLink("css"));

        // add(new ChromeMenu("menubar", getMenuList(), ChromeMenu.Theme.THEME1));
        add(new QuickMenu("menubar", getQuickMenuItemsList()));
    }

    /**
     * @return
     */
    private List<List<LinkVO>> getMenuList() {
        final List<List<LinkVO>> res = new LinkedList<List<LinkVO>>();
        // must not create the link result page now,
        // leads to StackoverflowError in case of resultpage extends OverviewPage

        res.add(Arrays.asList(new LinkVO(ShowSubtaskDefsPage.class, "Alle Aufgaben"), new LinkVO(new Create() {
                public WebPage createPage() {
                return new ShowSubtaskDefsPage(McSubTaskDef.class);
                }
                        }, "Alle MC-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowSubtaskDefsPage(ClozeSubTaskDef.class);
                }
                        }, "Alle Lückentext-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowSubtaskDefsPage(TextSubTaskDef.class);
                }
                        }, "Alle Text-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowSubtaskDefsPage(PaintSubTaskDef.class);
                }
                        }, "Alle Zeichen-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowSubtaskDefsPage(MappingSubTaskDef.class);
                }
                        }, "Alle Zuordnungs-Aufgaben")));
        res.add(Arrays.asList(new LinkVO(StatisticPage.class, "Statistiken")));
        res.add(Arrays.asList(new LinkVO(UploadComplexTaskdefPage.class, "Importieren")));
        return res;
    }

    private List<MenuItem> getQuickMenuItemsList() {
        final List<MenuItem> items = new ArrayList<MenuItem>();
        final MenuItem tl1 = new LabeledMenuItem(new Link(Constants.LINK_ID) {

            @Override
            public void onClick() {
                setResponsePage(ShowSubtaskDefsPage.class);
                }
                        }, Model.of("Alle Aufgaben"));
        final MenuItem tl2 = new LabeledMenuItem(new Link(Constants.LINK_ID) {

            @Override
            public void onClick() {
                setResponsePage(StatisticPage.class);
                }
                        }, Model.of("Statistik"));
        final MenuItem tl3 = new LabeledMenuItem(new Link(Constants.LINK_ID) {

            @Override
            public void onClick() {
                setResponsePage(UploadComplexTaskdefPage.class);
                }
                        }, Model.of("Statistik"));

        tl1.addMenuItem(new LabeledMenuItem(new Link(Constants.LINK_ID) {

            @Override
            public void onClick() {
                setResponsePage(new ShowSubtaskDefsPage(McSubTaskDef.class));
                }
                        }, Model.of("Alle MC-Aufgaben")));
        items.add(tl1);
        items.add(tl2);
        items.add(tl3);
        return items;
    }
}
