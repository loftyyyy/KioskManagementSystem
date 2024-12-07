package org.example.softfun_funsoft.model;

public class FoodCategory {
    private int categoryId;

    private String name;
    private String imgSrc;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int category_id) {
        this.categoryId = category_id;
    }
}
