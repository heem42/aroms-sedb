package se.aroms;

import java.util.Date;
import java.util.List;

public class order {
    private Date orderTIme;
    private List<item> orderItems;
    private String orderId;//unique order if
    private String uid;//table no
    private String order_status;// 0 in queue 1 cooking (2 cooked )

    public order() {
    }

    public order(Date orderTIme, List<item> orderItems, String orderId, String uid, String order_status) {
        this.orderTIme = orderTIme;
        this.orderItems = orderItems;
        this.orderId = orderId;
        this.uid = uid;
        this.order_status = order_status;
    }

    public Date getOrderTIme() {
        return orderTIme;
    }

    public void setOrderTIme(Date orderTIme) {
        this.orderTIme = orderTIme;
    }

    public List<item> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<item> orderItems) {
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
    public void addItem(item item)
    {
        this.orderItems.add(item);
    }
}//orderQueue object to store an order



/*public class order {
    String orderid;
    String itemid;

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getChef() {
        return chef;
    }

    public void setChef(String chef) {
        this.chef = chef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String priority;
    String chef;
    String status;

    public order(String orderid, String itemid, String priority, String chef, String status) {
        this.orderid = orderid;
        this.itemid = itemid;
        this.priority = priority;
        this.chef = chef;
        this.status = status;
    }
    public order() {
        }


}
*/