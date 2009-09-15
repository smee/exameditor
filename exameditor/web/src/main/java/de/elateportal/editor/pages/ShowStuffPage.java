package de.elateportal.editor.pages;

import net.databinder.components.hib.DataForm;
import net.databinder.models.hib.HibernateProvider;

import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.HeadersToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.data.DataView;
import org.apache.wicket.markup.repeater.data.IDataProvider;
import org.apache.wicket.model.Model;

import de.elateportal.editor.components.forms.SubtaskDefInputPanel;
import de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef;
import de.thorstenberger.taskmodel.complex.complextaskdef.SubTaskDefType;

/**
 * @author sdienst
 */
public class ShowStuffPage extends OverviewPage {

    public class InputForm<T extends SubTaskDefType> extends DataForm<T> {
        public InputForm(final String id, final Class<T> clazz) {
            super(id, clazz);

            add(new TextField("problem"));
        }

        @Override
        protected void onSubmit() {
            super.onSubmit();
            clearPersistentObject();
        }
    }

    public ShowStuffPage() {
        add(new SubtaskDefInputPanel("input", ClozeSubTaskDef.class));

        final IDataProvider<SubTaskDefType> provider = new HibernateProvider<SubTaskDefType>(SubTaskDefType.class);

        final DataView<SubTaskDefType> data = new DataView<SubTaskDefType>("data", provider) {
            @Override
            protected void populateItem(final Item<SubTaskDefType> item) {
                item.add(new Label("problem"));
            }
        };
        add(data);
        data.setVisible(false);

        final IColumn[] columns = new IColumn[2];

        columns[0] = new PropertyColumn(new Model<String>("ID"), "id");
        columns[1] = new PropertyColumn(new Model<String>("Problem"),
                "problem");

        final DataTable table = new DataTable("datatable", columns, provider,
                10);
        table.addBottomToolbar(new NavigationToolbar(table));
        table.addTopToolbar(new HeadersToolbar(table, null));
        add(table);
    }
}
