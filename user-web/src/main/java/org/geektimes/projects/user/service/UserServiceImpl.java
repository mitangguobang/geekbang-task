package org.geektimes.projects.user.service;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.DatabaseUserRepository;
import org.geektimes.projects.user.repository.InMemoryUserRepository;
import org.geektimes.projects.user.sql.DBConnectionManager;

/**
 * @author chenyue
 * @date 2021/2/28
 */
public class UserServiceImpl implements UserService{
    @Override
    public boolean register(User user) {
        try {
            DatabaseUserRepository repository = new DatabaseUserRepository(new DBConnectionManager(true));
            return repository.save(user);
        } catch (RuntimeException e) {
            return false;
        }
    }

    @Override
    public boolean deregister(User user) {
        return false;
    }

    @Override
    public boolean update(User user) {
        return false;
    }

    @Override
    public User queryUserById(Long id) {
        return null;
    }

    @Override
    public User queryUserByNameAndPassword(String name, String password) {
        return null;
    }
}
