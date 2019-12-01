package se.aroms.Devdroids;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredients implements Parcelable {
    private String id;
    private String quantity;
    private String dishId;

    public Ingredients() {
    }

    public Ingredients(String id, String quantity,String dishId) {
        this.id = id;
        this.quantity = quantity;
        this.dishId=dishId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public static Creator<Ingredients> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        public Ingredients createFromParcel(Parcel source) {
            Ingredients ingredients = new Ingredients();
            ingredients.id = source.readString();
            ingredients.quantity = source.readString();
            ingredients.dishId=source.readString();
            return ingredients;
        }

        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(quantity);
        dest.writeString(dishId);
    }
}
