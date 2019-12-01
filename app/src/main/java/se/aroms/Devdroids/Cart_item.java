package se.aroms.Devdroids;



public class Cart_item {
    private cart_Items Items;
    private int quantity;
    private int maxQuantity;
    private int localQuantity;

    public Cart_item(cart_Items items, int quantity,int maxQuantity) {
        Items = items;
        this.quantity = quantity;
        this.maxQuantity=maxQuantity;
        localQuantity=0;
    }
    public Cart_item(){

    }

    public int getLocalQuantity() {
        return localQuantity;
    }

    public void setLocalQuantity(int localQuantity) {
        this.localQuantity = localQuantity;
    }

    public int getMaxQuantity() {
        return maxQuantity;
    }

    public void setMaxQuantity(int maxQuantity) {
        this.maxQuantity = maxQuantity;
    }

    public cart_Items getItems() {
        return Items;
    }

    public void setItems(cart_Items items) {
        Items = items;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

