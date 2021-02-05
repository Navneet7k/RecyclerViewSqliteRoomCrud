package zingmyorder.kitchen.mobile.view.settings;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.base.BaseViewModel;

import javax.inject.Inject;

public class SettingsFragmentVM extends BaseViewModel {

    final DataManager mDataManager;

    @Inject
    public SettingsFragmentVM(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    public void performLogout() {
        mDataManager.clearSharedPref();
    }
}
