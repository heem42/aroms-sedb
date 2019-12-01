package se.aroms;

public class dishlist3 {
    String dish;
    String order_id;
    String dishid;
    String chefid;

    public String getDishid() {
        return dishid;
    }

    public void setDishid(String dishid) {
        this.dishid = dishid;
    }


    public String getChefid() {
        return chefid;
    }

    public void setChefid(String chefid) {
        this.chefid = chefid;
    }


    public String getorder_id() {
        return order_id;
    }

    public void setOrderid(String order) {
        this.order_id = order;
    }


    public String getDish() {
        return dish;
    }

    public void setDish(String dishh) {
        this.dish = dishh;
    }

    public dishlist3( String dishh,String ord) {
        this.dish = dishh;
        this.order_id=ord;
    }
    public dishlist3( ) {

    }
}
