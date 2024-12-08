package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.User;
import java.util.List;

public interface UserDao {
    void save(User user);
    User findById(String id);
    List<User> findAll();
    void update(User user);
    void delete(String id);
}
