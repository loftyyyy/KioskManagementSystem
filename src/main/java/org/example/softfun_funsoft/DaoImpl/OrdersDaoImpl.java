package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.OrdersDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.Orders;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrdersDaoImpl implements OrdersDao {
    @Override
    public void save(Orders order) {
        String sql = "INSERT INTO Orders (user_id, order_date, total_amount, payment_type, is_dine_in) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getUserId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setDouble(3, order.getTotalAmount());
            stmt.setString(4, order.getPaymentType());
            stmt.setBoolean(5, order.isDineIn());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Orders findById(int orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Orders order = new Orders();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getString("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setPaymentType(rs.getString("payment_type"));
                order.setDineIn(rs.getBoolean("is_dine_in"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                order.setUpdatedAt(rs.getTimestamp("updated_at"));
                return order;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Orders> findAll() {
        List<Orders> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getString("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setPaymentType(rs.getString("payment_type"));
                order.setDineIn(rs.getBoolean("is_dine_in"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                order.setUpdatedAt(rs.getTimestamp("updated_at"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void update(Orders order) {
        String sql = "UPDATE Orders SET user_id = ?, order_date = ?, total_amount = ?, payment_type = ?, is_dine_in = ? WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getUserId());
            stmt.setTimestamp(2, order.getOrderDate());
            stmt.setDouble(3, order.getTotalAmount());
            stmt.setString(4, order.getPaymentType());
            stmt.setBoolean(5, order.isDineIn());
            stmt.setInt(6, order.getOrderId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int orderId) {
        String sql = "DELETE FROM Orders WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Orders> findByUserId(String userId) {
        List<Orders> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE user_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Orders order = new Orders();
                order.setOrderId(rs.getInt("order_id"));
                order.setUserId(rs.getString("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setPaymentType(rs.getString("payment_type"));
                order.setDineIn(rs.getBoolean("is_dine_in"));
                order.setCreatedAt(rs.getTimestamp("created_at"));
                order.setUpdatedAt(rs.getTimestamp("updated_at"));
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}