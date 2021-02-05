package zingmyorder.kitchen.mobile;

import retrofit2.Call;
import zingmyorder.kitchen.mobile.view.login.model.KitchenLoginResponse;
import zingmyorder.kitchen.mobile.models.LoginModel;
import zingmyorder.kitchen.mobile.view.login.model.TokenUpdateSuccess;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderDetails;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderStatusUpdate;
import zingmyorder.kitchen.mobile.view.orders.model.OrdersWrapper;
import com.google.gson.JsonObject;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("")
    Observable<JsonObject> doSignup(@Body JsonObject request,
                                    @Header("User-Agent") String userAgent);

    @POST("")
    Observable<JsonObject> doLogin(@Body LoginModel request,
                                    @Header("User-Agent") String userAgent);

    @GET("/api/kitchen/cart/orders/list")
    Observable<OrdersWrapper> getOrderList(@Query("api_token")String api_token);

    @GET("/api/kitchen/cart/order/{order_id}")
    Observable<OrderDetails> getOrderDetails(@Path("order_id")String order_id, @Query("api_token")String api_token);

    @GET("/api/kitchen/cart/order/accept/{order_id}")
    Observable<OrderStatusUpdate> acceptOrderStatus(@Path ("order_id")String order_id, @Query("api_token")String api_token, @Query("prep_time")String prep_time);

    @GET("/api/kitchen/cart/order/delay/{order_id}/{delay_time}")
    Observable<OrderStatusUpdate> setDelayTime(@Path ("order_id")String order_id,@Path ("delay_time")String delay_time,@Query("api_token")String api_token);

    @GET("/api/kitchen/cart/order-update/{order_id}")
    Observable<OrderStatusUpdate> updateCustomerArrivedComplete(@Path ("order_id")String order_id,@Query("api_token")String api_token,@Query("is_car_pickup")String is_car_pickup);

    @GET("/api/kitchen")
    Observable<KitchenLoginResponse> performLoginNew(@Header("Authorization") String token);

    @GET("/api/kitchen/token-check")
    Observable<TokenUpdateSuccess> updateFcmToken(@Query("token")String api_token, @Query("fcm")String fcm,@Query("device_id")String android_id);

    @GET("/api/kitchen/cart/order/{order_type}/{order_id}")
    Observable<OrderStatusUpdate> updateOrderReady(@Path ("order_type")String order_type, @Path ("order_id")String order_id, @Query("api_token")String api_token);

    @GET("/api/kitchen/cart/order-update/{order_id}")
    Observable<OrderStatusUpdate> markAsComplete(@Path ("order_id")String order_id,@Query("api_token")String api_token,@Query("flag")String flag);

    @GET("/api/kitchen/diviceid-login")
    Observable<KitchenLoginResponse> deviceIdLoginNew(@Query("device_id") String token);
}
