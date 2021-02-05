package zingmyorder.kitchen.mobile.view.orders.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CarDetails implements Parcelable {
    private String number;
    private String color;
    private String info;

    protected CarDetails(Parcel in) {
        number = in.readString();
        color = in.readString();
        info = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(number);
        dest.writeString(color);
        dest.writeString(info);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CarDetails> CREATOR = new Creator<CarDetails>() {
        @Override
        public CarDetails createFromParcel(Parcel in) {
            return new CarDetails(in);
        }

        @Override
        public CarDetails[] newArray(int size) {
            return new CarDetails[size];
        }
    };

    public String getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public String getInfo() {
        return info;
    }
}
