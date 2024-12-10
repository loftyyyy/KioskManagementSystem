package org.example.softfun_funsoft;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.example.softfun_funsoft.DaoImpl.CartDaoImpl;
import org.example.softfun_funsoft.DaoImpl.CartItemDaoImpl;
import org.example.softfun_funsoft.DaoImpl.FoodDaoImpl;
import org.example.softfun_funsoft.listener.MyCartItemListener;
import org.example.softfun_funsoft.model.CartItem;
import org.example.softfun_funsoft.model.Food;

import java.util.List;

public class CartItemController {
    @FXML
    private ImageView img;

    @FXML
    private Label itemName;

    @FXML
    private Label itemPrice;

    @FXML
    private Label itemQuantity;

    @FXML
    private Label totalPrice;

    @FXML
    private Button removeBTN;

    @FXML
    private Label itemNumber;

    private CartItem cartItem;
    private MyCartItemListener myCartItemListener;
    private CartItemDaoImpl cartItemDaoImpl;
    private FoodDaoImpl foodDaoImpl;
    private int number;
    private CartDaoImpl cartDao;

public void setData(Food food, MyCartItemListener myCartItemListener) {
    this.myCartItemListener = myCartItemListener;
    this.cartItemDaoImpl = new CartItemDaoImpl(); // Initialize CartItemDaoImpl
    this.foodDaoImpl = new FoodDaoImpl(); // Initialize FoodDaoImpl
    this.cartDao = new CartDaoImpl(); // Initialize CartDaoImpl

    CartItem currentCartItem = cartItemDaoImpl.getCartItemByFoodId(food.getFoodId());
    int currentCartId = currentCartItem.getCartId(); // Get the current cart ID
    List<CartItem> cartItems = cartItemDaoImpl.findAllByCartId(currentCartId); // Get all cart items for the current cart

    // Find the index of the current cart item
    number = 1; // Initialize number to 1
    for (CartItem item : cartItems) {
        if (item.getFoodId() == currentCartItem.getFoodId()) {
            break;
        }
        number++;
    }

    itemName.setText(food.getName());
    itemNumber.setText(String.valueOf(number));
    itemQuantity.setText(String.valueOf(currentCartItem.getQuantity()));
    itemPrice.setText("PHP " + food.getPrice());
    totalPrice.setText("PHP " + (food.getPrice() * currentCartItem.getQuantity()));
    img.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(food.getImgSrc())));

    removeBTN.setOnAction(e -> {
        myCartItemListener.onRemoveItem(food);
    });
}
}