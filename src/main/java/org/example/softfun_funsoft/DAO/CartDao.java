package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.Cart;
import java.util.List;

public interface CartDao {
    void save(Cart cart);
    Cart findById(int cartId);
    List<Cart> findAll();
    void update(Cart cart);
    void delete(int cartId);
}