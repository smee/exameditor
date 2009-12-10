package de.elateportal.editor.pages;

import java.util.ArrayList;
import java.util.List;

import net.databinder.models.hib.CriteriaFilterAndSort;
import net.databinder.models.hib.SortableHibernateProvider;

import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeaderlessColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterForm;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.FilterToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.filter.TextFilteredPropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import de.elateportal.editor.components.panels.TaskActionsPanel;
import de.elateportal.model.SubTaskDefType;

/**
 * @author sdienst
 */
public class ShowSubtaskDefsPage<T extends SubTaskDefType> extends OverviewPage {

	public ShowSubtaskDefsPage() {
		this((Class<T>) SubTaskDefType.class);
	}

	@SuppressWarnings("unchecked")
	public ShowSubtaskDefsPage(final Class<T> clazz) {
		add(new Label("heading", "Alle Aufgaben"));
		final Link<Void> newTaskLink = new Link("newTaskdefLink") {
			@Override
			public void onClick() {
				setResponsePage(new EditSubtaskPage(clazz));
			}
		};
		// hide link if this is no specific subtask type
		if (clazz.equals(SubTaskDefType.class)) {
			newTaskLink.setVisible(false);
		}

		add(newTaskLink);

		CriteriaFilterAndSort builder = new CriteriaFilterAndSort(
		    new SubTaskDefType() {
		}, "id", true, false);
		FilterForm form = new FilterForm("form", builder);
		add(form);

		final SortableHibernateProvider<T> provider = new SortableHibernateProvider<T>(clazz, builder);

		final List<IColumn<T>> columns = new ArrayList<IColumn<T>>();

		columns.add(new PropertyColumn<T>(new Model<String>("ID"), "id", "id"));
		columns.add(new TextFilteredPropertyColumn<T, String>(Model.of("Aufgabenstellung"), "problem", "problem") {
			// columns.add(new PropertyColumn<T>(new Model<String>("Problem"),
			// "problem", "problem") {
			@Override
			public void populateItem(final Item<ICellPopulator<T>> item, final String componentId, final IModel<T> rowModel) {
				// add a label that renders it's html contents
				item.add(new Label(componentId, createLabelModel(rowModel)).setEscapeModelStrings(false));
			}
		});
		columns.add(new PropertyColumn<T>(new Model<String>("Typ"), "class.simpleName") {
			@Override
			protected IModel<String> createLabelModel(final IModel<T> rowModel) {
				return new ResourceModel(rowModel.getObject().getClass().getSimpleName() + ".short");
			}
		});
		// edit links
		if (!clazz.equals(SubTaskDefType.class)) {
			columns.add(new HeaderlessColumn<T>() {

				public void populateItem(final Item<ICellPopulator<T>> cellItem, final String componentId, final IModel<T> rowModel) {
					cellItem.add(new TaskActionsPanel<T>(componentId, rowModel));
				}
			});
		}
		DefaultDataTable<T> table = new DefaultDataTable<T>("datatable", columns, provider, 10);
		table.addTopToolbar(new FilterToolbar(table, form, builder));
		form.add(table);
	}
}
