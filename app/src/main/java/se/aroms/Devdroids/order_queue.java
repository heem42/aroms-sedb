package se.aroms.Devdroids;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class order_queue implements Serializable {
    private Date orderTIme;
    private List<order_queue_items> orderItems;
    private String orderId;//unique order if
    private String uid;//table no
    private String order_status;// 0 in queue 1 cooking 2 cooked
    private String priority;
    private String complimentaryDish;

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getComplimentaryDish() {
        return complimentaryDish;
    }

    public void setComplimentaryDish(String complimentaryDish) {
        this.complimentaryDish = complimentaryDish;
    }

    public order_queue() {
    }

    public order_queue(Date orderTIme, List<order_queue_items> orderItems, String orderId, String uid, String order_status, String priority, String complimentaryDish) {
        this.orderTIme = orderTIme;
        this.orderItems = orderItems;
        this.orderId = orderId;
        this.uid = uid;
        this.order_status = order_status;
        this.priority = priority;
        this.complimentaryDish = complimentaryDish;
    }
    public order_queue(order_queue o) {
        this.orderTIme = o.orderTIme;
        this.orderItems = o.orderItems;
        this.orderId = o.orderId;
        this.uid = o.uid;
        this.order_status = o.order_status;
        this.priority = o.priority;
        this.complimentaryDish = o.complimentaryDish;
    }

    public Date getOrderTIme() {
        return orderTIme;
    }

    public void setOrderTIme(Date orderTIme) {
        this.orderTIme = orderTIme;
    }

    public List<order_queue_items> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<order_queue_items> orderItems) {
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

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
    public void addItem(order_queue_items item)
    {
        this.orderItems.add(item);
    }
}
