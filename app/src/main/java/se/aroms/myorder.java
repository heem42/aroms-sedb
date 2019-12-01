package se.aroms;

public class myorder {
    String orderid;
    String itemid;

    public int getItemindex() {
        return itemindex;
    }

    public void setItemindex(int itemindex) {
        this.itemindex = itemindex;
    }

    int itemindex;
    int total;
    int complete;//to change order status

    public myorder(String orderid, String itemid, int total, int complete) {
        this.orderid = orderid;
        this.itemid = itemid;
        this.total = total;
        this.complete = complete;
    }

    public myorder(String orderid, String itemid,int i) {
        this.orderid = orderid;
        this.itemid = itemid;
        this.itemindex=i;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }



    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

}
