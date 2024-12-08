package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.CartDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.Cart;
import org.example.softfun_funsoft.model.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDaoImpl implements CartDao {
    @Override
    public void save(Cart cart) {
        String sql = "INSERT INTO Cart (user_id, cart_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cart.getUserId());
            stmt.setInt(2, cart.getCartId());
            stmt.setString(3, cart.getUserId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Cart findById(int cartId) {
        String sql = "SELECT * FROM Cart WHERE cart_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getString("user_id"));
                cart.setFoodId(rs.getInt("food_id"));
                cart.setQuantity(rs.getInt("quantity"));
                cart.setCreatedAt(rs.getTimestamp("created_at"));
                cart.setUpdatedAt(rs.getTimestamp("updated_at"));
                return cart;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Cart> findAll() {
        List<Cart> carts = new ArrayList<>();
        String sql = "SELECT * FROM Cart";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Cart cart = new Cart();
                cart.setCartId(rs.getInt("cart_id"));
                cart.setUserId(rs.getString("user_id"));
                cart.setFoodId(rs.getInt("food_id"));
                cart.setQuantity(rs.getInt("quantity"));
                cart.setCreatedAt(rs.getTimestamp("created_at"));
                cart.setUpdatedAt(rs.getTimestamp("updated_at"));
                carts.add(cart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return carts;
    }

    @Override
    public void update(Cart cart) {
        String sql = "UPDATE Cart SET quantity = ? WHERE cart_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cart.getQuantity());
            stmt.setInt(2, cart.getCartId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int cartId) {
        String sql = "DELETE FROM Cart WHERE cart_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAll(){
        String sql = "DELETE FROM Cart";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Food> getAllFoodsFromCart(String userId) {
        String sql = "SELECT f.food_id, f.name, f.price, f.category_id, f.img_src, f.stock, f.created_at, f.updated_at " +
                "FROM cart c " +
                "JOIN Foods f ON c.food_id = f.food_id " +
                "WHERE c.user_id = ?";
        List<Food> foods = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId); // Assuming user_id is used to filter cart for a specific user
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