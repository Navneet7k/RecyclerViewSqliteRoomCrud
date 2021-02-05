package zingmyorder.kitchen.mobile.view.order_details;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.print.PrintManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import zingmyorder.kitchen.mobile.BR;

import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.Utils;
import zingmyorder.kitchen.mobile.base.BaseActivity;
import zingmyorder.kitchen.mobile.databinding.ActivityOrderDetailsBinding;
import zingmyorder.kitchen.mobile.view.order_details.model.OrderFullDetail;
import zingmyorder.kitchen.mobile.view.order_details.vm.OrderDetailsActivityVM;
import zingmyorder.kitchen.mobile.view.orders.model.OrderFullDetailInner;
import zingmyorder.kitchen.mobile.view.orders.model.Orders;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.inject.Inject;

public class OrderDetailsActivity extends BaseActivity<ActivityOrderDetailsBinding, OrderDetailsActivityVM> {

    private ActivityOrderDetailsBinding binding;
    private AlertDialog alertDialog;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothDevice selectedBluetoothDevice;
    private ArrayList<OrderFullDetailInner> orderFullDetailInners=new ArrayList<>();
    private OrderFullDetail selectedOrderFullDetails;
    private Orders OrderResponse;

    OutputStream outputStream;
    InputStream inputStream;
    BluetoothSocket bluetoothSocket;
    Thread thread;
    byte FONT_TYPE;
    String app_token="";
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;
    String orderIDvalue="";
    private boolean isAccepted;
    PopupWindow popupWindow;
    MediaPlayer mPlayer2;
    private String selected_ringtone="1";
    private String tab_type="";
    private boolean isOrderComplete;
    private OrderContentsAdapter orderContentsAdapter;
    private AlertDialog dialog;

    String storeName="Boses Pizza - Keller",orderNo="",customerName="Customer : Ashlea McCleskey",cPhone="Phone : 000-000-0000",subTotal="$34.34",sTotal="$34.34",tax="",tip="",storeAddress="",deliveryType="";

    @Inject
    OrderDetailsActivityVM orderDetailsActivityVM;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_order_details;
    }

    @Override
    public OrderDetailsActivityVM getViewModel() {
        return orderDetailsActivityVM;
    }

    private BroadcastReceiver updates_receiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent i) {
            int countOrder=0;
            String filter = Utils. INFO_UPDATE_FILTER ;
            String orders_count=i.getStringExtra("orders_count");
            if (!TextUtils.isEmpty(orders_count))
                countOrder=Integer.parseInt(orders_count);
            if (i.getAction (). equals (filter)) {
                    if (getWindow()!=null && getWindow().getDecorView().getRootView().isShown()) {
                        if (countOrder>0) {
                            showNewOrderPopup(countOrder);
                        }
                    }
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=getViewDataBinding();

        LocalBroadcastManager. getInstance ( this ). registerReceiver ( updates_receiver , new IntentFilter(Utils. INFO_UPDATE_FILTER ));
        Intent intent=getIntent();
        if (intent!=null) {
            Orders orders = intent.getParcelableExtra("order_detail");
            String tabType =intent.getStringExtra("tabType");
            tab_type=tabType;
            setOrderMode(orders);
            if (!TextUtils.isEmpty(tabType))
                setupButtons(tabType,orders);
            if (orders!=null) {
                OrderResponse=orders;
                orderIDvalue = String.valueOf(orders.getId());
                binding.orderIdTV.setText("# "+orderIDvalue);
                orderDetailsActivityVM.getOrderDetails(orderIDvalue);
            }
        }

        mBluetoothAdapter=BluetoothAdapter.getDefaultAdapter();

        if (orderDetailsActivityVM.getPrinterType()==1 && mBluetoothAdapter!=null) {
            if (mBluetoothAdapter.isEnabled()) {
                fetchPairedItem();
            }

        }

        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.orderContentsRV.getContext(),
                layoutManager.getOrientation());
        binding.orderContentsRV.addItemDecoration(dividerItemDecoration);
        binding.orderContentsRV.setLayoutManager(layoutManager);
        fillOrderContents();

        binding.btnRingOff.setOnClickListener(view -> {
            binding.btnRingOff.setBackgroundTintList(ColorStateList.valueOf(Color
                    .parseColor("#9DA7B0")));
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            mPlayer2=null;
        });
    }

    private void setOrderMode(Orders order) {
        if (order.getNon_contact()==1){
            binding.pickupTV.setText("\u2022  Non Contact");
        } else if (order.getCar_pickup()==1){
            binding.pickupTV.setText("\u2022 Car Pickup");
            if (order.getCar_details()!=null) {
                binding.cardDetailsLL.setVisibility(View.VISIBLE);
                binding.carMakeTV.setText(order.getCar_details().getNumber());
                binding.carColorTV.setText(order.getCar_details().getColor());
                binding.parkingInfoTV.setText(order.getCar_details().getInfo());
            }
        } else {
            binding.pickupTV.setVisibility(View.GONE);
        }
    }

    private void updateReadyButtuon(String mailSent,String orderType) {
        final int mail_sent= TextUtils.isEmpty(mailSent)?0:Integer.parseInt(mailSent);
        if (orderType.equalsIgnoreCase("delivery")) {
            binding.orderTypeTV.setTextColor(getResources().getColor(R.color.mood_green));
            binding.btnReadyForPickup.setText("OUT FOR DELIVERY");
            if (mail_sent >= 1){
                binding.btnReadyForPickup.setEnabled(false);
//                binding.btnReadyForPickup.setText("COMPLETED");
                binding.btnReadyForPickup.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
            }

        } else {
            binding.orderTypeTV.setTextColor(getResources().getColor(R.color.vrob_color_pink));
            binding.btnReadyForPickup.setText("READY FOR PICKUP");
            if (mail_sent >= 1){
                binding.btnReadyForPickup.setEnabled(false);
//                binding.btnReadyForPickup.setText("COMPLETED");
                binding.btnReadyForPickup.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
            }
        }
    }

    private void setupButtons(String tabType,Orders order) {
        if (tabType.equalsIgnoreCase("new"))
            binding.newOrderBtnsLL.setVisibility(View.VISIBLE);
        else if (tabType.equalsIgnoreCase("future")) {
            binding.futureOrderBtnsLL.setVisibility(View.VISIBLE);
            if (order.getIs_ack()==1) {
                binding.futureAckBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
                binding.futureAckBtn.setEnabled(false);
            }
        }
        else if (tabType.equalsIgnoreCase("accepted")) {
            binding.acceptedOrderBtnsLL.setVisibility(View.VISIBLE);
            binding.btnReadyForPickup.setVisibility(View.VISIBLE);
            binding.delayCompleteLL.setVisibility(View.VISIBLE);
            if (order.getFlag()==null||order.getFlag().equalsIgnoreCase("0")) {
//                binding.orderContentsRV.setVisibility(View.VISIBLE);
                isOrderComplete=false;
                binding.orderCompleteTV.setVisibility(View.GONE);
                binding.completeBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey));
                binding.completeBtn.setEnabled(true);
            } else {
//                binding.orderContentsRV.setVisibility(View.GONE);
//                binding.noOfItemsTV.setVisibility(View.GONE);
//                binding.amountLL.setVisibility(View.GONE);
                isOrderComplete=true;
                binding.orderCompleteTV.setVisibility(View.VISIBLE);
                binding.completeBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
                binding.completeBtn.setEnabled(false);

                binding.delayBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
                binding.delayBtn.setEnabled(false);
            }
            updateReadyButtuon(order.getMail_sent(),order.getOrderType());
        }
    }

    @Override
    public void onBackPressed() {
        if (mPlayer2!=null && mPlayer2.isPlaying())
            mPlayer2.stop();
        Intent intent=new Intent();
        intent.putExtra("isAccepted",isAccepted);
        intent.putExtra("tab_type",tab_type);
        setResult(2333, intent);
        super.onBackPressed();
    }

    private void fillOrderContents() {
        ArrayList<OrderFullDetailInner> orderContentSamples=new ArrayList<>();
//        orderContentSamples.add(new OrderContentSample("Chicken Biryani","Instructions: Special Instructions","$7.58"));
//        orderContentSamples.add(new OrderContentSample("Grilled Ham & Cheese","Instructions: Special Instructions","$32.00"));
        OrderContentsAdapter adapter=new OrderContentsAdapter(orderContentSamples,this,isOrderComplete);
        binding.orderContentsRV.setAdapter(adapter);
    }

    private void fetchPairedItem() {
        String deviceAddress = orderDetailsActivityVM.getDefaultPrinterId();
        if (deviceAddress!=null) {
            Set<BluetoothDevice> bluetoothDevices=mBluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device:bluetoothDevices) {
                if (device.getAddress().equalsIgnoreCase(deviceAddress)){
                    selectedBluetoothDevice=device;
                    Toast.makeText(this, "Auto Connected to "+device.getName(), Toast.LENGTH_SHORT).show();
                }
            }
        }
