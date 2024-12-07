package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.MenuItemDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.Food;
import org.example.softfun_funsoft.model.MenuItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDaoImpl implements MenuItemDao {
    @Override
    public void save(MenuItem menuItem) {
        String sql = "INSERT INTO MenuItems (food_id, availability) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuItem.getFoodId());
            stmt.setBoolean(2, menuItem.isAvailability());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MenuItem findById(int id) {
        String sql = "SELECT * FROM MenuItems WHERE menu_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setMenuItemId(rs.getInt("menu_item_id"));
                menuItem.setFoodId(rs.getInt("food_id"));
                menuItem.setAvailability(rs.getBoolean("availability"));
                menuItem.setCreatedAt(rs.getTimestamp("created_at"));
                menuItem.setUpdatedAt(rs.getTimestamp("updated_at"));
                return menuItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<MenuItem> findAll() {
        List<MenuItem> menuItems = new ArrayList<>();
        String sql = "SELECT * FROM MenuItems";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                MenuItem menuItem = new MenuItem();
                menuItem.setMenuItemId(rs.getInt("menu_item_id"));
                menuItem.setFoodId(rs.getInt("food_id"));
                menuItem.setAvailability(rs.getBoolean("availability"));
                menuItem.setCreatedAt(rs.getTimestamp("created_at"));
                menuItem.setUpdatedAt(rs.getTimestamp("updated_at"));
                menuItems.add(menuItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return menuItems;
    }

    @Override
    public void update(MenuItem menuItem) {
        String sql = "UPDATE MenuItems SET food_id = ?, availability = ? WHERE menu_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, menuItem.getFoodId());
            stmt.setBoolean(2, menuItem.isAvailability());
            stmt.setInt(3, menuItem.getMenuItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM MenuItems WHERE menu_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public List<Food> getAvailableFoods() {
        List<Food> foods = new ArrayList<>();
        String sql = "SELECT f.* FROM Foods f JOIN MenuItems m ON f.food_id = m.food_id WHERE m.availability = TRUE";
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
}