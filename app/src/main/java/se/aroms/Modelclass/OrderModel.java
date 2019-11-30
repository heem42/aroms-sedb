package se.aroms.Modelclass;

public class OrderModel {

    private int status;
    private String orderId;


    public OrderModel(int status, String orderId) {
        this.status = status;
        this.orderId = orderId;
    }

    public OrderModel() {
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
