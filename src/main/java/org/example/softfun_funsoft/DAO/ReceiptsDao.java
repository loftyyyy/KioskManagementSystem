package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.Receipts;
import java.util.List;

public interface ReceiptsDao {
    void save(Receipts receipt);
    Receipts findById(int receiptId);
    List<Receipts> findAll();
    void update(Receipts receipt);
    void delete(int receiptId);
}