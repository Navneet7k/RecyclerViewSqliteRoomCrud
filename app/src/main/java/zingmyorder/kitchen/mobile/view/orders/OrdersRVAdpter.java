package zingmyorder.kitchen.mobile.view.orders;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.databinding.OrderListItemviewBinding;
import zingmyorder.kitchen.mobile.view.orders.model.Orders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrdersRVAdpter extends RecyclerView.Adapter<OrdersRVAdpter.ViewHolder> {

    private Context mContext;
    private ArrayList<Orders> orders;
    private OrderItemInterface orderItemInterface;

    public OrdersRVAdpter(Context mContext, ArrayList<Orders> orders,OrderItemInterface orderItemInterface) {
        this.mContext = mContext;
        this.orders = orders;
        this.orderItemInterface=orderItemInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_list_itemview,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orders.get(position),position);

    }

    public void updateOrderComplete(int position){
        orders.get(position).setIs_car_pickup(1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private OrderListItemviewBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }

        public void bind(Orders order,int position) {
            binding.setOrder(order);

            final int mail_sent= TextUtils.isEmpty(order.getMail_sent())?0:Integer.parseInt(order.getMail_sent());
            final boolean isAccepted;
            final String flag=order.getFlag();

            if (order.getOrderStatus().equalsIgnoreCase("New Orders") || order.getOrderStatus().equalsIgnoreCase("Scheduled")) {
                isAccepted=false;
            } else {
                isAccepted=true;
                if (order.getOrderType().equalsIgnoreCase("delivery")) {
                    //OUT FOR DELIVERY
                }
            }

            if (order.getIs_schedule()==1)
                binding.tvScheduled.setVisibility(View.VISIBLE);
            else
                binding.tvScheduled.setVisibility(View.GONE);

            if (flag==null||flag.equalsIgnoreCase("0")){
                binding.orderStatusTV.setVisibility(View.GONE);
                binding.orderItemview.setBackgroundColor(mContext.getResources().getColor(R.color.vrob_clr_white));
            }
            else {
                binding.orderStatusTV.setVisibility(View.VISIBLE);
                binding.orderItemview.setBackgroundColor(mContext.getResources().getColor(R.color.item_bg));
            }

            if (order.getOrderStatus().equalsIgnoreCase("Cancelled")){
                binding.orderStatusTV.setVisibility(View.VISIBLE);
                binding.orderStatusTV.setText("Order CANCELLED");
                binding.orderItemview.setBackgroundColor(mContext.getResources().getColor(R.color.item_bg));
            }


            if (order.getOrderType().equalsIgnoreCase("delivery")){
                binding.textView4.setTextColor(mContext.getResources().getColor(R.color.mood_green));
            }

            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            SimpleDateFormat dateFormat = new SimpleDateFormat("E, MMM dd, hh:mm a");
            try {
                if (!TextUtils.isEmpty(order.getReadyTime())) {
                    Date date = inputFormat.parse(order.getReadyTime());
                    binding.textView3.setText(dateFormat.format(date));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (order.getNon_contact()==1){
                binding.pickupTV.setText("\u2022  Non Contact");
            } else if (order.getCar_pickup()==1){
                binding.pickupTV.setText("\u2022 Car Pickup");
            } else {
                binding.pickupTV.setVisibility(View.GONE);
            }

            if (order.getCar_details()!=null) {
                binding.animationView.setVisibility(View.VISIBLE);
                binding.tvCustomerArrived.setVisibility(View.VISIBLE);
                if(order.getIs_car_pickup()==0){
                    binding.tvCustomerArrived.setBackground(mContext.getResources().getDrawable(R.drawable.red_solid_button_curved_edge));
                }
                else {
                    binding.tvCustomerArrived.setBackground(mContext.getResources().getDrawable(R.drawable.light_red_solid_button_curved_edge));
                    binding.animationView.setVisibility(View.GONE);
                }
            } else {
                binding.animationView.setVisibility(View.GONE);
                binding.tvCustomerArrived.setVisibility(View.GONE);
            }

            itemView.setOnClickListener(view -> {
                if (order.getOrderStatus().equalsIgnoreCase("Cancelled"))
                    Toast.makeText(mContext, "This Order has been cancelled!", Toast.LENGTH_SHORT).show();
                else
                    orderItemInterface.itemClicked(order, isAccepted);
            });

            binding.tvCustomerArrived.setOnClickListener(view -> orderItemInterface.onCustomerCardClicked(order,position));
        }
    }


    interface OrderItemInterface {
        void itemClicked(Orders order,boolean isAccepted);
        void onCustomerCardClicked(Orders order,int position);
    }
}
