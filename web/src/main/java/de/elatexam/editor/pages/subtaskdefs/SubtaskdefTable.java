/*

Copyright (C) 2010 Steffen Dienst

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
package de.elatexam.editor.pages.subtaskdefs;

import java.util.ArrayList;
import java.util.List;

import net.databinder.models.hib.CriteriaFilterAndSort;

import org.apache.wicket.extensions.ajax.markup.html.repeater.data.table.AjaxFallbackDefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeaderlessColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;

import de.elatexam.editor.components.panels.TaskActionsPanel;
import de.elatexam.editor.components.panels.tasks.SortableIdModel;
import de.elatexam.editor.pages.taskdef.TaskSelectionPanel;
import de.elatexam.model.SubTaskDef;

/**
 * @author Steffen Dienst
 *
 */
public class SubtaskdefTable<T extends SubTaskDef> extends Panel {


  public SubtaskdefTable(String id, Class<T> clazz) {
    this(id, clazz, null);
  }

  /**
   * Render subtaskdef selection table, without links to edit and delete but with selection checkboxes.
   *
   * @param id
   * @param clazz
   * @param doSubtaskdefSelection
   */
  public SubtaskdefTable(String id, Class<T> clazz, final TaskSelectionPanel<T> taskSelectionPanel) {
    super(id);


    CriteriaFilterAndSort builder = new CriteriaFilterAndSort(new SubTaskDef() {}, "xmlid", true, false);

    final FilterForm form = new FilterForm("form", builder);
    form.setOutputMarkupId(true);

    add(form);

    final List<IColumn<T>> columns = new ArrayList<IColumn<T>>();

    columns.add(new PropertyColumn<T>(new Model<String>("ID"), "xmlid", "xmlid") {
      @Override
      protected IModel<?> createLabelModel(IModel<T> rowModel) {
        return new SortableIdModel(new PropertyModel<String>(rowModel, "xmlid"));
      }
    });
    columns.add(new LabeledTextFilteredPropertyColumn<T>(Model.of("Aufgabenstellung"), Model.of("enthält:"), "problem", "problem"));

    if (taskSelectionPanel == null) {
      columns.add(new PropertyColumn<T>(new Model<String>("Typ"), "class.simpleName") {
        @Override
        protected IModel<String> createLabelModel(final IModel<T> rowModel) {
          if (rowModel.getObject() == null)
            return Model.of("???");
          else
            return new ResourceModel(rowModel.getObject().getClass().getSimpleName() + ".short");
        }
      });
      // edit links
      columns.add(new HeaderlessColumn<T>() {
        public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> rowModel) {
          cellItem.add(new TaskActionsPanel<T>(componentId, rowModel));
        }
      });
    } else {
      columns.add(new HeaderlessColumn<T>() {
        @Override
        public void populateItem(Item<ICellPopulator<T>> cellItem, String componentId, IModel<T> rowModel) {
          cellItem.add(new AddSubtaskdefPanel<T>(componentId, rowModel, taskSelectionPanel));
        }
      });
    }
    // XXX ugly hack, need to create own data access layer
    final PrivateSubtasksDataProvider<T> provider = new PrivateSubtasksDataProvider<T>(clazz, builder, builder, clazz);
    provider.setWrapWithPropertyModel(false);

    final AjaxFallbackDefaultDataTable<T> table = new AjaxFallbackDefaultDataTable<T>("datatable", columns, provider, 10);

    table.addTopToolbar(new FilterToolbar(table, form, builder));
    form.add(table);
  }

}
