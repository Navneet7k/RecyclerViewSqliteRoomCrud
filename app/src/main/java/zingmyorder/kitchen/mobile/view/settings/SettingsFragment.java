package zingmyorder.kitchen.mobile.view.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.base.BaseFragment;
import zingmyorder.kitchen.mobile.databinding.FragmentSettingsBinding;
import zingmyorder.kitchen.mobile.view.SignupAndSigninActivity;
import zingmyorder.kitchen.mobile.view.settings.models.SettingItem;

import java.util.ArrayList;

import javax.inject.Inject;

public class SettingsFragment extends BaseFragment<FragmentSettingsBinding,SettingsFragmentVM> implements SettingsRVAdapter.SettingsRVInteraction {

    private FragmentSettingsBinding binding;

    @Inject
    SettingsFragmentVM settingsFragmentVM;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_settings;
    }

    @Override
    public SettingsFragmentVM getViewModel() {
        settingsFragmentVM = ViewModelProviders.of(this, mViewModelFactory).
                get(SettingsFragmentVM.class);
        return settingsFragmentVM;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding=getViewDataBinding();

        ArrayList<SettingItem> settingItems=new ArrayList<>();
        settingItems.add(new SettingItem("Eatery Schedule","Change Open Hours",R.drawable.ic_shop));
        settingItems.add(new SettingItem("Menu Status","Turn Off unavailable Items",R.drawable.ic_menu_status));
        settingItems.add(new SettingItem("Orders","See Past Orders",R.drawable.ic_orders));
        settingItems.add(new SettingItem("Turn ON / OFF","Stop Taking Orders",R.drawable.ic_settings));
        settingItems.add(new SettingItem("App Settings","Wifi, Ringtone & More",R.drawable.ic_wifi));
        settingItems.add(new SettingItem("Printer","Wifi or Bluetooth",R.drawable.ic_printer));
        settingItems.add(new SettingItem("Logout","",R.drawable.ic_logout));
        SettingsRVAdapter adapter =new SettingsRVAdapter(getBaseActivity(),settingItems,this::onLogoutClick);
        GridLayoutManager layoutManager=new GridLayoutManager(getBaseActivity(),2);


        binding.settingsRV.setLayoutManager(layoutManager);
        binding.settingsRV.setAdapter(adapter);
    }

    public static SettingsFragment getInstance() {
        SettingsFragment settingsFragment=new SettingsFragment();
        return settingsFragment;
    }

    @Override
    public void onLogoutClick() {
        settingsFragmentVM.performLogout();
        getBaseActivity().finish();
        startActivity(new Intent(getBaseActivity(), SignupAndSigninActivity.class));
    }
}
