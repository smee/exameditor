package de.elatexam.editor.pages;

import de.elatexam.editor.components.panels.StatisticPanel;

/**
 * @author sdienst
 */
public class StatisticPage extends SecurePage {
	public StatisticPage() {
		add(new StatisticPanel("stats"));
	}
}
