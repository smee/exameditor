package de.elatexam.editor.components.panels.tasks.cloze;


import net.databinder.components.AjaxOnKeyPausedUpdater;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ClozeSubTaskDef.Cloze;

/**
 * @author Steffen Dienst
 *
 */
public class ClozeSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<ClozeSubTaskDef> {
	private static final IConverter<Cloze> clozeconverter = new ClozeConverter();
	private static final IConverter<Cloze> previewConverter = new ClozePreviewConverter();

	public ClozeSubtaskDefInputPanel(final String id, final IModel<ClozeSubTaskDef> model) {
		super(id, model);
		TextArea<Cloze> clozeText = new TextArea<Cloze>("cloze") {
			@Override
			public IConverter getConverter(Class type) {
				return clozeconverter;
			}
		};
		final Label previewLabel = new Label("preview", new PropertyModel<Cloze>(model, "cloze")) {
			@Override
			public IConverter getConverter(Class type) {
				return previewConverter;
			}
		};
		clozeText.add(new AjaxOnKeyPausedUpdater() {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.add(previewLabel);
			}
		});
		add(clozeText);
		add(previewLabel.setEscapeModelStrings(false).setOutputMarkupId(true));
	}
}
