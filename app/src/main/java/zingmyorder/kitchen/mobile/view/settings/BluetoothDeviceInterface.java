package zingmyorder.kitchen.mobile.view.settings;

import android.bluetooth.BluetoothDevice;

public interface BluetoothDeviceInterface {
    void toggleBluetoothPair(BluetoothDevice device, Boolean pair);
    void setAsDefaultPrinter(BluetoothDevice defaultPrinter);
}
