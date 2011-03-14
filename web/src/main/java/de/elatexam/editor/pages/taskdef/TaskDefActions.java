package de.elatexam.editor.pages.taskdef;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import net.databinder.hib.Databinder;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.link.DownloadLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.hibernate.Transaction;

import com.visural.wicket.component.confirmer.ConfirmerLink;

import de.elatexam.editor.TaskEditorSession;
import de.elatexam.editor.components.event.AjaxUpdateEvent;
import de.elatexam.editor.components.event.AjaxUpdateEvent.IAjaxUpdateListener;
import de.elatexam.editor.components.panels.tree.ComplexTaskDefTree;
import de.elatexam.editor.components.panels.tree.ComplexTaskHierarchyFacade;
import de.elatexam.editor.components.panels.tree.TreeSelectionEvent;
import de.elatexam.editor.pages.TaskDefPage;
import de.elatexam.editor.preview.PreviewLink;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.util.Stuff;
import de.elatexam.model.ComplexTaskDef;
import de.elatexam.model.ComplexTaskDef.Revisions.Revision;
import de.elatexam.model.Indexed;
import de.elatexam.model.ObjectFactory;
import de.elatexam.model.SubTaskDef;

/**
 * Actions operating on ComplexTaskDefs
 *
 *
 */
public class TaskDefActions extends Panel implements IAjaxUpdateListener{
	private static JAXBContext context = null;
	static{
		try {
			context = JAXBContext.newInstance(ComplexTaskDef.class);
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}
	private final Link<File> downloadLink;
	private final Link<ComplexTaskDef> previewLink;
	private final ConfirmerLink deleteLink;
	private final AddElementLink addLink;
	private final ComplexTaskDefTree<Indexed> tree;
	
	@SuppressWarnings("unchecked")
	public TaskDefActions(String id, ComplexTaskDefTree<Indexed> t) {
		super(id);
		this.tree = t;
		this.downloadLink = createDownloadLink();

		setOutputMarkupId(true);
		ModalWindow taskblockselectormodal = new TaskBlockSelectorModalWindow("taskblockmodal") {
			@Override
			void onSelect(Class taskblockclass) {
				addLink.createTaskblock(taskblockclass);
			}
		};
		add(taskblockselectormodal);
		TaskSelectorModalWindow<?> taskselectormodal = new TaskSelectorModalWindow("taskmodal") {
			@Override
			void onSelect(AjaxRequestTarget target, SubTaskDef... subtaskdefs) {
				addLink.addTasks(subtaskdefs);
				target.addComponent(tree);
			}
		};
		add(taskselectormodal);

		this.addLink = new AddElementLink("add", taskblockselectormodal,taskselectormodal, tree);

		deleteLink = new ConfirmerLink("delete") {

			@Override
			public void onClick() {
				// which domain object do we need to delete?
				final Object toDelete = new ComplexTaskHierarchyFacade<Indexed>(tree.getProvider()).removeFromParent(tree.getSelected().getObject());
				// do not delete subtaskdefs, only remove them from the current
				// complextaskdef
				if (!(toDelete instanceof SubTaskDef)) {
					final org.hibernate.classic.Session session = Databinder
							.getHibernateSession();
					final Transaction transaction = session.beginTransaction();
					session.delete(toDelete);
					transaction.commit();
				} else {
					Stuff.saveAll(tree.getSelected().getObject());
				}
			}
		};
		deleteLink.setOKButtonLabel("Ja");
		deleteLink.setCancelButtonLabel("Abbrechen");
		deleteLink.setMessageContentHTML("Sind Sie sicher, dass das selektierte Element gel&ouml;scht werden soll?");
		deleteLink.setEnabled(false);

		previewLink = new PreviewLink("preview",
				new AbstractReadOnlyModel<ComplexTaskDef>() {
					@Override
					public ComplexTaskDef getObject() {
						return tree.getCurrentTaskdef().getObject();
					}
				});
		previewLink.setEnabled(false);

		add(previewLink);
		add(downloadLink);
		add(addLink);
		add(deleteLink);
	}

	/**
	 * Create a link that allows to download a serialized xml file for the
	 * currently selected taskdef.
	 *
	 * @return
	 */
	private DownloadLink createDownloadLink() {
		DownloadLink downloadLink = new DownloadLink("export",
				new AbstractReadOnlyModel<File>() {

					@Override
					public File getObject() {
						File tempFile = null;
						try {
							tempFile = File.createTempFile("taskdef", "export");
							// marshal to xml
							
							final Marshaller marshaller = context.createMarshaller();
							final BufferedWriter bw = new BufferedWriter(new FileWriter(tempFile));
							final ComplexTaskDef ctd = tree.getCurrentTaskdef().getObject();
							addRevisionTo(ctd);
							marshaller.marshal(ctd, bw);
							bw.close();
						} catch (final IOException e) {
							error("Konnte leider keine Datei schreiben, Infos siehe Logfile.");
							e.printStackTrace();
						} catch (final JAXBException e) {
							error("Konnte leider kein XML erstellen, Infos siehe Logfile.");
							e.printStackTrace();
						}
						return tempFile;
					}

					/**
					 * Add current timestamp+author name as new revision.
					 *
					 * @param ctd
					 */
					private void addRevisionTo(final ComplexTaskDef ctd) {
						final Revision rev = new ObjectFactory().createComplexTaskDefRevisionsRevision();
						rev.setAuthor(TaskEditorSession.get().getUser().getUsername());
						rev.setDate(System.currentTimeMillis());
						final List<Revision> revisions = ctd.getRevisions().getRevision();
						rev.setSerialNumber(revisions.size() + 1);
						revisions.add(rev);
					}
				}, "pruefung.xml");

		downloadLink.setDeleteAfterDownload(true);
		downloadLink.setEnabled(false);

		return downloadLink;
	}


	/* (non-Javadoc)
	 * @see de.elatexam.editor.components.event.AjaxUpdateEvent.IAjaxUpdateListener#notifyAjaxUpdate(de.elatexam.editor.components.event.AjaxUpdateEvent)
	 */
	@Override
	public void notifyAjaxUpdate(AjaxUpdateEvent event) {
		if (event instanceof TreeSelectionEvent) {
			Object selected = ((TreeSelectionEvent) event).getSelectedModel()
					.getObject();
			boolean enabled = !(selected instanceof BasicUser);

			if (selected instanceof ComplexTaskDef) {
				this.previewLink.setEnabled(true);
				this.downloadLink.setEnabled(true);
			} else {
				this.previewLink.setEnabled(false);
				this.downloadLink.setEnabled(false);
			}
			if (selected instanceof SubTaskDef) {
				this.addLink.setEnabled(false);
			} else {
				this.addLink.setEnabled(true);
			}
			this.deleteLink.setEnabled(enabled);
			// no admin user should be able to delete himself....
			if (selected instanceof BasicUser) {
				this.deleteLink.setEnabled(
					false == ((BasicUser) selected).getUsername()
							.equals( TaskEditorSession.get().getUser().getUsername()) );
			}
			event.getTarget().addComponent(this);
		}
	}

}