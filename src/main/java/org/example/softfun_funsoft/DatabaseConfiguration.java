package org.example.softfun_funsoft;

import org.example.softfun_funsoft.DaoImpl.FoodCategoryDaoImpl;
import org.example.softfun_funsoft.DaoImpl.FoodDaoImpl;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.Food;
import org.example.softfun_funsoft.model.FoodCategory;
import org.example.softfun_funsoft.singleton.Categories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DatabaseConfiguration {
    public static void main(String[] args){
        Categories categories = Categories.getInstance();
        FoodDaoImpl foodDao = new FoodDaoImpl();
        FoodCategoryDaoImpl foodCategoryDao = new FoodCategoryDaoImpl();

//        for(FoodCategory category : categories.getCategories()){
//            foodCategoryDao.save(category);
//
//        }

//        for(Food food : menuItem.getFoods()){
//            foodDao.save(food);
//        }


        FoodDaoImpl ff = new FoodDaoImpl();
        List<Food> foods = ff.findAll();


        for(Food food : foods){
            boolean availability = food.getStock() > 0;
            addMenuItem(food.getFoodId(), availability);

        }



    }
        private static void addMenuItem(int foodId, boolean availability) {
        String sql = "INSERT INTO MenuItems (food_id, availability) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, foodId);
            stmt.setBoolean(2, availability);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
