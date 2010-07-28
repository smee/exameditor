package de.elatexam.editor;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import net.databinder.auth.data.hib.BasicPassword;
import net.databinder.auth.hib.AuthDataApplication;
import net.databinder.hib.Databinder;
import net.databinder.hib.SessionUnit;

import org.apache.wicket.Request;
import org.apache.wicket.Response;
import org.apache.wicket.authorization.strategies.role.Roles;
import org.hibernate.MappingException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Projections;
import org.hibernate.event.PreDeleteEvent;
import org.hibernate.event.PreDeleteEventListener;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import de.elatexam.editor.pages.OverviewPage;
import de.elatexam.editor.pages.ShowSubtaskDefsPage;
import de.elatexam.editor.pages.StatisticPage;
import de.elatexam.editor.pages.TaskDefPage;
import de.elatexam.editor.pages.UploadComplexTaskdefPage;
import de.elatexam.editor.user.BasicUser;

public class TaskEditorApplication extends AuthDataApplication {
  private final AtomicInteger activeUsers = new AtomicInteger(0);

  public int getActiveUsersCount() {
    return activeUsers.get();
  }

  /**
   * @return
   */
  public static TaskEditorApplication getInstance() {
    return (TaskEditorApplication) get();
  }

  @Override
  public org.apache.wicket.Session newSession(final Request request, final Response response) {
    return new TaskEditorSession(request);
  }

  /*
   * (non-Javadoc)
   *
   * @see
   * net.databinder.hib.DataApplication#configureHibernate(org.hibernate.cfg
   * .AnnotationConfiguration)
   */
  @Override
  protected void configureHibernate(final AnnotationConfiguration config) {
    super.configureHibernate(config);

    // add all model classes from persistence.xml
    addPersistentModelClasses(config, getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml"));

    // fix for bug in HSQLDB, see http://issues.appfuse.org/browse/APF-101
    config.setProperty("hibernate.jdbc.batch_size", "0");
    // config.setProperty("hibernate.show_sql", "true");

    config.setListener("pre-delete", new PreDeleteEventListener() {

      @Override
      public boolean onPreDelete(final PreDeleteEvent event) {
        if (TaskEditorSession.get().isSubtaskDeletionAllowed())
            return false;
        final String entityname = event.getEntity().getClass().getName();

        final boolean veto = entityname.contains("SubTaskDef") && !entityname.contains("TaskBlock");
        return veto;
      }
    });
  }

  /**
   * Fetch all &lt;class&gt; elements from META-INF/persistence.xml and, assuming these classes have JPA annotations,
   * add them to the hibernate configuration.
   *
   * @param config
   * @param in
   */
  private void addPersistentModelClasses(final AnnotationConfiguration config, final InputStream in) {
    try {
      final DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder;
      docBuilder = docBuilderFactory.newDocumentBuilder();
      final Document doc = docBuilder.parse(in);

      final XPath xpath = XPathFactory.newInstance().newXPath();
      final NodeList nodes = (NodeList) xpath.evaluate("//class/text()", doc, XPathConstants.NODESET);
      for (int i = 0; i < nodes.getLength(); i++) {
        config.addAnnotatedClass(Class.forName(nodes.item(i).getNodeValue()));
      }
      return;
    } catch (final ParserConfigurationException e) {
      e.printStackTrace();
    } catch (final SAXException e) {
      e.printStackTrace();
    } catch (final IOException e) {
      e.printStackTrace();
    } catch (final XPathExpressionException e) {
      e.printStackTrace();
    } catch (final MappingException e) {
      e.printStackTrace();
    } catch (final DOMException e) {
      e.printStackTrace();
    } catch (final ClassNotFoundException e) {
      e.printStackTrace();
    }
    System.exit(1);
  }

  @Override
  public Class getHomePage() {
    return OverviewPage.class;
  }

  public byte[] getSalt() {
    return "secrect hash salt".getBytes();
  }

  public Class getUserClass() {
    return BasicUser.class;
  }

  @Override
  protected void init() {
    super.init();
    mountBookmarkablePage("taskdefs", TaskDefPage.class);
    mountBookmarkablePage("statistics", StatisticPage.class);
    mountBookmarkablePage("subtaskdefs", ShowSubtaskDefsPage.class);
    mountBookmarkablePage("import", UploadComplexTaskdefPage.class);

    getMarkupSettings().setStripWicketTags(true);

    // make sure there is at least one user with role admin
    Databinder.ensureSession(new SessionUnit() {
      public Object run(final Session sess) {
        final Transaction transaction = sess.beginTransaction();
        final Integer count = (Integer) sess.createCriteria(BasicUser.class).setProjection(Projections.rowCount()).uniqueResult();
        if (count == 0) {
          final BasicUser adminUser = new BasicUser();
          adminUser.setUsername("admin");
          adminUser.setPassword(new BasicPassword("admin"));
          adminUser.addRole(Roles.ADMIN);
          sess.save(adminUser);
          transaction.commit();
        }
        return null;
      }
    });
  }

  /**
   * Has the current user the role ADMIN?
   *
   * @return
   */
  public static boolean isAdmin() {
    return TaskEditorSession.get().getUser().hasRole(Roles.ADMIN);
  }

  public void incrementUsers() {
    this.activeUsers.incrementAndGet();
  }

  public void decrementUsers() {
    this.activeUsers.decrementAndGet();
  }

}
