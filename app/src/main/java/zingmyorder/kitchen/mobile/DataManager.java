package zingmyorder.kitchen.mobile;

import zingmyorder.kitchen.mobile.base.BaseSchedulers;
import zingmyorder.kitchen.mobile.models.ApiResponse;
import zingmyorder.kitchen.mobile.view.login.model.KitchenLoginResponse;
import zingmyorder.kitchen.mobile.models.LoginModel;
import zingmyorder.kitchen.mobile.models.ResponseStatus;
import zingmyorder.kitchen.mobile.persistance.AppDataManager;
import zingmyorder.kitchen.mobile.view.login.model.TokenUpdateSuccess;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderDetails;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderStatusUpdate;
import zingmyorder.kitchen.mobile.view.orders.model.OrdersWrapper;
import com.google.gson.JsonObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DataManager {
    private final ApiInterface api;
    private final BaseSchedulers scheduler;
    private final AppDataManager appDataManager;

    @Inject
    public DataManager(ApiInterface api, BaseSchedulers scheduler, AppDataManager appDataManager) {
        this.api = api;
        this.scheduler = scheduler;
        this.appDataManager = appDataManager;
    }

    public String getLoginResponse() {
        return appDataManager.getLoginResponse();
    }

    public void setRingtone(String ringtoneID) {
        appDataManager.setRingtone(ringtoneID);
    }

    public String getRingtone() {
        return appDataManager.getRingtone();
    }

    public void saveAppToken(String token) {
        appDataManager.setAccessToken(token);
    }

    public void clearSharedPref() {
        appDataManager.clearSharedPref();
    }

    public String getAppToken() {
        return appDataManager.getAccessToken();
    }

    public void saveLoginResponse(String token) {
        appDataManager.saveLoginResponse(token);
    }

    public void saveAutoRefreshStatus(int status) {
        appDataManager.setAutoRefreshStatus(status);
    }

    public void updateLoginStatus(int loggedin) {
        appDataManager.setLoggedIn(loggedin);
    }

    public int isLoggedIn() {
        return appDataManager.getLoggedIn();
    }

    public void saveUserData(String userData) {
        appDataManager.setUserData(userData);
    }

    public String getUserData() {
       return appDataManager.getUserData();
    }

    public void fcmTokenUpdated(boolean isUpdated) {
        appDataManager.setFcmTokenUpdated(isUpdated);
    }

    public boolean isFcmUpdated() {
        return appDataManager.isFcmTokenUpdated();
    }

    public void savePrinterType(int printerType){
        appDataManager.setPrinterType(printerType);
    }

    public int getPrinterType() {
        return appDataManager.getPrinterType();
    }

    public String getDefaultPrinterID() {
        return appDataManager.getDefaultPrinterId();
    }

    public void saveDefaultPrinter(String printerID){
        appDataManager.setDefaultPrinterId(printerID);
    }

    private <T> void performRequest(Observable<T> observable, final ResponseListener<T> responseListener) {
        observable.subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(new Observer<T>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        responseListener.onStart();
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onNext(T t) {
                        responseListener.onResponse(new ApiResponse(ResponseStatus.SUCCESS, t, null));
                    }

                    @SuppressWarnings("unchecked")
                    @Override
                    public void onError(Throwable e) {
                        responseListener.onResponse(new ApiResponse(ResponseStatus.ERROR, null, e));
                    }

                    @Override
                    public void onComplete() {
                        responseListener.onFinish();
                    }
                });
    }

    public void doSignup(JsonObject request, ResponseListener<JsonObject> responseListener) {
        performRequest(api.doSignup(request, ""), responseListener);
    }

    public void doLogin(LoginModel loginModel, ResponseListener<JsonObject> responseListener) {
        performRequest(api.doLogin(loginModel, ""), responseListener);
    }

    public void performLogin(String basicAuth, ResponseListener<KitchenLoginResponse> responseListener) {
        performRequest(api.performLoginNew(basicAuth), responseListener);
    }

    public void updateFCMToken(String appToken,String fcmToken,String android_id,ResponseListener<TokenUpdateSuccess> responseListener) {
        performRequest(api.updateFcmToken(appToken,fcmToken,android_id),responseListener);
    }

    public void fetchOdersList(ResponseListener<OrdersWrapper> responseListener) {
        performRequest(api.getOrderList(getAppToken()),responseListener);
    }

    public void fetchOderDetails(String id,ResponseListener<OrderDetails> responseListener) {
        performRequest(api.getOrderDetails(id,getAppToken()),responseListener);
    }

    public void acceptOrder(String orderID,String prepTime,ResponseListener<OrderStatusUpdate> responseListener) {
        performRequest(api.acceptOrderStatus(orderID,getAppToken(),prepTime),responseListener);
    }

    public void setDelayTime(String orderID,String prepTime,ResponseListener<OrderStatusUpdate> responseListener) {
        performRequest(api.setDelayTime(orderID,prepTime,getAppToken()),responseListener);
    }

    public void updateCustomerArrivedComplete(String orderID,String carPickup,ResponseListener<OrderStatusUpdate> responseListener) {
        performRequest(api.updateCustomerArrivedComplete(orderID,getAppToken(),carPickup),responseListener);
    }

    public void updateOrderReady(String orderType,String orderID,ResponseListener<OrderStatusUpdate> responseListener) {
        performRequest(api.updateOrderReady(orderType,orderID,getAppToken()),responseListener);
    }

    public void markAsComplete(String orderID,String flag,ResponseListener<OrderStatusUpdate> responseListener) {
        performRequest(api.markAsComplete(orderID,getAppToken(),flag),responseListener);
    }

    public void performDeviceIDLogin(String deviceID, ResponseListener<KitchenLoginResponse> responseListener) {
        performRequest(api.deviceIdLoginNew(deviceID), responseListener);
    }
}