//        startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE),REQUEST_DISCOVER_BT);
    }

    private void orderReadyStatus() {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this,R.style.DialogSlideAnim);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_with_btns_view, null);
        dialogBuilder.setView(dialogView);
        String headerText="";
        AppCompatTextView dialogHeader=dialogView.findViewById(R.id.dialogHeader);

        if (selectedOrderFullDetails.getOrderType().equalsIgnoreCase("delivery")) {
            headerText="Update Status as Out for delivery";
        } else {
            headerText="Update Status as Ready for Pickup";
        }

        dialogHeader.setText(headerText);
        LinearLayout ok_button_wrap=dialogView.findViewById(R.id.ok_button_wrap);
        LinearLayout cancel_button_wrap=dialogView.findViewById(R.id.cancel_button_wrap);
        Button ok_button=dialogView.findViewById(R.id.ok_button);
        Button cancel_button=dialogView.findViewById(R.id.cancel_button);
        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            alertDialog.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        }

        ok_button_wrap.setOnClickListener(v -> {
            alertDialog.dismiss();
            orderDetailsActivityVM.updateOrderReady(selectedOrderFullDetails.getOrderType().equalsIgnoreCase("Delivery")?"delivery":"pickup",String.valueOf(selectedOrderFullDetails.getId()));
        });

        cancel_button_wrap.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void showNewOrderPopup(int newOrders) {
//        if (binding.btnRingOff.getVisibility()==View.GONE)
//            binding.btnRingOff.setVisibility(View.VISIBLE);
        selected_ringtone=orderDetailsActivityVM.getRingtone();
        if (mPlayer2==null)
        {
            if (selected_ringtone.equalsIgnoreCase("1"))
                mPlayer2 = MediaPlayer.create(this, R.raw.alert43);
            else if (selected_ringtone.equalsIgnoreCase("2"))
                mPlayer2 = MediaPlayer.create(this, R.raw.ring);
            else if (selected_ringtone.equalsIgnoreCase("3"))
                mPlayer2 = MediaPlayer.create(this, R.raw.short_ring);
            else if (selected_ringtone.equalsIgnoreCase("4"))
                mPlayer2 = MediaPlayer.create(this, R.raw.bell);
            else
                mPlayer2 = MediaPlayer.create(this, R.raw.alert43);
        }
        mPlayer2.setLooping(true);
        mPlayer2.start();
//        View view=getLayoutInflater().inflate(R.layout.zing_new_order_popup,null,false);
//
//        RelativeLayout popupLayout=view.findViewById(R.id.popupLayout);
//        TextView newOrdersNo=view.findViewById(R.id.newOrdersNo);
//        TextView clickDismiss=view.findViewById(R.id.clickDismiss);
//        ImageButton closeDismiss=view.findViewById(R.id.closeDismiss);
//        if (newOrders>1) {
//            newOrdersNo.setText("You have "+String.valueOf(newOrders)+" new orders!");
//        } else {
//            newOrdersNo.setText("You have "+String.valueOf(newOrders)+" new order!");
//        }

//        popupLayout.setOnClickListener(v -> {
//            if (mPlayer2!=null && mPlayer2.isPlaying())
//                mPlayer2.stop();
//            popupWindow.dismiss();
//            mPlayer2=null;
//            finish();
//        });
//
//        clickDismiss.setOnClickListener(v -> {
//            if (mPlayer2!=null && mPlayer2.isPlaying())
//                mPlayer2.stop();
//            popupWindow.dismiss();
//            mPlayer2=null;
//            finish();
//        });
//        closeDismiss.setOnClickListener(v -> {
//            if (mPlayer2!=null && mPlayer2.isPlaying())
//                mPlayer2.stop();
//            popupWindow.dismiss();
//            mPlayer2=null;
//            finish();
//        });
//
//        if (popupWindow==null)
//            popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
//
//        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

        if(dialog!=null) dialog.dismiss();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogLayout = getLayoutInflater().inflate(R.layout.zing_new_order_popup_small,
                null);


        RelativeLayout popupLayout=dialogLayout.findViewById(R.id.popupLayout);
        LinearLayout newOrderLayout=dialogLayout.findViewById(R.id.newOrderLayout);
        TextView newOrdersNo=dialogLayout.findViewById(R.id.newOrdersNo);
        TextView clickDismiss=dialogLayout.findViewById(R.id.clickDismiss);
        ImageButton closeDismiss=dialogLayout.findViewById(R.id.closeDismiss);
        if (newOrders>1) {
            newOrdersNo.setText("You have "+String.valueOf(newOrders)+" new orders!");
        } else {
            newOrdersNo.setText("You have "+String.valueOf(newOrders)+" new order!");
        }

        closeDismiss.setOnClickListener(view -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            dialog.dismiss();
            mPlayer2=null;
//            finish();
        });

        clickDismiss.setOnClickListener(view -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            dialog.dismiss();
            mPlayer2=null;
            finish();
        });

        newOrderLayout.setOnClickListener(view -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            dialog.dismiss();
            mPlayer2=null;
            finish();
        });


        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (mPlayer2!=null && mPlayer2.isPlaying())
                    mPlayer2.stop();
                dialog.dismiss();
                mPlayer2=null;
            }
        });

        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (mPlayer2!=null && mPlayer2.isPlaying())
                    mPlayer2.stop();
                dialog.dismiss();
                mPlayer2=null;
            }
        });

        builder.setView(dialogLayout);

        dialog = builder.create();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        WindowManager.LayoutParams wlmp = dialog.getWindow()
                .getAttributes();
        wlmp.gravity = Gravity.BOTTOM;

        dialog.show();
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent event) {
//        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//            if (mPlayer2 != null && mPlayer2.isPlaying())
//                mPlayer2.stop();
//            mPlayer2 = null;
//        }
//        return super.dispatchTouchEvent(event);
//    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer2!=null && mPlayer2.isPlaying())
            mPlayer2.stop();
    }

    @Override
    public void subScribeObserver() {
        super.subScribeObserver();

        orderDetailsActivityVM.closePage.observe(this,aVoid -> onBackPressed());
        orderDetailsActivityVM.error.observe(this,s -> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());
        orderDetailsActivityVM.loadingStatus.observe(this,aBoolean -> {
            if (aBoolean)
                showLoading();
            else
                hideLoading();
        });

        orderDetailsActivityVM.orderReadyClick.observe(this,aVoid ->{
         if (selectedOrderFullDetails!=null)
             orderReadyStatus();
        });

        orderDetailsActivityVM.acceptClick.observe(this,aVoid -> {
            if (selectedOrderFullDetails!=null) {
                if (selectedOrderFullDetails.getIs_schedule()!=null && selectedOrderFullDetails.getIs_schedule()==1)
                    showScheduledDialog();
                else if (selectedOrderFullDetails.getOrderStatus().equalsIgnoreCase("New Orders") || selectedOrderFullDetails.getOrderStatus().equalsIgnoreCase("Scheduled"))
                    showAcceptDialog(true);
                else
                    Toast.makeText(this, "Already Accepted!", Toast.LENGTH_SHORT).show();
            }
        });

        orderDetailsActivityVM.acknowledgeClick.observe(this,aVoid -> {
            showAlertDialogWithOptionWithAnim("Notify the customer that you acknowledge the order?", "OK", "Cancel", this, new OnDialogActionListener() {
                @Override
                public void onContinue() {
                    orderDetailsActivityVM.acknowledgeOrder("ack",String.valueOf(OrderResponse.getId()));
                }

                @Override
                public void onCancel() {

                }
            });
        });

        orderDetailsActivityVM.delayClick.observe(this,aVoid -> showDelayDialog());

        orderDetailsActivityVM.markComplete.observe(this,aVoid -> showAlertDialogWithOptionWithAnim("Would you like to mark this order as complete?","OK","Cancel",this,new OnDialogActionListener(){

            @Override
            public void onContinue() {
                orderDetailsActivityVM.markAsComplete(String.valueOf(OrderResponse.getId()),"1");
            }

            @Override
            public void onCancel() {

            }
        }));

        orderDetailsActivityVM.completeStatus.observe(this,status->{
            Toast.makeText(this, "Order Completed!", Toast.LENGTH_SHORT).show();
            isAccepted=true;
            isOrderComplete=true;
            orderDetailsActivityVM.getOrderDetails(orderIDvalue);
//            binding.orderContentsRV.setVisibility(View.GONE);
//            binding.noOfItemsTV.setVisibility(View.GONE);
//            binding.amountLL.setVisibility(View.GONE);
            binding.orderCompleteTV.setVisibility(View.VISIBLE);
            binding.completeBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
            binding.completeBtn.setEnabled(false);

            binding.delayBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
            binding.delayBtn.setEnabled(false);
        });

        orderDetailsActivityVM.printClick.observe(this,aVoid -> {
            int printerType=orderDetailsActivityVM.getPrinterType();

            if (printerType==1) {
                btPrintClicked();
            } else if (printerType==2) {
                if (!TextUtils.isEmpty(getPrintStringData())) {
                    PrintManager printManager = (PrintManager) this
                            .getSystemService(Context.PRINT_SERVICE);

                    String jobName = this.getString(R.string.app_name) +
                            " Document";

                    printManager.print(jobName, new MyPrintDocumentAdapter(this, getPrintStringData()),
                            null);
                } else {
                    Toast.makeText(this, "Print Data Missing!", Toast.LENGTH_SHORT).show();
                }
            } else
                Toast.makeText(this, "Please select printer from settings!", Toast.LENGTH_SHORT).show();
        });

        orderDetailsActivityVM.acceptStatus.observe(this,acceptStatus->{
            if (acceptStatus!=null && acceptStatus.data!=null) {
                if (acceptStatus.data.getStatus().equalsIgnoreCase("Completed")) {
                    Toast.makeText(this, "Order Accepted Successfully!", Toast.LENGTH_SHORT).show();
                    isAccepted=true;
                    binding.acceptBtn.setEnabled(false);
                    binding.acceptBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
                    orderDetailsActivityVM.getOrderDetails(orderIDvalue);
                }
                else
                    Toast.makeText(this, "Order Already Accepted!", Toast.LENGTH_SHORT).show();
            }
        });

        orderDetailsActivityVM.delayTimeStatus.observe(this,delayTimeStatus->{
            if (delayTimeStatus!=null && delayTimeStatus.data!=null) {
                orderDetailsActivityVM.getOrderDetails(orderIDvalue);
                Toast.makeText(this, delayTimeStatus.data.getStatus(), Toast.LENGTH_SHORT).show();
            }
        });

        orderDetailsActivityVM.updateOrderReadyStatus.observe(this,orderStatusUpdateApiResponse -> {
            binding.btnReadyForPickup.setEnabled(false);
//            binding.btnReadyForPickup.setText("COMPLETED");
            binding.btnReadyForPickup.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
        });

        orderDetailsActivityVM.acknowledgeStatus.observe(this,orderStatusUpdateApiResponse -> {
            binding.futureAckBtn.setEnabled(false);
//            binding.btnReadyForPickup.setText("COMPLETED");
            binding.futureAckBtn.setBackground(getResources().getDrawable(R.drawable.rounded_border_grey_unselected));
        });

        orderDetailsActivityVM.orderDetailsResponse.observe(this,orderDetailsApiResponse -> {
            if (orderDetailsApiResponse!=null && orderDetailsApiResponse.data!=null && orderDetailsApiResponse.data.getOrder()!=null) {
                selectedOrderFullDetails=orderDetailsApiResponse.data.getOrder();
                updateReadyButtuon(selectedOrderFullDetails.getMail_sent(),selectedOrderFullDetails.getOrderType());
                deliveryType=orderDetailsApiResponse.data.getOrder().getOrderType();
                storeName = orderDetailsApiResponse.data.getOrder().getRestaurant_name();
                orderNo="#"+String.valueOf(orderDetailsApiResponse.data.getOrder().getId());
                storeAddress="Address : "+orderDetailsApiResponse.data.getOrder().getAddress();

                cPhone="Phone : "+orderDetailsApiResponse.data.getOrder().getPhone();
                sTotal="$"+orderDetailsApiResponse.data.getOrder().getTotal();
                subTotal="$"+orderDetailsApiResponse.data.getOrder().getSubtotal();
                tax="$"+orderDetailsApiResponse.data.getOrder().getTax();
                tip="$"+orderDetailsApiResponse.data.getOrder().getTip();
                customerName="Customer : "+orderDetailsApiResponse.data.getOrder().getName();

                binding.customerNameTV.setText(orderDetailsApiResponse.data.getOrder().getName());
                binding.orderTypeTV.setText(orderDetailsApiResponse.data.getOrder().getOrderType());
                binding.customerPhoneTV.setText(cPhone);
                if (!TextUtils.isEmpty(orderDetailsApiResponse.data.getOrder().getAddress()))
                binding.customerAddressTV.setText(storeAddress);
                else
                    binding.customerAddressTV.setVisibility(View.GONE);

                binding.subTotalTV.setText(subTotal);
                binding.totalTV.setText(sTotal);
                binding.taxFeesTV.setText(tax);
                binding.tipTV.setText(tip);

//                binding.receivedTimeTV.setText(orderDetailsApiResponse.data.getOrder().getCreatedAt());
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                SimpleDateFormat dateFormat= new SimpleDateFormat("E, MMM dd, hh:mm a");
                SimpleDateFormat todayFormat= new SimpleDateFormat("hh:mm a");
                try {
                    Date date = inputFormat.parse(orderDetailsApiResponse.data.getOrder().getReadyTime());
                    Date date1 = inputFormat.parse(orderDetailsApiResponse.data.getOrder().getCreatedAt());
                    if (date!=null && DateUtils.isToday(date.getTime())) {
//                        date = todayFormat.format(orderDetailsApiResponse.data.getOrder().getReadyTime());
                        String todayStr="Today "+todayFormat.format(date);
                        binding.orderDateTV.setText(orderDetailsApiResponse.data.getOrder().getOrderType().equalsIgnoreCase("Delivery")?"Delivery at "+todayStr:"Pickup at "+todayStr);
                    }
                    else
                    binding.orderDateTV.setText(orderDetailsApiResponse.data.getOrder().getOrderType().equalsIgnoreCase("Delivery")?"Delivery at "+dateFormat.format(date):"Pickup at "+dateFormat.format(date));
                    binding.receivedTimeTV.setText("Received at "+dateFormat.format(date1));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
//                binding.orderDateTV.setText(orderDetailsApiResponse.data.getOrder().getReadyTime());

                orderFullDetailInners=new ArrayList<>(orderDetailsApiResponse.data.getOrder().getDetail());
                int totalQty = 0;
                for(OrderFullDetailInner ord:orderDetailsApiResponse.data.getOrder().getDetail()){
                    totalQty+=Integer.parseInt(ord.getQuantity());
                }
                String noOfItems= getResources().getQuantityString(R.plurals.str_item_total,orderFullDetailInners.size(), totalQty);
                binding.noOfItemsTV.setText(noOfItems);
                orderContentsAdapter=new OrderContentsAdapter(new ArrayList<>(orderDetailsApiResponse.data.getOrder().getDetail()),this,isOrderComplete);
                binding.orderContentsRV.setAdapter(orderContentsAdapter);
                if (isOrderComplete){
                   tvDisabledColor();
                    orderContentsAdapter.setOrderComplete();
                }
            }
        });
    }

    private void tvDisabledColor() {
        int lightgreyID=R.color.lightest_grey;
        binding.noOfItemsTV.setTextColor(getResources().getColor(lightgreyID));
        binding.customerNameTV.setTextColor(getResources().getColor(lightgreyID));
        binding.orderTypeTV.setTextColor(getResources().getColor(lightgreyID));
        binding.customerPhoneTV.setTextColor(getResources().getColor(lightgreyID));

        binding.subTotalTV.setTextColor(getResources().getColor(lightgreyID));
        binding.totalTV.setTextColor(getResources().getColor(lightgreyID));
        binding.taxFeesTV.setTextColor(getResources().getColor(lightgreyID));
        binding.tipTV.setTextColor(getResources().getColor(lightgreyID));
        binding.totalLabel.setTextColor(getResources().getColor(lightgreyID));
        binding.subTotalLabel.setTextColor(getResources().getColor(lightgreyID));
        binding.tipLabel.setTextColor(getResources().getColor(lightgreyID));
        binding.taxFeesLabel.setTextColor(getResources().getColor(lightgreyID));
    }

    private void btPrintClicked() {
        if (mBluetoothAdapter.isEnabled() && selectedBluetoothDevice != null) {
            try {
                printData();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!mBluetoothAdapter.isEnabled())
            Toast.makeText(this, "Please turn ON bluetooth", Toast.LENGTH_SHORT).show();

        if (selectedBluetoothDevice == null)
            Toast.makeText(this, "Please select a bluetooth device!", Toast.LENGTH_SHORT).show();
    }

    void printData() throws  IOException{
        try{
            byte[] cc = new byte[]{0x1B,0x21,0x03};
            byte[] bb3 = new byte[]{0x1B,0x21,0x08};
//            byte[] command = Utils.decodeBitmap(textImage);
//            String totalmt=  "Total :" + "     " + subTotal;
//            String msg = "Sample Message to print";
//            String bill="\t\t"+storeName+"\n" +
//                    "\t\t"+orderNo+"\n" +
//                    "\n" +
//                    "\t\t"+customerName+"\n" +
//                    "\t\t"+cPhone+"\n" +
//                    "\n" +
//                    "Item\t\t\t\t\t\tQty\n" +
//                    "—————————————————\n";
//            msg+="\n";
//
//            for (OrderFullDetailInner orderFullDetailInner:orderFullDetailInners) {
//                bill+=orderFullDetailInner.getMenu().getName()+"\t\t\t\t\t\t"+orderFullDetailInner.getQuantity()+"\n";
//            }
//
//            bill+="—————————————————\n"+
//                    "Total : "+subTotal;
//            bill+="\n\n";

            String empty="";

            String BILL = deliveryType.toUpperCase()+" ORDER "+orderNo+"\n"
                    +"zingmyorder.com"+"\n"
                    +storeName+"\n" +
                    customerName+"\n" +
                    cPhone+"\n";
            if (deliveryType.equalsIgnoreCase("delivery")) {
                BILL = BILL + storeAddress+"\n";
            }
            BILL = BILL
                    + "--------------------------------\n";


            BILL = BILL + String.format("%-3s %-15s", "Qty", "Item");
            BILL = BILL + "\n";
            BILL = BILL
                    + "--------------------------------";
            for (OrderFullDetailInner orderFullDetailInner:orderFullDetailInners) {
                int addonsSize=orderFullDetailInner.getAddons().size();
                int itemNameLength=orderFullDetailInner.getMenu().getName().length();
                String addonsData="";
                if (itemNameLength<=28) {
                    BILL = BILL + "\n" + String.format("%-3s %-15s", orderFullDetailInner.getQuantity(), (orderFullDetailInner.getMenu().getName())) + "\n";
                } else {
                    String[] itemNameSplits=getSplitStrings(orderFullDetailInner.getMenu().getName());
                    for (int i=0;i<itemNameSplits.length;i++){
                        if (i == 0)
                            BILL = BILL + "\n" + String.format("%-3s %-15s", orderFullDetailInner.getQuantity(), itemNameSplits[i]) + "\n";
                        else
                            BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[i]) + "\n";
                    }
                    Toast.makeText(this, "Length", Toast.LENGTH_SHORT).show();
                }
                if (!TextUtils.isEmpty(orderFullDetailInner.getVariation())) {
                    String fulltext="Variation : "+orderFullDetailInner.getVariation();
                    if (fulltext.length()<=28) {
                        BILL = BILL + String.format("%-3s %-15s", empty, fulltext) + "\n";
                    } else {
                        String[] itemNameSplits=getSplitStrings(fulltext);
                        for (int i=0;i<itemNameSplits.length;i++){
                            BILL = BILL + String.format("%-3s %-15s", empty,itemNameSplits[i]) + "\n";
                        }
                    }
                }

                if (addonsSize > 1) {
                    for (int i=0; i<addonsSize-1;i++) {
                        List<String> addons = orderFullDetailInner.getAddons().get(i);
                        String addonPlus=addons.get(1);
                        if (addonPlus.length()<=28) {
                            BILL += String.format("%-3s %-15s %n", empty, addonPlus);
                        } else {
                            String[] itemNameSplits=getSplitStrings(addonPlus);
                            for (int j=0;j<itemNameSplits.length;j++){
                                BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[j]) + "\n";
                            }
                        }
                    }
                    String addpnPlus1=orderFullDetailInner.getAddons().get(addonsSize-1).get(1);
                    if (addpnPlus1.length()<=28) {
                        BILL += String.format("%-3s %-15s", empty, addpnPlus1);
                    } else {
                        String[] itemNameSplits=getSplitStrings(addpnPlus1);
                        for (int j=0;j<itemNameSplits.length;j++){
                            BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[j]) + "\n";
                        }
                    }
                } else  if (orderFullDetailInner.getAddons().size() == 1) {
                    String addonPlus2=orderFullDetailInner.getAddons().get(0).get(1);
                    if (addonPlus2.length()<=28) {
                        BILL += String.format("%-3s %-15s", empty, addonPlus2);
                    } else {
                        String[] itemNameSplits=getSplitStrings(addonPlus2);
                        for (int j=0;j<itemNameSplits.length;j++){
                            BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[j]) + "\n";
                        }
                    }
                }

                if (!TextUtils.isEmpty(orderFullDetailInner.getSpecialInstr())) {
                    String fulltext="Special Instructions : "+orderFullDetailInner.getSpecialInstr().replaceAll("\n","");
                    if (fulltext.length()<=28) {
                        BILL = BILL + String.format("%-3s %-15s", empty, fulltext) + "\n";
                    } else {
                        String[] itemNameSplits=getSplitStrings(fulltext);
                        for (int i=0;i<itemNameSplits.length;i++){
                            BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[i]) + "\n";
                        }
                    }
                }
            }

            BILL = BILL
                    + "\n--------------------------------";
            BILL = BILL ;

            BILL = BILL + "                                             Total :" + "      " + sTotal + "\n";


            BILL = BILL + "\n\n\n ";

            byte[] printByte=combineBytes(bb3,BILL.getBytes());
            outputStream.write(printByte);
            Toast.makeText(this, "Printing Text...", Toast.LENGTH_SHORT).show();

        }catch (Exception ex){
            ex.printStackTrace();
            try {
                openBluetoothPrinter();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this,"Printer went to sleep, press again to print!",Toast.LENGTH_SHORT).show();
        }
    }

    void openBluetoothPrinter() throws IOException {
        try{

            //Standard uuid from string //
            UUID uuidSting = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
            bluetoothSocket=selectedBluetoothDevice.createRfcommSocketToServiceRecord(uuidSting);
            bluetoothSocket.connect();
            outputStream=bluetoothSocket.getOutputStream();
            inputStream=bluetoothSocket.getInputStream();

            beginListenData();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    void beginListenData(){
        try{

            final Handler handler =new Handler();
            final byte delimiter=10;
            stopWorker =false;
            readBufferPosition=0;
            readBuffer = new byte[1024];

            thread=new Thread(new Runnable() {
                @Override
                public void run() {

                    while (!Thread.currentThread().isInterrupted() && !stopWorker){
                        try{
                            int byteAvailable = inputStream.available();
                            if(byteAvailable>0){
                                byte[] packetByte = new byte[byteAvailable];
                                inputStream.read(packetByte);

                                for(int i=0; i<byteAvailable; i++){
                                    byte b = packetByte[i];
                                    if(b==delimiter){
                                        final byte[] encodedByte = new byte[readBufferPosition];
                                        System.arraycopy(
                                                readBuffer,0,
                                                encodedByte,0,
                                                encodedByte.length
                                        );
                                        final String data = new String(encodedByte,"US-ASCII");
                                        readBufferPosition=0;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
//                                                lblPrinterName.setText(data);
                                            }
                                        });
                                    }else{
                                        readBuffer[readBufferPosition++]=b;
                                    }
                                }
                            }
                        }catch(Exception ex){
                            stopWorker=true;
                        }
                    }

                }
            });

            thread.start();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private String[] getSplitStrings(String stringToSplit) {
        int itemNameLength=stringToSplit.length();
        Float noOfStrings=itemNameLength/28f;
        int noOfArrays= (int) Math.ceil(noOfStrings);
        String[] array = new String[noOfArrays];

        String[] strngs=new String[noOfArrays];
        int j=0;
        for (int i = 0; i < noOfArrays; i++) {
            j+=28;
            if (j<stringToSplit.length())
                strngs[i]=stringToSplit.substring(i*28,j);
            else
                strngs[i]=stringToSplit.substring(i*28,stringToSplit.length());
        }
        return strngs;
    }

    private byte[] combineBytes(byte[] a,byte[] b) {
        byte[] c = new byte[a.length + b.length];
        System.arraycopy(a, 0, c, 0, a.length);
        System.arraycopy(b, 0, c, a.length, b.length);
        return c;
    }

    public void showAcceptDialog(boolean isAccept){
        String ready_time="";
        Date createdDate = null;
        Calendar cal = Calendar.getInstance();
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this,R.style.DialogSlideAnim);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.zing_accept_order_dialog, null);
        dialogBuilder.setView(dialogView);

        int preptimeInit=10;

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialogTitleTV);
        TextView confirmTitle = (TextView) dialogView.findViewById(R.id.confirmTitle);
        TextView readyTimeTV = (TextView) dialogView.findViewById(R.id.readyTimeTV);
        Spinner hrsSpinner = (Spinner) dialogView.findViewById(R.id.hrsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, R.layout.zing_custom_spinner);
        adapter.setDropDownViewResource(R.layout.zing_custom_spinner);
        hrsSpinner.setAdapter(adapter);

