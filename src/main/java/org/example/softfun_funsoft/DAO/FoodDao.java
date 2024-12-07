package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.Food;
import java.util.List;

public interface FoodDao {
    void save(Food food);
    Food findById(int id);
    List<Food> findAll();
    void update(Food food);
    void delete(int id);
}