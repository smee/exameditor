package de.elatexam.editor.components.panels;

import net.databinder.components.NullPlug;
import net.databinder.hib.Databinder;
import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import com.visural.wicket.component.confirmer.ConfirmerLink;

import de.elatexam.editor.TaskEditorSession;
import de.elatexam.editor.components.GermanConfirmerLink;
import de.elatexam.editor.components.panels.tasks.PreviewSubtaskDefPanel;
import de.elatexam.editor.components.panels.tree.ComplexTaskDefTree;
import de.elatexam.editor.pages.EditSubtaskPage;
import de.elatexam.model.SubTaskDef;

/**
 * Small panel with two links: edit and delete
 *
 * @author sdienst
 */
public class TaskActionsPanel<T extends SubTaskDef> extends Panel {

    /**
     * @param id
     * @param model
     */
    public TaskActionsPanel(final String id, final IModel<T> model) {
        super(id);
        add(new Link<T>("edit", model) {

            @Override
            public void onClick() {
                final T modelObject = getModelObject();
                setResponsePage(new EditSubtaskPage(modelObject.getClass(), (HibernateObjectModel) getModel()));
            }

        });
        ConfirmerLink removeLink = new GermanConfirmerLink("remove") {

            @Override
            public void onClick() {
                final Object modelObject =  getModelObject();
                // remove subtaskdef from current user object
                TaskEditorSession.get().getUser().getSubtaskdefs().remove(modelObject);
                // TODO remove from taskblock iff this subtaskdef is used in a complextaskdef
                final Session session = Databinder.getHibernateSession();
                final Transaction trans = session.beginTransaction();
                session.delete(modelObject);
                trans.commit();
            }

        }.setMessageContentHTML("Sind Sie sicher, dass das selektierte Element gel&ouml;scht werden soll?");
        removeLink.setModel(model);
        add(removeLink);
        
        final ModalWindow previewWindow = new ModalWindow("previewModal");
        previewWindow.setTitle("Aufgabenvorschau");
        previewWindow.setResizable(true);
        previewWindow.setInitialWidth(650);
        previewWindow.setInitialHeight(450);
        previewWindow.setContent(new NullPlug(previewWindow.getContentId()));
        add(previewWindow);
        
        AjaxLink<T> previewLink = new AjaxLink<T>("preview") {
			@Override
			public void onClick(AjaxRequestTarget target) {
				IModel<T> m = getModel();
				previewWindow.setContent(new PreviewSubtaskDefPanel<T>(previewWindow.getContentId(), m));
				previewWindow.show(target);
			}
		};
		previewLink.setModel(model);
		add(previewLink);
    }

}
