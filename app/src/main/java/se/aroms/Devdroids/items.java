package se.aroms.Devdroids;

public class items {
    private String ItemID;
    private String size;
    private Double price;
    private Double incured_price;

    public items(String itemID, String size, Double price, Double incured_price) {
        ItemID = itemID;
        this.size = size;
        this.price = price;
        this.incured_price = incured_price;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getIncured_price() {
        return incured_price;
    }

    public void setIncured_price(Double incured_price) {
        this.incured_price = incured_price;
    }
}
