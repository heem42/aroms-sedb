package se.aroms.Devdroids;

import java.io.Serializable;

public class order_queue_items implements Serializable {
    private String ItemID;
    private String size;
    private String expected_time;//actual time for cooking
    private String required_time;// time required to start cooking in queue time
    private String status;
    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public order_queue_items() {
    }

    public order_queue_items(String itemID, String size, String expected_time, String required_time, String status,Double price) {
        this.ItemID = itemID;
        this.size = size;
        this.expected_time = expected_time;
        this.required_time = required_time;
        this.status=status;
        this.price = price;
    }
    public order_queue_items(order_queue_items i){
        this.expected_time = i.expected_time;
        this.ItemID = i.ItemID;
        this.price = i.price;
        this.required_time = i.required_time;
        this.size = i.size;
        this.status = i.status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public String getExpected_time() {
        return expected_time;
    }

    public void setExpected_time(String expected_time) {
        this.expected_time = expected_time;
    }

    public String getRequired_time() {
        return required_time;
    }

    public void setRequired_time(String required_time) {
        this.required_time = required_time;
    }
}
