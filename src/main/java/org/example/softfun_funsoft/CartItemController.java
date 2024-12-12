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
import org.example.softfun_funsoft.singleton.CurrentUser;

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

    private Food food;
    private CurrentUser currentUser;
    public void setData(Food food, MyCartItemListener myCartItemListener) {
        this.food = food;
        this.myCartItemListener = myCartItemListener;
        this.cartItemDaoImpl = new CartItemDaoImpl(); // Initialize CartItemDaoImpl
        this.foodDaoImpl = new FoodDaoImpl(); // Initialize FoodDaoImpl
        this.cartDao = new CartDaoImpl(); // Initialize CartDaoImpl
        this.currentUser = CurrentUser.getInstance();

        CartItem cartItem = cartItemDaoImpl.getCartItemByFoodId(food.getFoodId(), currentUser.getCartId());
        System.out.println("Food ID: " + food.getFoodId());
        System.out.println("Cart Item: " + cartItem.getFoodId());

        List<CartItem> cartItems = cartItemDaoImpl.findAllByCartId(currentUser.getCartId()); // Get all cart items for the current cart

        // Find the index of the current cart item
        number = 1; // Initialize number to 1
        for (CartItem item : cartItems) {
            if (item.getFoodId() == cartItem.getFoodId()) {
                break;
            }
            number++;
        }

        itemName.setText(food.getName());
        itemNumber.setText(String.valueOf(number));
        System.out.println("Item Quantity In the Controller: " + cartItem.getQuantity());
        itemQuantity.setText(String.valueOf(cartItem.getQuantity()));
        itemPrice.setText(String.valueOf(food.getPrice()));
        totalPrice.setText(String.valueOf(food.getPrice() * cartItem.getQuantity()));
        img.setImage(new javafx.scene.image.Image(getClass().getResourceAsStream(food.getImgSrc())));

        removeBTN.setOnAction(e -> {
            myCartItemListener.onRemoveItem(this.food);
        });
    }
}