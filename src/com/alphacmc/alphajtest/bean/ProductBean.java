package com.alphacmc.alphajtest.bean;

public class ProductBean {
    private int productId;
    private String productName;
    private int productPrice;
    private int productStock;
    
    public void setProductName(String productName) {
        this.productName = productName;
    }
    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }
    public void setProductId(int productId) {
        this.productId = productId;
    }
    public void setProductStock(int productStock) {
        this.productStock = productStock;
    }
    public String getProductName() {
        return productName;
    }
    public int getProductPrice() {
        return productPrice;
    }
    public int getProductStock() {
        return productStock;
    }
    public int getProductId() {
        return productId;
    }

}
