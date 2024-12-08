package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.Orders;
import java.util.List;

public interface OrdersDao {
    void save(Orders order);
    Orders findById(int orderId);
    List<Orders> findAll();
    void update(Orders order);
    void delete(int orderId);
    List<Orders> findByUserId(String userId);
}