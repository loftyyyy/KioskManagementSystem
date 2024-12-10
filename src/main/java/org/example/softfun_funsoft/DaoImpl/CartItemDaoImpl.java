package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.CartItemDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.CartItem;
import org.example.softfun_funsoft.model.Food;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDaoImpl implements CartItemDao {
    @Override
    public void save(CartItem cartItem) {
        String sql = "INSERT INTO CartItems (cart_id, food_id, quantity, price) VALUES (?, ?, ?, ?) " +
                     "ON DUPLICATE KEY UPDATE quantity = quantity + VALUES(quantity)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getCartId());
            stmt.setInt(2, cartItem.getFoodId());
            stmt.setInt(3, cartItem.getQuantity());
            stmt.setDouble(4, cartItem.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public CartItem findById(int cartItemId) {
        String sql = "SELECT * FROM Cart_Items WHERE cart_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartItemId(rs.getInt("cart_item_id"));
                cartItem.setCartId(rs.getInt("cart_id"));
                cartItem.setFoodId(rs.getInt("food_id"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setPrice(rs.getDouble("price"));
                return cartItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CartItem> findAll() {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM CartItems";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartItemId(rs.getInt("cart_item_id"));
                cartItem.setCartId(rs.getInt("cart_id"));
                cartItem.setFoodId(rs.getInt("food_id"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setPrice(rs.getDouble("price"));
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    @Override
    public void update(CartItem cartItem) {
        String sql = "UPDATE CartItems SET cart_id = ?, food_id = ?, quantity = ?, price = ? WHERE cart_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItem.getCartId());
            stmt.setInt(2, cartItem.getFoodId());
            stmt.setInt(3, cartItem.getQuantity());
            stmt.setDouble(4, cartItem.getPrice());
            stmt.setInt(5, cartItem.getCartItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int cartItemId) {
        String sql = "DELETE FROM cartitems WHERE cart_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Food> findFoodsByCartId(int cartId) {
    List<Food> foods = new ArrayList<>();
    String sql = "SELECT f.food_id, f.name, f.price, f.category_id, f.img_src, f.stock, f.created_at, f.updated_at " +
                 "FROM CartItems ci " +
                 "JOIN foods f ON ci.food_id = f.food_id " +
                 "WHERE ci.cart_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, cartId);
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
    public int getCartIdByFoodId(int foodId) {
        String sql = "SELECT cart_id FROM CartItems WHERE food_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, foodId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("cart_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if no cartId is found for the given foodId
    }
    public CartItem getCartItemByFoodId(int foodId) {
    String sql = "SELECT * FROM CartItems WHERE food_id = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, foodId);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            CartItem cartItem = new CartItem();
            cartItem.setCartItemId(rs.getInt("cart_item_id"));
            cartItem.setCartId(rs.getInt("cart_id"));
            cartItem.setFoodId(rs.getInt("food_id"));
            cartItem.setQuantity(rs.getInt("quantity"));
            cartItem.setPrice(rs.getDouble("price"));
            return cartItem;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Return null if no CartItem is found for the given foodId
    }

    public void deleteAllByCartId(int cartId) {
        String sql = "DELETE FROM CartItems WHERE cart_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<CartItem> findAllByCartId(int cartId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM CartItems WHERE cart_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                CartItem cartItem = new CartItem();
                cartItem.setCartItemId(rs.getInt("cart_item_id"));
                cartItem.setCartId(rs.getInt("cart_id"));
                cartItem.setFoodId(rs.getInt("food_id"));
                cartItem.setQuantity(rs.getInt("quantity"));
                cartItem.setPrice(rs.getDouble("price"));
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cartItems;
    }

    public Double getTotalAmountByCartId(int cartId) {
        String sql = "SELECT SUM(price * quantity) AS total_amount FROM CartItems WHERE cart_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cartId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("total_amount");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}