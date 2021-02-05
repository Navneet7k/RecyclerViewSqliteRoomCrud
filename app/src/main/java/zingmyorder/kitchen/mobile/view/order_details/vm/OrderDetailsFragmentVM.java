package zingmyorder.kitchen.mobile.view.order_details.vm;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.base.BaseViewModel;

import javax.inject.Inject;

public class OrderDetailsFragmentVM extends BaseViewModel {

    final DataManager mDataManager;

    @Inject
    public OrderDetailsFragmentVM(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager=mDataManager;
    }
}
