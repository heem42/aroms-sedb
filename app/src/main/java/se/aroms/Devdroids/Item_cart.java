package se.aroms.Devdroids;

public class Item_cart {
    private String ItemID;
    private String size;
    private String name;
    private String desp;
    private String picture;
    private String time;

    public Item_cart(String itemID, String size, String name, String desp, String picture,String time) {
        ItemID = itemID;
        this.size = size;
        this.name = name;
        this.desp = desp;
        this.picture = picture;
        this.time=time;
    }

    public Item_cart() {
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
