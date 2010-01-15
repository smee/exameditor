package de.elateportal.editor.pages;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.databinder.auth.components.DataSignInPanel;
import net.databinder.auth.components.DataSignInPageBase.ReturnPage;
import net.databinder.auth.components.hib.DataSignInPage;
import net.databinder.auth.components.hib.DataUserStatusPanel;
import net.databinder.auth.hib.AuthDataSession;
import net.databinder.components.NullPlug;

import org.apache.wicket.Page;
import org.apache.wicket.behavior.HeaderContributor;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.link.Link;

import wicket.contrib.tinymce.settings.TinyMCESettings;
import de.elateportal.editor.components.menu.ChromeMenu;
import de.elateportal.editor.components.menu.LinkVO;
import de.elateportal.editor.components.menu.LinkVO.Create;
import de.elateportal.editor.components.panels.Footer;
import de.elateportal.editor.user.BasicUser;
import de.elateportal.model.ClozeSubTaskDef;
import de.elateportal.model.MappingSubTaskDef;
import de.elateportal.model.McSubTaskDef;
import de.elateportal.model.PaintSubTaskDef;
import de.elateportal.model.TextSubTaskDef;

public class OverviewPage extends WebPage {

  /** Add components to be displayed on page. */
  public OverviewPage() {
    if (AuthDataSession.get().isSignedIn()) {
      add(new ChromeMenu("menubar", getMenuList(), ChromeMenu.Theme.THEME5));
    } else {
      add(new NullPlug("menubar"));
    }

    // make sure tinymce works, even when adding it via ajax
    // see http://wicketbyexample.com/wicket-tinymce-some-advanced-tips/
    add(new HeaderContributor(new IHeaderContributor() {
      public void renderHead(final IHeaderResponse response) {
        response.renderJavascriptReference(TinyMCESettings.javaScriptReference());
      }
    }));

    // sign in/out links
    add(new DataUserStatusPanel("userStatus") {
      @Override
      protected Link getSignInLink(final String id) {
        return new Link(id) {
          @Override
          public boolean isVisible() {
            // return !getAuthSession().isSignedIn();
            return false;
          }

          @Override
          public void onClick() {
            setResponsePage(new DataSignInPage(new DataSignInPage.ReturnPage() {
              public Page get() {
                return new OverviewPage();
              }
            }));
          }
        };
      }
    });
    add(new DataSignInPanel<BasicUser>("loginpanel", new ReturnPage() {
      public Page get() {
        return new OverviewPage();
      }
    }) {
      @Override
      public boolean isVisible() {
        return !AuthDataSession.get().isSignedIn();
      }
    });
    add(new Footer("footer"));
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
    res.add(Arrays.asList(new LinkVO(ShowSubtaskDefsPage.class, "Alle Aufgaben").setSelected(pageClass.equals(ShowSubtaskDefsPage.class)), new LinkVO(new Create() {
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
    res.add(Arrays.asList(new LinkVO(UploadComplexTaskdefPage.class, "Importieren").setSelected(pageClass.equals(UploadComplexTaskdefPage.class))));
    return res;
  }
}
