package de.elateportal.editor.components.listeditor;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ResourceReference;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.model.AbstractReadOnlyModel;

public abstract class EditorButton extends ImageButton {
    private transient ListItem<?> parent;

    public EditorButton(final String id) {
        this(id, new ResourceReference(EditorButton.class, "images/delete.png"));
    }

    public EditorButton(final String id, final ResourceReference imgRef) {
        super(id, imgRef);
        setDefaultFormProcessing(false);

        add(new AttributeModifier("class", true, new AbstractReadOnlyModel() {
            @Override
            public Object getObject() {
                return isEnabled() ? null : "disabled-image";
            }
        }));
    }

    protected final ListEditor<?> getEditor() {
        return (ListEditor<?>) getItem().getParent();
    }

    protected final ListItem<?> getItem() {
        if (parent == null) {
            parent = findParent(ListItem.class);
        }
        return parent;
    }

    protected final List<?> getList() {
        return getEditor().items;
    }

    @Override
    protected void onDetach() {
        parent = null;
        super.onDetach();
    }

}