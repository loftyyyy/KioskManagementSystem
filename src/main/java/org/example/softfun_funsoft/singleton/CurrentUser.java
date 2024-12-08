package org.example.softfun_funsoft.singleton;

public class CurrentUser {
    //TODO: Create an attribute for every primary key here in the Current User singleton for easy access. 
    private static CurrentUser instance;
    private String userId;

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
}
