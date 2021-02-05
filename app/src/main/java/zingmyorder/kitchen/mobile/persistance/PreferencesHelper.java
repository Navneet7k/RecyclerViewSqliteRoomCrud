package zingmyorder.kitchen.mobile.persistance;

public interface PreferencesHelper {
    void setAccessToken(String accessToken);
    String getAccessToken();
    void setUserData(String userData);
    String getUserData();
    void setLoggedIn(int logged);
    int getLoggedIn();
    boolean isFcmTokenUpdated();
    void setFcmTokenUpdated(boolean isUpdated);
    void saveLoginResponse(String loginData);
    String getLoginResponse();
    void setAutoRefreshStatus(int status);
    int getAutoRefreshStatus();
    void setPrinterType(int printerType);
    int getPrinterType();
    void setDefaultPrinterId(String defaultPrinterId);
    String getDefaultPrinterId();
    void clearSharedPref();
    void setRingtone(String ringtoneID);
    String getRingtone();
}

