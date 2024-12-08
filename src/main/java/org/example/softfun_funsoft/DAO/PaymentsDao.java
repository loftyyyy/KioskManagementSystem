package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.Payments;
import java.util.List;

public interface PaymentsDao {
    void save(Payments payment);
    Payments findById(int paymentId);
    List<Payments> findAll();
    void update(Payments payment);
    void delete(int paymentId);
}