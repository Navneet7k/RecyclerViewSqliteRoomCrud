package zingmyorder.kitchen.mobile.view.settings;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;

import javax.inject.Inject;

public class PrinterSettingsActivityVM extends BaseViewModel {

    final DataManager mDataManager;
    SingleLiveEvent<Void> closePage=new SingleLiveEvent<>();
    SingleLiveEvent<Void> bluetoothON=new SingleLiveEvent<>();

    @Inject
    public PrinterSettingsActivityVM(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager=mDataManager;
    }

    public void savePrinterType(int printerType){
        mDataManager.savePrinterType(printerType);
    }

    public void saveDefaultPrinter(String printerID){
        mDataManager.saveDefaultPrinter(printerID);
    }

    public String getDefaultPrinter(){
        return mDataManager.getDefaultPrinterID();
    }

    public int getPrinterType() {
        return mDataManager.getPrinterType();
    }

    public void onClosePage() {
        closePage.call();
    }

    public void onBluetoothTurnOn() {
        bluetoothON.call();
    }
}
