package de.elateportal.editor.components.listeditor;

import java.util.Collections;

import org.apache.wicket.ResourceReference;

public class MoveUpButton extends EditorButton {

    public MoveUpButton(final String id) {
        super(id, new ResourceReference(MoveUpButton.class, "images/up.png"));
        setDefaultFormProcessing(false);
    }

    @Override
    public boolean isEnabled() {
        return getEditor().canMoveUp(getItem());
    }

    @Override
    public void onSubmit() {
        final int idx = getItem().getIndex();
        final int itemCount = getItem().getParent().size();

        if (idx > 0 && itemCount > 1) {
            Collections.swap(getList(), idx, idx - 1);
            System.out.println(getList());
        }
    }
}
