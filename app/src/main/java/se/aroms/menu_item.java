package se.aroms;

public class menu_item {

    public String uid;
    public String name;
    public String reg_price;
    public String large_price;
    public String reg_price_incurred;
    public String large_price_incurred;
    public String description;
    public String time;
    public String type;
    public String picture;

    menu_item(){}




    public menu_item(String uid, String name, String reg_price, String large_price, String reg_price_incurred, String large_price_incurred, String description, String time, String type, String picture) {
        this.uid = uid;
        this.name = name;
        this.reg_price = reg_price;
        this.large_price = large_price;
        this.reg_price_incurred = reg_price_incurred;
        this.large_price_incurred = large_price_incurred;
        this.description = description;
        this.time = time;
        this.type = type;
        this.picture = picture;
    }

    public String getUid() {
        return uid;
    }

    public String getReg_price_incurred() {
        return reg_price_incurred;
    }

    public void setReg_price_incurred(String reg_price_incurred) {
        this.reg_price_incurred = reg_price_incurred;
    }

    public String getLarge_price_incurred() {
        return large_price_incurred;
    }

    public void setLarge_price_incurred(String large_price_incurred) {
        this.large_price_incurred = large_price_incurred;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReg_price() {
        return reg_price;
    }

    public void setReg_price(String reg_price) {
        this.reg_price = reg_price;
    }

    public String getLarge_price() {
        return large_price;
    }

    public void setLarge_price(String large_price) {
        this.large_price = large_price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
