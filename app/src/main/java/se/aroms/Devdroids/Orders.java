package se.aroms.Devdroids;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Orders implements Parcelable {
    private Date orderTIme;
    private List<orders_items> orderItems;
    private String orderId;//unique order if
    private String uid;//table no
    private int status; //paid or not paid
    public Orders(Date orderTIme, List<orders_items> orderItems, String orderId, String uid, int status) {
        this.orderTIme = orderTIme;
        this.orderItems = orderItems;
        this.orderId = orderId;
        this.uid = uid;
        this.status=status;
    }
public Orders(){

}
    public Date getOrderTIme() {
        return orderTIme;
    }

    public void setOrderTIme(Date orderTIme) {
        this.orderTIme = orderTIme;
    }

    public List<orders_items> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<orders_items> orderItems) {
        this.orderItems = orderItems;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public static final Creator<Orders> CREATOR = new Creator<Orders>() {
        public Orders createFromParcel(Parcel source) {
            Orders dish = new Orders();
            dish.orderTIme=(Date)source.readSerializable();
            dish.orderItems=new ArrayList<orders_items>();
            source.readList(dish.orderItems,orders_items.class.getClassLoader());


            dish.orderId=source.readString();
            dish.uid=source.readString();
            dish.status=source.readInt();
            return dish;
        }

        public Orders[] newArray(int size) {
            return new Orders[size];
        }
    };
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(orderTIme);
        dest.writeList(orderItems);
        dest.writeString(orderId);
        dest.writeString(uid);
        dest.writeInt(status);
    }
}
