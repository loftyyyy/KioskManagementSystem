package org.example.softfun_funsoft;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import org.example.softfun_funsoft.DaoImpl.CartItemDaoImpl;
import org.example.softfun_funsoft.DaoImpl.FoodDaoImpl;
import org.example.softfun_funsoft.listener.MyCartItemListener;
import org.example.softfun_funsoft.model.CartItem;
import org.example.softfun_funsoft.model.Food;

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

    public void setData(Food food, MyCartItemListener myCartItemListener) {
        //TODO: Fix this error here
        this.myCartItemListener = myCartItemListener;
        this.cartItemDaoImpl = new CartItemDaoImpl(); // Initialize CartItemDaoImpl
        this.foodDaoImpl = new FoodDaoImpl(); // Initialize FoodDaoImpl
        CartItem currentCartItem = cartItemDaoImpl.getCartItemByFoodId(food.getFoodId());

        //Get CardItemIdByFoodId
//        number = cartItemDaoImpl.findAll().indexOf(cartItem) + 1; // Use CartItemDaoImpl to get the index
//        food = foodDaoImpl.findById(cartItem.getFoodId()); // Assuming you have a method to get Food by ID
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