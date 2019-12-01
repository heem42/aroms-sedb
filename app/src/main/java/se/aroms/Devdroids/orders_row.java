package se.aroms.Devdroids;

public class orders_row {
    private String dishName;
    private String quantity;
    private String dishId;
    private String size;

    public orders_row(String dishName, String quantity, String dishId,String size) {
        this.dishName = dishName;
        this.quantity = quantity;
        this.dishId = dishId;
        this.size=size;
    }
    public orders_row(){

    }

    public String getDishName() {
        return dishName;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }
}
