package org.vaadin.example.jndiexample;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

import org.vaadin.example.jndiexample.domain.Person;
import org.vaadin.example.jndiexample.ui.BasicCrudView;

import com.vaadin.Application;
import com.vaadin.addon.jpacontainer.JPAContainerFactory;
import com.vaadin.addon.jpacontainer.provider.jndijta.JndiAddresses;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.UserTransaction;

/**
 * The Application's "main" class
 */
@SuppressWarnings("serial")
public class MyVaadinApplication extends Application {

    @Override
    public void init() {
        setMainWindow(new AutoCrudViews());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    class AutoCrudViews extends Window {

        public AutoCrudViews() {
            final HorizontalSplitPanel horizontalSplitPanel = new HorizontalSplitPanel();
            Tree navTree = new Tree();
            navTree.addListener(new Property.ValueChangeListener() {

                @Override
                public void valueChange(ValueChangeEvent event) {
                    BasicCrudView cv = (BasicCrudView) event.getProperty().getValue();
                    cv.refreshContainer();
                    horizontalSplitPanel.setSecondComponent(cv);
                }
            });
            navTree.setSelectable(true);
            navTree.setNullSelectionAllowed(false);
            navTree.setImmediate(true);

            horizontalSplitPanel.setSplitPosition(200,
                    HorizontalSplitPanel.UNITS_PIXELS);
            horizontalSplitPanel.addComponent(navTree);
            setContent(horizontalSplitPanel);

            // add a basic crud view for all entities known by the JPA
            // implementation, most often this is not desired and developers
            // should just list those entities they want to have editors for

            try {
                InitialContext initialContext = new InitialContext();
                EntityManager em = (EntityManager) initialContext.lookup(JndiAddresses.DEFAULTS.getEntityManagerName());
                Metamodel metamodel = em.getMetamodel();
                
                Set<EntityType<?>> entities = metamodel.getEntities();
                for (EntityType<?> entityType : entities) {
                    Class<?> javaType = entityType.getJavaType();
                    BasicCrudView view = new BasicCrudView(javaType);
                    navTree.addItem(view);
                    navTree.setItemCaption(view, view.getCaption());
                    navTree.setChildrenAllowed(view, false);
                    if (javaType == Person.class) {
                        view.setVisibleTableProperties("firstName", "lastName", "boss");
                        view.setVisibleFormProperties("firstName", "lastName", "phoneNumber", "street", "city", "zipCode", "boss");
                    }

                }

                // select first entity view
                navTree.setValue(navTree.getItemIds().iterator().next());
            } catch (NamingException e) {
            }
        }
    }

    static {
        try {
            InitialContext initialContext = new InitialContext();
            EntityManager em = (EntityManager) initialContext.lookup(JndiAddresses.DEFAULTS.getEntityManagerName());

            long size = (Long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            if (size == 0) {
                // create two Person objects as test data

            UserTransaction utx = (UserTransaction) (new InitialContext())
                    .lookup(JndiAddresses.DEFAULTS.getUserTransactionName());
            utx.begin();
                
                Person boss = new Person();
                boss.setFirstName("John");
                boss.setLastName("Bigboss");
                boss.setCity("Turku");
                boss.setPhoneNumber("+358 02 555 221");
                boss.setZipCode("20200");
                boss.setStreet("Ruukinkatu 2-4");
                em.persist(boss);

                Person p = new Person();
                p.setFirstName("Marc");
                p.setLastName("Hardworker");
                p.setCity("Turku");
                p.setPhoneNumber("+358 02 555 222");
                p.setZipCode("20200");
                p.setStreet("Ruukinkatu 2-4");
                p.setBoss(boss);
                em.persist(p);

                utx.commit();
            }
        } catch (Exception ex) {
            Logger.getLogger(MyVaadinApplication.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
