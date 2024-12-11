package org.example.softfun_funsoft;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.softfun_funsoft.DaoImpl.*;
import org.example.softfun_funsoft.model.CardPayments;
import org.example.softfun_funsoft.model.CartItem;
import org.example.softfun_funsoft.model.Food;
import org.example.softfun_funsoft.singleton.CardReceiptData;

import javafx.print.PrinterJob;
import org.example.softfun_funsoft.singleton.CurrentUser;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ReceiptController implements Initializable {
    @FXML
    private AnchorPane receiptAnchorPane;

    @FXML
    private VBox itemsContainer;

    @FXML
    private Label cardHolderName;

    @FXML
    private Label cardType;

    @FXML
    private Label grandTotal;

    @FXML
    private Label orderID;

    @FXML
    private Label paymentDate;

    @FXML
    private Label paymentType;

    @FXML
    private Label subTotal;

    @FXML
    private Label orderType;

    @FXML
    private Label timer;
    private int timeRemaining = 10;
    private Timeline timeline;
    private CurrentUser currentUser;
    private OrdersDaoImpl ordersDao;
    private CartItemDaoImpl cartItemDao;

    private FoodDaoImpl foodDao;

    private PaymentsDaoImpl paymentsDao;
    private CardPaymentsDaoImpl cardPaymentsDao;
    private ReceiptsDaoImpl receiptsDao;

    private Label taxLabel;
    private Double totalAmount;
    private Double taxAmount;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        currentUser = CurrentUser.getInstance();

        foodDao = new FoodDaoImpl();
        currentUser = CurrentUser.getInstance();
        cartItemDao = new CartItemDaoImpl();
        ordersDao = new OrdersDaoImpl();
        paymentsDao = new PaymentsDaoImpl();
        cardPaymentsDao = new CardPaymentsDaoImpl();
        receiptsDao = new ReceiptsDaoImpl();
        currentUser = CurrentUser.getInstance();

        if(currentUser.getPaymentType().equals("Cash")) {
            paymentType.setText("Payment Type: Cash");
            cardType.setVisible(false);
            cardHolderName.setVisible(false);
            orderID.setText("Order ID: " + ordersDao.getOrderIdByUserId(currentUser.getUserId()));

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            paymentDate.setText("Payment Date: " + formattedDateTime);

            totalAmount = cartItemDao.getTotalAmountByCartId(currentUser.getCartId());
            taxAmount = totalAmount * 0.05;

            taxLabel.setText("PHP " +  taxAmount);
            grandTotal.setText("PHP " + (totalAmount + taxAmount));
            subTotal.setText("PHP " + totalAmount);
            orderType.setText("Order Type: " + (currentUser.getDineIn() ? "Dine In" : "Take out"));
        } else {
            CardPayments cardPayment = cardPaymentsDao.findByPaymentId(paymentsDao.getPaymentIdByOrderId(ordersDao.getOrderIdByUserId(currentUser.getUserId())));
            orderID.setText("Order ID: " + cardPayment.getTransactionId());
            paymentDate.setText("Payment Date: " + cardPayment.getCreatedAt());
            cardHolderName.setText("Card Holder Name: " + cardPayment.getCardHolderName());
            cardType.setText("Card Type: " + cardPayment.getCardType());
            paymentType.setText("Payment Type: Card");
            taxLabel.setText("PHP " +  taxAmount);
            grandTotal.setText("PHP " + (totalAmount + taxAmount));
            subTotal.setText("PHP " + totalAmount);
            orderType.setText("Order Type: " + (currentUser.getDineIn() ? "Dine In" : "Take out"));
        }

        for (CartItem food : cartItemDao.findAllByCartId(currentUser.getCartId())) {
            Label itemLabel = new Label(String.format("%s x%d - PHP %.2f", foodDao.getFoodNameByFoodId(food.getFoodId()), food.getQuantity(), food.getPrice() * food.getQuantity()));
            itemLabel.setStyle("-fx-font-size: 15px; -fx-font-family: Monospaced");
            itemsContainer.getChildren().add(itemLabel);
        }

    }

    public void startTimer() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    // Update the label every second
                    if (timeRemaining > 0) {
                        timeRemaining--;
                        timer.setText(timeRemaining + "s");
                    } else {
                        // Call the method when the timer reaches zero
                        changeScene();
                        timeline.stop(); // Stop the timer when finished
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); // Run indefinitely until stopped
        timeline.play(); // Start the timer
    }

    private void changeScene() {
        System.out.println("changing scenes");
        try {
            Stage currentStage = (Stage) receiptAnchorPane.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bye.fxml"));
            Parent newRoot = fxmlLoader.load();
            Scene currentScene = currentStage.getScene();

            // Preload the new scene
            newRoot.setOpacity(0);
            currentScene.setRoot(newRoot);

            // Create a fade-in transition for the new scene
            FadeTransition fadeIn = new FadeTransition(Duration.millis(500), newRoot);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printReceipt() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(receiptAnchorPane.getScene().getWindow())) {
            boolean success = job.printPage(receiptAnchorPane);
            if (success) {
                job.endJob();
            }
        }
    }
}