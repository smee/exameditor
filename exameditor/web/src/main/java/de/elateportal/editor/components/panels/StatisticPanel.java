package de.elateportal.editor.components.panels;

import java.util.Arrays;
import java.util.List;

import net.databinder.models.hib.CriteriaBuilder;
import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.SubTaskDefType;
import de.elateportal.model.TextSubTaskDef;

/**
 * @author sdienst
 */
public class StatisticPanel extends Panel {

    /**
     * @param id
     */
    public StatisticPanel(final String id) {
        super(id);
        final List<Class<? extends SubTaskDefType>> classes = Arrays.asList(McSubTaskDef.class, ClozeSubTaskDef.class,
                MappingSubTaskDef.class, PaintSubTaskDef.class, TextSubTaskDef.class);

        final ListView container = new ListView<Class<? extends SubTaskDefType>>("statslist", classes) {

            @Override
            protected void populateItem(final ListItem<Class<? extends SubTaskDefType>> item) {
                // new model that queries the number of rows in a table
                final HibernateObjectModel<Integer> model = new HibernateObjectModel<Integer>(item.getModelObject(),
                        new CriteriaBuilder() {

                    public void build(final Criteria criteria) {
                                                // return "count(*)"
                        criteria.setProjection(Projections.rowCount());
                        }
                                        });
                item.add(new Label("statname", new ResourceModel(item.getModelObject().getSimpleName())));
                item.add(new Label("statvalue", model));
            }
        };

        add(container);
    }
}
