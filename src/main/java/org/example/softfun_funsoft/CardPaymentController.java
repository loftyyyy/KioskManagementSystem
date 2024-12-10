package org.example.softfun_funsoft;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.jfoenix.controls.JFXButton;
import javafx.animation.FadeTransition;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.example.softfun_funsoft.DaoImpl.CartItemDaoImpl;
import org.example.softfun_funsoft.DaoImpl.OrdersDaoImpl;
import org.example.softfun_funsoft.model.CartItem;
import org.example.softfun_funsoft.model.Orders;
import org.example.softfun_funsoft.singleton.CardReceiptData;
import org.example.softfun_funsoft.singleton.CurrentUser;
import org.example.softfun_funsoft.utils.SoundManager;
import org.json.JSONObject;
import com.jfoenix.controls.JFXComboBox;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import org.example.softfun_funsoft.model.Food;
import org.example.softfun_funsoft.payment.Card;

import java.io.IOException;
import javafx.scene.web.WebView;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CardPaymentController implements Initializable {
    @FXML
    private StackPane rootStackPane;

    @FXML
    private TextField cardCVCField;

    @FXML
    private TextField cardHolderNameField;

    @FXML
    private TextField cardMMYYField;

    @FXML
    private TextField cardNumberField;

    @FXML
    private TextField emailField;

    @FXML
    private Label grandTotal;

    @FXML
    private TextArea orderItemsArea;

    @FXML
    private AnchorPane progressPane;

    @FXML
    private ComboBox<String> regionComboBox;

    @FXML
    private TextField zipField;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private Button cancelBTN;

    @FXML
    private JFXButton continueBTN;

    @FXML
    private Button paymentBTN;

    @FXML
    private Label taxLabel;


    @FXML
    private Hyperlink bankReceiptLink;

    @FXML
    private AnchorPane successPane;

    @FXML
    private WebView webViewLink;

    private String receiptLink;

    private OrdersDaoImpl ordersDao;
    private CurrentUser currentUser;
    private CartItemDaoImpl cartItemDao;

    //TODO: implement another scene for the receipt

    public void submit() {
        if (emailField.getText().isEmpty() || cardNumberField.getText().isEmpty() || cardHolderNameField.getText().isEmpty() || cardMMYYField.getText().isEmpty() || cardCVCField.getText().isEmpty() || zipField.getText().isEmpty() || regionComboBox.getValue() == null) {
            showError("Please fill out all fields");
        } else {
            Platform.runLater(() -> progressPane.setVisible(true));
            Card cardPayment = new Card();
            Order order = Order.getInstance();
            CardReceiptData cardReceiptData = CardReceiptData.getInstance();
            new Thread(() -> {
                try {
                    System.out.println(cardNumberField.getText().replaceAll("\\s+", ""));
                    Charge charge = cardPayment.createCharge("tok_visa", (int) order.getGrandTotal(), emailField.getText(), cardHolderNameField.getText(), cardNumberField.getText().replaceAll("\\s+", ""), cardCVCField.getText(), cardMMYYField.getText(), regionComboBox.getValue(), zipField.getText());
                    String chargeJson = charge.toJson();

                    JSONObject jsonObject = new JSONObject(chargeJson);
                    JSONObject metadata = jsonObject.getJSONObject("metadata");

                    long createdTimestamp = jsonObject.getLong("created");
                    LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(createdTimestamp), ZoneId.systemDefault());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    String formattedDateTime = dateTime.format(formatter);
                    System.out.println("Payment Date and Time: " + formattedDateTime);
                    receiptLink = jsonObject.getString("receipt_url");
                    System.out.println(metadata.getString("cardholder_name"));

                    String cardType = charge.getPaymentMethodDetails().getCard().getBrand();

                    cardReceiptData.setReceiptId(jsonObject.getString("id"));
                    cardReceiptData.setReceiptUrl(receiptLink);
                    cardReceiptData.setCardHolderName(metadata.getString("cardholder_name"));
                    cardReceiptData.setPaymentDateTime(formattedDateTime);
                    cardReceiptData.setCardType(cardType);

//                    order.setOrderID(jsonObject.getString("id"));

                    Platform.runLater(() -> {
                        successPane.setVisible(true);
                        cancelBTN.setDisable(true);
                        paymentBTN.setDisable(true);

                        bankReceiptLink.setOnAction(event -> {
                            try {
                                WebEngine engine = webViewLink.getEngine();

                                if (webViewLink.isVisible()) {
                                    webViewLink.setVisible(false);
                                    bankReceiptLink.setText("Show Bank Receipt");
                                } else {
                                    webViewLink.setVisible(true);
                                    bankReceiptLink.setText("Hide Bank Receipt");
                                    engine.load(receiptLink);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    });
                } catch (IllegalArgumentException e) {
                    Platform.runLater(() -> showError(e.getMessage()));
                } catch (StripeException e) {
                    Platform.runLater(() -> showError(e.getMessage()));
                } finally {
                    Platform.runLater(() -> progressPane.setVisible(false));
                }
            }).start();
        }
    }
    public void createOrder(){
        Orders order = new Orders();

        order.setUserId(currentUser.getUserId());
        order.setDineIn(currentUser.getDineIn());
        order.setTotalAmount();


        ordersDao.save(order);

    }


    public void setContinueBTN() throws IOException {
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

    }
    public void setCancelBTN() throws IOException {
        Parent newRoot = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));

        Node currentRoot = rootStackPane.getChildren().get(rootStackPane.getChildren().size() - 1);

        FadeTransition fadeOut = new FadeTransition(Duration.millis(500), currentRoot);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setOnFinished(e -> {

            rootStackPane.getChildren().remove(currentRoot);
            rootStackPane.getChildren().add(newRoot);

        });

        SoundManager.playRemove();
        fadeOut.play();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String generateReceiptLayout() {
        StringBuilder receipt = new StringBuilder();
        for (CartItem food : cartItemDao.findAllByCartId(currentUser.getCartId())) {
            receipt.append(String.format("%s x%d - PHP %.2f\n", food.getName(), food.getQuantity(), food.getPrice() * food.getQuantity()));
        }
        return receipt.toString();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        currentUser = CurrentUser.getInstance();
        cartItemDao = new CartItemDaoImpl();
        ordersDao = new OrdersDaoImpl();
        regionComboBox.setValue("Philippines");

        regionComboBox.getItems().addAll(
                Arrays.stream(Locale.getISOCountries())
                        .map(countryCode -> new Locale("", countryCode).getDisplayCountry())
                        .collect(Collectors.toList())
        );

        orderItemsArea.setText(generateReceiptLayout());
        grandTotal.setStyle("-fx-text-fill: green;");
        taxLabel.setText("Tax: PHP " + cartItemDao.getTotalAmountByCartId(currentUser.getCartId()) * 0.05 );
        grandTotal.setText("Total: PHP " + order.getGrandTotal());
    }
}