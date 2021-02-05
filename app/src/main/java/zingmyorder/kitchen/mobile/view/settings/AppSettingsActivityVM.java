package zingmyorder.kitchen.mobile.view.settings;

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

import javax.inject.Inject;

public class AppSettingsActivityVM extends BaseViewModel {

    final DataManager mDataManager;
    public SingleLiveEvent<Void> closePage=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> closeSheet=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> notifSync=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> wifiSettingsClick=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> ringtonePick=new SingleLiveEvent<>();
    public SingleLiveEvent<Boolean> loadingStatus=new SingleLiveEvent<>();
    public MutableLiveData<String> messages=new MutableLiveData<>();

    @Inject
    public AppSettingsActivityVM(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager=mDataManager;
    }

    public void onClosePage() {
        closePage.call();
    }

    public void onCloseSheet() {
        closeSheet.call();
    }

    public void onWifiSettings() {
        wifiSettingsClick.call();
    }

    public void onRingtonePick() {
        ringtonePick.call();
    }

    public String getSelectedRingtone() {
        return mDataManager.getRingtone();
    }

    public void setRingtone(String ringID) {
        mDataManager.setRingtone(ringID);
    }

    public void notificationSync() {
        notifSync.call();
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
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<TokenUpdateSuccess> apiResponse) {
                loadingStatus.setValue(false);
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
