package de.elateportal.editor.pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.wicket.markup.html.WebPage;

import de.elateportal.editor.components.menu.ChromeMenu;
import de.elateportal.editor.components.menu.LinkVO;
import de.elateportal.editor.components.menu.LinkVO.Create;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.TextSubTaskDef;

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
}
