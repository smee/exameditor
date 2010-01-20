package de.elateportal.editor.pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.databinder.auth.components.DataSignInPanel;
import net.databinder.components.NullPlug;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;

import wicket.contrib.tinymce.settings.TinyMCESettings;
import de.elateportal.editor.TaskEditorSession;
import de.elateportal.editor.components.menu.ChromeMenu;
import de.elateportal.editor.components.menu.LinkVO;
import de.elateportal.editor.components.menu.LinkVO.Create;
import de.elateportal.editor.components.panels.Footer;
import de.elateportal.editor.user.BasicUser;
import de.elateportal.editor.user.UserStatusPanel;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.TextSubTaskDef;

public class OverviewPage extends WebPage {

  /** Add components to be displayed on page. */
  public OverviewPage() {
    if (TaskEditorSession.get().isSignedIn()) {
      add(new ChromeMenu("menubar", getMenuList(), ChromeMenu.Theme.THEME5));
      add(createToolbar("toolbar"));
    } else {
      add(new NullPlug("menubar"));
      add(new EmptyPanel("toolbar"));
    }

    // make sure tinymce works, even when adding it via ajax
    // see http://wicketbyexample.com/wicket-tinymce-some-advanced-tips/
    add(new HeaderContributor(new IHeaderContributor() {
      public void renderHead(final IHeaderResponse response) {
        response.renderJavascriptReference(TinyMCESettings.javaScriptReference());
      }
    }));

    // sign in/out links
    add(new UserStatusPanel("userStatus"));
    add(new DataSignInPanel<BasicUser>("loginpanel", null) {
      @Override
      public boolean isVisible() {
        return !TaskEditorSession.get().isSignedIn();
      }
    });
    add(new Footer("footer"));
  }

  /**
   * Create a panel with links etc. that act as toolbar. This panel gets rendered right below the menu.
   * 
   * @return
   */
  protected Component createToolbar(final String id) {
    return new EmptyPanel(id);
  }

  /**
   * @return
   */
  private List<List<LinkVO>> getMenuList() {
    final Class<? extends Page> pageClass = getPage().getClass();
    final List<List<LinkVO>> res = new LinkedList<List<LinkVO>>();
    // must not create the link result page now,
    // leads to StackoverflowError in case of resultpage extends OverviewPage

    res.add(Arrays.asList(new LinkVO(TaskDefPage.class, "Prüfungen").setSelected(pageClass.equals(TaskDefPage.class))));
    res.add(Arrays.asList(new LinkVO(ShowSubtaskDefsPage.class, "Alle Aufgaben").setSelected(pageClass
        .equals(ShowSubtaskDefsPage.class)), new LinkVO(new Create() {
          public WebPage createPage() {
            return new ShowSubtaskDefsPage(McSubTaskDef.class);
          }
        }, "Alle MC-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage(ClozeSubTaskDef.class);
          }
        }, "Alle Lückentext-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage(TextSubTaskDef.class);
          }
        }, "Alle Text-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage(PaintSubTaskDef.class);
          }
        }, "Alle Zeichen-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage(MappingSubTaskDef.class);
          }
        }, "Alle Zuordnungs-Aufgaben")));
    res.add(Arrays.asList(new LinkVO(StatisticPage.class, "Statistiken").setSelected(pageClass.equals(StatisticPage.class))));
    res.add(Arrays.asList(new LinkVO(UploadComplexTaskdefPage.class, "Importieren").setSelected(pageClass
        .equals(UploadComplexTaskdefPage.class))));
    return res;
  }
}
