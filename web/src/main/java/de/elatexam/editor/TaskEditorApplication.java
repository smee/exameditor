package de.elatexam.editor;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import net.databinder.auth.data.hib.BasicPassword;
import net.databinder.auth.hib.AuthDataApplication;
import net.databinder.hib.Databinder;
import net.databinder.hib.SessionUnit;

import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.markup.html.IHeaderContributor;
import org.apache.wicket.markup.html.IHeaderResponse;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.criterion.Projections;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.wicketstuff.jslibraries.CDN;
import org.wicketstuff.jslibraries.JSLib;
import org.wicketstuff.jslibraries.Library;
import org.wicketstuff.jslibraries.VersionDescriptor;

import de.elatexam.editor.components.form.EnhanceFormsListener;
import de.elatexam.editor.pages.OverviewPage;
import de.elatexam.editor.pages.ShowSubtaskDefsPage;
import de.elatexam.editor.pages.StatisticPage;
import de.elatexam.editor.pages.TaskDefPage;
import de.elatexam.editor.pages.UploadComplexTaskdefPage;
import de.elatexam.editor.user.BasicUser;

public class TaskEditorApplication extends AuthDataApplication {

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
     * @see net.databinder.hib.DataApplication#configureHibernate(org.hibernate.cfg .AnnotationConfiguration)
     */
    @Override
    protected void configureHibernate(final AnnotationConfiguration config) {
        super.configureHibernate(config);

        // add all model classes from persistence.xml
        addPersistentModelClasses(config, getClass().getClassLoader().getResourceAsStream("META-INF/persistence.xml"));

//		 config.setProperty("hibernate.show_sql", "true");

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
		} catch (final Exception e) {
            e.printStackTrace();
        }
        System.exit(1);
    }

    @Override
    public Class<OverviewPage> getHomePage() {
        return OverviewPage.class;
    }

    public byte[] getSalt() {
        return "secrect hash salt".getBytes();
    }

    public Class<BasicUser> getUserClass() {
        return BasicUser.class;
    }

    /* (non-Javadoc)
    * @see net.databinder.auth.hib.AuthDataApplication#getSignInPageClass()
    */
    @Override
    public Class<? extends WebPage> getSignInPageClass() {
      return OverviewPage.class;
    }
    @Override
    protected void init() {
        super.init();
        getHeaderContributorListenerCollection().add(JSLib.getHeaderContribution(VersionDescriptor.exactVersion(Library.JQUERY, 1,4,2), true, CDN.GOOGLE));
        // enable request logger, needed to show live session count
        getRequestLoggerSettings().setRequestLoggerEnabled(true);
        getApplicationSettings().setPageExpiredErrorPage(OverviewPage.class);
        getMarkupSettings().setDefaultMarkupEncoding("UTF8");
        mountPage("taskdefs", TaskDefPage.class);
        mountPage("statistics", StatisticPage.class);
        mountPage("subtaskdefs", ShowSubtaskDefsPage.class);
        mountPage("import", UploadComplexTaskdefPage.class);

        getMarkupSettings().setStripWicketTags(true);

        // make sure there is at least one user with role admin
        Databinder.ensureSession(new SessionUnit() {
            public Object run(final Session sess) {
                final Number count = (Number) sess.createCriteria(BasicUser.class).setProjection(Projections.rowCount()).uniqueResult();
                if (count.intValue() == 0) {
                    final BasicUser adminUser = new BasicUser();
                    adminUser.setUsername("admin");
                    adminUser.setPassword(new BasicPassword("admin"));
                    adminUser.addRole(Roles.ADMIN);
                    sess.save(adminUser);
                    sess.getTransaction().commit();
                }
                return null;
            }
        });
        // add error highlighting behaviour to any form components
        getComponentInstantiationListeners().add(new EnhanceFormsListener());
    }

    /**
     * Has the current user the role ADMIN?
     *
     * @return
     */
    public static boolean isAdmin() {
        return TaskEditorSession.get().getUser().hasRole(Roles.ADMIN);
    }

}
