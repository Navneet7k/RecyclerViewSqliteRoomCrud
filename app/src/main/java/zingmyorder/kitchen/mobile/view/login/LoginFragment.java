package zingmyorder.kitchen.mobile.view.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.CommonUtils;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.base.BaseFragment;
import zingmyorder.kitchen.mobile.custom.MyCustomEditText;
import zingmyorder.kitchen.mobile.databinding.FragmentLoginViewBinding;
import zingmyorder.kitchen.mobile.view.login.vm.LoginViewModel;
import zingmyorder.kitchen.mobile.view.orders.OrdersListActivity;

import javax.inject.Inject;

import okhttp3.Credentials;

public class LoginFragment extends BaseFragment<FragmentLoginViewBinding, LoginViewModel> {

    private FragmentLoginViewBinding loginViewBinding;
    private String android_id = "";

    @Inject
    LoginViewModel loginVM;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private ProgressDialog progressDialog;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_login_view;
    }

    @Override
    public LoginViewModel getViewModel() {
        loginVM = ViewModelProviders.of(this, mViewModelFactory).
                get(LoginViewModel.class);
        return loginVM;
    }

    public static LoginFragment newInstance() {
        LoginFragment loginFragment=new LoginFragment();
        return loginFragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        android_id=Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        loginViewBinding = getViewDataBinding();
        initEdtListeners(loginViewBinding.emailEdt);
        initEdtListeners(loginViewBinding.pwdEdt);

    }

    private void initEdtListeners(MyCustomEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isAllFieldsEntered()) {
                    loginViewBinding.loginBtn.setEnabled(true);
                } else {
                    loginViewBinding.loginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isAllFieldsEntered() {
        return !TextUtils.isEmpty(loginViewBinding.emailEdt.getText()) && !TextUtils.isEmpty(loginViewBinding.pwdEdt.getText());
    }

    @Override
    public void subScribeObserver() {
        super.subScribeObserver();

        loginVM.loadingStatus.observe(this,aBoolean -> {
            if (aBoolean)
                progressDialog = CommonUtils.showLoadingDialog(getActivity());
            else
                progressDialog.dismiss();
        });
        loginVM.loginClicked.observe(this,aVoid -> {
            if (isAllFieldsEntered()){
                String basicauth= Credentials.basic(loginViewBinding.emailEdt.getText().toString(), loginViewBinding.pwdEdt.getText().toString());
                loginVM.startLogin(basicauth);
            }
        });

        loginVM.deviceLoginClicked.observe(this,aVoid -> {
            if (!TextUtils.isEmpty(android_id))
                loginVM.startDeviceIdLogin(android_id);
        });

        loginVM.wifiSettingClicked.observe(this,aVoid -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));

        loginVM.loginToken.observe(this,s -> {
            Toast.makeText(getBaseActivity(),"Success"+s,Toast.LENGTH_SHORT).show();
            getBaseActivity().finish();
            Intent intent=new Intent(getBaseActivity(), OrdersListActivity.class);
            intent.putExtra("isFirstTime",true);
            startActivity(intent);
        });

        loginVM.messages.observe(this, s -> onError(s));
    }

    @Override
    public void unSubScribeObserver() {
        super.unSubScribeObserver();

        loginVM.loadingStatus.removeObservers(this);
        loginVM.loginClicked.removeObservers(this);
        loginVM.responseMutableLiveData.removeObservers(this);
        loginVM.messages.removeObservers(this);
    }

    private void onError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

}
