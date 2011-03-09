package de.elatexam.editor.components.event;

import org.apache.wicket.Component;
import org.apache.wicket.IClusterable;
import org.apache.wicket.Page;
import org.apache.wicket.Component.IVisitor;
import org.apache.wicket.ajax.AjaxRequestTarget;

/**
 * very simple event mechanism for loose coupling of wicket components for ajax
 * updates
 * 
 * @author Stefan Fussenegger
 * 
 */
public class AjaxUpdateEvent implements IClusterable {

	private static final long serialVersionUID = 1L;
	private final Component _source;
	private final AjaxRequestTarget _target;

	/**
	 * create a new event. create subclasses to add further parameters
	 * 
	 * @param source
	 * @param target
	 */
	public AjaxUpdateEvent(final Component source, final AjaxRequestTarget target) {
		_source = source;
		_target = target;
	}

	/**
	 * @return the source Component
	 */
	public Component getSource() {
		return _source;
	}

	/**
	 * @return the AjaxRequestTarget
	 */
	public AjaxRequestTarget getTarget() {
		return _target;
	}

	/**
	 * fire this event, i.e. notify listeners. this is done by visiting all
	 * children of the source component's page
	 */
	public final void fire() {
		final Page p = getSource().getPage();
		if (p instanceof IAjaxUpdateListener) {
			((IAjaxUpdateListener) p).notifyAjaxUpdate(this);
		}
		p.visitChildren(new NotifyVisitor(this));
	}

	/**
	 * Components listening for events have to implement this interface
	 */
	public static interface IAjaxUpdateListener extends IClusterable {
		/**
		 * you will typically first do a <code>(event instanceof MyEvent)</code>
		 * and finally <code>event.addComponent(myComponent)</code>
		 * 
		 * @param event
		 *            event fired by event.getSource()
		 */
		public void notifyAjaxUpdate(final AjaxUpdateEvent event);
	}

	/**
	 * Visitor to notify all children of a page
	 */
	private static final class NotifyVisitor implements IVisitor {
		private final AjaxUpdateEvent _event;

		public NotifyVisitor(final AjaxUpdateEvent event) {
			_event = event;
		}

		public Object component(final Component component) {
			if (component instanceof IAjaxUpdateListener) {
				((IAjaxUpdateListener) component).notifyAjaxUpdate(_event);
			}
			return IVisitor.CONTINUE_TRAVERSAL;
		}
	}
}
