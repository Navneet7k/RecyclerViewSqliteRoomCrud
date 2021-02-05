package zingmyorder.kitchen.mobile.view.login.vm;

import android.text.TextUtils;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.ResponseListener;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;
import zingmyorder.kitchen.mobile.models.ApiResponse;
import zingmyorder.kitchen.mobile.view.login.model.KitchenLoginResponse;
import zingmyorder.kitchen.mobile.models.RegSuccess;
import zingmyorder.kitchen.mobile.view.login.model.TokenUpdateSuccess;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;

import javax.inject.Inject;

public class LoginViewModel extends BaseViewModel {

    private final DataManager mDataManager;
    public SingleLiveEvent<Void> loginClicked=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> deviceLoginClicked=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> wifiSettingClicked=new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> loadingStatus=new SingleLiveEvent<>();
    public MutableLiveData<String> messages =new MutableLiveData<>();
    public MutableLiveData<ApiResponse<RegSuccess>> responseMutableLiveData=new MutableLiveData<>();
    public MutableLiveData<String> loginToken=new MutableLiveData<>();

    @Inject
    public LoginViewModel(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager=mDataManager;
    }

    public void onLoginClicked() {
        loginClicked.call();
    }

    public void onDeviceLoginClicked() {
        deviceLoginClicked.call();
    }

    public void onWifiSettingClicked() {
        wifiSettingClicked.call();
    }

    public void startLogin(String basicAuth) {
       mDataManager.performLogin(basicAuth, new ResponseListener<KitchenLoginResponse>() {
           @Override
           public void onStart() {
               loadingStatus.setValue(true);
           }

           @Override
           public void onFinish() {
               loadingStatus.setValue(false);
           }

           @Override
           public void onResponse(ApiResponse<KitchenLoginResponse> apiResponse) {
               loadingStatus.setValue(false);
               switch (apiResponse.errorCode) {
                   case "200" :
                       if (apiResponse.data!=null && !TextUtils.isEmpty(apiResponse.data.getApiToken())){
                           loginToken.setValue(apiResponse.data.getApiToken());
                           mDataManager.saveAppToken(apiResponse.data.getApiToken());
                           String loginResponse=new Gson().toJson(apiResponse.data);
                           mDataManager.saveLoginResponse(loginResponse);
//                           fetchFCMTokenSDK();
                       }
                       break;
                   case "400":
                       messages.setValue("Oops.. Something went wrong!");
                       break;
                   case "404":
                       messages.setValue("Oops.. Something went wrong!");
                       break;
                   case "500":
                       messages.setValue("Oops.. Something went wrong!");
                       break;
                   default:
                       messages.setValue("Oops.. Something went wrong!");
                       break;
               }
           }
       });
    }

    public void startDeviceIdLogin(String deviceID) {
        mDataManager.performDeviceIDLogin(deviceID, new ResponseListener<KitchenLoginResponse>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<KitchenLoginResponse> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200" :
                        if (apiResponse.data!=null && !TextUtils.isEmpty(apiResponse.data.getApiToken())){
                            loginToken.setValue(apiResponse.data.getApiToken());
                            mDataManager.saveAppToken(apiResponse.data.getApiToken());
                            String loginResponse=new Gson().toJson(apiResponse.data);
                            mDataManager.saveLoginResponse(loginResponse);
//                           fetchFCMTokenSDK();
                        }
                        break;
                    case "400":
                        messages.setValue("Oops.. Something went wrong!");
                        break;
                    case "404":
                        messages.setValue("Oops.. Something went wrong!");
                        break;
                    case "500":
                        messages.setValue("Oops.. Something went wrong!");
                        break;
                    default:
                        messages.setValue("Oops.. Something went wrong!");
                        break;
                }
            }
        });
    }

    public void fetchFCMTokenSDK() {
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
                            updateFcmToBackend(token);
                    }
                });
    }

    private void updateFcmToBackend(String token) {
//        mDataManager.updateFCMToken(mDataManager.getAppToken(), token, new ResponseListener<TokenUpdateSuccess>() {
//            @Override
//            public void onStart() {
//
//            }
//
//            @Override
//            public void onFinish() {
//
//            }
//
//            @Override
//            public void onResponse(ApiResponse<TokenUpdateSuccess> apiResponse) {
//                switch (apiResponse.errorCode){
//                    case "200" :
//                        if (apiResponse.data!=null) {
//                            mDataManager.fcmTokenUpdated(true);
//                            mDataManager.saveAutoRefreshStatus(apiResponse.data.getAuto_refresh());
//                            messages.setValue("FCM update : " +apiResponse.data.isData());
//                        }
//                        break;
//                    case "500" :
//                        messages.setValue("Oops.. Something went wrong!");
//                        break;
//                    default:
//                        messages.setValue("Oops.. Something went wrong!");
//                        break;
//                }
//            }
//        });
    }
}
