package org.geektimes.projects.user.web.listener;

import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.service.UserServiceImpl;
import org.geektimes.projects.user.sql.DBConnectionManager;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Set;
import java.util.logging.Logger;

/**
 * 测试用途
 */
@Deprecated
public class TestingListener implements ServletContextListener {

    private Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ComponentContext context = ComponentContext.getInstance();
        DBConnectionManager dbConnectionManager = context.getComponent("bean/DBConnectionManager");
        dbConnectionManager.getConnection();
        testPropertyFromServletContext(sce.getServletContext());
        testPropertyFromJNDI(context);
        testUser(dbConnectionManager.getEntityManager());
        logger.info("所有的 JNDI 组件名称：[");
        context.getComponentNames().forEach(logger::info);
        logger.info("]");
    }

    private void testPropertyFromServletContext(ServletContext servletContext) {
        String propertyName = "application.name";
        logger.info("ServletContext Property[" + propertyName + "] : "
                + servletContext.getInitParameter(propertyName));
    }

    private void testPropertyFromJNDI(ComponentContext context) {
        String propertyName = "maxValue";
        logger.info("JNDI Property[" + propertyName + "] : "
                + context.lookupComponent(propertyName));
    }

    private void testUser(EntityManager entityManager) {
        User user = new User();
        user.setName("小马哥");
        user.setPassword("******");
        user.setEmail("mercyblitz@gmail.com");
        user.setPhoneNumber("14000000000");
        UserServiceImpl userService = ComponentContext.getInstance().getComponent("bean/UserService");
        Validator validator = ComponentContext.getInstance().getComponent("bean/Validator");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (0 < violations.size()) {
            violations.forEach(c -> {
                System.out.println(c.getMessage());
            });
            return;
        }
        System.out.println(userService.register(user).getMessage());

//        EntityTransaction transaction = entityManager.getTransaction();
//        transaction.begin();
//        entityManager.persist(user);
//        transaction.commit();
//        System.out.println(entityManager.find(User.class, user.getId()));
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

}
