package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.CardPayments;
import java.util.List;

public interface CardPaymentsDao {
    void save(CardPayments cardPayment);
    CardPayments findById(int cardPaymentId);
    List<CardPayments> findAll();
    void update(CardPayments cardPayment);
    void delete(int cardPaymentId);
}