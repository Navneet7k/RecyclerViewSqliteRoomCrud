package zingmyorder.kitchen.mobile.view.settings;

import com.google.gson.Gson;

import javax.inject.Inject;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;
import zingmyorder.kitchen.mobile.view.login.model.KitchenLoginResponse;

public class DashboardSettingsVM extends BaseViewModel {

    final DataManager dataManager;
    public SingleLiveEvent<Void> closePage=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> reloadClick=new SingleLiveEvent<>();

    @Inject
    public DashboardSettingsVM(DataManager mDataManager) {
        super(mDataManager);
        this.dataManager = mDataManager;
    }

    public KitchenLoginResponse getLoginResponse() {
        return dataManager.getLoginResponse() != null ? new Gson().fromJson(dataManager.getLoginResponse(), KitchenLoginResponse.class) : null;
    }

    public String getRingtone() {
        return dataManager.getRingtone();
    }

    public void onClosePage() {
        closePage.call();
    }

    public void onReloadPage() {
        reloadClick.call();
    }
}
