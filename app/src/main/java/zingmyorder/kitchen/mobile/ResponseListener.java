package zingmyorder.kitchen.mobile;


import zingmyorder.kitchen.mobile.models.ApiResponse;

public interface ResponseListener<T> {
    void onStart();

    void onFinish();

    void onResponse(ApiResponse<T> apiResponse);
}
