package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.FoodCategory;

import java.util.List;

public interface FoodCategoryDao {
    void save(FoodCategory foodCategory);
    FoodCategory findById(int id);
    List<FoodCategory> findAll();
    void update(FoodCategory foodCategory);
    void delete(int id);
}
