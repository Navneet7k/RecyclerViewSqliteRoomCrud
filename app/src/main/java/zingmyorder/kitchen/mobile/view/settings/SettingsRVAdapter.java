package zingmyorder.kitchen.mobile.view.settings;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.databinding.SettingsItemViewBinding;
import zingmyorder.kitchen.mobile.view.settings.models.SettingItem;

import java.util.ArrayList;

public class SettingsRVAdapter extends RecyclerView.Adapter<SettingsRVAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<SettingItem> settings;
    private SettingsRVInteraction listener;

    public SettingsRVAdapter(Context mContext, ArrayList<SettingItem> settings,SettingsRVInteraction listener) {
        this.mContext = mContext;
        this.settings = settings;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.settings_item_view,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(settings.get(position),position);
    }

    @Override
    public int getItemCount() {
        return settings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private SettingsItemViewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding= DataBindingUtil.bind(itemView);
        }

        public void bind(SettingItem settingItem,int pos) {
            binding.setSetting(settingItem);
            binding.settingIconIV.setImageDrawable(mContext.getResources().getDrawable(settingItem.getSettingIcon()));
            binding.settingName.setText(settingItem.getSettingName());
            binding.settingDesc.setText(settingItem.getSettingDesc());

            itemView.setOnClickListener(view -> {
                if (pos==0) {
                    Intent intent=new Intent(mContext,DashboardSettingsActivity.class);
                    intent.putExtra("slug","eatery");
                    mContext.startActivity(intent);
                } else if (pos==1) {
                    Intent intent=new Intent(mContext,DashboardSettingsActivity.class);
                    intent.putExtra("slug","menu_status");
                    mContext.startActivity(intent);
                } else if (pos==2) {
                    Intent intent=new Intent(mContext,DashboardSettingsActivity.class);
                    intent.putExtra("slug","orders");
                    mContext.startActivity(intent);
                } else if (pos==3) {
                    Intent intent=new Intent(mContext,DashboardSettingsActivity.class);
                    intent.putExtra("slug","settings");
                    mContext.startActivity(intent);
                }
                else if (pos==4)
                    mContext.startActivity(new Intent(mContext,AppSettingsActivity.class));
                else if (pos==5)
                    mContext.startActivity(new Intent(mContext,PrinterSettingsActivity.class));
                else if (pos==6) {
                    listener.onLogoutClick();
                }
            });
        }
    }

    interface SettingsRVInteraction{
        void onLogoutClick();
    }
}
