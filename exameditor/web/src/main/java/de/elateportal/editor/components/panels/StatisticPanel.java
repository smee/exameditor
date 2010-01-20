package de.elateportal.editor.components.panels;

import java.util.ArrayList;
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

import de.elateportal.editor.TaskEditorSession;
import de.elateportal.editor.user.BasicUser;
import de.elateportal.model.AddonSubTaskDef;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
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

    final List<Class> classes = new ArrayList<Class>();
    classes.add(ComplexTaskDef.class);
    classes.add(McSubTaskDef.class);
    classes.add(ClozeSubTaskDef.class);
    classes.add(MappingSubTaskDef.class);
    classes.add(PaintSubTaskDef.class);
    classes.add(TextSubTaskDef.class);
    classes.add(AddonSubTaskDef.class);

    final ListView<Class> container = new AlternatingListView<Class>("globalstatslist", classes) {

      @Override
      protected void populateItem(final ListItem<Class> item) {
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
    final ListView container2 = new AlternatingListView<Class>("privatestatslist", classes) {
      @Override
      protected void populateItem(final ListItem<Class> item) {
        item.add(new Label("statname", new ResourceModel(item.getModelObject().getSimpleName())));
        item.add(new Label("statvalue", count(item.getModelObject())));
      }
    };

    add(container);
    add(container2);
  }

  protected String count(final Class clazz) {
    final BasicUser user = TaskEditorSession.get().getUser();
    if (clazz.isAssignableFrom(ComplexTaskDef.class)) {
      return Integer.toString(user.getTaskdefs().size());
    } else {
      return Integer.toString(user.getSubtaskdefsOf(clazz).size());
    }
  }
}
