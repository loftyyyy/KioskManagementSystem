package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.OrderItemsDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.OrderItems;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class OrderItemsDaoImpl implements OrderItemsDao {
    @Override
    public void save(OrderItems orderItem) {
        String sql = "INSERT INTO OrderItems (order_id, food_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getFoodId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setDouble(4, orderItem.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public OrderItems findById(int orderItemId) {
        String sql = "SELECT * FROM OrderItems WHERE order_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItemId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                OrderItems orderItem = new OrderItems();
                orderItem.setOrderItemId(rs.getInt("order_item_id"));
                orderItem.setOrderId(rs.getInt("order_id"));
                orderItem.setFoodId(rs.getInt("food_id"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setPrice(rs.getDouble("price"));
                orderItem.setSubtotal(rs.getDouble("subtotal"));
                orderItem.setCreatedAt(rs.getTimestamp("created_at"));
                orderItem.setUpdatedAt(rs.getTimestamp("updated_at"));
                return orderItem;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderItems> findAll() {
        List<OrderItems> orderItems = new ArrayList<>();
        String sql = "SELECT * FROM OrderItems";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                OrderItems orderItem = new OrderItems();
                orderItem.setOrderItemId(rs.getInt("order_item_id"));
                orderItem.setOrderId(rs.getInt("order_id"));
                orderItem.setFoodId(rs.getInt("food_id"));
                orderItem.setQuantity(rs.getInt("quantity"));
                orderItem.setPrice(rs.getDouble("price"));
                orderItem.setSubtotal(rs.getDouble("subtotal"));
                orderItem.setCreatedAt(rs.getTimestamp("created_at"));
                orderItem.setUpdatedAt(rs.getTimestamp("updated_at"));
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderItems;
    }

    @Override
    public void update(OrderItems orderItem) {
        String sql = "UPDATE OrderItems SET order_id = ?, food_id = ?, quantity = ?, price = ? WHERE order_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItem.getOrderId());
            stmt.setInt(2, orderItem.getFoodId());
            stmt.setInt(3, orderItem.getQuantity());
            stmt.setDouble(4, orderItem.getPrice());
            stmt.setInt(5, orderItem.getOrderItemId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int orderItemId) {
        String sql = "DELETE FROM OrderItems WHERE order_item_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderItemId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}