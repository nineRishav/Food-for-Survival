package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail {

    @SerializedName("item_name")
    @Expose
    private String itemName;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("price")
    @Expose
    private Integer price;
    @SerializedName("seller_name")
    @Expose
    private String sellerName;
    @SerializedName("item_url")
    @Expose
    private String itemUrl;

    public ProductDetail(String itemName, Integer quantity, Integer price, String sellerName, String itemUrl) {
        super();
        this.itemName = itemName;
        this.quantity = quantity;
        this.price = price;
        this.sellerName = sellerName;
        this.itemUrl = itemUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getItemUrl() {

        return itemUrl;
    }

    public void setItemUrl(String itemUrl) {
        this.itemUrl = itemUrl;
    }

}

