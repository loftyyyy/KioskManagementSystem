package org.example.softfun_funsoft.DaoImpl;

import org.example.softfun_funsoft.DAO.CardPaymentsDao;
import org.example.softfun_funsoft.Database.DatabaseConnection;
import org.example.softfun_funsoft.model.CardPayments;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CardPaymentsDaoImpl implements CardPaymentsDao {
    @Override
    public void save(CardPayments cardPayment) {
        String sql = "INSERT INTO CardPayments (payment_id, card_type, card_holder_name, transaction_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cardPayment.getPaymentId());
            stmt.setString(2, cardPayment.getCardType());
            stmt.setString(3, cardPayment.getCardHolderName());
            stmt.setString(4, cardPayment.getTransactionId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CardPayments findById(int cardPaymentId) {
        String sql = "SELECT * FROM CardPayments WHERE card_payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cardPaymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CardPayments cardPayment = new CardPayments();
                cardPayment.setCardPaymentId(rs.getInt("card_payment_id"));
                cardPayment.setPaymentId(rs.getInt("payment_id"));
                cardPayment.setCardType(rs.getString("card_type"));
                cardPayment.setCardHolderName(rs.getString("card_holder_name"));
                cardPayment.setTransactionId(rs.getString("transaction_id"));
                cardPayment.setCreatedAt(rs.getTimestamp("created_at"));
                cardPayment.setUpdatedAt(rs.getTimestamp("updated_at"));
                return cardPayment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<CardPayments> findAll() {
        List<CardPayments> cardPayments = new ArrayList<>();
        String sql = "SELECT * FROM CardPayments";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                CardPayments cardPayment = new CardPayments();
                cardPayment.setCardPaymentId(rs.getInt("card_payment_id"));
                cardPayment.setPaymentId(rs.getInt("payment_id"));
                cardPayment.setCardType(rs.getString("card_type"));
                cardPayment.setCardHolderName(rs.getString("card_holder_name"));
                cardPayment.setTransactionId(rs.getString("transaction_id"));
                cardPayment.setCreatedAt(rs.getTimestamp("created_at"));
                cardPayment.setUpdatedAt(rs.getTimestamp("updated_at"));
                cardPayments.add(cardPayment);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cardPayments;
    }

    @Override
    public void update(CardPayments cardPayment) {
        String sql = "UPDATE CardPayments SET payment_id = ?, card_type = ?, card_holder_name = ?, transaction_id = ? WHERE card_payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cardPayment.getPaymentId());
            stmt.setString(2, cardPayment.getCardType());
            stmt.setString(3, cardPayment.getCardHolderName());
            stmt.setString(4, cardPayment.getTransactionId());
            stmt.setInt(5, cardPayment.getCardPaymentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int cardPaymentId) {
        String sql = "DELETE FROM CardPayments WHERE card_payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cardPaymentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public CardPayments findByPaymentId(int paymentId) {
        String sql = "SELECT * FROM CardPayments WHERE payment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                CardPayments cardPayment = new CardPayments();
                cardPayment.setCardPaymentId(rs.getInt("card_payment_id"));
                cardPayment.setPaymentId(rs.getInt("payment_id"));
                cardPayment.setCardType(rs.getString("card_type"));
                cardPayment.setCardHolderName(rs.getString("card_holder_name"));
                cardPayment.setTransactionId(rs.getString("transaction_id"));
                cardPayment.setCreatedAt(rs.getTimestamp("created_at"));
                cardPayment.setUpdatedAt(rs.getTimestamp("updated_at"));
                return cardPayment;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}