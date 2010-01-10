package de.elateportal.editor.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.databinder.auth.hib.AuthDataSession;
import net.databinder.hib.Databinder;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.lang.Bytes;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import de.elateportal.editor.user.BasicUser;
import de.elateportal.editor.util.Stuff;
import de.elateportal.model.AddonSubTaskDef;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.SubTaskDefType;
import de.elateportal.model.TaskBlockType;
import de.elateportal.model.TextSubTaskDef;
import de.elateportal.model.ComplexTaskDef.Category;
import de.elateportal.model.ComplexTaskDef.Category.McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem;

/**
 * @author sdienst
 */
public class UploadComplexTaskdefPage extends SecurePage {

	private class FileUploadForm<T> extends Form<T> {
		private FileUploadField fileUploadField;

		public FileUploadForm(final String name) {
			super(name);

			// set this form to multipart mode (allways needed for uploads!)
			setMultiPart(true);

			// Add one file input field
			add(fileUploadField = new FileUploadField("fileInput"));

			setMaxSize(Bytes.kilobytes(1000));
			add(new FeedbackPanel("feedback"));
		}

		/**
		 * @see org.apache.wicket.markup.html.form.Form#onSubmit()
		 */
		@Override
		protected void onSubmit() {
			final FileUpload upload = fileUploadField.getFileUpload();
			if (upload != null) {
				try {
					final ComplexTaskDef taskdef = loadTaskDef(upload);
					if (taskdef != null) {
						persistIntoDB(taskdef);
						setResponsePage(StatisticPage.class);
					} else {
						error(String.format("Die Datei '%s' enthält keine gültige Aufgabendefinition!", upload.getClientFileName()));
					}
				} catch (final Exception e) {
					throw new IllegalStateException("Unable to add new complextaskdef to database!", e);
				}
			}
		}
	}

	public UploadComplexTaskdefPage() {
		add(new FileUploadForm("uploadform"));
	}

	private <T extends SubTaskDefType> Collection<T> getAllSubtaskdefs(ComplexTaskDef taskdef, Class<T> clazz) throws Exception {
		Collection<T> stds = new ArrayList<T>();
		for (Category cat : taskdef.getCategory())
			for (McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem block : cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlockItems()) {
				stds.addAll(getAllSubtaskdefsFromBlock((TaskBlockType) Stuff.call(block, "getItem%sTaskBlock", clazz), clazz));
			}
		return stds;
	}

	private <T extends SubTaskDefType> Collection<T> getAllSubtaskdefsFromBlock(TaskBlockType block, Class<T> clazz) throws Exception {
		Collection<T> stds = new ArrayList<T>();
		if (block == null)
			return stds;
		List items = (List) Stuff.call(block, "get%sSubTaskDefOrChoiceItems", clazz);
		if (items != null)
			for (Object item : items) {
				T st = (T) Stuff.call(item, "getItem%sSubTaskDef", clazz);
				if (st != null)
					stds.add(st);
				else {
					Object choice = Stuff.call(item, "getItemChoice");
					stds.addAll((Collection<T>) Stuff.call(choice, "get%sSubTaskDef", clazz));
				}
			}
		return stds;
	}

	/**
	 * @param upload
	 * @return
	 */
	public ComplexTaskDef loadTaskDef(final FileUpload upload) {
		try {
			final JAXBContext context = JAXBContext.newInstance(ComplexTaskDef.class);
			final Unmarshaller unmarshaller = context.createUnmarshaller();

			final JAXBElement unmarshalledElement;
			final Object unmarshalledObject;

			final Object result = unmarshaller.unmarshal(upload.getInputStream());
			return (ComplexTaskDef) result;

		} catch (final JAXBException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void persistIntoDB(final ComplexTaskDef taskdef) throws Exception {
		final Session session = Databinder.getHibernateSession();
		final Transaction trans = session.beginTransaction();
		session.save(taskdef);
		// add to current user
		BasicUser user = (BasicUser) ((AuthDataSession) org.apache.wicket.Session.get()).getUser();

		user.getTaskdefs().add(taskdef);

		user.getAddonSubtaskdefs().addAll(getAllSubtaskdefs(taskdef, AddonSubTaskDef.class));
		user.getMcSubtaskdefs().addAll(getAllSubtaskdefs(taskdef, McSubTaskDef.class));
		user.getPaintSubtaskdefs().addAll(getAllSubtaskdefs(taskdef, PaintSubTaskDef.class));
		user.getMappingSubtaskdefs().addAll(getAllSubtaskdefs(taskdef, MappingSubTaskDef.class));
		user.getClozeSubtaskdefs().addAll(getAllSubtaskdefs(taskdef, ClozeSubTaskDef.class));
		user.getTextSubtaskdefs().addAll(getAllSubtaskdefs(taskdef, TextSubTaskDef.class));

		session.saveOrUpdate(user);

		trans.commit();

	}
}
