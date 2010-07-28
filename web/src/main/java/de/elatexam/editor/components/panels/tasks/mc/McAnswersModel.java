/*

Copyright (C) 2010 Steffen Dienst

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package de.elatexam.editor.components.panels.tasks.mc;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

import de.elatexam.model.McSubTaskDef.McSubTaskDefAnswerDefinitionsItem;
import de.elatexam.model.NamedString;

/**
 * @author Steffen Dienst
 *
 */
public class McAnswersModel extends Model<String> {

    private IModel<McSubTaskDefAnswerDefinitionsItem> nestedModel;

    public McAnswersModel(IModel<McSubTaskDefAnswerDefinitionsItem> model) {
        super();
        this.nestedModel = model;
    }

    @Override
    public String getObject() {
        McSubTaskDefAnswerDefinitionsItem answerDef = nestedModel.getObject();
        NamedString ns = answerDef.getItemCorrect();
        if (ns != null)
            return ns.getValue();
        else
            return answerDef.getItemIncorrect().getValue();
    }

    @Override
    public void setObject(String newvalue) {
        McSubTaskDefAnswerDefinitionsItem answerDef = nestedModel.getObject();
        NamedString ns = answerDef.getItemCorrect();
        if (ns != null) {
            ns.setValue(newvalue);
        } else {
            answerDef.getItemIncorrect().setValue(newvalue);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see org.apache.wicket.model.Model#detach()
     */
    @Override
    public void detach() {
        nestedModel.detach();
    }
}
