package se.aroms.Devdroids;

public class editOrder {
    String orderId;
    String status;//1 new dishes added 2 items quantity removed 3 3 items quantity increased

    public editOrder() {
    }

    public editOrder(String orderId, String status) {
        this.orderId = orderId;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
