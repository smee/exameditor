package de.elateportal.editor.components.listeditor;

public class RemoveButton extends EditorButton {

    public RemoveButton(final String id) {
        super(id);
        setDefaultFormProcessing(false);
    }

    @Override
    public boolean isEnabled() {
        return getEditor().checkRemove(getItem());
    }

    @Override
    public void onSubmit() {
        final int idx = getItem().getIndex();

        for (int i = idx + 1; i < getItem().getParent().size(); i++) {
            final ListItem<?> item = (ListItem<?>) getItem().getParent().get(i);
            item.setIndex(item.getIndex() - 1);
        }

        getList().remove(idx);
        getEditor().remove(getItem());
    }
}
