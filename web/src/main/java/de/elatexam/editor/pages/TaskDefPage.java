/*

Copyright (C) 2009 Steffen Dienst

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package de.elatexam.editor.pages;

import java.util.List;

import net.databinder.models.hib.HibernateListModel;
import net.databinder.models.hib.HibernateObjectModel;
import net.databinder.models.hib.QueryBuilder;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.CSSPackageResource;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.hibernate.Query;
import org.hibernate.Session;

import wicketdnd.theme.WebTheme;
import de.elatexam.editor.TaskEditorApplication;
import de.elatexam.editor.TaskEditorSession;
import de.elatexam.editor.components.event.AjaxUpdateEvent;
import de.elatexam.editor.components.event.AjaxUpdateEvent.IAjaxUpdateListener;
import de.elatexam.editor.components.panels.tasks.CategoryPanel;
import de.elatexam.editor.components.panels.tasks.ComplexTaskdefPanel;
import de.elatexam.editor.components.panels.tasks.SubtaskDefInputPanel;
import de.elatexam.editor.components.panels.tasks.TaskBlockConfigPanel;
import de.elatexam.editor.components.panels.tree.ComplexTaskDefTree;
import de.elatexam.editor.components.panels.tree.ComplexTaskdefTreeProvider;
import de.elatexam.editor.components.panels.tree.TreeSelectionEvent;
import de.elatexam.editor.pages.taskdef.TaskDefActions;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.RemoveNullResultTransformer;
import de.elatexam.model.Category;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.SubTaskDef;
import de.elatexam.model.TaskBlock;
import de.elatexam.model.TaskblockConfig;

/**
 * @author Steffen Dienst
 *
 */
public class TaskDefPage extends SecurePage implements IAjaxUpdateListener{

    private Panel editPanel;
    private ComplexTaskDefTree tree;
    TaskDefActions taskdefactions;

    public TaskDefPage() {
        super();
        // add drag-n-drop theme
        add(CSSPackageResource.getHeaderContribution(new WebTheme()));
        add(tree = getTree());
        editPanel = new EmptyPanel("editpanel");
        add(editPanel.setOutputMarkupId(true));
    }


	/**
	 * @param tasklistmodel
	 * @return
	 */
	ComplexTaskDefTree getTree() {
		if(tree == null){
	        @SuppressWarnings("unchecked")
			IModel<List<?>> tasklistmodel = new HibernateListModel(new QueryBuilder() {
	            public Query build(final Session sess) {
	                final Query q = sess.createQuery(String.format("select tasks from BasicUser u left join u.taskdefs tasks where u.username='%s'",
	                        TaskEditorSession.get().getUser().getUsername()));
	                q.setResultTransformer(RemoveNullResultTransformer.INSTANCE);
	                return q;
	            }
	        });
	        // the admin sees all taskdefs
	        if (TaskEditorApplication.isAdmin()) {
	            tasklistmodel = new HibernateListModel(BasicUser.class);
	        }
	        tree = new ComplexTaskDefTree("tree", new ComplexTaskdefTreeProvider(tasklistmodel));
		}
		return tree;
	}


    private void replaceEditPanelWith(final AjaxRequestTarget target, final Panel edit) {
        edit.setOutputMarkupId(true);
        editPanel.replaceWith(edit);
        editPanel = edit;
        target.addComponent(editPanel);
    }

    /*
     * (non-Javadoc)
     *
     * @see de.elatexam.editor.pages.OverviewPage#createToolbar(java.lang.String)
     */
    @Override
    protected Component createToolbar(final String id) {
        return new TaskDefActions(id, getTree());
    }


	/* (non-Javadoc)
	 * @see de.elatexam.editor.components.event.AjaxUpdateEvent.IAjaxUpdateListener#notifyAjaxUpdate(de.elatexam.editor.components.event.AjaxUpdateEvent)
	 */
	@Override
	public void notifyAjaxUpdate(AjaxUpdateEvent event) {
		if(event instanceof TreeSelectionEvent){
			IModel<?> selectedModel = ((TreeSelectionEvent) event).getSelectedModel();
			AjaxRequestTarget target = event.getTarget();
			renderPanelFor(selectedModel, target);
		}
	}
    /**
     * Replace right hand form panel with an edit panel for the given model object.
     *
     * @param t
     * @param target
     */
    private void renderPanelFor(final IModel<?> selectedModel, final AjaxRequestTarget target) {
        if(selectedModel !=null){
	    	final Object t = selectedModel.getObject();
	        if (t instanceof ComplexTaskDef) {
	            replaceEditPanelWith(target, new ComplexTaskdefPanel("editpanel", (HibernateObjectModel<ComplexTaskDef>) selectedModel));
	        } else if (t instanceof Category) {
	            replaceEditPanelWith(target, new CategoryPanel("editpanel", (HibernateObjectModel<Category>) selectedModel));
	        } else if (t instanceof TaskBlock) {
	            // TODO render panels for taskblock subtypes, not just generic block config
	            replaceEditPanelWith(target, new TaskBlockConfigPanel("editpanel", new HibernateObjectModel<TaskblockConfig>(TaskblockConfig.class, ((TaskBlock) selectedModel.getObject()).getConfig().getHjid())));
	        } else if (t instanceof SubTaskDef) {
	            final SubTaskDef st = (SubTaskDef) t;
	            replaceEditPanelWith(target, new SubtaskDefInputPanel("editpanel", (HibernateObjectModel<SubTaskDef>) selectedModel));
	        }
        }else{
			replaceEditPanelWith(target, new EmptyPanel("editpanel"));
        }
    }
}
