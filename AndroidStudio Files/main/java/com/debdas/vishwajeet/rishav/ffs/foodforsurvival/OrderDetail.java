package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetail {

    @SerializedName("item_name")
    @Expose
    private String SitemName;
    @SerializedName("quantity")
    @Expose
    private Integer Squantity;
    @SerializedName("buyer_name")
    @Expose
    private String SbuyerName;
    @SerializedName("order_no")
    @Expose
    private Integer SOrder_no;
    @SerializedName("price")
    @Expose
    private Integer Sprice;

    public OrderDetail(String sitemName, Integer squantity, String sbuyerName, Integer sorder_no, Integer sprice) {
        SitemName = sitemName;
        Squantity = squantity;
        SbuyerName = sbuyerName;
        SOrder_no = sorder_no;
        Sprice = sprice;
    }

    public String getSitemName() {
        return SitemName;
    }

    public void setSitemName(String sitemName) {
        SitemName = sitemName;
    }

    public Integer getSprice() {
        return Sprice;
    }

    public void setSprice(Integer sprice) {
        Sprice = sprice;
    }

    public Integer getSquantity() {
        return Squantity;
    }

    public void setSquantity(Integer squantity) {
        Squantity = squantity;
    }

    public String getSbuyerName() {
        return SbuyerName;
    }

    public void setSbuyerName(String sbuyerName) {
        SbuyerName = sbuyerName;
    }

    public Integer getSOrder_no() {
        return SOrder_no;
    }

    public void setSOrder_no(Integer SOrder_no) {
        this.SOrder_no = SOrder_no;
    }
}

