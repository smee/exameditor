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

import de.thorstenberger.taskmodel.complex.complextaskdef.ClozeSubTaskDef;

/**
 * @author sdienst
 */
public class ShowStuffPage extends OverviewPage {
    public class InputForm extends DataForm<ClozeSubTaskDef> {
        public InputForm(final String id) {
            super(id, ClozeSubTaskDef.class);

            add(new TextField("problem"));
        }

        @Override
        protected void onSubmit() {
            super.onSubmit();
            clearPersistentObject();
        }
    }

    public ShowStuffPage() {
        add(new InputForm("input"));

        final IDataProvider<ClozeSubTaskDef> provider = new HibernateProvider<ClozeSubTaskDef>(ClozeSubTaskDef.class);

        final DataView<ClozeSubTaskDef> data = new DataView<ClozeSubTaskDef>("data", provider) {
            @Override
            protected void populateItem(final Item<ClozeSubTaskDef> item) {
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
