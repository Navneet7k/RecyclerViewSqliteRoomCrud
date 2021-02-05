package zingmyorder.kitchen.mobile.persistance;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_TOKEN = "prefkeytoken";
    private static final String PREF_SAVE_USER = "prefsaveuser";
    private static final String PREF_CHECK_LOGIN = "prefchecklogin";
    private static final String PREF_FCM_TOKEN = "preffcmtoken";
    private static final String PREF_LOGIN_RESPONSE = "prefloginresponse";
    private static final String PREF_AUTO_REFRESH = "prefautorefresh";
    private static final String PREF_PRINTER_TYPE = "prefprintertype";
    private static final String PREF_DEFAULT_PRINTER = "prefdefaultprinter";
    private static final String PREF_DEFAULT_RINGTONE = "prefdefaultringtone";
    private final SharedPreferences mPrefs;

    @Inject
    public AppPreferencesHelper(Context context, @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPrefs.edit().putString(PREF_KEY_TOKEN, accessToken).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_TOKEN, "");
    }

    @Override
    public void setUserData(String userData) {
        mPrefs.edit().putString(PREF_SAVE_USER, userData).apply();
    }

    @Override
    public String getUserData() {
        return mPrefs.getString(PREF_SAVE_USER, "");
    }

    @Override
    public void setLoggedIn(int logged) {
        mPrefs.edit().putInt(PREF_CHECK_LOGIN,logged).apply();
    }

    @Override
    public int getLoggedIn() {
        return mPrefs.getInt(PREF_CHECK_LOGIN, 0);
    }

    @Override
    public boolean isFcmTokenUpdated() {
        return mPrefs.getBoolean(PREF_FCM_TOKEN,false);
    }

    @Override
    public void setFcmTokenUpdated(boolean isUpdated) {
        mPrefs.edit().putBoolean(PREF_FCM_TOKEN,isUpdated).apply();
    }

    @Override
    public void saveLoginResponse(String loginData) {
        mPrefs.edit().putString(PREF_LOGIN_RESPONSE,loginData).apply();
    }

    @Override
    public String getLoginResponse() {
        return mPrefs.getString(PREF_LOGIN_RESPONSE,"");
    }

    @Override
    public void setAutoRefreshStatus(int status) {
        mPrefs.edit().putInt(PREF_AUTO_REFRESH,status).apply();
    }

    @Override
    public int getAutoRefreshStatus() {
        return mPrefs.getInt(PREF_AUTO_REFRESH,0);
    }

    @Override
    public void setPrinterType(int printerType) {
        mPrefs.edit().putInt(PREF_PRINTER_TYPE, printerType).apply();
    }

    @Override
    public int getPrinterType() {
        return mPrefs.getInt(PREF_PRINTER_TYPE, 0);
    }

    @Override
    public void setDefaultPrinterId(String defaultPrinter) {
        mPrefs.edit().putString(PREF_DEFAULT_PRINTER, defaultPrinter).apply();
    }

    @Override
    public String getDefaultPrinterId() {
        return mPrefs.getString(PREF_DEFAULT_PRINTER, "");
    }

    @Override
    public void clearSharedPref() {
        mPrefs.edit().clear().apply();
    }

    @Override
    public void setRingtone(String ringtoneID) {
        mPrefs.edit().putString(PREF_DEFAULT_RINGTONE, ringtoneID).apply();
    }

    @Override
    public String getRingtone() {
        return mPrefs.getString(PREF_DEFAULT_RINGTONE, "1");
    }
}
