package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.OrderItems;
import java.util.List;

public interface OrderItemsDao {
    void save(OrderItems orderItem);
    OrderItems findById(int orderItemId);
    List<OrderItems> findAll();
    void update(OrderItems orderItem);
    void delete(int orderItemId);
}