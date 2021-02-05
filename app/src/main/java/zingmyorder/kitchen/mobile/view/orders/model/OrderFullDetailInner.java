package zingmyorder.kitchen.mobile.view.orders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderFullDetailInner {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("menu_id")
    @Expose
    private String menuId;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("unit_price")
    @Expose
    private String unitPrice;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("menu_addon")
    @Expose
    private String menuAddon;
    @SerializedName("special_instr")
    @Expose
    private String specialInstr;
    @SerializedName("addons")
    @Expose
    private List<List<String>> addons;
    @SerializedName("variation")
    @Expose
    private String variation;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("parameters")
    @Expose
    private Object parameters;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("menu")
    @Expose
    private MenuDetail menu;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuAddon() {
        return menuAddon;
    }

    public void setMenuAddon(String menuAddon) {
        this.menuAddon = menuAddon;
    }

    public String getSpecialInstr() {
        return specialInstr;
    }

    public void setSpecialInstr(String specialInstr) {
        this.specialInstr = specialInstr;
    }

    public List<List<String>> getAddons() {
        return addons;
    }

    public void setAddons(List<List<String>> addons) {
        this.addons = addons;
    }

    public String getVariation() {
        return variation;
    }

    public void setVariation(String variation) {
        this.variation = variation;
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

    public Object getParameters() {
        return parameters;
    }

    public void setParameters(Object parameters) {
        this.parameters = parameters;
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

    public MenuDetail getMenu() {
        return menu;
    }

    public void setMenu(MenuDetail menu) {
        this.menu = menu;
    }

}
