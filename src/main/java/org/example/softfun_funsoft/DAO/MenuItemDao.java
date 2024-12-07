package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.MenuItem;
import java.util.List;

public interface MenuItemDao {
    void save(MenuItem menuItem);
    MenuItem findById(int id);
    List<MenuItem> findAll();
    void update(MenuItem menuItem);
    void delete(int id);
}