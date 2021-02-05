package zingmyorder.kitchen.mobile.view.orders.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuDetail {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("category_id")
    @Expose
    private String categoryId;
    @SerializedName("master_category")
    @Expose
    private Object masterCategory;
    @SerializedName("search_text")
    @Expose
    private Object searchText;
    @SerializedName("master_search")
    @Expose
    private Object masterSearch;
    @SerializedName("master_updated")
    @Expose
    private Object masterUpdated;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("category_temp")
    @Expose
    private Object categoryTemp;
    @SerializedName("description")
    @Expose
    private Object description;
    @SerializedName("price")
    @Expose
    private String price;
//    @SerializedName("image")
//    @Expose
//    private List<Image> image = null;
    @SerializedName("menu_variations")
    @Expose
    private Object menuVariations;
    @SerializedName("timing")
    @Expose
    private Object timing;
    @SerializedName("offer")
    @Expose
    private Object offer;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("stock_status")
    @Expose
    private String stockStatus;
    @SerializedName("youtube_link")
    @Expose
    private Object youtubeLink;
    @SerializedName("home_show")
    @Expose
    private String homeShow;
    @SerializedName("serves")
    @Expose
    private Object serves;
    @SerializedName("max_order_count")
    @Expose
    private Object maxOrderCount;
    @SerializedName("catering")
    @Expose
    private String catering;
    @SerializedName("image_published")
    @Expose
    private String imagePublished;
    @SerializedName("popular_dish_no")
    @Expose
    private String popularDishNo;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_type")
    @Expose
    private String userType;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("deleted_at")
    @Expose
    private Object deletedAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public Object getMasterCategory() {
        return masterCategory;
    }

    public void setMasterCategory(Object masterCategory) {
        this.masterCategory = masterCategory;
    }

    public Object getSearchText() {
        return searchText;
    }

    public void setSearchText(Object searchText) {
        this.searchText = searchText;
    }

    public Object getMasterSearch() {
        return masterSearch;
    }

    public void setMasterSearch(Object masterSearch) {
        this.masterSearch = masterSearch;
    }

    public Object getMasterUpdated() {
        return masterUpdated;
    }

    public void setMasterUpdated(Object masterUpdated) {
        this.masterUpdated = masterUpdated;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCategoryTemp() {
        return categoryTemp;
    }

    public void setCategoryTemp(Object categoryTemp) {
        this.categoryTemp = categoryTemp;
    }

    public Object getDescription() {
        return description;
    }

    public void setDescription(Object description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

//    public List<Image> getImage() {
//        return image;
//    }
//
//    public void setImage(List<Image> image) {
//        this.image = image;
//    }

    public Object getMenuVariations() {
        return menuVariations;
    }

    public void setMenuVariations(Object menuVariations) {
        this.menuVariations = menuVariations;
    }

    public Object getTiming() {
        return timing;
    }

    public void setTiming(Object timing) {
        this.timing = timing;
    }

    public Object getOffer() {
        return offer;
    }

    public void setOffer(Object offer) {
        this.offer = offer;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStockStatus() {
        return stockStatus;
    }

    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    public Object getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(Object youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getHomeShow() {
        return homeShow;
    }

    public void setHomeShow(String homeShow) {
        this.homeShow = homeShow;
    }

    public Object getServes() {
        return serves;
    }

    public void setServes(Object serves) {
        this.serves = serves;
    }

    public Object getMaxOrderCount() {
        return maxOrderCount;
    }

    public void setMaxOrderCount(Object maxOrderCount) {
        this.maxOrderCount = maxOrderCount;
    }

    public String getCatering() {
        return catering;
    }

    public void setCatering(String catering) {
        this.catering = catering;
    }

    public String getImagePublished() {
        return imagePublished;
    }

    public void setImagePublished(String imagePublished) {
        this.imagePublished = imagePublished;
    }

    public String getPopularDishNo() {
        return popularDishNo;
    }

    public void setPopularDishNo(String popularDishNo) {
        this.popularDishNo = popularDishNo;
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

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
