package de.elateportal.editor.components.panels.tasks.cloze;

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

import de.elateportal.editor.components.panels.tasks.SubtaskSpecificsInputPanel;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.ClozeSubTaskDef.Cloze;
import de.elateportal.model.ClozeSubTaskDef.Cloze.Gap;
import de.elateportal.model.ClozeSubTaskDef.Cloze.TextOrGapItem;
import de.elateportal.model.ClozeSubTaskDef.Cloze.Gap.CorrectItem;

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
			List<TextOrGapItem> items = new ArrayList<TextOrGapItem>();
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
			for (TextOrGapItem togi : cloze.getTextOrGapItems()) {
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

		private TextOrGapItem createGapItem(String token) {
			List<CorrectItem> correctValues = new ArrayList<CorrectItem>();
			for (String correctValue : token.split(";")) {
				CorrectItem item = new CorrectItem();
				item.setItem(correctValue);
				correctValues.add(item);
			}
			Gap gap = new Gap();
			gap.setCorrectItems(correctValues);
			TextOrGapItem textOrGapItem = new TextOrGapItem();
			textOrGapItem.setItemGap(gap);
			return textOrGapItem;
		}

		/**
		 * @param gap
		 * @return
		 */
		protected String createGapText(Gap gap) {
			StringBuilder sb = new StringBuilder("[");
			for (CorrectItem ci : gap.getCorrectItems()) {
				sb.append(ci.getItem()).append(";");
			}
			removeTrailingSemicolon(sb);
			sb.append("]");
			System.out.println(sb);
			return sb.toString();
		}

		private TextOrGapItem createTextItem(String text) {
			TextOrGapItem textOrGapItem = new TextOrGapItem();
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
			for (CorrectItem ci : gap.getCorrectItems()) {
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
