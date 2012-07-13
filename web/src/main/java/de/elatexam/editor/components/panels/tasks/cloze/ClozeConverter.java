package de.elatexam.editor.components.panels.tasks.cloze;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.wicket.util.convert.IConverter;

import de.elatexam.model.ClozeSubTaskDef.Cloze;
import de.elatexam.model.ClozeSubTaskDef.Cloze.ClozeTextOrGapItem;
import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap;
import static org.apache.commons.lang.StringUtils.isEmpty;
/**
 * Converter for cloze/text to string representation.
 * <p>
 * Syntax: [aaa;bbb] means gap with "aaa" or "bbb" as correct input
 *
 * @author Steffen Dienst
 *
 */
public class ClozeConverter implements IConverter<Cloze> {
	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.apache.wicket.util.convert.IConverter#convertToObject(java.lang.String
	 * , java.util.Locale)
	 */
	public Cloze convertToObject(String value, Locale locale) {
		List<ClozeTextOrGapItem> items = new ArrayList<ClozeTextOrGapItem>();
		StringTokenizer st = new StringTokenizer(value, "[]", true);
		boolean inGap = false;

		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			if ("[".equals(token)) {
				inGap = true;
			} else if ("]".equals(token)) {
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

	/* (non-Javadoc)
	 * @see org.apache.wicket.util.convert.IConverter#convertToString(java.lang.Object, java.util.Locale)
	 */
	public String convertToString(Cloze value, Locale locale) {
		Cloze cloze = (Cloze) value;
		StringBuilder sb = new StringBuilder();
		for (ClozeTextOrGapItem togi : cloze.getTextOrGapItems()) {
			String text = togi.getItemText();
			if (!isEmpty(text)) {
				sb.append(text);
			}
			Gap itemGap = togi.getItemGap();
			if (itemGap != null) {
				sb.append(createGapText(itemGap));
			}
		}
		return sb.toString();
	}

	private ClozeTextOrGapItem createGapItem(String s) {
		int gapSize = -1;
		String initialValue="";
		List<String> correctValues = new ArrayList<String>();
		
		for (String token : s.split("\\|")) {
			token = token.trim();
			if(isEmpty(token))
				continue;

			if (token.startsWith("{") && token.endsWith("}")) {
				String gapLen = token.substring(1,token.length() - 1);
				gapSize = NumberUtils.toInt(gapLen, -1);
			}else if (token.startsWith("\"") && token.endsWith("\"")) {
				initialValue = token.substring(1,token.length() - 1);
			} else {
				correctValues.add(token);
			}
		}
		Gap gap = new Gap();
		if (gapSize > 0) {
			gap.setInputLength(gapSize);
		}
		gap.setInitialValue(initialValue);
		gap.setCorrect(correctValues);
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
		if(!isEmpty(gap.getInitialValue())){
			sb.append('"')
			  .append(gap.getInitialValue())
			  .append('"')
			  .append('|');
		}
		
		for(String gci: gap.getCorrect())
			sb.append(gci).append('|');
		if(gap.getInputLength()!=null)
			sb.append('{')
			  .append(gap.getInputLength())
			  .append('}');
		removeTrailingSplitchar(sb);
		sb.append(']');
		return sb.toString();
	}

	private ClozeTextOrGapItem createTextItem(String text) {
		ClozeTextOrGapItem textOrGapItem = new ClozeTextOrGapItem();
		textOrGapItem.setItemText(text);
		return textOrGapItem;
	}

	protected void removeTrailingSplitchar(StringBuilder sb) {
		// remove last |
		int lastCharIdx = sb.length() - 1;
		if (sb.charAt(lastCharIdx) == '|') {
			sb.deleteCharAt(lastCharIdx);
		}
	}
}