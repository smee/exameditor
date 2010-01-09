package de.elateportal.editor.pages;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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
import de.elateportal.model.AddonSubTaskDef;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.ComplexTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.SubTaskDefType;
import de.elateportal.model.TextSubTaskDef;
import de.elateportal.model.ComplexTaskDef.Category;
import de.elateportal.model.ComplexTaskDef.Category.AddonTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.McTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem;
import de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock;
import de.elateportal.model.ComplexTaskDef.Category.AddonTaskBlock.AddonSubTaskDefOrChoiceItem;
import de.elateportal.model.ComplexTaskDef.Category.AddonTaskBlock.Choice;
import de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock.ClozeSubTaskDefOrChoiceItem;
import de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock.MappingSubTaskDefOrChoiceItem;
import de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.McSubTaskDefOrChoiceItem;
import de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock.PaintSubTaskDefOrChoiceItem;
import de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock.TextSubTaskDefOrChoiceItem;

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

	private Collection<? extends SubTaskDefType> getAllSubtaskdefs(AddonTaskBlock block) {
		Collection<SubTaskDefType> stds = new ArrayList<SubTaskDefType>();
		if (block == null)
			return stds;
		if (block.getAddonSubTaskDefOrChoiceItems() != null)
			for (AddonSubTaskDefOrChoiceItem item : block.getAddonSubTaskDefOrChoiceItems()) {
				AddonSubTaskDef st = item.getItemAddonSubTaskDef();
				if (st != null)
					stds.add(st);
				else {
					Choice choice = item.getItemChoice();
					stds.addAll(choice.getAddonSubTaskDef());
				}
			}
		return stds;
	}

	private Collection<? extends SubTaskDefType> getAllSubtaskdefs(ClozeTaskBlock block) {
		Collection<SubTaskDefType> stds = new ArrayList<SubTaskDefType>();
		if (block == null)
			return stds;
		if (block.getClozeSubTaskDefOrChoiceItems() != null)
			for (ClozeSubTaskDefOrChoiceItem item : block.getClozeSubTaskDefOrChoiceItems()) {
				ClozeSubTaskDef st = item.getItemClozeSubTaskDef();
				if (st != null)
					stds.add(st);
				else {
					de.elateportal.model.ComplexTaskDef.Category.ClozeTaskBlock.Choice choice = item.getItemChoice();
					stds.addAll(choice.getClozeSubTaskDef());
				}
			}
		return stds;
	}

	private Collection<? extends SubTaskDefType> getAllSubtaskdefs(ComplexTaskDef taskdef) {
		Collection<SubTaskDefType> stds = new ArrayList<SubTaskDefType>();
		for (Category cat : taskdef.getCategory())
			for (McTaskBlockOrClozeTaskBlockOrTextTaskBlockItem block : cat.getMcTaskBlockOrClozeTaskBlockOrTextTaskBlockItems()) {
				stds.addAll(getAllSubtaskdefs(block.getItemAddonTaskBlock()));
				stds.addAll(getAllSubtaskdefs(block.getItemClozeTaskBlock()));
				stds.addAll(getAllSubtaskdefs(block.getItemMappingTaskBlock()));
				stds.addAll(getAllSubtaskdefs(block.getItemMcTaskBlock()));
				stds.addAll(getAllSubtaskdefs(block.getItemPaintTaskBlock()));
				stds.addAll(getAllSubtaskdefs(block.getItemTextTaskBlock()));
			}
		return stds;
	}

	private Collection<? extends SubTaskDefType> getAllSubtaskdefs(MappingTaskBlock block) {
		Collection<SubTaskDefType> stds = new ArrayList<SubTaskDefType>();
		if (block == null)
			return stds;
		if (block.getMappingSubTaskDefOrChoiceItems() != null)
			for (MappingSubTaskDefOrChoiceItem item : block.getMappingSubTaskDefOrChoiceItems()) {
				MappingSubTaskDef st = item.getItemMappingSubTaskDef();
				if (st != null)
					stds.add(st);
				else {
					de.elateportal.model.ComplexTaskDef.Category.MappingTaskBlock.Choice choice = item.getItemChoice();
					stds.addAll(choice.getMappingSubTaskDef());
				}
			}
		return stds;
	}

	private Collection<? extends SubTaskDefType> getAllSubtaskdefs(McTaskBlock block) {
		Collection<SubTaskDefType> stds = new ArrayList<SubTaskDefType>();
		if (block == null)
			return stds;
		if (block.getMcSubTaskDefOrChoiceItems() != null)
			for (McSubTaskDefOrChoiceItem item : block.getMcSubTaskDefOrChoiceItems()) {
				McSubTaskDef st = item.getItemMcSubTaskDef();
				if (st != null)
					stds.add(st);
				else {
					de.elateportal.model.ComplexTaskDef.Category.McTaskBlock.Choice choice = item.getItemChoice();
					stds.addAll(choice.getMcSubTaskDef());
				}
			}
		return stds;
	}

	private Collection<? extends SubTaskDefType> getAllSubtaskdefs(PaintTaskBlock block) {
		Collection<SubTaskDefType> stds = new ArrayList<SubTaskDefType>();
		if (block == null)
			return stds;
		if (block.getPaintSubTaskDefOrChoiceItems() != null)
			for (PaintSubTaskDefOrChoiceItem item : block.getPaintSubTaskDefOrChoiceItems()) {
				PaintSubTaskDef st = item.getItemPaintSubTaskDef();
				if (st != null)
					stds.add(st);
				else {
					de.elateportal.model.ComplexTaskDef.Category.PaintTaskBlock.Choice choice = item.getItemChoice();
					stds.addAll(choice.getPaintSubTaskDef());
				}
			}
		return stds;
	}

	private Collection<? extends SubTaskDefType> getAllSubtaskdefs(TextTaskBlock block) {
		Collection<SubTaskDefType> stds = new ArrayList<SubTaskDefType>();
		if (block == null)
			return stds;
		if (block.getTextSubTaskDefOrChoiceItems() != null)
			for (TextSubTaskDefOrChoiceItem item : block.getTextSubTaskDefOrChoiceItems()) {
				TextSubTaskDef st = item.getItemTextSubTaskDef();
				if (st != null)
					stds.add(st);
				else {
					de.elateportal.model.ComplexTaskDef.Category.TextTaskBlock.Choice choice = item.getItemChoice();
					stds.addAll(choice.getTextSubTaskDef());
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

	public void persistIntoDB(final ComplexTaskDef taskdef) {
		final Session session = Databinder.getHibernateSession();
		final Transaction trans = session.beginTransaction();
		session.save(taskdef);
		// add to current user
		BasicUser user = (BasicUser) ((AuthDataSession) org.apache.wicket.Session.get()).getUser();
		user.getTaskDefs().add(taskdef);
		user.getSubtaskdefs().addAll(getAllSubtaskdefs(taskdef));
		session.saveOrUpdate(user);

		trans.commit();

	}
}
