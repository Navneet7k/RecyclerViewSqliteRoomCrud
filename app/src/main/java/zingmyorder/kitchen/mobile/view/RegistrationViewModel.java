package zingmyorder.kitchen.mobile.view;

import androidx.lifecycle.MutableLiveData;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;
import zingmyorder.kitchen.mobile.models.ApiResponse;
import zingmyorder.kitchen.mobile.models.RegRequest;
import zingmyorder.kitchen.mobile.models.RegSuccess;

import javax.inject.Inject;

public class RegistrationViewModel extends BaseViewModel {

    private final DataManager mDataManager;
    public SingleLiveEvent<Boolean> loadingStatus=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> regClick=new SingleLiveEvent<>();
    public MutableLiveData<String> errors=new MutableLiveData<>();
    public MutableLiveData<ApiResponse<RegSuccess>> responseMutableLiveData=new MutableLiveData<>();
    @Inject
    public RegistrationViewModel(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager=mDataManager;
    }

    public void onRegClicked() {
        regClick.call();
    }

    public void startRegistration (RegRequest regRequest) {
//        mDataManager.doRegistration(regRequest, new ResponseListener<RegSuccess>() {
//            @Override
//            public void onStart() {
//                loadingStatus.setValue(true);
//            }
//
//            @Override
//            public void onFinish() {
//                loadingStatus.setValue(false);
//            }
//
//            @Override
//            public void onResponse(ApiResponse<RegSuccess> apiResponse) {
//                loadingStatus.setValue(false);
//                if (apiResponse.data!=null) {
//                    responseMutableLiveData.setValue(apiResponse);
//                    Gson gson = new Gson();
//                    String json = gson.toJson(apiResponse.data.getUser());
//                    mDataManager.updateRegistrationToken(apiResponse.data.getAccess_token());
//                    mDataManager.updateLoginStatus(1);
//                    mDataManager.saveUserData(json);
//                } else {
//                    errors.setValue("Please enter all required fields!");
//                }
//            }
//        });
    }
}
