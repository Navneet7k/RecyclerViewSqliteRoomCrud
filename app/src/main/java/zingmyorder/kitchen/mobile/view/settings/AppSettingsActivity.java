package zingmyorder.kitchen.mobile.view.settings;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.base.BaseActivity;
import zingmyorder.kitchen.mobile.databinding.ActivityAppSettingsBinding;
import zingmyorder.kitchen.mobile.view.settings.models.RingtoneModel;

import javax.inject.Inject;

public class AppSettingsActivity extends BaseActivity<ActivityAppSettingsBinding,AppSettingsActivityVM> {

    private ActivityAppSettingsBinding binding;
    private BottomSheetBehavior sheetBehavior;
    private String android_id ="";

    @Inject
    AppSettingsActivityVM settingsActivityVM;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_app_settings;
    }

    @Override
    public AppSettingsActivityVM getViewModel() {
        return settingsActivityVM;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=getViewDataBinding();
        android_id=Settings.Secure.getString(getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sheetBehavior = BottomSheetBehavior.from(binding.pickRingtoneLayout.bottomSheet);
        binding.pickRingtoneLayout.ringListRV.setLayoutManager(new LinearLayoutManager(this));

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
        populateRingList();
    }

    private void populateRingList() {
        ArrayList<RingtoneModel> ringtoneModels=new ArrayList<>();
        ringtoneModels.add(new RingtoneModel("Phone alert","1"));
        ringtoneModels.add(new RingtoneModel("Telephone Ring","2"));
        ringtoneModels.add(new RingtoneModel("Orchestra","3"));
        ringtoneModels.add(new RingtoneModel("Bell","4"));
        RingtonePickerAdapter adapter=new RingtonePickerAdapter(ringtoneModels, this, ringtoneModel -> {
            settingsActivityVM.setRingtone(ringtoneModel.getRingID());
            Toast.makeText(this, "Ringtone set as "+ringtoneModel.getRingName(), Toast.LENGTH_SHORT).show();
        },settingsActivityVM.getSelectedRingtone());
        binding.pickRingtoneLayout.ringListRV.setAdapter(adapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                Rect outRect = new Rect();
                binding.pickRingtoneLayout.bottomSheet.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    return true;
                }

            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void subScribeObserver() {
        super.subScribeObserver();

        settingsActivityVM.loadingStatus.observe(this,aBoolean -> {
            if (aBoolean)
                showLoading();
            else
                hideLoading();
        });
        settingsActivityVM.messages.observe(this,s -> Toast.makeText(this, s, Toast.LENGTH_SHORT).show());
        settingsActivityVM.closePage.observe(this,aVoid -> finish());
        settingsActivityVM.closeSheet.observe(this,aVoid -> {
            if (sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        settingsActivityVM.notifSync.observe(this,aVoid -> {
            showAlertDialogWithOptionWithAnim("Would you like to sync Notification Settings?","OK","Cancel",this,new OnDialogActionListener(){
                @Override
                public void onCancel() {

                }

                @Override
                public void onContinue() {
                    settingsActivityVM.fetchFCMTokenSDK(android_id);
                }
            });
        });
        settingsActivityVM.ringtonePick.observe(this,aVoid -> {
            if (sheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            } else {
                sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        settingsActivityVM.wifiSettingsClick.observe(this,aVoid -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));
    }
}
