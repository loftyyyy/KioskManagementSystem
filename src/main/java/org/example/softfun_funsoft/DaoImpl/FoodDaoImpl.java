package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodDaoImpl implements org.example.softfun_funsoft.DAO.FoodDao {
    @Override
    public void save(Food food) {
        String sql = "INSERT INTO Foods (name, price, category_id, img_src, stock) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, food.getName());
            stmt.setDouble(2, food.getPrice());
            stmt.setInt(3, food.getCategoryId());
            stmt.setString(4, food.getImgSrc());
            stmt.setInt(5, food.getStock());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Food findById(int id) {
        String sql = "SELECT * FROM Foods WHERE food_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setName(rs.getString("name"));
                food.setPrice(rs.getDouble("price"));
                food.setCategoryId(rs.getInt("category_id"));
                food.setImgSrc(rs.getString("img_src"));
                food.setStock(rs.getInt("stock"));
                food.setCreatedAt(rs.getTimestamp("created_at"));
                food.setUpdatedAt(rs.getTimestamp("updated_at"));
                return food;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Food> findAll() {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT * FROM Foods";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setName(rs.getString("name"));
                food.setPrice(rs.getDouble("price"));
                food.setCategoryId(rs.getInt("category_id"));
                food.setImgSrc(rs.getString("img_src"));
                food.setStock(rs.getInt("stock"));
                food.setCreatedAt(rs.getTimestamp("created_at"));
                food.setUpdatedAt(rs.getTimestamp("updated_at"));
                foods.add(food);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }

    @Override
    public void update(Food food) {
        String sql = "UPDATE Foods SET name = ?, price = ?, category_id = ?, img_src = ?, stock = ? WHERE food_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, food.getName());
            stmt.setDouble(2, food.getPrice());
            stmt.setInt(3, food.getCategoryId());
            stmt.setString(4, food.getImgSrc());
            stmt.setInt(5, food.getStock());
            stmt.setInt(6, food.getFoodId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Foods WHERE food_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Food> getAllFoodByCategoryId(int id){
        String sql = "SELECT * FROM Foods WHERE category_id = ?";
        List<Food> foods = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Food food = new Food();
                food.setFoodId(rs.getInt("food_id"));
                food.setName(rs.getString("name"));
                food.setPrice(rs.getDouble("price"));
                food.setCategoryId(rs.getInt("category_id"));
                food.setImgSrc(rs.getString("img_src"));
                food.setStock(rs.getInt("stock"));
                food.setCreatedAt(rs.getTimestamp("created_at"));
                food.setUpdatedAt(rs.getTimestamp("updated_at"));
                foods.add(food);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foods;
    }
}