package zingmyorder.kitchen.mobile.base;

import androidx.lifecycle.ViewModel;

import zingmyorder.kitchen.mobile.DataManager;

public abstract class BaseViewModel extends ViewModel {
    private final DataManager mDataManager;
    @Override
    protected void onCleared() {
        super.onCleared();
    }

    public BaseViewModel(DataManager mDataManager) {
        this.mDataManager=mDataManager;
    }
}
