package zingmyorder.kitchen.mobile.view.orders.vm;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.iid.FirebaseInstanceId;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.ResponseListener;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;
import zingmyorder.kitchen.mobile.models.ApiResponse;
import zingmyorder.kitchen.mobile.view.login.model.TokenUpdateSuccess;
import zingmyorder.kitchen.mobile.view.orders.model.OrdersWrapper;

import javax.inject.Inject;

public class OrdersListActivityVM extends BaseViewModel {

    final DataManager mDataManager;
    public SingleLiveEvent<Void> closePage=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> reloadClick=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> helpClick=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> wifiClick=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> closeSheet=new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> loadingStatus=new SingleLiveEvent<>();
    public MutableLiveData<ApiResponse<OrdersWrapper>> ordersResponse=new MutableLiveData<>();
    public MutableLiveData<String> error=new MutableLiveData<>();
    public MutableLiveData<String> messages =new MutableLiveData<>();

    @Inject
    public OrdersListActivityVM(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    public void setRingtone(String ringtoneID) {
        mDataManager.setRingtone(ringtoneID);
    }

    public String getRingtone() {
        return mDataManager.getRingtone();
    }

    public void onReload(){
        reloadClick.call();
    }

    public void onHelp() {
        helpClick.call();
    }

    public void onWifi() {
        wifiClick.call();
    }

    public void onCloseSheet() {
        closeSheet.call();
    }

    public void onClosePage() {
        closePage.call();
    }

    public void fetchOrdersList() {
        mDataManager.fetchOdersList(new ResponseListener<OrdersWrapper>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrdersWrapper> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        ordersResponse.setValue(apiResponse);
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

    public void fetchNoLoaderOrdersList() {
        mDataManager.fetchOdersList(new ResponseListener<OrdersWrapper>() {
            @Override
            public void onStart() {
//                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
//                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrdersWrapper> apiResponse) {
//                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        ordersResponse.setValue(apiResponse);
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

    public void fetchFCMTokenSDK(String android_id) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("LOGIN FCM", "getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    if (task.getResult()!=null) {
                        String token = task.getResult().getToken();
                        if (!TextUtils.isEmpty(token))
                            updateFcmToBackend(token,android_id);
                    }
                });
    }

    private void updateFcmToBackend(String token,String android_id) {
        mDataManager.updateFCMToken(mDataManager.getAppToken(), token,android_id, new ResponseListener<TokenUpdateSuccess>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onResponse(ApiResponse<TokenUpdateSuccess> apiResponse) {
                switch (apiResponse.errorCode){
                    case "200" :
                        if (apiResponse.data!=null) {
                            mDataManager.fcmTokenUpdated(true);
                            mDataManager.saveAutoRefreshStatus(apiResponse.data.getAuto_refresh());
                            messages.setValue("FCM update : " +apiResponse.data.isData());
                        }
                        break;
                    case "500" :
                        messages.setValue("Oops.. Something went wrong!");
                        break;
                    default:
                        messages.setValue("Oops.. Something went wrong!");
                        break;
                }
            }
        });
    }
}
