package org.geektimes.projects.user.web.listener;

import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.management.UserManager;

import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.lang.management.ManagementFactory;

/**
 * {@link ComponentContext} 初始化器
 * ContextLoaderListener
 */
public class ComponentContextInitializerListener implements ServletContextListener {

    private ServletContext servletContext;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        ComponentContext context = new ComponentContext();
        context.init(servletContext);
        registerMBean();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
//        ComponentContext context = ComponentContext.getInstance();
//        context.destroy();
    }

    public void registerMBean() {
        // 获取平台 MBean Server
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        // 为 UserMXBean 定义 ObjectName
        ObjectName objectName = null;
        try {
            objectName = new ObjectName("org.geektimes.projects.user.management:type=User");

            // 创建 UserMBean 实例
            User user = new User();
            mBeanServer.registerMBean(new UserManager(user), objectName);
            System.out.println(user);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}
