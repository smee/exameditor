package de.elatexam.editor.components.panels.tasks.mapping;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxFallbackLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.MappingSubTaskDef.Assignment;
import de.elatexam.model.MappingSubTaskDef.Concept;
import de.elatexam.editor.components.event.AjaxUpdateEvent;
import de.elatexam.editor.components.listeditor.ListEditor;
import de.elatexam.editor.components.listeditor.ListItem;
import de.elatexam.editor.components.listeditor.RemoveButton;
import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;

/**
 * @author Steffen Dienst
 * 
 */
public class MappingSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<MappingSubTaskDef> {
  private final WebMarkupContainer assignmentContainer, conceptContainer;

  public MappingSubtaskDefInputPanel(final String id, final IModel<MappingSubTaskDef> model) {
    super(id, model);
    // assignments
    assignmentContainer = new WebMarkupContainer("assignementContainer");
    final ListEditor<Assignment> assignmentsList = new ListEditor<Assignment>("assignments", new PropertyModel<List<Assignment>>(
        model, "assignment")) {

      @Override
      protected void onPopulateItem(final ListItem<Assignment> item) {
        item.add(new TextField("id", new PropertyModel(item.getModel().getObject(), "id")));
        item.add(new TextField("name", new PropertyModel(item.getModel().getObject(), "name")));
        item.add(new RemoveButton("delete"));
      }

    };
    final Link addAssignement = new AjaxFallbackLink("addassignment") {
      @Override
      public void onClick(final AjaxRequestTarget target) {
        assignmentsList.addItem(new Assignment());
        if (target != null) {
          target.addComponent(assignmentContainer);
          new AjaxUpdateEvent(this, target).fire();
        }
      }
    };
    add(addAssignement);

    assignmentContainer.setOutputMarkupId(true);
    assignmentContainer.add(assignmentsList);
    add(assignmentContainer);

    conceptContainer = new WebMarkupContainer("conceptContainer");
    final ListEditor<Concept> conceptList = new ListEditor<Concept>("concepts",
        new PropertyModel<List<Concept>>(model, "concept")) {

      @Override
      protected void onPopulateItem(final ListItem<Concept> item) {
        item.add(new TextField<String>("name", new PropertyModel<String>(item.getModel().getObject(), "name")).setEscapeModelStrings(false));
        // use getCorrectAssignmentIDItems instead of correctAssignmentID,
        // the former is jpa specific, the later jaxb.... they don't get synchronized
        // #$%##%!!
        item.add(new TextField<String>("correctAssignmentID", new PropertyModel<String>(item.getModel(), "correctAssignmentIDItems")) {
          @Override
          public IConverter getConverter(final Class<?> type) {
            if (List.class.isAssignableFrom(type)) {
              return new CorrectAssignementConverter();
            } else {
              return super.getConverter(type);
            }
          }
        });
        item.add(new RemoveButton("delete"));
      }

    };
    add(conceptList);
    final Link addConcept = new AjaxFallbackLink("addconcept") {
      @Override
      public void onClick(final AjaxRequestTarget target) {
        conceptList.addItem(new Concept());
        if (target != null) {
          target.addComponent(conceptContainer);
          new AjaxUpdateEvent(this, target).fire();
        }
      }
    };
    add(addConcept);

    conceptContainer.setOutputMarkupId(true);
    conceptContainer.add(conceptList);
    add(conceptContainer);
  }
}
