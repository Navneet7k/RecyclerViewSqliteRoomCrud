package zingmyorder.kitchen.mobile.view.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import javax.inject.Inject;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.Utils;
import zingmyorder.kitchen.mobile.base.BaseActivity;
import zingmyorder.kitchen.mobile.databinding.ActivityDashboardSettingsBinding;
import zingmyorder.kitchen.mobile.view.login.model.KitchenLoginResponse;

public class DashboardSettingsActivity extends BaseActivity<ActivityDashboardSettingsBinding,DashboardSettingsVM> {

    @Inject
    DashboardSettingsVM settingsVM;

    private ActivityDashboardSettingsBinding binding;
    PopupWindow popupWindow;
    private String selected_ringtone="1";
    MediaPlayer mPlayer2;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_dashboard_settings;
    }

    @Override
    public DashboardSettingsVM getViewModel() {
        return settingsVM;
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
            KitchenLoginResponse loginResponse=settingsVM.getLoginResponse();
            String slug=intent.getStringExtra("slug");

            if (loginResponse!=null && !TextUtils.isEmpty(loginResponse.getRestaurantId()) && !TextUtils.isEmpty(slug))
                initWebView("https://restaurant.zingmyorder.com/restaurant/loginas-kitchen/"+loginResponse.getRestaurantId()+"/"+slug);
        }

    }

    private void initWebView(String url) {
        binding.dashbaordVW.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                binding.dashbaordVW.setVisibility(View.GONE);
                binding.loadingView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                binding.dashbaordVW.setVisibility(View.VISIBLE);
                binding.loadingView.setVisibility(View.GONE);
            }
        });

        binding.dashbaordVW.getSettings().setJavaScriptEnabled(true);
        binding.dashbaordVW.setHorizontalScrollBarEnabled(false);
        binding.dashbaordVW.loadUrl(url);
    }

    private void showNewOrderPopup(int newOrders) {
        selected_ringtone=settingsVM.getRingtone();
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
        View view=getLayoutInflater().inflate(R.layout.zing_new_order_popup,null,false);

        RelativeLayout popupLayout=view.findViewById(R.id.popupLayout);
        TextView newOrdersNo=view.findViewById(R.id.newOrdersNo);
        TextView clickDismiss=view.findViewById(R.id.clickDismiss);
        ImageButton closeDismiss=view.findViewById(R.id.closeDismiss);

        if (newOrders>1) {
            newOrdersNo.setText("You have "+String.valueOf(newOrders)+" new orders!");
        } else {
            newOrdersNo.setText("You have "+String.valueOf(newOrders)+" new order!");
        }

        popupLayout.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
            finish();
        });

        clickDismiss.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
            finish();
        });
        closeDismiss.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
            finish();
        });

        if (popupWindow==null)
            popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer2!=null && mPlayer2.isPlaying())
            mPlayer2.stop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mPlayer2!=null && mPlayer2.isPlaying())
            mPlayer2.stop();
    }

    @Override
    public void subScribeObserver() {
        super.subScribeObserver();

        settingsVM.closePage.observe(this,aVoid -> finish());
        settingsVM.reloadClick.observe(this,aVoid -> binding.dashbaordVW.reload());
    }
}
