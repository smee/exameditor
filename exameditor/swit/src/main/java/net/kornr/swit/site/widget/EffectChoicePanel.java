/*
 * Copyright 2009 Rodrigo Reyes reyes.rr at gmail dot com
 *
 * Licensed under the Apache License, Version 2.0 (the &quot;License&quot;);
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an &quot;AS IS&quot; BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package net.kornr.swit.site.widget;

import java.util.List;

import net.kornr.swit.button.ButtonResource;
import net.kornr.swit.button.ButtonTemplate;
import net.kornr.swit.button.effect.Effect;
import net.kornr.swit.site.buttoneditor.EffectUtils;
import net.kornr.swit.util.Pair;

import org.apache.wicket.markup.html.form.FormComponentPanel;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.markup.html.image.Image;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;


public class EffectChoicePanel extends FormComponentPanel<Integer> 
{
	private IModel<Integer> m_index;
	private int m_choice;
	private RadioGroup<Integer> m_effectgroup;
	
	public EffectChoicePanel(String id, IModel<Integer> effectIndex, List<Pair<String,IModel<ButtonTemplate>>> templates) 
	{
		super(id, effectIndex);
		m_choice = effectIndex.getObject();
		m_effectgroup = new RadioGroup<Integer>("effectgroup", new PropertyModel<Integer>(this, "choice"));
		this.add(m_effectgroup);
		m_effectgroup.add(new ListView<Pair<String,IModel<ButtonTemplate>>>("line", templates) {
			@Override
			protected void populateItem(ListItem<Pair<String,IModel<ButtonTemplate>>> item) 
			{
				Integer index = item.getIndex();
				Pair<String,IModel<ButtonTemplate>> p = item.getModelObject();
				item.add(new Radio<Integer>("radio", new Model<Integer>(index)));
				ButtonTemplate template = p.getSecond().getObject();
				item.add(new Image("img", ButtonResource.getReference(), ButtonResource.getValueMap(template, p.getFirst())));
			}
		});
	}

	@Override
	protected void convertInput() 
	{
		setConvertedInput(m_effectgroup.getConvertedInput());
	}

	@Override
	public void updateModel() 
	{
		getModel().setObject(this.getConvertedInput());
	}

	public int getChoice() {
		return m_choice;
	}

	public void setChoice(int choice) {
		m_choice = choice;
	}

	
	
}
