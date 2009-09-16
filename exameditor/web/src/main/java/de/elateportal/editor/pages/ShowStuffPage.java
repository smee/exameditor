package de.elateportal.editor.pages;

import net.databinder.models.hib.HibernateProvider;

import org.apache.wicket.Component;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.ResourceModel;

import de.elateportal.editor.components.panels.tasks.SubtaskDefInputPanel;
import de.elateportal.editor.components.panels.tasks.TextSubtaskDefInputPanel;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefType;
import de.thorstenberger.taskmodel.complex.complextaskdef.TextSubTaskDef;

/**
 * @author sdienst
 */
public class ShowStuffPage extends OverviewPage {

    public ShowStuffPage() {
        this(SubTaskDefType.class);
    }

    public ShowStuffPage(final Class<? extends SubTaskDefType> clazz) {
        if (clazz.equals(SubTaskDefType.class)) {
            add(new EmptyPanel("input"));
        } else {
            add(createInputPanelFor(clazz));
        }

        final IDataProvider<SubTaskDefType> provider = new HibernateProvider<SubTaskDefType>(
                clazz);

        final IColumn[] columns = new IColumn[3];

        columns[0] = new PropertyColumn(new Model<String>("ID"), "id");
        columns[1] = new PropertyColumn(new Model<String>("Problem"), "problem");
        columns[2] = new PropertyColumn(new Model<String>("Aufgabentyp"), "class.simpleName") {
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

    private Component createInputPanelFor(final Class<? extends SubTaskDefType> clazz) {
        final String ID = "input";

        if (clazz == TextSubTaskDef.class) {
            return new TextSubtaskDefInputPanel(ID);
        } else {
            return new SubtaskDefInputPanel(ID, clazz);
        }
    }
}
