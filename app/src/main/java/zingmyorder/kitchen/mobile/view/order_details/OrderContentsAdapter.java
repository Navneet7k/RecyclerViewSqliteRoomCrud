package zingmyorder.kitchen.mobile.view.order_details;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.databinding.OrderContentItemBinding;
import zingmyorder.kitchen.mobile.view.orders.model.OrderFullDetailInner;

import java.util.ArrayList;
import java.util.List;

public class OrderContentsAdapter extends RecyclerView.Adapter<OrderContentsAdapter.ViewHolder> {

    private ArrayList<OrderFullDetailInner> orderContentSamples;
    private Context mContext;
    private boolean isOrderComplete;

    public OrderContentsAdapter(ArrayList<OrderFullDetailInner> orderContentSamples, Context mContext,boolean isOrderComplete) {
        this.orderContentSamples = orderContentSamples;
        this.mContext = mContext;
        this.isOrderComplete = isOrderComplete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_content_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(orderContentSamples.get(position),position);
    }

    public void setOrderComplete() {
        isOrderComplete=true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return orderContentSamples.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private OrderContentItemBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }

        public void bind(OrderFullDetailInner orderFullDetailInner,int pos) {
            binding.setOrderItem(orderFullDetailInner);
            binding.textView7.setText(String.valueOf(pos+1));
            if (orderFullDetailInner.getMenu()!=null) {
                String menuItem="";
                int qtyCount=Integer.parseInt(orderFullDetailInner.getQuantity());
                if (qtyCount>1)
                    menuItem=orderFullDetailInner.getMenu().getName() + " X <big><font color='red'>" + orderFullDetailInner.getQuantity() + "</font> QTY </big>";
                else
                    menuItem=orderFullDetailInner.getMenu().getName() + " X <big>" + orderFullDetailInner.getQuantity() + " QTY </big>";
                binding.itemNameTV.setText(Html.fromHtml(menuItem));
            }
            if (!TextUtils.isEmpty(orderFullDetailInner.getSpecialInstr()))
                binding.instructionsTV.setText("Special Instruction : " + orderFullDetailInner.getSpecialInstr());
            else
                binding.instructionsTV.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(orderFullDetailInner.getVariation()))
                binding.variationTV.setText("Variation : " + orderFullDetailInner.getVariation());
            else
                binding.variationTV.setVisibility(View.GONE);
            binding.amtTV.setText("$"+orderFullDetailInner.getPrice());

            int addonsSize=orderFullDetailInner.getAddons().size();
            if (addonsSize > 1) {
                for (int i=0; i<addonsSize-1;i++) {
                    List<String> addons = orderFullDetailInner.getAddons().get(i);
                    binding.addonsList.append("• " + addons.get(1) + "  -  $"+addons.get(2)+"\n");
                }
                binding.addonsList.append("• " + orderFullDetailInner.getAddons().get(addonsSize-1).get(1) + "  -  $"+orderFullDetailInner.getAddons().get(addonsSize-1).get(2));
            } else  if (orderFullDetailInner.getAddons().size() == 1) {
                binding.addonsList.setText("• " + orderFullDetailInner.getAddons().get(0).get(1) + "  -  $"+orderFullDetailInner.getAddons().get(0).get(2));
            } else {
                binding.addonsList.setVisibility(View.GONE);
            }

            if (isOrderComplete) {
                binding.itemNameTV.setTextColor(mContext.getResources().getColor(R.color.lightest_grey));
                binding.instructionsTV.setTextColor(mContext.getResources().getColor(R.color.lightest_grey));
                binding.variationTV.setTextColor(mContext.getResources().getColor(R.color.lightest_grey));
                binding.addonsList.setTextColor(mContext.getResources().getColor(R.color.lightest_grey));
                binding.amtTV.setTextColor(mContext.getResources().getColor(R.color.lightest_grey));
            }
        }
    }
}
