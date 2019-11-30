package se.aroms.Devdroids;

import java.io.Serializable;

public class items implements Serializable {
    private String ItemID;
    private String size;
    private Double price;
    private Double incured_price;
    private Long status;

    public items(String itemID, String size, Double price, Double incured_price,Long status) {
        ItemID = itemID;
        this.size = size;
        this.price = price;
        this.incured_price = incured_price;
        this.status = status;
    }
    public items(){}
    public items(items i) {
        this.ItemID = i.ItemID;
        this.incured_price = i.incured_price;
        this.price = i.price;
        this.size = i.size;
        this.status = i.status;

    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getIncured_price() {
        return incured_price;
    }

    public void setIncured_price(Double incured_price) {
        this.incured_price = incured_price;
    }
}
