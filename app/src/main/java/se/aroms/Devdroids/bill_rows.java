package se.aroms.Devdroids;

public class bill_rows {
    private String name;
    private String qunatity;
    private Double price;
   private String dishId;
   private String size;

    public bill_rows(String name, String qunatity, Double price,String dishId,String size) {
        this.name = name;
        this.qunatity = qunatity;
        this.price = price;
        this.dishId=dishId;
        this.size=size;
    }

    public String getDishId() {
        return dishId;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQunatity() {
        return qunatity;
    }

    public void setQunatity(String qunatity) {
        this.qunatity = qunatity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
