package de.elateportal.editor.pages;

import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import net.databinder.hib.Databinder;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.lang.Bytes;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import de.thorstenberger.taskmodel.complex.complextaskdef.ComplexTaskDef;

/**
 * @author sdienst
 */
public class UploadComplexTaskdefPage extends OverviewPage {

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
                    throw new IllegalStateException("Unable to write file");
                }
            }
        }
    }

    public UploadComplexTaskdefPage() {
        add(new FileUploadForm("uploadform"));
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
        trans.commit();

    }
}
