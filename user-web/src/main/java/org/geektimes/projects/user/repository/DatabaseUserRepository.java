package org.geektimes.projects.user.repository;

import org.geektimes.function.ThrowableFunction;
import org.geektimes.context.ComponentContext;
import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.sql.DBConnectionManager;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.*;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.apache.commons.lang.ClassUtils.wrapperToPrimitive;

public class DatabaseUserRepository implements UserRepository {

    private static Logger logger = Logger.getLogger(DatabaseUserRepository.class.getName());

    /**
     * 通用处理方式
     */
    private static Consumer<Throwable> COMMON_EXCEPTION_HANDLER = e -> logger.log(Level.SEVERE, e.getMessage());

    public static final String INSERT_USER_DML_SQL =
            "INSERT INTO users(name,password,email,phoneNumber) VALUES " +
                    "(?,?,?,?)";

    public static final String QUERY_ALL_USERS_DML_SQL = "SELECT id,name,password,email,phoneNumber FROM users";

    private final DBConnectionManager dbConnectionManager;

    public DatabaseUserRepository() {
        this.dbConnectionManager = ComponentContext.getInstance().getComponent("bean/DBConnectionManager");
    }

    private Connection getConnection() {
        return dbConnectionManager.getConnection();
    }

    @Override
    public boolean save(User user) {
        return 0 < executeUpdate(INSERT_USER_DML_SQL, COMMON_EXCEPTION_HANDLER,
                user.getName(), user.getPassword(), user.getEmail(), user.getPhoneNumber());
    }

    @Override
    public boolean deleteById(Long userId) {
        return 0 < executeUpdate("delete from users where id=?", COMMON_EXCEPTION_HANDLER, userId);
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User getById(Long userId) {
        return null;
    }

    public static void main(String[] args) {
        DatabaseUserRepository u = new DatabaseUserRepository();

        User user = new User();
        user.setName("test-add3");
        user.setPassword("**");
        user.setEmail("");
        user.setPhoneNumber("");
        System.out.println(u.save(user)); //true
//        u.getByNameAndPassword("a","***");

//        Collection<User> all = u.getAll();
        u.getAll().forEach(each -> {
            System.out.println(String.format("id=%s, name=%s, password=%s, email=%s, phoneNumber=%s",
                    each.getId(), each.getName(), each.getPassword(), each.getEmail(), each.getPhoneNumber()));
        });
    }
    @Override
    public User getByNameAndPassword(String userName, String password) {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users WHERE name=? and password=?",
                resultSet -> {
                    // TODO
                    User user = null;
                    if (resultSet.next()) {
                        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
                        user = new User();
                        for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                            String fieldName = propertyDescriptor.getName();
                            Class fieldType = propertyDescriptor.getPropertyType();
                            String methodName = resultSetMethodMappings.get(fieldType);
                            // 可能存在映射关系（不过此处是相等的）
                            String columnLabel = mapColumnLabel(fieldName);
                            Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                            // 通过放射调用 getXXX(String) 方法
                            Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                            // 获取 User 类 Setter方法
                            // PropertyDescriptor ReadMethod 等于 Getter 方法
                            // PropertyDescriptor WriteMethod 等于 Setter 方法
                            Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                            // 以 id 为例，  user.setId(resultSet.getLong("id"));
                            setterMethodFromUser.invoke(user, resultValue);
                        }
                    }
                    return user;
                }, COMMON_EXCEPTION_HANDLER, userName, password);
    }

    @Override
    public Collection<User> getAll() {
        return executeQuery("SELECT id,name,password,email,phoneNumber FROM users", resultSet -> {
            // BeanInfo -> IntrospectionException
            BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
            List<User> users = new ArrayList<>();
            while (resultSet.next()) { // 如果存在并且游标滚动 // SQLException
                User user = new User();
                users.add(user);
                for (PropertyDescriptor propertyDescriptor : userBeanInfo.getPropertyDescriptors()) {
                    String fieldName = propertyDescriptor.getName();
                    Class fieldType = propertyDescriptor.getPropertyType();
                    String methodName = resultSetMethodMappings.get(fieldType);
                    // 可能存在映射关系（不过此处是相等的）
                    String columnLabel = mapColumnLabel(fieldName);
                    Method resultSetMethod = ResultSet.class.getMethod(methodName, String.class);
                    // 通过放射调用 getXXX(String) 方法
                    Object resultValue = resultSetMethod.invoke(resultSet, columnLabel);
                    // 获取 User 类 Setter方法
                    // PropertyDescriptor ReadMethod 等于 Getter 方法
                    // PropertyDescriptor WriteMethod 等于 Setter 方法
                    Method setterMethodFromUser = propertyDescriptor.getWriteMethod();
                    // 以 id 为例，  user.setId(resultSet.getLong("id"));
                    setterMethodFromUser.invoke(user, resultValue);
                }
            }
            return users;
        }, e -> {
            // 异常处理
        });
    }

    /**
     * @param sql
     * @param function
     * @param <T>
     * @return
     */
    protected <T> T executeQuery(String sql, ThrowableFunction<ResultSet, T> function,
                                 Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class argType = arg.getClass();

                Class wrapperType = wrapperToPrimitive(argType);

                if (wrapperType == null) {
                    wrapperType = argType;
                }

                // Boolean -> boolean
                String methodName = preparedStatementMethodMappings.get(argType);
                Method method = PreparedStatement.class.getMethod(methodName, int.class, wrapperType);
                method.invoke(preparedStatement, i + 1, arg);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            // 返回一个 POJO List -> ResultSet -> POJO List
            // ResultSet -> T
            return function.apply(resultSet);
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
        return null;
    }

    protected int executeUpdate(String sql, Consumer<Throwable> exceptionHandler, Object... args) {
        Connection connection = getConnection();
        int result = -1;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                Class argType = arg.getClass();
                Class wrapperType = wrapperToPrimitive(argType);
                if (wrapperType == null) {
                    wrapperType = argType;
                }
                // Boolean -> boolean
                String methodName = preparedStatementMethodMappings.get(argType);
                Method method = PreparedStatement.class.getMethod(methodName, int.class, wrapperType);
                method.invoke(preparedStatement, i + 1, arg);
            }
            result = preparedStatement.executeUpdate();
        } catch (Throwable e) {
            exceptionHandler.accept(e);
        }
        return result;
    }

    private static String mapColumnLabel(String fieldName) {
        return fieldName;
    }

    /**
     * 数据类型与 ResultSet 方法名映射
     */
    static Map<Class, String> resultSetMethodMappings = new HashMap<>();

    static Map<Class, String> preparedStatementMethodMappings = new HashMap<>();

    static {
        resultSetMethodMappings.put(Long.class, "getLong");
        resultSetMethodMappings.put(String.class, "getString");

        preparedStatementMethodMappings.put(Long.class, "setLong"); // long
        preparedStatementMethodMappings.put(String.class, "setString"); //


    }
}
