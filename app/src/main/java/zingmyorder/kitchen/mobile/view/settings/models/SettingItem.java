package zingmyorder.kitchen.mobile.view.settings.models;

public class SettingItem {
    private String settingName;
    private String settingDesc;
    private int settingIcon;

    public SettingItem(String settingName, String settingDesc,int settingIcon) {
        this.settingName = settingName;
        this.settingDesc = settingDesc;
        this.settingIcon = settingIcon;
    }

    public String getSettingName() {
        return settingName;
    }

    public String getSettingDesc() {
        return settingDesc;
    }

    public int getSettingIcon() {
        return settingIcon;
    }
}
