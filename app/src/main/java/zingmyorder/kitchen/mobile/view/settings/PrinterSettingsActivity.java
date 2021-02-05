package zingmyorder.kitchen.mobile.view.settings;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.base.BaseActivity;
import zingmyorder.kitchen.mobile.databinding.ActivityPrinterSettingsBinding;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;

import javax.inject.Inject;

public class PrinterSettingsActivity extends BaseActivity<ActivityPrinterSettingsBinding,PrinterSettingsActivityVM> implements BluetoothDeviceInterface {

    private ActivityPrinterSettingsBinding binding;
    private BluetoothAdapter mBluetoothAdapter;
    public static final int REQUEST_ENABLE_BT = 1113;
    private BluetoothItemAdapter bluetoothItemAdapter;
    private ArrayList<BluetoothDevice> mDeviceList1 = new ArrayList<>();
    private BluetoothDevice selectedBluetoothPrinter;

    @Inject
    PrinterSettingsActivityVM printerSettingsActivityVM;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_printer_settings;
    }

    @Override
    public PrinterSettingsActivityVM getViewModel() {
        return printerSettingsActivityVM;
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (!mDeviceList1.contains(device)) {
                    mDeviceList1.add(device);
                    Log.i("BT", device.getName() + "\n" + device.getAddress());
                }
                if (mDeviceList1.size()>0) {
                    bluetoothItemAdapter = new BluetoothItemAdapter(PrinterSettingsActivity.this, mDeviceList1, PrinterSettingsActivity.this,getDefaultPrinterPos());
                    binding.scanResultRv.setAdapter(bluetoothItemAdapter);
                    binding.btSearchIndication.setVisibility(View.GONE);
//                    fetchPaired();
                }
            }
        }
    };

    private final BroadcastReceiver mPairReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final BluetoothDevice device=intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                final int state        = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState    = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(context, "Paired!", Toast.LENGTH_SHORT).show();
                    if (device!=null)
                        printerSettingsActivityVM.saveDefaultPrinter(device.getAddress());
//                        editor.putString("deviceAddress", device.getAddress());
//                        editor.apply();
//                    }
                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED){
                    Toast.makeText(context, "Unpaired!", Toast.LENGTH_SHORT).show();
                    printerSettingsActivityVM.saveDefaultPrinter("");
                }
