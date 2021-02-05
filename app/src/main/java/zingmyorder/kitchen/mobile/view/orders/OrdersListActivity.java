package zingmyorder.kitchen.mobile.view.orders;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.Utils;
import zingmyorder.kitchen.mobile.base.BaseActivity;
import zingmyorder.kitchen.mobile.custom.TextDrawable;
import zingmyorder.kitchen.mobile.databinding.ActivityOrdersListBinding;
import zingmyorder.kitchen.mobile.view.orders.model.OrdersWrapper;
import zingmyorder.kitchen.mobile.view.orders.vm.OrdersListActivityVM;
import zingmyorder.kitchen.mobile.view.settings.SettingsFragment;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class OrdersListActivity extends BaseActivity<ActivityOrdersListBinding, OrdersListActivityVM> implements HasSupportFragmentInjector {

    private ActivityOrdersListBinding binding;
    public static FragmentManager fragmentManager;
    private OrdersWrapper ordersWrapper;
    PopupWindow popupWindow;
    private String selected_ringtone="1";
    MediaPlayer mPlayer2;
    private BottomSheetBehavior sheetBehavior;
    private String tb_type="";
    private String android_id = "";

    private BroadcastReceiver updates_receiver = new BroadcastReceiver () {
        @Override
        public void onReceive (Context context, Intent i) {
            int countOrder=0;
            String filter = Utils. INFO_UPDATE_FILTER ;
            String orders_count=i.getStringExtra("orders_count");
            String refresh_tag=i.getStringExtra("refresh_tag");
            if (!TextUtils.isEmpty(orders_count))
                countOrder=Integer.parseInt(orders_count);
            if (i.getAction (). equals (filter)) {
                    if (getWindow()!=null && getWindow().getDecorView().getRootView().isShown()) {
                        if (countOrder>0) {
                            showNewOrderPopup(countOrder);
                        }
                        if (!TextUtils.isEmpty(refresh_tag))
                            showCustomerArrivedPopup();

                        ordersListActivityVM.fetchNoLoaderOrdersList(); // update UI or data
                    }
            }
        }
    };

    @Inject
    OrdersListActivityVM ordersListActivityVM;

    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_orders_list;
    }

    @Override
    public OrdersListActivityVM getViewModel() {
        return ordersListActivityVM;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentManager=getSupportFragmentManager();
        binding=getViewDataBinding();

        android_id=Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sheetBehavior = BottomSheetBehavior.from(binding.customerCareBottomsheet.customerCareBottomSheet);
        LocalBroadcastManager. getInstance ( this ). registerReceiver ( updates_receiver , new IntentFilter(Utils. INFO_UPDATE_FILTER ));
        Intent intent=getIntent();
        if (intent!=null) {
            int notificationId = intent.getIntExtra("zingmyorder.kitchen.mobile.notifyId", 0);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager!=null)
                manager.cancel(notificationId);
            boolean isFirstTime=intent.getBooleanExtra("isFirstTime",false);
            if (isFirstTime)
                ordersListActivityVM.fetchFCMTokenSDK(android_id);
        }

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    binding.bg.setVisibility(View.GONE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                binding.bg.setVisibility(View.VISIBLE);
                binding.bg.setAlpha(slideOffset);
            }
        });

