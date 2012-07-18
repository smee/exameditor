package de.elatexam.editor.components.panels;

import java.awt.Dimension;
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
import org.wicketstuff.googlecharts.AbstractChartData;
import org.wicketstuff.googlecharts.Chart;
import org.wicketstuff.googlecharts.ChartDataEncoding;
import org.wicketstuff.googlecharts.ChartProvider;
import org.wicketstuff.googlecharts.ChartType;
import org.wicketstuff.googlecharts.IChartProvider;

import de.elatexam.editor.TaskEditorSession;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.model.AddonSubTaskDef;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.TextSubTaskDef;

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
        item.add(new Label("statvalue", Integer.toString(count(item.getModelObject()))));
      }
    };

    add(container);
    add(container2);
    
    add(new Chart("subtaskchart", createPiechart()));
  }

  /**
   * @return
   */
  private IChartProvider createPiechart() {
    AbstractChartData data = new AbstractChartData() {
      @Override
      public double[][] getData() {
        return new double[][]{{count(McSubTaskDef.class), 
          count(ClozeSubTaskDef.class), 
          count(MappingSubTaskDef.class), 
          count(TextSubTaskDef.class), 
          count(PaintSubTaskDef.class), 
          count(AddonSubTaskDef.class)}};
      }
    };
    data.setEncoding(ChartDataEncoding.EXTENDED);
    String[] labels=new String[]{"Multiple Choice", "Lückentext", "Zuordnung", "Freitext", "Zeichnen", "sonstiges"};
    ChartProvider provider = new ChartProvider(new Dimension(400,200), ChartType.PIE , data);
    provider.setTitle("Eigene Aufgaben");
    provider.setPieLabels(labels);

    return provider;
  }

  protected int count(final Class clazz) {
    final BasicUser user = TaskEditorSession.get().getUser();
    if (clazz.isAssignableFrom(ComplexTaskDef.class)) {
      return user.getTaskdefs().size();
    } else {
      return user.getSubtaskdefsOf(clazz).size();
    }
  }
}
