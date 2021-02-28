package org.geektimes.projects.user.service;

import org.geektimes.projects.user.domain.User;
import org.geektimes.projects.user.repository.InMemoryUserRepository;

/**
 * @author chenyue
 * @date 2021/2/28
 */
public class UserServiceImpl implements UserService{
    private InMemoryUserRepository inMemoryUserRepository = new InMemoryUserRepository();
    @Override
    public boolean register(User user) {
        return inMemoryUserRepository.save(user);
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
