package de.elateportal.editor.pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;

import de.elateportal.editor.components.menu.ChromeMenu;
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

        add(new ChromeMenu("menubar", getMenuList(), ChromeMenu.Theme.THEME1));
    }

    /**
     * @return
     */
    private List<List<LinkVO>> getMenuList() {
        final List<List<LinkVO>> res = new LinkedList<List<LinkVO>>();
        // must not create the link result page now,
        // leads to StackoverflowError in case of resultpage extends OverviewPage

        res.add(Arrays.asList(new LinkVO(ShowStuffPage.class, "Alle Aufgaben"), new LinkVO(new Create() {
                public WebPage createPage() {
                return new ShowStuffPage(McSubTaskDef.class);
                }
                        }, "Alle MC-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowStuffPage(ClozeSubTaskDef.class);
                }
                        }, "Alle Lückentext-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowStuffPage(TextSubTaskDef.class);
                }
                        }, "Alle Text-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowStuffPage(PaintSubTaskDef.class);
                }
                        }, "Alle Zeichen-Aufgaben"), new LinkVO(new Create() {

            public WebPage createPage() {
                return new ShowStuffPage(MappingSubTaskDef.class);
                }
                        }, "Alle Zuordnungs-Aufgaben")));
        res.add(Arrays.asList(new LinkVO(StatisticPage.class, "Statistiken")));
        return res;
    }
}
