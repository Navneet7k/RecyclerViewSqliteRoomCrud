package zingmyorder.kitchen.mobile.view.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KitchenLoginResponse {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("api_token")
    @Expose
    private String apiToken;
    @SerializedName("remember_token")
    @Expose
    private String rememberToken;
    @SerializedName("memobirdID")
    @Expose
    private Object memobirdID;
    @SerializedName("printer_userID")
    @Expose
    private Object printerUserID;
    @SerializedName("ak")
    @Expose
    private Object ak;
    @SerializedName("last_accessed")
    @Expose
    private String lastAccessed;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("kitchen_status")
    @Expose
    private String kitchenStatus;
    @SerializedName("stop_date")
    @Expose
    private Object stopDate;
    @SerializedName("fcm")
    @Expose
    private String fcm;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getRememberToken() {
        return rememberToken;
    }

    public void setRememberToken(String rememberToken) {
        this.rememberToken = rememberToken;
    }

    public Object getMemobirdID() {
        return memobirdID;
    }

    public void setMemobirdID(Object memobirdID) {
        this.memobirdID = memobirdID;
    }

    public Object getPrinterUserID() {
        return printerUserID;
    }

    public void setPrinterUserID(Object printerUserID) {
        this.printerUserID = printerUserID;
    }

    public Object getAk() {
        return ak;
    }

    public void setAk(Object ak) {
        this.ak = ak;
    }

    public String getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(String lastAccessed) {
        this.lastAccessed = lastAccessed;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getKitchenStatus() {
        return kitchenStatus;
    }

    public void setKitchenStatus(String kitchenStatus) {
        this.kitchenStatus = kitchenStatus;
    }

    public Object getStopDate() {
        return stopDate;
    }

    public void setStopDate(Object stopDate) {
        this.stopDate = stopDate;
    }

    public String getFcm() {
        return fcm;
    }

    public void setFcm(String fcm) {
        this.fcm = fcm;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }
}
