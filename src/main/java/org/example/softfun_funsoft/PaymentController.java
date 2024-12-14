package org.example.softfun_funsoft;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.softfun_funsoft.DaoImpl.*;
import org.example.softfun_funsoft.lang.LangCheck;
import org.example.softfun_funsoft.model.Orders;
import org.example.softfun_funsoft.model.Payments;
import org.example.softfun_funsoft.model.Receipts;
import org.example.softfun_funsoft.singleton.CurrentUser;
import org.example.softfun_funsoft.utils.SoundManager;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.concurrent.Task;

public class PaymentController implements Initializable {
    @FXML
    private StackPane rootStackPane;

    @FXML
    private Button cashBTN;

    private MediaPlayer soundPlayer;

    CurrentUser currentUser;

    private OrdersDaoImpl ordersDao;
    private CartItemDaoImpl cartItemDao;

    private FoodDaoImpl foodDao;

    private PaymentsDaoImpl paymentsDao;
    private CardPaymentsDaoImpl cardPaymentsDao;
    private ReceiptsDaoImpl receiptsDao;
    public void cardPayment() {
        currentUser.setPaymentType("Card");
        proceedToPaymentPage();

    }

    public void cashPayment() {
        currentUser.setPaymentType("Cash");
        proceedToPaymentPage();


    }


private void proceedToPaymentPage() {
    Task<Void> task = new Task<Void>() {
        @Override
        protected Void call() throws Exception {
            if (currentUser.getPaymentType().equals("Card")) {
                Platform.runLater(() -> {
                    try {
                        Parent newRoot = FXMLLoader.load(getClass().getResource("CardPayment.fxml"));
                        Node currentRoot = rootStackPane.getChildren().get(rootStackPane.getChildren().size() - 1);
                        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currentRoot);
                        fadeOut.setFromValue(1.0);
                        fadeOut.setToValue(0.0);
                        fadeOut.setOnFinished(e -> {
                            rootStackPane.getChildren().remove(currentRoot);
                            rootStackPane.getChildren().add(newRoot);
                        });
                        fadeOut.play();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } else {
                try {
                    double totalFoodAmount = cartItemDao.getTotalAmountByCartId(currentUser.getCartId());
                    double taxAmount = totalFoodAmount * 0.05;
                    Orders order = new Orders();
                    order.setUserId(currentUser.getUserId());
                    order.setDineIn(currentUser.getDineIn());
                    order.setPaymentType(currentUser.getPaymentType());
                    order.setTotalAmount(totalFoodAmount + taxAmount);
                    ordersDao.save(order);

                    Payments payments = new Payments();
                    payments.setOrderId(ordersDao.getOrderIdByUserId(currentUser.getUserId()));
                    payments.setAmount(totalFoodAmount + taxAmount);
                    paymentsDao.save(payments);

                    Receipts receipts = new Receipts();
                    receipts.setOrderId(ordersDao.getOrderIdByUserId(currentUser.getUserId()));
                    receipts.setReceiptUrl("Not Applicable, Only for Card Payment");
                    receiptsDao.save(receipts);

                    Platform.runLater(() -> {
                        try {
                            Parent newRoot = FXMLLoader.load(getClass().getResource("Receipt.fxml"));
                            Node currentRoot = rootStackPane.getChildren().get(rootStackPane.getChildren().size() - 1);
                            FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currentRoot);
                            fadeOut.setFromValue(1.0);
                            fadeOut.setToValue(0.0);
                            fadeOut.setOnFinished(e -> {
                                rootStackPane.getChildren().remove(currentRoot);
                                rootStackPane.getChildren().add(newRoot);
                            });
                            fadeOut.play();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    };

    new Thread(task).start();
}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        foodDao = new FoodDaoImpl();
        currentUser = CurrentUser.getInstance();
        cartItemDao = new CartItemDaoImpl();
        ordersDao = new OrdersDaoImpl();
        paymentsDao = new PaymentsDaoImpl();
        cardPaymentsDao = new CardPaymentsDaoImpl();
        receiptsDao = new ReceiptsDaoImpl();
        currentUser = CurrentUser.getInstance();
        SoundManager.playPaymentType();

    }
}
