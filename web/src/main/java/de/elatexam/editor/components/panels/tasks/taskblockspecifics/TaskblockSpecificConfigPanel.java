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
package de.elatexam.editor.components.panels.tasks.taskblockspecifics;

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
import org.apache.wicket.validation.validator.MinimumValidator;
import org.hibernate.Session;

import de.elatexam.model.ClozeTaskBlock.ClozeConfig;
import de.elatexam.model.ComplexTaskDef.Config;
import de.elatexam.model.ClozeTaskBlock;
import de.elatexam.model.MappingTaskBlock;
import de.elatexam.model.MappingTaskBlock.MappingConfig;
import de.elatexam.model.McTaskBlock;
import de.elatexam.model.McTaskBlock.McConfig;
import de.elatexam.model.McTaskBlock.McConfig.Different;
import de.elatexam.model.McTaskBlock.McConfig.Regular;
import de.elatexam.model.TaskBlock;

/**
 * Panel that may render each of {@link McConfig}, {@link ClozeConfig}, {@link MappingConfig}.
 * {@link McConfig} may have one of {@link Regular} or {@link Different} set, where {@link Different} has two point values
 * whereas everyone else has exactly one.
 * @author Steffen Dienst
 *
 * TODO dropdown selection of {@link Regular} vs {@link Different} for {@link McConfig} 
 * TODO {@link ClozeConfig} has a property {@link ClozeConfig#isIgnoreCase()}
 */
public class TaskblockSpecificConfigPanel extends Panel implements IFormModelUpdateListener {
    enum TBConfig {
        mc, cloze, mapping, none;

        /**
         * @param mode
         * @return
         */
        public static TBConfig getConfigType(TaskBlock tb) {
            if (tb instanceof McTaskBlock)
                return mc;
            else if (tb instanceof ClozeTaskBlock)
                return cloze;
            else if (tb instanceof MappingTaskBlock)
                return mapping;
            else
                return none;
        }

        public static boolean hasTwoValues(TaskBlock tb){
        	return (tb instanceof McTaskBlock) && ((McTaskBlock)tb).getMcConfig().getDifferent()!=null;
        }
		/**
		 * @param object
		 * @return
		 */
		public static float getValue1(TaskBlock tb) {
            if (tb instanceof McTaskBlock) {
				McConfig c = ((McTaskBlock) tb).getMcConfig();
				if(c.getRegular()!=null)
					return c.getRegular().getNegativePoints();
				else
					return c.getDifferent().getCorrectAnswerNegativePoints();
			} else if (tb instanceof ClozeTaskBlock)
                return ((ClozeTaskBlock) tb).getClozeConfig().getNegativePoints();
            else if (tb instanceof MappingTaskBlock)
                return ((MappingTaskBlock) tb).getMappingConfig().getNegativePoints();
            else
                return 0;
		}

		/**
		 * @param object
		 * @return
		 */
		public static float getValue2(TaskBlock tb) {
            if (tb instanceof McTaskBlock) {
				McConfig c = ((McTaskBlock) tb).getMcConfig();
				if(c.getDifferent()!=null)
					return c.getDifferent().getIncorrectAnswerNegativePoints();
			}
            return 0;
		}

		/**
		 * @param tbType
		 * @param tb 
		 * @param val1
		 * @param val2
		 */
		public static void setValues(TBConfig tbType, TaskBlock tb, float val1, float val2) {
	        switch (tbType) {
	            case mc:
	                McConfig m = ((McTaskBlock)tb).getMcConfig();
	                if(m.getDifferent()!=null){
	                	final Session session = Databinder.getHibernateSession();
	                	session.delete(m.getDifferent());
	                	m.setRegular(new Regular(val1));
	                	session.save(m.getRegular());
	                }else
	                	m.getRegular().setNegativePoints(val1);	                
	                break;
	            case cloze:
	                ((ClozeTaskBlock)tb).getClozeConfig().setNegativePoints(val1);
	                break;
	            case mapping:
	                ((MappingTaskBlock)tb).getMappingConfig().setNegativePoints(val1);
	                break;
	            default:
	                break;
	            }
			
		}

        
    }

    private final TBConfig tbType;

    private final TextField<Integer> value1Textfield,value2Textfield;
    private float val1, val2;

    /**
     * @param id
     * @param model
     */
    public TaskblockSpecificConfigPanel(String id, IModel<TaskBlock> model) {
        super(id, model);
       
        tbType = TBConfig.getConfigType(model.getObject());
        val1=TBConfig.getValue1(model.getObject());
        val2=TBConfig.getValue2(model.getObject());
        
        value1Textfield = new TextField<Integer>("val1", new PropertyModel(this, "val1")){
        	@Override
        	public boolean isVisible() {
        		return tbType!=TBConfig.none;
        	};
        };
        value2Textfield = new TextField<Integer>("val2", new PropertyModel(this, "val2")) {
            @Override
            public boolean isVisible() {
                return hasTwoValues();
            };
        };
        value2Textfield.setOutputMarkupId(true);
        value2Textfield.setOutputMarkupPlaceholderTag(true);
        add(value1Textfield);
        add(value2Textfield);

        // use dynamic string key for i18n: ${} means this.toString().
        add( new Label("label1", new StringResourceModel("label1.${}", this, new PropertyModel(this, "tbType"))));
        add( new Label("label2", new StringResourceModel("label2.${}", this, new PropertyModel(this, "tbType"))){
        	@Override
        	public boolean isVisible() {
        		return hasTwoValues();
        	}
        });
        
    }

    /**
	 * 
	 */
	protected boolean hasTwoValues() {
		return TBConfig.hasTwoValues((TaskBlock) getDefaultModelObject());		
	}

	/*
     * (non-Javadoc)
     * 
     * @see org.apache.wicket.markup.html.form.IFormModelUpdateListener#updateModel()
     */
    @Override
    public void updateModel() {
        final Session session = Databinder.getHibernateSession();

        TaskBlock tb = (TaskBlock) getDefaultModelObject();
        TBConfig.setValues(tbType, tb,val1,val2);

    }

}
