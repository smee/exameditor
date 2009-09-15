package de.elateportal.editor.pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.databinder.components.DataStyleLink;

import org.apache.wicket.markup.html.WebPage;

import de.elateportal.editor.components.menu.ChromeMenu;
import de.elateportal.editor.components.menu.LinkVO;

public class OverviewPage extends WebPage {

    /** Add components to be displayed on page. */
    public OverviewPage() {
        add(new DataStyleLink("css"));

        add(new ChromeMenu("menubar", getMenuList(), ChromeMenu.Theme.THEME1));

    }

    /**
     * @return
     */
    private List<List<LinkVO>> getMenuList() {
        final List<List<LinkVO>> res = new LinkedList<List<LinkVO>>();
        res.add(Arrays.asList(new LinkVO(ShowStuffPage.class, "Alle Aufgaben")));
        res.add(Arrays.asList(new LinkVO(StatisticPage.class, "Statistiken")));
        return res;
    }
}
