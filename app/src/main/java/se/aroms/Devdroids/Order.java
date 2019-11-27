package se.aroms.Devdroids;

import java.util.Date;
import java.util.List;

public class Order {
    private Date orderTIme;
    private List<items> orderItems;
    private String orderId;//unique order if
    private String uid;//table no
    private int status; //paid or not paid
    public Order(Date orderTIme, List<items> orderItems, String orderId, String uid,int status) {
        this.orderTIme = orderTIme;
        this.orderItems = orderItems;
        this.orderId = orderId;
        this.uid = uid;
        this.status=status;
    }

    public Date getOrderTIme() {
        return orderTIme;
    }

    public void setOrderTIme(Date orderTIme) {
        this.orderTIme = orderTIme;
    }

    public List<items> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<items> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
