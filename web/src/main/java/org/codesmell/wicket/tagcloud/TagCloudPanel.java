/**
 * 
 */
package org.codesmell.wicket.tagcloud;

import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

/**
 * The actual Panel creating a TagCloud
 * 
 * @author Uwe Schäfer, (uwe@codesmell.org)
 * 
 */
public class TagCloudPanel extends org.apache.wicket.markup.html.panel.Panel {
	/**
     * 
     */
	public TagCloudPanel(final String id, final IModel<TagCloudData> tagCloudDataModel) {
		super(id, tagCloudDataModel);
		setOutputMarkupId(true);

		add(new ListView<TagData>("tags", new PropertyModel(getDefaultModel(), "tags")) {

			@Override
			protected void populateItem(final ListItem<TagData> item) {
				TagData modelObject = item.getModelObject();
				TagCloudData tagProvider = tagCloudDataModel.getObject();
				item.add(new Tag("tag", modelObject, tagProvider.getLink("link", modelObject), tagProvider.weightToPixel(modelObject.getWeight())));
				item.setRenderBodyOnly(true);
			}
		});
	}

	public TagCloudPanel(final String id, final TagCloudData tagProvider) {
		this(id, new Model<TagCloudData>(tagProvider));
	}

}
