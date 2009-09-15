package de.elateportal.editor.pages;

import java.util.Arrays;
import java.util.List;

import net.databinder.models.hib.CriteriaBuilder;
import net.databinder.models.hib.HibernateObjectModel;

import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.ResourceModel;
import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.MappingSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.McSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.PaintSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.complextaskdef.TextSubTaskDef;

/**
 * @author sdienst
 */
public class StatisticPage extends OverviewPage {
    public StatisticPage() {
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
