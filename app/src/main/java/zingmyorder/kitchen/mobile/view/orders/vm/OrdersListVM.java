package zingmyorder.kitchen.mobile.view.orders.vm;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.ResponseListener;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;
import zingmyorder.kitchen.mobile.models.ApiResponse;
import zingmyorder.kitchen.mobile.view.login.model.KitchenLoginResponse;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderStatusUpdate;

import javax.inject.Inject;

public class OrdersListVM extends BaseViewModel {

    final DataManager mDataManager;
    public SingleLiveEvent<Boolean> loadingStatus=new SingleLiveEvent<>();
    public SingleLiveEvent<String> error=new SingleLiveEvent<>();
    public MutableLiveData<Integer> customerArrived=new MutableLiveData<>();
    public SingleLiveEvent<Void> allOrders=new SingleLiveEvent<>();

    @Inject
    public OrdersListVM(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager=mDataManager;
    }

    public void onAllOrdersClick() {
        allOrders.call();
    }

    public KitchenLoginResponse getLoginResponse() {
        return mDataManager.getLoginResponse() != null ? new Gson().fromJson(mDataManager.getLoginResponse(), KitchenLoginResponse.class) : null;
    }

    public void updateCustomerArrivedComplete(String orderID,int pos) {
        mDataManager.updateCustomerArrivedComplete(orderID, "1", new ResponseListener<OrderStatusUpdate>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrderStatusUpdate> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        customerArrived.setValue(pos);
                        break;
                    case "400":
                        error.setValue("error 400");
                        break;
                    case "500":
                        error.setValue("error 500");
                        break;
                    default:
                        error.setValue("Oops..Something went wrong!");
                        break;
                }
            }
        });
    }
}
