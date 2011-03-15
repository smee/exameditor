package de.elatexam.editor.components.panels.tasks.cloze;

import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap;
import de.elatexam.model.ClozeSubTaskDef.Cloze.Gap.GapCorrectItem;

/**
 * @author Steffen Dienst
 *
 */
public class ClozePreviewConverter extends ClozeConverter {
	@Override
	protected String createGapText(Gap gap) {
		StringBuilder sb = new StringBuilder("");
		sb.append("<input type=\"text\" value=\"");
		int maxLen = 0;
        boolean valueAdded = false;
		for (GapCorrectItem ci : gap.getCorrectItems()) {
            if (!valueAdded) {
                // add the first correct value into the input field, suffices for previews
                sb.append(ci.getItem());
                valueAdded = true;
            }
			maxLen = Math.max(maxLen, ci.getItem().length());
		}
		removeTrailingSemicolon(sb);

  if (gap.getInputLength() != null && gap.getInputLength() > maxLen) {
    maxLen = gap.getInputLength();
  }

		sb.append("\" size=\"" + maxLen + "\"");
		sb.append(" disabled=\"disabled\"/>");
		return sb.toString();
	}
}