package zingmyorder.kitchen.mobile.view.settings.models;

public class RingtoneModel {
    private String ringName;
    private String ringID;

    public RingtoneModel(String ringName, String ringID) {
        this.ringName = ringName;
        this.ringID = ringID;
    }

    public String getRingName() {
        return ringName;
    }

    public String getRingID() {
        return ringID;
    }
}
