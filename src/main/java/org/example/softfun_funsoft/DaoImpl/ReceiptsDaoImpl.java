package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.ReceiptsDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.Receipts;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptsDaoImpl implements ReceiptsDao {
    @Override
    public void save(Receipts receipt) {
        String sql = "INSERT INTO Receipts (order_id, receipt_url) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, receipt.getOrderId());
            stmt.setString(2, receipt.getReceiptUrl());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Receipts findById(int receiptId) {
        String sql = "SELECT * FROM Receipts WHERE receipt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, receiptId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Receipts receipt = new Receipts();
                receipt.setReceiptId(rs.getInt("receipt_id"));
                receipt.setOrderId(rs.getInt("order_id"));
                receipt.setReceiptUrl(rs.getString("receipt_url"));
                receipt.setReceiptDate(rs.getTimestamp("receipt_date"));
                receipt.setCreatedAt(rs.getTimestamp("created_at"));
                receipt.setUpdatedAt(rs.getTimestamp("updated_at"));
                return receipt;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Receipts> findAll() {
        List<Receipts> receipts = new ArrayList<>();
        String sql = "SELECT * FROM Receipts";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Receipts receipt = new Receipts();
                receipt.setReceiptId(rs.getInt("receipt_id"));
                receipt.setOrderId(rs.getInt("order_id"));
                receipt.setReceiptUrl(rs.getString("receipt_url"));
                receipt.setReceiptDate(rs.getTimestamp("receipt_date"));
                receipt.setCreatedAt(rs.getTimestamp("created_at"));
                receipt.setUpdatedAt(rs.getTimestamp("updated_at"));
                receipts.add(receipt);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receipts;
    }

    @Override
    public void update(Receipts receipt) {
        String sql = "UPDATE Receipts SET order_id = ?, receipt_url = ? WHERE receipt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, receipt.getOrderId());
            stmt.setString(2, receipt.getReceiptUrl());
            stmt.setInt(3, receipt.getReceiptId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int receiptId) {
        String sql = "DELETE FROM Receipts WHERE receipt_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, receiptId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}