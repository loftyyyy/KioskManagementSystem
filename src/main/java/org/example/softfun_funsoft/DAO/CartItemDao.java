package org.example.softfun_funsoft.DAO;

import org.example.softfun_funsoft.model.CartItem;
import java.util.List;

public interface CartItemDao {
    void save(CartItem cartItem);
    CartItem findById(int cartItemId);
    List<CartItem> findAll();
    void update(CartItem cartItem);
    void delete(int cartItemId);
}