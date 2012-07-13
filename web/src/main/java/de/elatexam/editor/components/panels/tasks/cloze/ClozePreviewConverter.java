package de.elatexam.editor.components.panels.tasks.cloze;

import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap;

/**
 * @author Steffen Dienst
 *
 */
public class ClozePreviewConverter extends ClozeConverter {
	@Override
	protected String createGapText(Gap gap) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<input type=\"text\" value=\"");
		if(gap.getInitialValue() != null)
			sb.append(gap.getInitialValue());
		sb.append('"');
		int maxLen = 0;
		for (String ci : gap.getCorrect()) {
			maxLen = Math.max(maxLen, ci.length());
		}
		removeTrailingSplitchar(sb);

		if (gap.getInputLength() != null && gap.getInputLength() > maxLen) {
			maxLen = gap.getInputLength();
		}

		sb.append("\" size=\"" + maxLen + "\"");
		sb.append(" disabled=\"disabled\"/>");
		return sb.toString();
	}
}