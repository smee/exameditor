package de.elatexam.editor.components.listeditor;

import java.util.Iterator;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.ImageButton;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.request.resource.PackageResourceReference;
import org.apache.wicket.request.resource.ResourceReference;

public abstract class EditorButton extends ImageButton {
    private transient ListItem<?> parent;

    public EditorButton(final String id) {
        this(id, new PackageResourceReference(EditorButton.class, "images/delete.png"));
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

		/**
     * Reset raw input of {@link FormComponent} children of {@link ListItem}s.
     * @param idx
     */
    protected void resetInputFields(int... idx) {
    	for (int i : idx) {
    		ListItem li = (ListItem) getEditor().get(i);
    		for (Iterator<Component> it = (Iterator<Component>) li.iterator(); it.hasNext();) {
    			Component c = it.next();
    			if (c instanceof FormComponent)
    				((FormComponent) c).modelChanged();
    		}
    	}
    
    }

}