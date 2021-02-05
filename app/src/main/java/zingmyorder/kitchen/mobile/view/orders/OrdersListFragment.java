package zingmyorder.kitchen.mobile.view.orders;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.base.BaseFragment;
import zingmyorder.kitchen.mobile.databinding.FragmentOrdersListBinding;
import zingmyorder.kitchen.mobile.view.login.model.KitchenLoginResponse;
import zingmyorder.kitchen.mobile.view.order_details.OrderDetailsActivity;
import zingmyorder.kitchen.mobile.view.orders.model.Orders;
import zingmyorder.kitchen.mobile.view.orders.model.OrdersWrapper;
import zingmyorder.kitchen.mobile.view.orders.vm.OrdersListVM;
import zingmyorder.kitchen.mobile.view.settings.DashboardSettingsActivity;

import java.util.ArrayList;

import javax.inject.Inject;

public class OrdersListFragment extends BaseFragment<FragmentOrdersListBinding, OrdersListVM> {

    private FragmentOrdersListBinding binding;
    private OrderListInterface listener;
    private OrdersRVAdpter adpter;
    private OrdersWrapper ordersWrapper;
    private String tabType="";
    private AlertDialog alertDialog;

    @Inject
    OrdersListVM ordersListVM;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_orders_list;
    }

    @Override
    public OrdersListVM getViewModel() {
        ordersListVM = ViewModelProviders.of(this, mViewModelFactory).
                get(OrdersListVM.class);
        return ordersListVM;
    }



    public static OrdersListFragment getInstance() {
        OrdersListFragment ordersListFragment=new OrdersListFragment();
        return ordersListFragment;
    }

    public void setOrdersListener(OrderListInterface listener) {
        this.listener=listener;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=getViewDataBinding();

        if (getArguments()!=null){
            ordersWrapper=getArguments().getParcelable("order_response");
            tabType=getArguments().getString("type","");
            fillOrders(tabType,ordersWrapper);
        }
        LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(binding.ordersRV.getContext(),
                layoutManager.getOrientation());
        binding.ordersRV.addItemDecoration(dividerItemDecoration);
        binding.ordersRV.setLayoutManager(layoutManager);
        binding.ordersRV.setAdapter(adpter);
    }

    private void fillOrders(String tabType, OrdersWrapper ordersWrapper) {
        if (!TextUtils.isEmpty(tabType)) {
            if (tabType.equalsIgnoreCase("new")) {
                if (ordersWrapper != null && ordersWrapper.getOrders() != null && ordersWrapper.getOrders().size() > 0) {
                    adpter = new OrdersRVAdpter(getBaseActivity(), new ArrayList<>(ordersWrapper.getOrders()), new OrdersRVAdpter.OrderItemInterface() {
                        @Override
                        public void itemClicked(Orders order, boolean isAccepted) {
                            gotoOrderDetails(order,isAccepted);
                        }

                        @Override
                        public void onCustomerCardClicked(Orders order, int position) {

                        }
                    });
                } else {
                    binding.emptyTV.setVisibility(View.VISIBLE);
                    binding.emptyTV.setText("Only New Orders that are pending to be accepted will be seen here");
                    KitchenLoginResponse loginResponse=ordersListVM.getLoginResponse();
                    if (loginResponse!=null) {
                        binding.addressTV.setVisibility(View.VISIBLE);
                        binding.addressTV.setText(loginResponse.getName());
                    }
                }
            } else if (tabType.equalsIgnoreCase("future")) {
                if (ordersWrapper != null && ordersWrapper.getOrders() != null && ordersWrapper.getFuture_orders().size() > 0) {
                    adpter = new OrdersRVAdpter(getBaseActivity(), new ArrayList<>(ordersWrapper.getFuture_orders()), new OrdersRVAdpter.OrderItemInterface() {
                        @Override
                        public void itemClicked(Orders order,boolean isAccepted) {
                            gotoOrderDetails(order,isAccepted);
                        }

                        @Override
                        public void onCustomerCardClicked(Orders order, int position) {

                        }
                    });
                }else {
                    binding.emptyTV.setVisibility(View.VISIBLE);
                    binding.emptyTV.setText("Orders that are scheduled for a future time will be seen here");
                }
            } else if (tabType.equalsIgnoreCase("accepted")) {
                binding.allOrderBtnLL.setVisibility(View.VISIBLE);
                if (ordersWrapper != null && ordersWrapper.getOrders() != null && ordersWrapper.getAccepted_orders().size() > 0) {
                    adpter = new OrdersRVAdpter(getBaseActivity(), new ArrayList<>(ordersWrapper.getAccepted_orders()), new OrdersRVAdpter.OrderItemInterface() {
                        @Override
                        public void itemClicked(Orders order,boolean isAccepted) {
                            gotoOrderDetails(order,isAccepted);
                        }

                        @Override
                        public void onCustomerCardClicked(Orders order, int position) {
                            showCarDetails(order,position);
                        }
                    });
                }else {
                    binding.emptyTV.setVisibility(View.VISIBLE);
                    binding.emptyTV.setText("Only Accepted Orders will be seen here. ");
                }
            } else {
                binding.emptyTV.setVisibility(View.GONE);
                if (ordersWrapper != null && ordersWrapper.getOrders() != null && ordersWrapper.getOrders().size() > 0) {
                    adpter = new OrdersRVAdpter(getBaseActivity(), new ArrayList<>(ordersWrapper.getOrders()), new OrdersRVAdpter.OrderItemInterface() {
                        @Override
                        public void itemClicked(Orders order,boolean isAccepted) {
                            gotoOrderDetails(order,isAccepted);
                        }

                        @Override
                        public void onCustomerCardClicked(Orders order, int position) {

                        }
                    });
                }
            }
        }
    }

    public void showCarDetails(Orders order, int position) {
        androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getBaseActivity(),R.style.DialogSlideAnim);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.car_details_dialog_view, null);
        dialogBuilder.setView(dialogView);

        TextView headerTextTV=dialogView.findViewById(R.id.headerTextTV);

        TextView carMakeTV=dialogView.findViewById(R.id.carMakeTV);
        TextView carColourTV=dialogView.findViewById(R.id.carColourTV);
        TextView carInfoTV=dialogView.findViewById(R.id.carInfoTV);
        CardView completeBtn=dialogView.findViewById(R.id.completeBtn);
        ImageButton btnClose=dialogView.findViewById(R.id.btnClose);

        headerTextTV.setText("#"+String.valueOf(order.getId())+" Car and Parking Information");

        carMakeTV.setText(order.getCar_details().getNumber());
        carColourTV.setText(order.getCar_details().getColor());
        carInfoTV.setText(order.getCar_details().getInfo());

        completeBtn.setOnClickListener(v -> {
            alertDialog.dismiss();
            ordersListVM.updateCustomerArrivedComplete(String.valueOf(order.getId()),position);
        });

        btnClose.setOnClickListener(v -> alertDialog.dismiss());

        alertDialog = dialogBuilder.create();
        if (alertDialog.getWindow()!=null) {
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }
        if (alertDialog.getWindow()!=null) {
            WindowManager.LayoutParams wmlp = alertDialog.getWindow().getAttributes();
            wmlp.width = WindowManager.LayoutParams.MATCH_PARENT;
//            wmlp.gravity = Gravity.BOTTOM;
        }
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }

    private void gotoOrderDetails(Orders order,boolean isAccepted) {
        Intent intent=new Intent(getActivity(), OrderDetailsActivity.class);
        intent.putExtra("order_detail",order);
        intent.putExtra("isAccepted",isAccepted);
        intent.putExtra("tabType",tabType);
        startActivityForResult(intent,2333);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2333 && data!=null) {
            boolean isAccepted = data.getBooleanExtra("isAccepted",false);
            String tab_type = data.getStringExtra("tab_type");
            if (isAccepted)
            listener.onListRefresh(tab_type);
        }
    }

    @Override
    public void subScribeObserver() {
        super.subScribeObserver();

        ordersListVM.loadingStatus.observe(this,aBoolean -> {
            if (aBoolean)
                getBaseActivity().showLoading();
            else
                getBaseActivity().hideLoading();
        });

        ordersListVM.allOrders.observe(this,aVoid -> {
            Intent intent=new Intent(getActivity(), DashboardSettingsActivity.class);
            intent.putExtra("slug","orders");
            getActivity().startActivity(intent);
        });

        ordersListVM.error.observe(this,s -> Toast.makeText(getBaseActivity(), s, Toast.LENGTH_SHORT).show());

        ordersListVM.customerArrived.observe(this,orderStatusUpdateApiResponse -> {
            if (orderStatusUpdateApiResponse!=null) {
                Toast.makeText(getBaseActivity(),"Order Completed Successfully!",Toast.LENGTH_SHORT).show();
                adpter.updateOrderComplete(orderStatusUpdateApiResponse);
            }
        });
    }

    public interface OrderListInterface {
        void onListSelected();
        void onListRefresh(String tab_type);
    }
}
