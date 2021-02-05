package zingmyorder.kitchen.mobile.persistance;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AppDataManager implements PreferencesHelper {

    private final AppPreferencesHelper mPreferencesHelper;

    @Inject
    public AppDataManager(AppPreferencesHelper preferencesHelper) {
        mPreferencesHelper = preferencesHelper;
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);
    }

    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setUserData(String userData) {
        mPreferencesHelper.setUserData(userData);
    }

    @Override
    public String getUserData() {
        return mPreferencesHelper.getUserData();
    }

    @Override
    public void setLoggedIn(int logged) {
        mPreferencesHelper.setLoggedIn(logged);
    }

    @Override
    public int getLoggedIn() {
        return mPreferencesHelper.getLoggedIn();
    }

    @Override
    public boolean isFcmTokenUpdated() {
        return mPreferencesHelper.isFcmTokenUpdated();
    }

    @Override
    public void setFcmTokenUpdated(boolean isUpdated) {
        mPreferencesHelper.setFcmTokenUpdated(isUpdated);
    }

    @Override
    public void saveLoginResponse(String loginData) {
        mPreferencesHelper.saveLoginResponse(loginData);
    }

    @Override
    public String getLoginResponse() {
        return mPreferencesHelper.getLoginResponse();
    }

    @Override
    public void setAutoRefreshStatus(int status) {

    }

    @Override
    public int getAutoRefreshStatus() {
        return 0;
    }

    @Override
    public void setPrinterType(int printerType) {
        mPreferencesHelper.setPrinterType(printerType);
    }

    @Override
    public int getPrinterType() {
        return mPreferencesHelper.getPrinterType();
    }

    @Override
    public void setDefaultPrinterId(String defaultPrinterId) {
        mPreferencesHelper.setDefaultPrinterId(defaultPrinterId);
    }

    @Override
    public String getDefaultPrinterId() {
        return mPreferencesHelper.getDefaultPrinterId();
    }

    @Override
    public void clearSharedPref() {
        mPreferencesHelper.clearSharedPref();
    }

    @Override
    public void setRingtone(String ringtoneID) {
        mPreferencesHelper.setRingtone(ringtoneID);
    }

    @Override
    public String getRingtone() {
        return mPreferencesHelper.getRingtone();
    }
}
