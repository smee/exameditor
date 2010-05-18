package de.elatexam.editor.components.panels.tasks.paint;

import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.HiddenField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.validator.MinimumValidator;

import com.mrhaki.wicket.components.DeployJava;

import de.elatexam.editor.components.panels.tasks.SubtaskSpecificsInputPanel;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.PaintSubTaskDef.Images;

public class PaintSubtaskDefInputPanel extends SubtaskSpecificsInputPanel<PaintSubTaskDef> {

  private final DeployJava applet;

  public PaintSubtaskDefInputPanel(final String id, final IModel<PaintSubTaskDef> model) {
        super(id,model);

    applet = new DeployJava("paintapplet");
    applet.setHeight(400);
    applet.setWidth(600);
    applet.setArchive("applet/drawapplet.jar");
    applet.setCode("drawing/DrawingApplet.class");
    applet.setMinimalVersion("1.5");

    final Images images = model.getObject().getImages();
    applet.addParameter("background", images.getImmutableBackgroundImage());
    applet.addParameter("mutableForeground", images.getMutableTemplateImage());
    applet.addParameter("tutorMode", true);
    add(applet);

    add(new TextField("textualAnswer.textFieldWidth").add(new MinimumValidator<Integer>(0)));
    add(new TextField("textualAnswer.textFieldHeight").add(new MinimumValidator<Integer>(0)));
    add(new CheckBox("colorChangeable"));
    add(new CheckBox("strokewidthChangeable"));

    add(new HiddenField<String>("hiddenBGImage", Model.of("foo")));
    add(new Link<String>("storeImage") {
      @Override
      protected CharSequence getOnClickScript(final CharSequence url) {
        return "document.getElementById('taskform12').hiddenBGImage.value=document.taskform.paintapplet.getForegroundPictureWithUndoData(5);return false;";
      }
      @Override
      public void onClick() {

      }

    });
    }

}
