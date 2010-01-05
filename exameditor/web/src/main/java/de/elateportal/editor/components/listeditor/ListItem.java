package de.elateportal.editor.components.listeditor;

import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.model.AbstractReadOnlyModel;

public class ListItem<T> extends Item<T> {
    private class ListItemModel extends AbstractReadOnlyModel<T> {
        @SuppressWarnings("unchecked")
        @Override
        public T getObject() {
            return ((ListEditor<T>) ListItem.this.getParent()).items.get(getIndex());
        }
    }

    public ListItem(final String id, final int index) {
        super(id, index);
        setModel(new ListItemModel());
    }
}
