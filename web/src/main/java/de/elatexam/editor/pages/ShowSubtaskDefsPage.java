package de.elatexam.editor.pages;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;

import de.elatexam.editor.pages.subtaskdefs.SubtaskdefTable;
import de.elatexam.model.SubTaskDef;

/**
 * @author sdienst
 */
public class ShowSubtaskDefsPage<T extends SubTaskDef> extends SecurePage {

	private Class<T> clazz;
	private AddSubtaskDefPanel<T> toolbar;
	
	public ShowSubtaskDefsPage() {
		this((Class<T>) SubTaskDef.class);
	}

	// TODO use subtaskdefs from current BasicUser
	@SuppressWarnings("unchecked")
	public ShowSubtaskDefsPage(final Class<T> clazz) {
		this.clazz = clazz;
		add(new Label("heading", "Alle Aufgaben"));
		add(new SubtaskdefTable<T>("table", clazz));
	    // hide link if this is no specific subtask type
	    if (clazz.equals(SubTaskDef.class)) {
	    	toolbar.setVisible(false);
	    }

	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.elatexam.editor.pages.OverviewPage#createToolbar(java.lang.String)
	 */
	@Override
	protected Component createToolbar(final String id) {
		if(this.toolbar ==null)
			this.toolbar = new AddSubtaskDefPanel<T>(id, this);
		return this.toolbar;
	}

	/**
	 * @return
	 */
	Class<T> getClazz() {
		return clazz;
	}
}
