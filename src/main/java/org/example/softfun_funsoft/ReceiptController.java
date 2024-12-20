package org.example.softfun_funsoft;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
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

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.embed.swing.SwingFXUtils;

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

    @FXML
    private Label taxLabel;
    private int timeRemaining = 10;
    private Timeline timeline;
    private CurrentUser currentUser;
    private OrdersDaoImpl ordersDao;
    private CartItemDaoImpl cartItemDao;

    private FoodDaoImpl foodDao;

    private PaymentsDaoImpl paymentsDao;
    private CardPaymentsDaoImpl cardPaymentsDao;
    private ReceiptsDaoImpl receiptsDao;

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

        totalAmount = cartItemDao.getTotalAmountByCartId(currentUser.getCartId());
        taxAmount = totalAmount * 0.05;

        if (currentUser.getPaymentType().equals("Cash")) {
            paymentType.setText("Payment Type: Cash");
            cardType.setVisible(false);
            cardHolderName.setVisible(false);
            orderID.setText("Order ID: " + ordersDao.getOrderIdByUserId(currentUser.getUserId()));

            LocalDateTime currentDateTime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = currentDateTime.format(formatter);
            paymentDate.setText("Payment Date: " + formattedDateTime);


            taxLabel.setText(String.format("PHP: %.2f", taxAmount));
            grandTotal.setText(String.format("PHP: %.2f", totalAmount + taxAmount));
            subTotal.setText(String.format("PHP: %.2f", totalAmount));
            orderType.setText("Order Type: " + (currentUser.getDineIn() ? "Dine In" : "Take out"));
        } else {
            CardPayments cardPayment = cardPaymentsDao.findByPaymentId(paymentsDao.getPaymentIdByOrderId(ordersDao.getOrderIdByUserId(currentUser.getUserId())));
            orderID.setText("Order ID: " + cardPayment.getTransactionId());
            paymentDate.setText("Payment Date: " + cardPayment.getCreatedAt());
            cardHolderName.setText("Card Holder Name: " + cardPayment.getCardHolderName());
            cardType.setText("Card Type: " + cardPayment.getCardType());
            paymentType.setText("Payment Type: Card");
            taxLabel.setText(String.format("PHP: %.2f", taxAmount));
            grandTotal.setText(String.format("PHP: %.2f", totalAmount + taxAmount));
            subTotal.setText(String.format("PHP: %.2f", totalAmount));
            orderType.setText("Order Type: " + (currentUser.getDineIn() ? "Dine In" : "Take out"));
        }

        for (CartItem food : cartItemDao.findAllByCartId(currentUser.getCartId())) {
            Label itemLabel = new Label(String.format("%s x%d - PHP %.2f", foodDao.getFoodNameByFoodId(food.getFoodId()), food.getQuantity(), food.getPrice() * food.getQuantity()));
            itemLabel.setStyle("-fx-font-size: 15px; -fx-font-family: Monospaced");
            itemsContainer.getChildren().add(itemLabel);
        }

        updateStock();

    }

    public void startTimer() {
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (timeRemaining > 0) {
                        timeRemaining--;
                        timer.setText(timeRemaining + "s");
                    } else {
                        changeScene();
                        timeline.stop();
                    }
                })
        );
        timeline.setCycleCount(Timeline.INDEFINITE); 
        timeline.play();

    }


    private void updateStock() {
        for (CartItem food : cartItemDao.findAllByCartId(currentUser.getCartId())) {
            Food currentFood = foodDao.findById(food.getFoodId());
            currentFood.setStock(currentFood.getStock() - food.getQuantity());
            foodDao.update(currentFood);
        }
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

    public void printReceipt() throws IOException {
        WritableImage snapshot = receiptAnchorPane.snapshot(new SnapshotParameters(), null);
        File file = new File("receipt.png");
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", file);

        Image image = new Image(file.toURI().toString());

        ImageView imageView = new ImageView(image);

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(receiptAnchorPane.getScene().getWindow())) {
            boolean success = job.printPage(imageView);
            if (success) {
                job.endJob();
            }
        }
    }
}