//        if (!isAccept) {
//            if (selectedOrderFullDetails.getOrderType().equalsIgnoreCase("Delivery"))
//                dialogTitle.setText("Revised Delivery Time");
//            else
//                dialogTitle.setText("Revised Pick Up Time");
//        } else {
//            if (selectedOrderFullDetails.getOrderType().equalsIgnoreCase("Delivery"))
//                dialogTitle.setText("Estimated Delivery Time");
//            else
//                dialogTitle.setText("Estimated Pick Up Time");
//        }

        if (selectedOrderFullDetails.getOrderType().equalsIgnoreCase("Delivery"))
            confirmTitle.setText("Confirm Delivery Time");
        else
            confirmTitle.setText("Confirm Pick Up Time");

        Spinner minutesSpinner = (Spinner) dialogView.findViewById(R.id.minutesSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.minutes_array, R.layout.zing_custom_spinner);
        adapter1.setDropDownViewResource(R.layout.zing_custom_spinner);
        minutesSpinner.setAdapter(adapter1);


        String[] minArray=getResources().getStringArray(R.array.minutes_array);
        for(int i=0;i<minArray.length;i++){
            if (minArray[i].equalsIgnoreCase(OrderResponse.getPreparation_time()))
                minutesSpinner.setSelection(i);
        }

        Button acceptBtn=dialogView.findViewById(R.id.acceptBtn);

        if (selectedOrderFullDetails!=null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            try {
                Date date = inputFormat.parse(selectedOrderFullDetails.getCreatedAt());
                dialogTitle.setText("Order Received at : "+dateFormat.format(date));
                readyTimeTV.setText(dateFormat.format(date));
                ready_time=dateFormat.format(date);
                cal.setTime(date);
                createdDate=date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        acceptBtn.setOnClickListener(view -> {
            if (isAccept) {
                if (!TextUtils.isEmpty(orderIDvalue)) {
                    orderDetailsActivityVM.acceptOrder(orderIDvalue, minutesSpinner.getSelectedItem().toString());
                    alertDialog.dismiss();
                } else Toast.makeText(this, "Oops, try again later!", Toast.LENGTH_SHORT).show();
            } else {
                if (!TextUtils.isEmpty(orderIDvalue)) {
                    orderDetailsActivityVM.setDelayTime(orderIDvalue, minutesSpinner.getSelectedItem().toString());
                    alertDialog.dismiss();
                } else Toast.makeText(this, "Oops, try again later!", Toast.LENGTH_SHORT).show();
            }
        });

        Date finalCreatedDate = createdDate;
        minutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                readyTimeTV.setText(finalReady_time +" + "+adapter1.getItem(position)+" minutes");
                if (finalCreatedDate !=null)
                    cal.setTime(finalCreatedDate);
                cal.add(Calendar.MINUTE, Integer.parseInt(adapter1.getItem(position).toString()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                readyTimeTV.setText(dateFormat.format(cal.getTime()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        if (alertDialog.getWindow()!=null) {
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public void showDelayDialog(){
        String ready_time="";
        Date createdDate = null;
        Calendar cal = Calendar.getInstance();
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this,R.style.DialogSlideAnim);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.zing_order_delay_dialog, null);
        dialogBuilder.setView(dialogView);

        int preptimeInit=10;

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialogTitleTV);
        TextView confirmTitle = (TextView) dialogView.findViewById(R.id.confirmTitle);
        TextView readyTimeTV = (TextView) dialogView.findViewById(R.id.readyTimeTV);
        Spinner hrsSpinner = (Spinner) dialogView.findViewById(R.id.hrsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, R.layout.zing_custom_spinner);
        adapter.setDropDownViewResource(R.layout.zing_custom_spinner);
        hrsSpinner.setAdapter(adapter);



        Spinner minutesSpinner = (Spinner) dialogView.findViewById(R.id.minutesSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.minutes_array, R.layout.zing_custom_spinner);
        adapter1.setDropDownViewResource(R.layout.zing_custom_spinner);
        minutesSpinner.setAdapter(adapter1);


        String[] minArray=getResources().getStringArray(R.array.minutes_array);
        for(int i=0;i<minArray.length;i++){
            if (minArray[i].equalsIgnoreCase(OrderResponse.getPreparation_time()))
                minutesSpinner.setSelection(i);
        }

        Button acceptBtn=dialogView.findViewById(R.id.acceptBtn);

        if (selectedOrderFullDetails!=null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            try {
                Date date = inputFormat.parse(selectedOrderFullDetails.getReadyTime());
                if (selectedOrderFullDetails.getOrderType().equalsIgnoreCase("Delivery")) {
                    confirmTitle.setText("New Delivery Time");
                    dialogTitle.setText("Current Delivery Time : "+dateFormat.format(date));
                }
                else {
                    confirmTitle.setText("New Pick Up Time");
                    dialogTitle.setText("Current Pick Up Time : "+dateFormat.format(date));
                }
                readyTimeTV.setText(dateFormat.format(date));
                ready_time=dateFormat.format(date);
                cal.setTime(date);
                createdDate=date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        acceptBtn.setOnClickListener(view -> {
                if (!TextUtils.isEmpty(orderIDvalue)) {
                    orderDetailsActivityVM.setDelayTime(orderIDvalue, minutesSpinner.getSelectedItem().toString());
                    alertDialog.dismiss();
                }
        });

        Date finalCreatedDate = createdDate;
        minutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                readyTimeTV.setText(finalReady_time +" + "+adapter1.getItem(position)+" minutes");
                if (finalCreatedDate !=null)
                    cal.setTime(finalCreatedDate);
                cal.add(Calendar.MINUTE, Integer.parseInt(adapter1.getItem(position).toString()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                readyTimeTV.setText(dateFormat.format(cal.getTime()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        if (alertDialog.getWindow()!=null) {
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    public void showScheduledDialog(){
        String ready_time="";
        Date createdDate = null;
        Calendar cal = Calendar.getInstance();
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this,R.style.DialogSlideAnim);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.zing_order_delay_dialog, null);
        dialogBuilder.setView(dialogView);

        int preptimeInit=10;

        TextView dialogTitle = (TextView) dialogView.findViewById(R.id.dialogTitleTV);
        TextView confirmTitle = (TextView) dialogView.findViewById(R.id.confirmTitle);
        TextView readyTimeTV = (TextView) dialogView.findViewById(R.id.readyTimeTV);
        Spinner hrsSpinner = (Spinner) dialogView.findViewById(R.id.hrsSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.hours_array, R.layout.zing_custom_spinner);
        adapter.setDropDownViewResource(R.layout.zing_custom_spinner);
        hrsSpinner.setAdapter(adapter);



        Spinner minutesSpinner = (Spinner) dialogView.findViewById(R.id.minutesSpinner);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.minutes_array, R.layout.zing_custom_spinner);
        adapter1.setDropDownViewResource(R.layout.zing_custom_spinner);
        minutesSpinner.setAdapter(adapter1);


        String[] minArray=getResources().getStringArray(R.array.minutes_array);
        for(int i=0;i<minArray.length;i++){
            if (minArray[i].equalsIgnoreCase(OrderResponse.getPreparation_time()))
                minutesSpinner.setSelection(i);
        }

        Button acceptBtn=dialogView.findViewById(R.id.acceptBtn);

        if (selectedOrderFullDetails!=null) {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            try {
                Date date = inputFormat.parse(selectedOrderFullDetails.getReadyTime());
                if (selectedOrderFullDetails.getOrderType().equalsIgnoreCase("Delivery")) {
                    confirmTitle.setText("Customer scheduled time");
                    dialogTitle.setText("Customer scheduled time : "+dateFormat.format(date));
                }
                else {
                    confirmTitle.setText("Customer scheduled time");
                    dialogTitle.setText("Customer scheduled time : "+dateFormat.format(date));
                }
                readyTimeTV.setText(dateFormat.format(date));
                ready_time=dateFormat.format(date);
                cal.setTime(date);
                createdDate=date;
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        acceptBtn.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(orderIDvalue)) {
                orderDetailsActivityVM.acceptOrder(orderIDvalue, minutesSpinner.getSelectedItem().toString());
                alertDialog.dismiss();
            }
        });

        Date finalCreatedDate = createdDate;
        minutesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                readyTimeTV.setText(finalReady_time +" + "+adapter1.getItem(position)+" minutes");
                if (finalCreatedDate !=null)
                    cal.setTime(finalCreatedDate);
                cal.add(Calendar.MINUTE, Integer.parseInt(adapter1.getItem(position).toString()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
                readyTimeTV.setText(dateFormat.format(cal.getTime()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        if (alertDialog.getWindow()!=null) {
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
        }
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private String getPrintStringData() {
        String empty="";
        String BILL = deliveryType.toUpperCase()+" ORDER "+orderNo+"\n"
                +"zingmyorder.com"+"\n"
                +storeName+"\n" +
                customerName+"\n" +
                cPhone+"\n";
        if (deliveryType.equalsIgnoreCase("delivery")) {
            BILL = BILL + storeAddress+"\n";
        }
        BILL = BILL
                + "--------------------------------\n";


        BILL = BILL + String.format("%-3s %-15s", "Qty     ", "Item");
        BILL = BILL + "\n";
        BILL = BILL
                + "--------------------------------";
        for (OrderFullDetailInner orderFullDetailInner:orderFullDetailInners) {
            int addonsSize=orderFullDetailInner.getAddons().size();
            int itemNameLength=orderFullDetailInner.getMenu().getName().length();
            String addonsData="";
            if (itemNameLength<=28) {
                BILL = BILL + "\n" + String.format("%-3s %-15s", orderFullDetailInner.getQuantity()+"         ", (orderFullDetailInner.getMenu().getName())) + "\n";
            } else {
                String[] itemNameSplits=getSplitStrings(orderFullDetailInner.getMenu().getName());
                for (int i=0;i<itemNameSplits.length;i++){
                    if (i == 0)
                        BILL = BILL + "\n" + String.format("%-3s %-15s", orderFullDetailInner.getQuantity()+"         ", itemNameSplits[i]) + "\n";
                    else
                        BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[i]) + "\n";
                }
                Toast.makeText(this, "Length", Toast.LENGTH_SHORT).show();
            }
            if (!TextUtils.isEmpty(orderFullDetailInner.getVariation())) {
                String fulltext="         Variation : "+orderFullDetailInner.getVariation();
                if (fulltext.length()<=28) {
                    BILL = BILL + String.format("%-3s %-15s", empty, fulltext) + "\n";
                } else {
                    String[] itemNameSplits=getSplitStrings(fulltext);
                    for (int i=0;i<itemNameSplits.length;i++){
                        BILL = BILL + String.format("%-3s %-15s", empty,itemNameSplits[i]) + "\n";
                    }
                }
            }

            if (addonsSize > 1) {
                for (int i=0; i<addonsSize-1;i++) {
                    List<String> addons = orderFullDetailInner.getAddons().get(i);
                    String addonPlus=addons.get(1);
                    if (addonPlus.length()<=28) {
                        BILL += String.format("%-3s %-15s %n", empty, addonPlus);
                    } else {
                        String[] itemNameSplits=getSplitStrings(addonPlus);
                        for (int j=0;j<itemNameSplits.length;j++){
                            BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[j]) + "\n";
                        }
                    }
                }
                String addpnPlus1=orderFullDetailInner.getAddons().get(addonsSize-1).get(1);
                if (addpnPlus1.length()<=28) {
                    BILL += String.format("%-3s %-15s", empty, addpnPlus1);
                } else {
                    String[] itemNameSplits=getSplitStrings(addpnPlus1);
                    for (int j=0;j<itemNameSplits.length;j++){
                        BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[j]) + "\n";
                    }
                }
            } else  if (orderFullDetailInner.getAddons().size() == 1) {
                String addonPlus2=orderFullDetailInner.getAddons().get(0).get(1);
                if (addonPlus2.length()<=28) {
                    BILL += String.format("%-3s %-15s", empty, addonPlus2);
                } else {
                    String[] itemNameSplits=getSplitStrings(addonPlus2);
                    for (int j=0;j<itemNameSplits.length;j++){
                        BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[j]) + "\n";
                    }
                }
            }

            if (!TextUtils.isEmpty(orderFullDetailInner.getSpecialInstr())) {
                String fulltext="Special Instructions : "+orderFullDetailInner.getSpecialInstr().replaceAll("\n","");
                if (fulltext.length()<=28) {
                    BILL = BILL + String.format("%-3s %-15s", empty, fulltext) + "\n";
                } else {
                    String[] itemNameSplits=getSplitStrings(fulltext);
                    for (int i=0;i<itemNameSplits.length;i++){
                        BILL = BILL + String.format("%-3s %-15s", empty, itemNameSplits[i]) + "\n";
                    }
                }
            }
        }

        BILL = BILL
                + "\n--------------------------------";
        BILL = BILL ;

//            BILL = BILL + String.format("%15s",totalmt) + "\n";
        BILL = BILL + "\nTotal :" + "     " + sTotal + "\n";

//            BILL = BILL
//                    + "--------------------------------------\n";
        BILL = BILL + "\n\n\n ";
        return BILL;
    }
}
