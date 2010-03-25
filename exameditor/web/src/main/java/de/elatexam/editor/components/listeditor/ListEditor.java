package de.elatexam.editor.components.listeditor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;

public abstract class ListEditor<T> extends RepeatingView implements IFormModelUpdateListener {
	protected List<T> items;

	public ListEditor(final String id, final IModel<List<T>> model) {
		super(id, model);
	}

	public void addItem(final T value) {
		items.add(value);
		final ListItem<T> item = new ListItem<T>(newChildId(), items.size() - 1);
		add(item);
		onPopulateItem(item);
	}

	public boolean canMoveDown(final ListItem<?> item) {
		return item.getIndex() < items.size() - 1;
	}

	public boolean canMoveUp(final ListItem<?> item) {
		return item.getIndex() != 0;
	}

	/**
	 * Indicates whether or not the item can be removed, usually by the use of
	 * {@link RemoveButton}
	 * 
	 * @param items
	 * @param item
	 * @return
	 */
	public boolean canRemove(final List<T> items, final T item) {
		return true;
	}

	@SuppressWarnings("unchecked")
	final boolean checkRemove(final ListItem<?> item) {
		final List<T> list = Collections.unmodifiableList(items);
		final ListItem<T> li = (ListItem<T>) item;
		return canRemove(list, li.getModelObject());
	}

	/**
	 * Gets model
	 * 
	 * @return model
	 */
	@SuppressWarnings("unchecked")
	public final IModel<List<T>> getModel() {
		return (IModel<List<T>>) getDefaultModel();
	}

	/**
	 * Gets model object
	 * 
	 * @return model object
	 */
	@SuppressWarnings("unchecked")
	public final List<T> getModelObject() {
		return (List<T>) getDefaultModelObject();
	}

	@Override
	protected void onBeforeRender() {
		if (!hasBeenRendered()) {
			items = new ArrayList<T>(getModelObject());
			for (int i = 0; i < items.size(); i++) {
				final ListItem<T> li = new ListItem<T>(newChildId(), i);
				add(li);
				onPopulateItem(li);
			}
		}
		super.onBeforeRender();
	}

	protected abstract void onPopulateItem(ListItem<T> item);

	/**
	 * Sets model
	 * 
	 * @param model
	 */
	public final void setModel(final IModel<List<T>> model) {
		setDefaultModel(model);
	}

	/**
	 * Sets model object
	 * 
	 * @param object
	 */
	public final void setModelObject(final List<T> object) {
		setDefaultModelObject(object);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.apache.wicket.markup.html.form.IFormModelUpdateListener#updateModel()
	 */
	public void updateModel() {
		setModelObject(items);
	}

}
