package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.FoodCategoryDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.FoodCategory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FoodCategoryDaoImpl implements FoodCategoryDao {
    @Override
    public void save(FoodCategory foodCategory) {
        String sql = "INSERT INTO FoodCategories (name, img_src) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, foodCategory.getName());
            preparedStatement.setString(2, foodCategory.getImgSrc());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public FoodCategory findById(int id) {
        String sql = "SELECT * FROM FoodCategories WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                FoodCategory foodCategory = new FoodCategory();
                foodCategory.setCategoryId(rs.getInt("category_id"));
                foodCategory.setName(rs.getString("name"));
                foodCategory.setImgSrc(rs.getString("img_src"));
                return foodCategory;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FoodCategory> findAll() {
        List<FoodCategory> foodCategories = new ArrayList<>();
        String sql = "SELECT * FROM FoodCategories";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                FoodCategory foodCategory = new FoodCategory();
                foodCategory.setCategoryId(rs.getInt("category_id"));
                foodCategory.setName(rs.getString("name"));
                foodCategory.setImgSrc(rs.getString("img_src"));
                foodCategories.add(foodCategory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return foodCategories;
    }

    @Override
    public void update(FoodCategory foodCategory) {
        String sql = "UPDATE FoodCategories SET name = ?, img_src = ? WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, foodCategory.getName());
            stmt.setString(2, foodCategory.getImgSrc());
            stmt.setInt(3, foodCategory.getCategoryId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM FoodCategories WHERE category_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}