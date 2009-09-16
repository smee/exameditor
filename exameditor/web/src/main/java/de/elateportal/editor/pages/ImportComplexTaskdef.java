package de.elateportal.editor.pages;

import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.util.lang.Bytes;

/**
 * @author sdienst
 */
public class ImportComplexTaskdef extends OverviewPage {

    private class FileUploadForm<T> extends Form<T> {
        private FileUploadField fileUploadField;

        public FileUploadForm(final String name) {
            super(name);

            // set this form to multipart mode (allways needed for uploads!)
            setMultiPart(true);

            // Add one file input field
            add(fileUploadField = new FileUploadField("fileInput"));

            // Set maximum size to 100K for demo purposes
            setMaxSize(Bytes.kilobytes(100));
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
                    final byte[] bytes = upload.getBytes();
                    System.out.println(bytes.length);
                } catch (final Exception e) {
                    throw new IllegalStateException("Unable to write file");
                }
            }
        }
    }

    public ImportComplexTaskdef() {
        add(new FileUploadForm("uploadform"));
    }
}
