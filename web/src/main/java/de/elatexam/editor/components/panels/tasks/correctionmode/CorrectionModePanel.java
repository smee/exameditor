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
package de.elatexam.editor.components.panels.tasks.correctionmode;

import java.util.Arrays;

import net.databinder.hib.Databinder;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EnumChoiceRenderer;
import org.apache.wicket.markup.html.form.IFormModelUpdateListener;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.hibernate.classic.Session;

import de.elatexam.model.ComplexTaskDef.Config;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode.CorrectOnlyProcessedTasks;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode.MultipleCorrectors;
import de.elatexam.model.ComplexTaskDef.Config.CorrectionMode.Regular;

/**
 * @author Steffen Dienst
 *
 */
public class CorrectionModePanel extends Panel implements IFormModelUpdateListener {
    enum CMode {
        REGULAR, PROCESSEDONLY, MULTIPLE;

        /**
         * @param mode
         * @return
         */
        public static CMode getCorrectionMode(de.elatexam.model.ComplexTaskDef.Config.CorrectionMode mode) {
            if (mode.getRegular() != null)
                return REGULAR;
            else if (mode.getCorrectOnlyProcessedTasks() != null)
                return PROCESSEDONLY;
            else if (mode.getMultipleCorrectors() != null)
                return MULTIPLE;
            else
                throw new IllegalArgumentException("This correction mode is invalid!");
        }

        public static int getValue(de.elatexam.model.ComplexTaskDef.Config.CorrectionMode mode) {
            if (mode.getRegular() != null)
                return 0;// dummy value
            else if (mode.getCorrectOnlyProcessedTasks() != null)
                return mode.getCorrectOnlyProcessedTasks().getNumberOfTasks();
            else if (mode.getMultipleCorrectors() != null)
                return mode.getMultipleCorrectors().getNumberOfCorrectors();
            else
                throw new IllegalArgumentException("This correction mode is invalid!");

        }
    }

    private CMode selectedMode;
    private int additionalValue;

    private TextField<Integer> valueTextfield;
    private Label label;

    /**
     * @param id
     * @param model
     */
    public CorrectionModePanel(String id, IModel<de.elatexam.model.ComplexTaskDef.Config.CorrectionMode> model) {
        super(id, model);

        Config.CorrectionMode modelObject = (Config.CorrectionMode) getDefaultModelObject();
        selectedMode = CMode.getCorrectionMode(modelObject);
        additionalValue = CMode.getValue(modelObject);


        // add correction mode via custom model, ichoicerenderer?
        final DropDownChoice correctionModeDropDown = new DropDownChoice("correctionMode",
                new PropertyModel(this, "selectedMode"),
                Model.ofList(Arrays.asList(CMode.values())),
                new EnumChoiceRenderer(this));

        correctionModeDropDown.add(new AjaxFormComponentUpdatingBehavior("onchange") {
            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(valueTextfield);
                target.addComponent(label);
            }
        });
        add(correctionModeDropDown);

        valueTextfield = new TextField<Integer>("additionalValue", new PropertyModel(this, "additionalValue")) {
            @Override
            public boolean isVisible() {
                return selectedMode != CMode.REGULAR;
            };
        };
        valueTextfield.setOutputMarkupId(true);
        valueTextfield.setOutputMarkupPlaceholderTag(true);
        add(valueTextfield);

        // use dynamic string key for i18n: ${} means this.toString().
        add(label = new Label("selectedMode", new StringResourceModel("label.${}", this, new PropertyModel(this, "selectedMode"))));
        label.setOutputMarkupId(true);
    }

    @Override
    public void updateModel() {
        System.out.println("About to update " + getDefaultModelObject());
        System.out.println("using " + selectedMode + " and " + additionalValue);
        final Session session = Databinder.getHibernateSession();

        CorrectionMode cm = (CorrectionMode) getDefaultModelObject();
        // remove current correction modes
        if(cm.getRegular()!=null){
            session.delete(cm.getRegular());
            cm.setRegular(null);
        }
        if(cm.getCorrectOnlyProcessedTasks()!=null){
            session.delete(cm.getCorrectOnlyProcessedTasks());
            cm.setCorrectOnlyProcessedTasks(null);
        }
        if(cm.getMultipleCorrectors()!=null){
            session.delete(cm.getMultipleCorrectors());
            cm.setMultipleCorrectors(null);
        }
        switch (selectedMode) {
        case REGULAR:
            cm.setRegular(new Regular());
            break;
        case PROCESSEDONLY:
            cm.setCorrectOnlyProcessedTasks(new CorrectOnlyProcessedTasks());
            cm.getCorrectOnlyProcessedTasks().setNumberOfTasks(additionalValue);
            break;
        case MULTIPLE:
            cm.setMultipleCorrectors(new MultipleCorrectors());
            cm.getMultipleCorrectors().setNumberOfCorrectors(additionalValue);
            break;
        default:
            break;
        }
    }

}
