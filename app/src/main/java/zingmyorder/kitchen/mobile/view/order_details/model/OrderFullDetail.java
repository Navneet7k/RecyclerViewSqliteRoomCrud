package zingmyorder.kitchen.mobile.view.order_details.model;

import zingmyorder.kitchen.mobile.view.orders.model.OrderFullDetailInner;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderFullDetail {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("address_id")
    @Expose
    private Object addressId;
    @SerializedName("billing_address_id")
    @Expose
    private Object billingAddressId;
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("apartment_no")
    @Expose
    private Object apartmentNo;
    @SerializedName("delivery_instr")
    @Expose
    private Object deliveryInstr;
    @SerializedName("delivery_time")
    @Expose
    private String deliveryTime;
    @SerializedName("ready_time")
    @Expose
    private String readyTime;
    @SerializedName("flag")
    @Expose
    private String flag;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("notification_status")
    @Expose
    private String notificationStatus;
    @SerializedName("notification_app")
    @Expose
    private String notificationApp;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
    @SerializedName("tax")
    @Expose
    private String tax;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("payment_methods")
    @Expose
    private Object paymentMethods;
    @SerializedName("payment_details")
    @Expose
    private String paymentDetails;
    @SerializedName("loyalty_points")
    @Expose
    private String loyaltyPoints;
    @SerializedName("tip")
    @Expose
    private String tip;
    @SerializedName("tip_value")
    @Expose
    private String tipValue;
    @SerializedName("tip_value_cus")
    @Expose
    private Object tipValueCus;
    @SerializedName("discount_points")
    @Expose
    private String discountPoints;
    @SerializedName("discount_amount")
    @Expose
    private String discountAmount;
    @SerializedName("delay_min")
    @Expose
    private Object delayMin;
    @SerializedName("order_type")
    @Expose
    private String orderType;
    @SerializedName("pay_to")
    @Expose
    private String payTo;
    @SerializedName("tax_rate")
    @Expose
    private String taxRate;
    @SerializedName("delivery_charge")
    @Expose
    private String deliveryCharge;
    @SerializedName("CCR")
    @Expose
    private String cCR;
    @SerializedName("CCF")
    @Expose
    private String cCF;
    @SerializedName("ZR")
    @Expose
    private String zR;
    @SerializedName("ZF")
    @Expose
    private String zF;
    @SerializedName("ZC")
    @Expose
    private String zC;
    @SerializedName("feedback_mail_status")
    @Expose
    private String feedbackMailStatus;
    @SerializedName("delay_mail_status")
    @Expose
    private String delayMailStatus;
    @SerializedName("week_reorder_mail_status")
    @Expose
    private String weekReorderMailStatus;
    @SerializedName("min_order_difference")
    @Expose
    private String minOrderDifference;
    @SerializedName("card")
    @Expose
    private Object card;
    @SerializedName("json_data")
    @Expose
    private Object jsonData;
    @SerializedName("del_charge")
    @Expose
    private Object delCharge;
    @SerializedName("guest")
    @Expose
    private String guest;
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
    @SerializedName("restaurant_name")
    @Expose
    private String restaurant_name;
    @SerializedName("detail")
    @Expose
    private List<OrderFullDetailInner> detail = null;
    @SerializedName("mail_sent")
    @Expose
    private String mail_sent;
    @SerializedName("is_schedule")
    @Expose
    private Integer is_schedule;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Object getAddressId() {
        return addressId;
    }

    public void setAddressId(Object addressId) {
        this.addressId = addressId;
    }

    public Object getBillingAddressId() {
        return billingAddressId;
    }

    public void setBillingAddressId(Object billingAddressId) {
        this.billingAddressId = billingAddressId;
    }

    public String getFlag() {
        return flag;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Object getApartmentNo() {
        return apartmentNo;
    }

    public void setApartmentNo(Object apartmentNo) {
        this.apartmentNo = apartmentNo;
    }

    public Object getDeliveryInstr() {
        return deliveryInstr;
    }

    public void setDeliveryInstr(Object deliveryInstr) {
        this.deliveryInstr = deliveryInstr;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getReadyTime() {
        return readyTime;
    }

    public void setReadyTime(String readyTime) {
        this.readyTime = readyTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getNotificationApp() {
        return notificationApp;
    }

    public void setNotificationApp(String notificationApp) {
        this.notificationApp = notificationApp;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public Object getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(Object paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public String getPaymentDetails() {
        return paymentDetails;
    }

    public void setPaymentDetails(String paymentDetails) {
        this.paymentDetails = paymentDetails;
    }

    public String getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(String loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTipValue() {
        return tipValue;
    }

    public void setTipValue(String tipValue) {
        this.tipValue = tipValue;
    }

    public Object getTipValueCus() {
        return tipValueCus;
    }

    public void setTipValueCus(Object tipValueCus) {
        this.tipValueCus = tipValueCus;
    }

    public String getDiscountPoints() {
        return discountPoints;
    }

    public void setDiscountPoints(String discountPoints) {
        this.discountPoints = discountPoints;
    }

    public String getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(String discountAmount) {
        this.discountAmount = discountAmount;
    }

    public Object getDelayMin() {
        return delayMin;
    }

    public void setDelayMin(Object delayMin) {
        this.delayMin = delayMin;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getPayTo() {
        return payTo;
    }

    public void setPayTo(String payTo) {
        this.payTo = payTo;
    }

    public String getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }

    public String getDeliveryCharge() {
        return deliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
    }

    public String getCCR() {
        return cCR;
    }

    public void setCCR(String cCR) {
        this.cCR = cCR;
    }

    public String getCCF() {
        return cCF;
    }

    public void setCCF(String cCF) {
        this.cCF = cCF;
    }

    public String getZR() {
        return zR;
    }

    public void setZR(String zR) {
        this.zR = zR;
    }

    public String getZF() {
        return zF;
    }

    public void setZF(String zF) {
        this.zF = zF;
    }

    public String getZC() {
        return zC;
    }

    public void setZC(String zC) {
        this.zC = zC;
    }

    public String getFeedbackMailStatus() {
        return feedbackMailStatus;
    }

    public void setFeedbackMailStatus(String feedbackMailStatus) {
        this.feedbackMailStatus = feedbackMailStatus;
    }

    public String getDelayMailStatus() {
        return delayMailStatus;
    }

    public void setDelayMailStatus(String delayMailStatus) {
        this.delayMailStatus = delayMailStatus;
    }

    public String getWeekReorderMailStatus() {
        return weekReorderMailStatus;
    }

    public void setWeekReorderMailStatus(String weekReorderMailStatus) {
        this.weekReorderMailStatus = weekReorderMailStatus;
    }

    public String getMinOrderDifference() {
        return minOrderDifference;
    }

    public void setMinOrderDifference(String minOrderDifference) {
        this.minOrderDifference = minOrderDifference;
    }

    public Object getCard() {
        return card;
    }

    public void setCard(Object card) {
        this.card = card;
    }

    public Object getJsonData() {
        return jsonData;
    }

    public void setJsonData(Object jsonData) {
        this.jsonData = jsonData;
    }

    public Object getDelCharge() {
        return delCharge;
    }

    public void setDelCharge(Object delCharge) {
        this.delCharge = delCharge;
    }

    public String getGuest() {
        return guest;
    }

    public void setGuest(String guest) {
        this.guest = guest;
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

    public List<OrderFullDetailInner> getDetail() {
        return detail;
    }

    public void setDetail(List<OrderFullDetailInner> detail) {
        this.detail = detail;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getMail_sent() {
        return mail_sent;
    }

    public void setMail_sent(String mail_sent) {
        this.mail_sent = mail_sent;
    }

    public Integer getIs_schedule() {
        return is_schedule;
    }
}
