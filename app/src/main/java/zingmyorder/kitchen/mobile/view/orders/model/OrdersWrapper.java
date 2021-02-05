package zingmyorder.kitchen.mobile.view.orders.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrdersWrapper implements Parcelable {
    private String device_id;
    @SerializedName("new_orders")
    private List<Orders> orders;
    @SerializedName("scedule_orders")
    private List<Orders> future_orders;
    @SerializedName("complete_orders")
    private List<Orders> accepted_orders;

    protected OrdersWrapper(Parcel in) {
        device_id = in.readString();
        orders = in.createTypedArrayList(Orders.CREATOR);
        future_orders = in.createTypedArrayList(Orders.CREATOR);
        accepted_orders = in.createTypedArrayList(Orders.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(device_id);
        dest.writeTypedList(orders);
        dest.writeTypedList(future_orders);
        dest.writeTypedList(accepted_orders);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<OrdersWrapper> CREATOR = new Creator<OrdersWrapper>() {
        @Override
        public OrdersWrapper createFromParcel(Parcel in) {
            return new OrdersWrapper(in);
        }

        @Override
        public OrdersWrapper[] newArray(int size) {
            return new OrdersWrapper[size];
        }
    };

    public List<Orders> getOrders() {
        return orders;
    }

    public String getDevice_id() {
        return device_id;
    }

    public List<Orders> getFuture_orders() {
        return future_orders;
    }

    public List<Orders> getAccepted_orders() {
        return accepted_orders;
    }
}
