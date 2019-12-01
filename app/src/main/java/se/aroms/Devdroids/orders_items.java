package se.aroms.Devdroids;

import android.os.Parcel;
import android.os.Parcelable;

public class orders_items implements Parcelable {
    private String ItemID;
    private String size;
    private Double price;
    private Double incured_price;

    public orders_items(String itemID, String size, Double price, Double incured_price) {
        ItemID = itemID;
        this.size = size;
        this.price = price;
        this.incured_price = incured_price;
    }
    public orders_items(){

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

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<orders_items> CREATOR = new Creator<orders_items>() {
        public orders_items createFromParcel(Parcel source) {
            orders_items dish = new orders_items();
            dish.ItemID=source.readString();
            dish.size=source.readString();

            dish.price=source.readDouble();
            dish.incured_price=source.readDouble();
            return dish;
        }

        public orders_items[] newArray(int size) {
            return new orders_items[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ItemID);
        dest.writeString(size);
        dest.writeDouble(price);
        dest.writeDouble(incured_price);
    }
}
