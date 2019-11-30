package se.aroms.Devdroids;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Dishes implements Parcelable {
    private String description;
    private String large_price;
    private String large_price_incurred;
    private String name;
    private String reg_price;
    private String reg_price_incurred;
    private String time;
    private String type;
    private String uid;
    private List<String> img_ids;
    private int availability;
    public Dishes()
    {

    }
    public Dishes(String description, String large_price, String large_price_incurred, String name, String reg_price, String reg_price_incurred, String time, String type, String uid, List<String> img_ids,int availability) {
        this.description = description;
        this.large_price = large_price;
        this.large_price_incurred = large_price_incurred;
        this.name = name;
        this.reg_price = reg_price;
        this.reg_price_incurred = reg_price_incurred;
        this.time = time;
        this.type = type;
        this.uid = uid;
        this.img_ids = img_ids;
        this.availability=availability;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLarge_price() {
        return large_price;
    }

    public void setLarge_price(String large_price) {
        this.large_price = large_price;
    }

    public String getLarge_price_incurred() {
        return large_price_incurred;
    }

    public void setLarge_price_incurred(String large_price_incurred) {
        this.large_price_incurred = large_price_incurred;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
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

    public String getReg_price_incurred() {
        return reg_price_incurred;
    }

    public void setReg_price_incurred(String reg_price_incurred) {
        this.reg_price_incurred = reg_price_incurred;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getImg_ids() {
        return img_ids;
    }

    public void setImg_ids(List<String> img_ids) {
        this.img_ids = img_ids;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Parcelable.Creator<Dishes> CREATOR = new Creator<Dishes>() {
        public Dishes createFromParcel(Parcel source) {
            Dishes dish = new Dishes();
            dish.description=source.readString();
            dish.large_price=source.readString();
            dish.large_price_incurred=source.readString();
            dish.name=source.readString();
            dish.reg_price=source.readString();
            dish.reg_price_incurred=source.readString();
            dish.time=source.readString();
            dish.type=source.readString();
            dish.uid=source.readString();
            dish.img_ids=new ArrayList<>();
            source.readStringList(dish.img_ids);
            dish.availability=source.readInt();

            return dish;
        }

        public Dishes[] newArray(int size) {
            return new Dishes[size];
        }
    };


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(large_price);
        dest.writeString(large_price_incurred);
        dest.writeString(name);
        dest.writeString(reg_price);
        dest.writeString(reg_price_incurred);
        dest.writeString(time);
        dest.writeString(type);
        dest.writeString(uid);
        dest.writeStringList(img_ids);
        dest.writeInt(availability);
    }
}
