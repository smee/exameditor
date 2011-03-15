package de.elatexam.editor.components.panels.tasks.cloze;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.apache.wicket.util.convert.IConverter;

import de.elatexam.model.ClozeSubTaskDef.Cloze;
import de.elatexam.model.ClozeSubTaskDef.Cloze.ClozeTextOrGapItem;
import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap;
import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap.GapCorrectItem;

/**
 * Converter for cloze/text to string representation.
 * <p>
 * Syntax: [aaa;bbb] means gap with "aaa" or "bbb" as correct input
 *
 * @author Steffen Dienst
 *
 */
public class ClozeConverter implements IConverter {
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
    // System.out.println(token);
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
  int gapSize = -1;
  // System.out.println(token);
		List<GapCorrectItem> correctValues = new ArrayList<GapCorrectItem>();
		for (String correctValue : token.split(";")) {
    correctValue = correctValue.trim();
    // System.out.println(correctValue);
    if (correctValue.startsWith("{") && correctValue.endsWith("}")) {
      String gapLen = correctValue.substring(1, correctValue.length() - 1);
      // System.out.println(gapLen);
      try {
        gapSize = Integer.parseInt(gapLen);
      } catch (NumberFormatException e) {
        e.printStackTrace();
      }

    } else {
			GapCorrectItem item = new GapCorrectItem();
			item.setItem(correctValue);
			correctValues.add(item);
    }
  }
		Gap gap = new Gap();
  if (gapSize > 0) {
    gap.setInputLength(gapSize);
  }
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
		if (sb.charAt(lastCharIdx) == ';') {
    sb.deleteCharAt(lastCharIdx);
  }
	}
}