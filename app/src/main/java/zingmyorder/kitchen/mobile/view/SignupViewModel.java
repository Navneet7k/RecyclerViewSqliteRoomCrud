package zingmyorder.kitchen.mobile.view;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;

import javax.inject.Inject;

public class SignupViewModel extends BaseViewModel {

    final DataManager dataManager;

    public SingleLiveEvent<Void> closePage=new SingleLiveEvent<>();

    @Inject
    public SignupViewModel(DataManager dataManager) {
        super(dataManager);
        this.dataManager=dataManager;
    }

    public int isLoggedIn() {
        return dataManager.isLoggedIn();
    }

    public String getAppToken(){
        return dataManager.getAppToken();
    }

//    public int getRoleId() {
//        String json=dataManager.getUserData();
//        Gson gson=new Gson();
//        User user=gson.fromJson(json,User.class);
//        if (user!=null)
//        return user.getRole_id();
//        else return 0;
//    }
//
//    public void clearSharedPref() {
//        dataManager.clearAppData();
//    }
//
//    public String getSavedCookie() {
//        if (dataManager.getCookies()!=null) {
//            String cookie=dataManager.getCookies().toString();
//            return cookie.substring(1, cookie.length()-1);
//        } else {
//            return null;
//        }
//    }

    public void onClosePage() {
        closePage.call();
    }

    public void NavigateToForgotPasword(){

    }
}