//        initPagerData();
        ordersListActivityVM.fetchOrdersList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ordersListActivityVM.fetchOrdersList();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                binding.customerCareBottomsheet.customerCareBottomSheet.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true;
                }

            }
        }
        return super.dispatchTouchEvent(event);
    }

    private void initPagerData(int n,int f,int a) {
        binding.pager.setOffscreenPageLimit(1);
        bindViewPagerAdapter(binding.pager, this);
        bindViewPagerTabs(binding.tabLayout, binding.pager);

        TextDrawable new_drawable=new TextDrawable(this,String.valueOf(n));
        TextDrawable future_drawable=new TextDrawable(this,String.valueOf(f));
        TextDrawable accepted_drawable=new TextDrawable(this,String.valueOf(a));

        if (binding.tabLayout.getTabAt(0)!=null && n>0)
            binding.tabLayout.getTabAt(0).setIcon(new_drawable);

        if (binding.tabLayout.getTabAt(1)!=null && f>0)
            binding.tabLayout.getTabAt(1).setIcon(future_drawable);

        if (binding.tabLayout.getTabAt(2)!=null && a>0)
            binding.tabLayout.getTabAt(2).setIcon(accepted_drawable);

        if (binding.tabLayout.getTabAt(3)!=null)
            binding.tabLayout.getTabAt(3).setIcon(R.drawable.menu);
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    public void bindViewPagerAdapter(final ViewPager view, final OrdersListActivity activity) {
        final OrdersListPagerAdapter adapter = new OrdersListPagerAdapter(view.getContext(), fragmentManager);
        view.setAdapter(adapter);
    }


    public void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView) {
        view.setupWithViewPager(pagerView, true);
    }

    public class OrdersListPagerAdapter extends FragmentStatePagerAdapter {

        private static final int NEW = 0;
        private static final int FUTURE = 1;
        private static final int ACCEPTED = 2;
        private static final int SETTINGS = 3;
        private Fragment mFragmentAtPos0;

        private final int[] TABS = new int[]{NEW, FUTURE,ACCEPTED,SETTINGS};

        private Context mContext;

        public OrdersListPagerAdapter(final Context context, final FragmentManager fm) {
            super(fm);
            mContext = context.getApplicationContext();
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle=new Bundle();
            if (ordersWrapper!=null)
                bundle.putParcelable("order_response",ordersWrapper);
            switch (TABS[position]) {
                case NEW:
                    bundle.putString("type","new");
                    OrdersListFragment ordersListFragment = getOrdersListFragment();
                    ordersListFragment.setArguments(bundle);
                    return (ordersListFragment);
                case FUTURE:
                    bundle.putString("type","future");
                    OrdersListFragment ordersListFragment1 = getOrdersListFragment();
                    ordersListFragment1.setArguments(bundle);
                    return (ordersListFragment1);
                case ACCEPTED:
                    bundle.putString("type","accepted");
                    OrdersListFragment ordersListFragment2 = getOrdersListFragment();
                    ordersListFragment2.setArguments(bundle);
                    return (ordersListFragment2);
                case SETTINGS:
                    return (SettingsFragment.getInstance());
            }
            return null;
        }

        private OrdersListFragment getOrdersListFragment() {
            OrdersListFragment ordersListFragment = OrdersListFragment.getInstance();
            ordersListFragment.setOrdersListener(new OrdersListFragment.OrderListInterface() {
                @Override
                public void onListSelected() {

                }

                @Override
                public void onListRefresh(String tab_type) {
                    tb_type=tab_type;
                    ordersListActivityVM.fetchOrdersList();
                }
            });
            return ordersListFragment;
        }


        @Override
        public int getCount() {
            return TABS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (TABS[position]) {
                case NEW:
                    return "New";
                case FUTURE:
                    return "Future";
                case ACCEPTED:
                    return "Accepted";
                case SETTINGS:
                    return "Home";
            }
            return null;
        }
    }

    private void showNewOrderPopup(int newOrders) {
//        if (popupWindow!=null)
//            popupWindow.dismiss();
        selected_ringtone=ordersListActivityVM.getRingtone();
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
        mPlayer2.setVolume(0.9f,0.9f);
        mPlayer2.setLooping(true);
        mPlayer2.start();
        View view=getLayoutInflater().inflate(R.layout.zing_new_order_popup,null,false);

        RelativeLayout popupLayout=view.findViewById(R.id.popupLayout);
        LinearLayout newOrderLayout=view.findViewById(R.id.newOrderLayout);
        TextView newOrdersNo=view.findViewById(R.id.newOrdersNo);
        TextView clickDismiss=view.findViewById(R.id.clickDismiss);
        ImageView zingLogo=view.findViewById(R.id.zingLogoIM);
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
        });

        clickDismiss.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
        });
        closeDismiss.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
        });

        if (popupWindow==null)
            popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
    }

    private void showCustomerArrivedPopup() {
        if (popupWindow!=null)
            popupWindow.dismiss();
        selected_ringtone=ordersListActivityVM.getRingtone();
        if (mPlayer2==null)
        {
            if (selected_ringtone.equalsIgnoreCase("1"))
                mPlayer2 = MediaPlayer.create(this, R.raw.short_ring);
            else if (selected_ringtone.equalsIgnoreCase("2"))
                mPlayer2 = MediaPlayer.create(this, R.raw.ring);
            else if (selected_ringtone.equalsIgnoreCase("3"))
                mPlayer2 = MediaPlayer.create(this, R.raw.orderreceived);
            else if (selected_ringtone.equalsIgnoreCase("4"))
                mPlayer2 = MediaPlayer.create(this, R.raw.bell);
            else
                mPlayer2 = MediaPlayer.create(this, R.raw.short_ring);
        }
        mPlayer2.setVolume(0.9f,0.9f);
        mPlayer2.setLooping(true);
        mPlayer2.start();
        View view=getLayoutInflater().inflate(R.layout.zing_customer_arrived_popup,null,false);

        RelativeLayout popupLayout=view.findViewById(R.id.popupLayout);
        LinearLayout newOrderLayout=view.findViewById(R.id.newOrderLayout);
        TextView newOrdersNo=view.findViewById(R.id.newOrdersNo);
        TextView clickDismiss=view.findViewById(R.id.clickDismiss);
        ImageView zingLogo=view.findViewById(R.id.zingLogoIM);
        ImageButton closeDismiss=view.findViewById(R.id.closeDismiss);



        popupLayout.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
        });

        clickDismiss.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
        });
        closeDismiss.setOnClickListener(v -> {
            if (mPlayer2!=null && mPlayer2.isPlaying())
                mPlayer2.stop();
            popupWindow.dismiss();
            mPlayer2=null;
        });

