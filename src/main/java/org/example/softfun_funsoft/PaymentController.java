package org.example.softfun_funsoft;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import org.example.softfun_funsoft.DaoImpl.*;
import org.example.softfun_funsoft.model.Orders;
import org.example.softfun_funsoft.model.Payments;
import org.example.softfun_funsoft.model.Receipts;
import org.example.softfun_funsoft.singleton.CurrentUser;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentController implements Initializable {
    @FXML
    private StackPane rootStackPane;

    private CurrentUser currentUser;
    private OrdersDaoImpl ordersDao;
    private CartItemDaoImpl cartItemDao;
    private PaymentsDaoImpl paymentsDao;
    private ReceiptsDaoImpl receiptsDao;
    private ExecutorService executorService;
    private FoodDaoImpl foodDao;

    public void cardPayment() {
        currentUser.setPaymentType("Card");
        proceedToPaymentPage();
    }

    public void cashPayment() {
        currentUser.setPaymentType("Cash");
        proceedToPaymentPage();
    }

    private void proceedToPaymentPage() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() {
                try {
                    if (currentUser.getPaymentType().equals("Card")) {
                        Platform.runLater(() -> loadPage("CardPayment.fxml"));
                    } else {
                        // Perform all database operations here
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

                        Platform.runLater(() -> loadPage("Receipt.fxml"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };

        executorService.submit(task);
    }

    private void loadPage(String fxmlFile) {
        try {
            Parent newRoot = FXMLLoader.load(getClass().getResource(fxmlFile));
            Node currentRoot = rootStackPane.getChildren().get(rootStackPane.getChildren().size() - 1);
            FadeTransition fadeOut = new FadeTransition(Duration.millis(100), currentRoot);
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
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        foodDao = new FoodDaoImpl();
        currentUser = CurrentUser.getInstance();
        cartItemDao = new CartItemDaoImpl();
        ordersDao = new OrdersDaoImpl();
        paymentsDao = new PaymentsDaoImpl();
        receiptsDao = new ReceiptsDaoImpl();
        executorService = Executors.newFixedThreadPool(2); // Limit threads to avoid overload
    }

    public void shutdownExecutor() {
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }
}
