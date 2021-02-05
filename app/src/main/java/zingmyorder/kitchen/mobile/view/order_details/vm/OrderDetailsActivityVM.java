package zingmyorder.kitchen.mobile.view.order_details.vm;

import androidx.lifecycle.MutableLiveData;

import zingmyorder.kitchen.mobile.DataManager;
import zingmyorder.kitchen.mobile.ResponseListener;
import zingmyorder.kitchen.mobile.SingleLiveEvent;
import zingmyorder.kitchen.mobile.base.BaseViewModel;
import zingmyorder.kitchen.mobile.models.ApiResponse;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderDetails;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderStatusUpdate;

import javax.inject.Inject;

public class OrderDetailsActivityVM extends BaseViewModel {

    final DataManager mDataManager;
    public SingleLiveEvent<Boolean> loadingStatus=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> closePage=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> acceptClick=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> acknowledgeClick=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> delayClick=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> markComplete=new SingleLiveEvent<>();
    public SingleLiveEvent<Void> printClick=new SingleLiveEvent<>();
    public MutableLiveData<ApiResponse<OrderDetails>> orderDetailsResponse=new MutableLiveData<>();
    public MutableLiveData<ApiResponse<OrderStatusUpdate>> acceptStatus=new MutableLiveData<>();
    public MutableLiveData<ApiResponse<OrderStatusUpdate>> delayTimeStatus=new MutableLiveData<>();
    public MutableLiveData<ApiResponse<OrderStatusUpdate>> updateOrderReadyStatus=new MutableLiveData<>();
    public MutableLiveData<ApiResponse<OrderStatusUpdate>> acknowledgeStatus=new MutableLiveData<>();
    public MutableLiveData<ApiResponse<OrderStatusUpdate>> completeStatus=new MutableLiveData<>();
    public MutableLiveData<Integer> customerArrived=new MutableLiveData<>();
    public MutableLiveData<String> error=new MutableLiveData<>();
    public SingleLiveEvent<Void> orderReadyClick=new SingleLiveEvent<>();

    @Inject
    public OrderDetailsActivityVM(DataManager mDataManager) {
        super(mDataManager);
        this.mDataManager = mDataManager;
    }

    public void onClosePage() {
        closePage.call();
    }

    public void onAcceptClick(){
        acceptClick.call();
    }

    public void onAckClick(){
        acknowledgeClick.call();
    }

    public void onDelayTime() {
        delayClick.call();
    }

    public void onMarkComplete() {
        markComplete.call();
    }

    public void onPrintClick(){
        printClick.call();
    }

    public int getPrinterType() {
        return mDataManager.getPrinterType();
    }

    public void onOrderReady() {
        orderReadyClick.call();
    }

    public String getRingtone() {
        return mDataManager.getRingtone();
    }

    public String getDefaultPrinterId() {
        return mDataManager.getDefaultPrinterID();
    }

    public void getOrderDetails(String orderID) {
        mDataManager.fetchOderDetails(orderID, new ResponseListener<OrderDetails>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrderDetails> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        orderDetailsResponse.setValue(apiResponse);
                        break;
                    case "400":
                        error.setValue("error 400");
                        break;
                    case "500":
                        error.setValue("error 500");
                        break;
                    default:
                        error.setValue("Oops..Something went wrong!");
                        break;
                }
            }
        });
    }

    public void acceptOrder(String orderID,String prepTime) {
        mDataManager.acceptOrder(orderID, prepTime, new ResponseListener<OrderStatusUpdate>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrderStatusUpdate> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        acceptStatus.setValue(apiResponse);
                        break;
                    case "400":
                        error.setValue("error 400");
                        break;
                    case "500":
                        error.setValue("error 500");
                        break;
                    default:
                        error.setValue("Oops..Something went wrong!");
                        break;
                }
            }
        });
    }

    public void setDelayTime(String orderID,String delayTime) {
        mDataManager.setDelayTime(orderID, delayTime, new ResponseListener<OrderStatusUpdate>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrderStatusUpdate> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        delayTimeStatus.setValue(apiResponse);
                        break;
                    case "400":
                        error.setValue("error 400");
                        break;
                    case "500":
                        error.setValue("error 500");
                        break;
                    default:
                        error.setValue("Oops..Something went wrong!");
                        break;
                }
            }
        });
    }

    public void updateOrderReady(String orderType,String orderID) {
        mDataManager.updateOrderReady(orderType, orderID, new ResponseListener<OrderStatusUpdate>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrderStatusUpdate> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        updateOrderReadyStatus.setValue(apiResponse);
                        break;
                    case "400":
                        error.setValue("error 400");
                        break;
                    case "500":
                        error.setValue("error 500");
                        break;
                    default:
                        error.setValue("Oops..Something went wrong!");
                        break;
                }
            }
        });
    }

    public void acknowledgeOrder(String orderType,String orderID) {
        mDataManager.updateOrderReady(orderType, orderID, new ResponseListener<OrderStatusUpdate>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrderStatusUpdate> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        acknowledgeStatus.setValue(apiResponse);
                        break;
                    case "400":
                        error.setValue("error 400");
                        break;
                    case "500":
                        error.setValue("error 500");
                        break;
                    default:
                        error.setValue("Oops..Something went wrong!");
                        break;
                }
            }
        });
    }

    public void markAsComplete(String orderId,String flag) {
        mDataManager.markAsComplete(orderId, flag, new ResponseListener<OrderStatusUpdate>() {
            @Override
            public void onStart() {
                loadingStatus.setValue(true);
            }

            @Override
            public void onFinish() {
                loadingStatus.setValue(false);
            }

            @Override
            public void onResponse(ApiResponse<OrderStatusUpdate> apiResponse) {
                loadingStatus.setValue(false);
                switch (apiResponse.errorCode) {
                    case "200":
                        completeStatus.setValue(apiResponse);
                        break;
                    case "400":
                        error.setValue("error 400");
                        break;
                    case "500":
                        error.setValue("error 500");
                        break;
                    default:
                        error.setValue("Oops..Something went wrong!");
                        break;
                }
            }
        });
    }
}
