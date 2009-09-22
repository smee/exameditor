package de.elateportal.editor.components.panels;

import net.databinder.hib.Databinder;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import de.elateportal.editor.pages.EditSubtaskPage;
import de.elateportal.model.SubTaskDefType;

/**
 * Small panel with two links: edit and delete
 * 
 * @author sdienst
 */
public class TaskActionsPanel<T extends SubTaskDefType> extends Panel {

    /**
     * @param id
     * @param model
     */
    public TaskActionsPanel(final String id, final IModel<T> model) {
        super(id, model);
        add(new Link("edit") {

            @Override
            public void onClick() {
                final T modelObject = (T) getParent().getDefaultModelObject();
                setResponsePage(new EditSubtaskPage(modelObject.getClass(), modelObject));
            }

        });
        add(new Link("remove") {

            @Override
            public void onClick() {
                final T modelObject = (T) getParent().getDefaultModelObject();
                final Session session = Databinder.getHibernateSession();
                final Transaction trans = session.beginTransaction();
                session.delete(modelObject);
                trans.commit();
            }

        });
    }

}