//                if (bluetoothItemAdapter!=null)
//                    bluetoothItemAdapter.notifyDataSetChanged();
//                if (bluetoothPairedItemAdapter!=null)
//                    bluetoothPairedItemAdapter.notifyDataSetChanged();
                fetchPaired();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=getViewDataBinding();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        binding.scanResultRv.setLayoutManager(new LinearLayoutManager(this));
        binding.btSettingToggle.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                printerSettingsActivityVM.savePrinterType(1);
            }
            else {
                printerSettingsActivityVM.savePrinterType(0);
            }

            updatePrinterPreferences();
        });

        binding.wifiSettingToggle.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                printerSettingsActivityVM.savePrinterType(2);
            }else {
                printerSettingsActivityVM.savePrinterType(0);
            }

            updatePrinterPreferences();
        });

        updatePrinterPreferences();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        IntentFilter intent = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mPairReceiver, intent);

        startBtScan();
    }

    private void updatePrinterPreferences() {
        if (printerSettingsActivityVM.getPrinterType()==1){
            binding.btSettingExpandRL.setVisibility(View.VISIBLE);
            binding.btSettingToggle.setChecked(true);
            binding.wifiSettingToggle.setChecked(false);
            if (mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled()){
                binding.btEnabledLL.setVisibility(View.VISIBLE);
                binding.btDisabledLL.setVisibility(View.GONE);
                startBtScan();
            }
        } else if (printerSettingsActivityVM.getPrinterType()==2){
            binding.wifiSettingToggle.setChecked(true);
            binding.btSettingToggle.setChecked(false);
            binding.btSettingExpandRL.setVisibility(View.GONE);
        } else {
            binding.btSettingExpandRL.setVisibility(View.GONE);
            binding.btSettingToggle.setChecked(false);
            binding.wifiSettingToggle.setChecked(false);
        }
    }

    private void startBtScan() {
        if (mBluetoothAdapter!=null && mBluetoothAdapter.isEnabled() && checkLocationPermission()){
            mBluetoothAdapter.startDiscovery();
        }else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void pairDevice(BluetoothDevice device) {
        Toast.makeText(this, "Pairing initiated, confirmation dialog\n will be shown in a few seconds..", Toast.LENGTH_LONG).show();
        try {
            Method m = device.getClass().getMethod("createBond", (Class[]) null);
            m.invoke(device, (Object[]) null);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(),"Exception: "+e.getMessage(),Toast.LENGTH_LONG ).show();
        }
    }

    private void unpairDevice(BluetoothDevice device) {
        try {
            Method method = device.getClass().getMethod("removeBond", (Class[]) null);
            method.invoke(device, (Object[]) null);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getDefaultPrinterPos() {
        if (!TextUtils.isEmpty(printerSettingsActivityVM.getDefaultPrinter())) {
            for (int i = 0; i < mDeviceList1.size(); i++) {
                if (mDeviceList1.get(i).getAddress().equalsIgnoreCase(printerSettingsActivityVM.getDefaultPrinter()))
                    return i;
            }
            return -1;
        } else return -1;
    }

    private void fetchPaired() {
//        String deviceAddress = prefs.getString("deviceAddress", null);
//        pairedDevicesList=new ArrayList<>();
        Set<BluetoothDevice> bluetoothDevices=mBluetoothAdapter.getBondedDevices();
        int defaultPrinterPos=-1;
        if (bluetoothDevices.size()>0) {
            mDeviceList1 = new ArrayList<>(bluetoothDevices);
            defaultPrinterPos=getDefaultPrinterPos();
//        bluetoothPairedItemAdapter=new BluetoothItemAdapter(this,pairedDevices,HomeScreenActivity.this);
//        pairedItemsRecycler.setAdapter(bluetoothItemAdapter);
//        for (BluetoothDevice device:bluetoothDevices) {
//            if (device.getAddress().equalsIgnoreCase(deviceAddress)) {
//                this.selectedBluetoothPrinter=device;
//                printerName.setText("PRINTER : "
//                        +device.getName());
//            }
//        }

            bluetoothItemAdapter = new BluetoothItemAdapter(PrinterSettingsActivity.this, mDeviceList1, PrinterSettingsActivity.this,defaultPrinterPos);
            binding.scanResultRv.setAdapter(bluetoothItemAdapter);
            binding.btSearchIndication.setVisibility(View.GONE);
        } else {
            startBtScan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mBluetoothAdapter.startDiscovery();
                } else {
                    Toast.makeText(this, "Please turn On Location to enable bluetooth scan!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void deviceBluetoothToogle() {
        if (!mBluetoothAdapter.isEnabled()){
            Toast.makeText(this, "Turning on bluetooth...", Toast.LENGTH_SHORT).show();
            Intent intent1 =new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent1,REQUEST_ENABLE_BT);
        } else {
            Toast.makeText(this, "Bluetooth is already On!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT :
                if (resultCode==RESULT_OK) {
                    binding.btEnabledLL.setVisibility(View.VISIBLE);
                    binding.btDisabledLL.setVisibility(View.GONE);
                    Toast.makeText(this, "Bluetooth turned ON!", Toast.LENGTH_SHORT).show();
                    startBtScan();
                    Toast.makeText(this, "Scanning for devices....", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Couldn't turn ON Bluetooth!", Toast.LENGTH_SHORT).show();
                }
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    public boolean checkLocationPermission()
    {
        String permission = "android.permission.ACCESS_FINE_LOCATION";
        int res = this.checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        unregisterReceiver(mPairReceiver);
        super.onDestroy();
        // Make sure we're not doing discovery anymore
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    @Override
    public void subScribeObserver() {
        super.subScribeObserver();

        printerSettingsActivityVM.closePage.observe(this,aVoid -> finish());
        printerSettingsActivityVM.bluetoothON.observe(this,aVoid -> deviceBluetoothToogle());
    }

    @Override
    public void toggleBluetoothPair(BluetoothDevice device,Boolean pair) {
        if (pair)
            pairDevice(device);
        else
            unpairDevice(device);
    }

    @Override
    public void setAsDefaultPrinter(BluetoothDevice defaultPrinter) {
        if (defaultPrinter.getBondState()==BluetoothDevice.BOND_BONDED) {
            this.selectedBluetoothPrinter=defaultPrinter;
            printerSettingsActivityVM.saveDefaultPrinter(defaultPrinter.getAddress());
            Toast.makeText(this, defaultPrinter.getName()+" is your default printer now!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Can't set this printer as default, pair to set as default!", Toast.LENGTH_SHORT).show();
        }

    }
}
