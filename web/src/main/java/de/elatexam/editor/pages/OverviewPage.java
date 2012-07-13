package de.elatexam.editor.pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.databinder.auth.components.DataSignInPanel;
import net.databinder.components.NullPlug;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.wicketstuff.jslibraries.CDN;
import org.wicketstuff.jslibraries.JSLib;
import org.wicketstuff.jslibraries.Library;
import org.wicketstuff.jslibraries.VersionDescriptor;

import com.mrhaki.wicket.components.DeployJava;

import de.elatexam.editor.TaskEditorSession;
import de.elatexam.editor.components.menu.ChromeMenu;
import de.elatexam.editor.components.menu.LinkVO;
import de.elatexam.editor.components.menu.LinkVO.Create;
import de.elatexam.editor.components.panels.Footer;
import de.elatexam.editor.user.BasicUser;
import de.elatexam.editor.user.UserStatusPanel;
import de.elatexam.model.ClozeSubTaskDef;
import de.elatexam.model.MappingSubTaskDef;
import de.elatexam.model.McSubTaskDef;
import de.elatexam.model.PaintSubTaskDef;
import de.elatexam.model.TextSubTaskDef;

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

  /* (non-Javadoc)
   * @see org.apache.wicket.Component#renderHead(org.apache.wicket.markup.html.IHeaderResponse)
   */
  @Override
  public void renderHead(IHeaderResponse response) {
       super.renderHead(response);
       response.renderJavaScriptReference(DeployJava.LOCAL_REFERENCE);
       JSLib.getHeaderContribution(VersionDescriptor.exactVersion(Library.JQUERY, 1,4,2), true, CDN.GOOGLE).renderHead(response);
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
    final List<List<LinkVO>> res = new LinkedList<List<LinkVO>>();
    // must not create the link result page now,
    // leads to StackoverflowError in case of resultpage extends OverviewPage
    // TODO make the menu behave like a tabbed panel
    res.add(Arrays.asList(new LinkVO(TaskDefPage.class, "Prüfungen")));
        res.add(Arrays.asList(new LinkVO(ShowSubtaskDefsPage.class, "Alle Aufgaben"),
                new LinkVO(new Create() {
          public WebPage createPage() {
            return new ShowSubtaskDefsPage<McSubTaskDef>(McSubTaskDef.class);
          }
        }, "Alle MC-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage<ClozeSubTaskDef>(ClozeSubTaskDef.class);
          }
        }, "Alle Lückentext-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage<TextSubTaskDef>(TextSubTaskDef.class);
          }
        }, "Alle Text-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage<PaintSubTaskDef>(PaintSubTaskDef.class);
          }
        }, "Alle Zeichen-Aufgaben"), new LinkVO(new Create() {

          public WebPage createPage() {
            return new ShowSubtaskDefsPage<MappingSubTaskDef>(MappingSubTaskDef.class);
          }
        }, "Alle Zuordnungs-Aufgaben")));
    res.add(Arrays.asList(new LinkVO(StatisticPage.class, "Statistiken")));
    res.add(Arrays.asList(new LinkVO(UploadComplexTaskdefPage.class, "Importieren")));
    return res;
  }
}
