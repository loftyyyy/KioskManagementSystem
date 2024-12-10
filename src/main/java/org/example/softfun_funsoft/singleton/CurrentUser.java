package org.example.softfun_funsoft.singleton;

public class CurrentUser {
    //TODO: Create an attribute for every primary key here in the Current User singleton for easy access. 
    private static CurrentUser instance;
    private String userId;
    private int cartId;
    private Boolean dineIn;

    private String paymentType;
    private CurrentUser() {
    }

    public static CurrentUser getInstance() {
        if (instance == null) {
            instance = new CurrentUser();
        }
        return instance;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getDineIn() {
        return dineIn;
    }
    public void setDineIn(Boolean dineIn) {
        this.dineIn = dineIn;
    }

    public int getCartId() {
        return cartId;
    }
    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}

