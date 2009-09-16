package de.elateportal.editor.pages;

import de.elateportal.editor.components.panels.StatisticPanel;

/**
 * @author sdienst
 */
public class StatisticPage extends OverviewPage {
    public StatisticPage() {
        add(new StatisticPanel("stats"));
    }
}
