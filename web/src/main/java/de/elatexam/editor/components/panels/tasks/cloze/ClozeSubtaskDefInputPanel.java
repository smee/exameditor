package de.elatexam.editor.components.panels.tasks.cloze;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import net.databinder.components.AjaxOnKeyPausedUpdater;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.convert.IConverter;

import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.ClozeSubTaskDef.Cloze;
import de.elatexam.model.ClozeSubTaskDef.Cloze.ClozeTextOrGapItem;
import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap;
import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap.GapCorrectItem;
import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;

/**
 * @author Steffen Dienst
 * 
 */
public class ClozeSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<ClozeSubTaskDef> {
	/**
	 * Converter for cloze/text to string representation.
	 * <p>
	 * Syntax: [aaa;bbb] means gap with "aaa" or "bbb" as correct input
	 * 
	 * @author Steffen Dienst
	 * 
	 */
	private static class ClozeConverter implements IConverter {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String
		 * , java.util.Locale)
		 */
		public Object convertToObject(String value, Locale locale) {
			List<ClozeTextOrGapItem> items = new ArrayList<ClozeTextOrGapItem>();
			StringTokenizer st = new StringTokenizer(value, "[]", true);
			boolean inGap = false;

			while (st.hasMoreTokens()) {
				String token = st.nextToken();
				if ("[".equals(token)) {
					inGap = true;
				} else if ("]".equals(token)) {
					// create a gap with correct values
					inGap = false;
				} else if (inGap) {
					items.add(createGapItem(token));
				} else {
					items.add(createTextItem(token));
				}
			}
			Cloze cloze = new Cloze();
			cloze.setTextOrGapItems(items);
			return cloze;
		}

		public String convertToString(Object value, Locale locale) {
			Cloze cloze = (Cloze) value;
			StringBuilder sb = new StringBuilder();
			for (ClozeTextOrGapItem togi : cloze.getTextOrGapItems()) {
				String text = togi.getItemText();
				if (text != null) {
					sb.append(text);
				}
				Gap itemGap = togi.getItemGap();
				if (itemGap != null) {
					sb.append(createGapText(itemGap));
				}
			}
			return sb.toString();
		}

		private ClozeTextOrGapItem createGapItem(String token) {
			List<GapCorrectItem> correctValues = new ArrayList<GapCorrectItem>();
			for (String correctValue : token.split(";")) {
				GapCorrectItem item = new GapCorrectItem();
				item.setItem(correctValue);
				correctValues.add(item);
			}
			Gap gap = new Gap();
			gap.setCorrectItems(correctValues);
			ClozeTextOrGapItem textOrGapItem = new ClozeTextOrGapItem();
			textOrGapItem.setItemGap(gap);
			return textOrGapItem;
		}

		/**
		 * @param gap
		 * @return
		 */
		protected String createGapText(Gap gap) {
			StringBuilder sb = new StringBuilder("[");
			for (GapCorrectItem ci : gap.getCorrectItems()) {
				sb.append(ci.getItem()).append(";");
			}
			removeTrailingSemicolon(sb);
			sb.append("]");
			return sb.toString();
		}

		private ClozeTextOrGapItem createTextItem(String text) {
			ClozeTextOrGapItem textOrGapItem = new ClozeTextOrGapItem();
			textOrGapItem.setItemText(text);
			return textOrGapItem;
		}

		protected void removeTrailingSemicolon(StringBuilder sb) {
			// remove last semicolon
			int lastCharIdx = sb.length() - 1;
			if (sb.charAt(lastCharIdx) == ';')
				sb.deleteCharAt(lastCharIdx);
		}
	}

	private static final IConverter clozeconverter = new ClozeConverter();
	private static final IConverter previewConverter = new ClozeConverter() {
		@Override
		protected String createGapText(Gap gap) {
			StringBuilder sb = new StringBuilder("");
			sb.append("<input type=\"text\" value=\"");
			int maxLen = 0;
			for (GapCorrectItem ci : gap.getCorrectItems()) {
				sb.append(ci.getItem() + ";");
				maxLen = Math.max(maxLen, ci.getItem().length());
			}
			removeTrailingSemicolon(sb);
			sb.append("\" size=\"" + maxLen + "\"");
			sb.append(" disabled=\"disabled\"/>");
			return sb.toString();
		}
	};

	public ClozeSubtaskDefInputPanel(final String id, final IModel<ClozeSubTaskDef> model) {
		super(id, model);
		TextArea<Cloze> clozeText = new TextArea<Cloze>("cloze") {
			@Override
			public IConverter getConverter(Class<?> type) {
				return clozeconverter;
			}
		};
		final Label previewLabel = new Label("preview", new PropertyModel<Cloze>(model, "cloze")) {
			@Override
			public IConverter getConverter(Class<?> type) {
				return previewConverter;
			}
		};
		clozeText.add(new AjaxOnKeyPausedUpdater() {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				target.addComponent(previewLabel);
			}
		});
		add(clozeText);
		add(previewLabel.setEscapeModelStrings(false).setOutputMarkupId(true));
	}
}
