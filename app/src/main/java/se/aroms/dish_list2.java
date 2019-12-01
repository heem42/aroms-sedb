package se.aroms;

public class dish_list2 {
    String dish;
    String order_id;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getDish() {
        return dish;
    }

    public void setDish(String dishh) {
        this.dish = dishh;
    }

    public dish_list2( String dishh,String id) {
        this.dish = dishh;
        this.order_id=id;
    }
    public dish_list2( ) {

    }

}
