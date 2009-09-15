package de.elateportal.editor.pages;

import net.databinder.models.hib.HibernateProvider;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import de.elateportal.editor.components.forms.SubtaskDefInputPanel;
import de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefType;

/**
 * @author sdienst
 */
public class ShowStuffPage extends OverviewPage {

    public ShowStuffPage() {
        add(new SubtaskDefInputPanel("input", ClozeSubTaskDef.class));

        final IDataProvider<SubTaskDefType> provider = new HibernateProvider<SubTaskDefType>(SubTaskDefType.class);

        final IColumn[] columns = new IColumn[3];

        columns[0] = new PropertyColumn(new Model<String>("ID"), "id");
        columns[1] = new PropertyColumn(new Model<String>("Problem"),
                "problem");
        columns[2] = new PropertyColumn(new Model<String>("Aufgabentyp"),
                "class.simpleName") {
            @Override
            protected IModel createLabelModel(final IModel rowModel) {

                return new ResourceModel(rowModel.getObject().getClass().getSimpleName() + ".short");
            }
        };

        final DataTable table = new DataTable("datatable", columns, provider, 10);
        table.addBottomToolbar(new NavigationToolbar(table));
        table.addTopToolbar(new HeadersToolbar(table, null));
        add(table);
    }
}
