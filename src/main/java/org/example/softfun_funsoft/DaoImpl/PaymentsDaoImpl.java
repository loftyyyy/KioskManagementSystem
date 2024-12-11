package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.PaymentsDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.Payments;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentsDaoImpl implements PaymentsDao {
    @Override
    public void save(Payments payment) {
        String sql = "INSERT INTO Payments (order_id, amount) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getOrderId());
            stmt.setDouble(2, payment.getAmount());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Payments findById(int paymentId) {
        String sql = "SELECT * FROM Payments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Payments payment = new Payments();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setCreatedAt(rs.getTimestamp("created_at"));
                payment.setUpdatedAt(rs.getTimestamp("updated_at"));
                return payment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Payments> findAll() {
        List<Payments> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Payments payment = new Payments();
                payment.setPaymentId(rs.getInt("payment_id"));
                payment.setOrderId(rs.getInt("order_id"));
                payment.setPaymentDate(rs.getTimestamp("payment_date"));
                payment.setAmount(rs.getDouble("amount"));
                payment.setCreatedAt(rs.getTimestamp("created_at"));
                payment.setUpdatedAt(rs.getTimestamp("updated_at"));
                payments.add(payment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public void update(Payments payment) {
        String sql = "UPDATE Payments SET order_id = ?, amount = ? WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, payment.getOrderId());
            stmt.setDouble(2, payment.getAmount());
            stmt.setInt(3, payment.getPaymentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int paymentId) {
        String sql = "DELETE FROM Payments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPaymentIdByOrderId(int orderId) {
        String sql = "SELECT payment_id FROM Payments WHERE order_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("payment_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}