//        if (popupWindow==null)
            popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPlayer2!=null && mPlayer2.isPlaying()) {
            mPlayer2.stop();
        }
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

        ordersListActivityVM.closePage.observe(this,aVoid -> finish());

        ordersListActivityVM.reloadClick.observe(this,aVoid -> ordersListActivityVM.fetchOrdersList());

        ordersListActivityVM.helpClick.observe(this,aVoid -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        ordersListActivityVM.wifiClick.observe(this,aVoid -> {
            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
        });

        ordersListActivityVM.closeSheet.observe(this,aVoid -> sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED));

        ordersListActivityVM.loadingStatus.observe(this,aBoolean -> {
            if (aBoolean)
                showLoading();
            else
                hideLoading();
        });

        ordersListActivityVM.ordersResponse.observe(this,ordersWrapperApiResponse -> {
            if (ordersWrapperApiResponse!=null && ordersWrapperApiResponse.data!=null){
                ordersWrapper=ordersWrapperApiResponse.data;
                int newSize=ordersWrapperApiResponse.data.getOrders().size();
                int futureSize=ordersWrapperApiResponse.data.getFuture_orders().size();
                int acceptedSize=ordersWrapperApiResponse.data.getAccepted_orders().size();
                initPagerData(newSize,futureSize,acceptedSize);
                if (!TextUtils.isEmpty(tb_type))
                    updateViewPagerPos();
            }
        });

        ordersListActivityVM.error.observe(this,s -> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());
        ordersListActivityVM.messages.observe(this, s -> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());
    }

    private void updateViewPagerPos() {
        if (tb_type.equalsIgnoreCase("accepted")){
            new Handler().post(() -> {
                binding.pager.setCurrentItem(2);
            });
        }
    }
}
