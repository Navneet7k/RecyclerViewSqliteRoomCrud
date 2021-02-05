package zingmyorder.kitchen.mobile.view;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import zingmyorder.kitchen.mobile.databinding.FragmentSignupViewBinding;

import javax.inject.Inject;

public class RegistrationFragment extends BaseFragment<FragmentSignupViewBinding, RegistrationViewModel> {

    private FragmentSignupViewBinding signupViewBinding;

    @Inject
    RegistrationViewModel registrationVM;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    private ProgressDialog progressDialog;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_signup_view;
    }

    public static RegistrationFragment newInstance() {
        RegistrationFragment registrationFragment = new RegistrationFragment();
        return registrationFragment;
    }

    @Override
    public RegistrationViewModel getViewModel() {
        registrationVM = ViewModelProviders.of(this, mViewModelFactory).
                get(RegistrationViewModel.class);
        return registrationVM;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        signupViewBinding = getViewDataBinding();
        initEdtListeners(signupViewBinding.fullNameEdt);
        initEdtListeners(signupViewBinding.emailEdt);
        initEdtListeners(signupViewBinding.mobileEdt);
        initEdtListeners(signupViewBinding.pwdEdt);

    }

    private void fieldValidations() {
        if (TextUtils.isEmpty(signupViewBinding.fullNameEdt.getText()))
            signupViewBinding.fullNameEdt.setError("Field is mandatory");
        if (TextUtils.isEmpty(signupViewBinding.mobileEdt.getText()))
            signupViewBinding.mobileEdt.setError("Field is mandatory");
        if (TextUtils.isEmpty(signupViewBinding.emailEdt.getText()))
            signupViewBinding.emailEdt.setError("Field is mandatory");
        if (TextUtils.isEmpty(signupViewBinding.addressEdt.getText()))
            signupViewBinding.addressEdt.setError("Field is mandatory");
        if (TextUtils.isEmpty(signupViewBinding.pwdEdt.getText()))
            signupViewBinding.pwdEdt.setError("Field is mandatory");
    }

    private void initEdtListeners(MyCustomEditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isAllFieldsEntered()) {
                    signupViewBinding.loginBtn.setEnabled(true);
                } else {
                    signupViewBinding.loginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isAllFieldsEntered() {
        return !TextUtils.isEmpty(signupViewBinding.fullNameEdt.getText()) && !TextUtils.isEmpty(signupViewBinding.emailEdt.getText()) && !TextUtils.isEmpty(signupViewBinding.mobileEdt.getText()) && !TextUtils.isEmpty(signupViewBinding.pwdEdt.getText());
    }

    @Override
    public void subScribeObserver() {
        super.subScribeObserver();

        registrationVM.loadingStatus.observe(this,aBoolean -> {
            if (aBoolean)
                progressDialog = CommonUtils.showLoadingDialog(getActivity());
            else
                progressDialog.dismiss();
        });

        registrationVM.responseMutableLiveData.observe(this,regSuccessApiResponse -> {
//            if (regSuccessApiResponse!=null && regSuccessApiResponse.data!=null)
//                onRegistrationSuccess();
        });

        registrationVM.errors.observe(this, this::onError);
        registrationVM.regClick.observe(this,aVoid -> {
            if (isAllFieldsEntered()) {
//                RegRequest regRequest=new RegRequest(signupViewBinding.fullNameEdt.getText().toString(),signupViewBinding.emailEdt.getText().toString(),signupViewBinding.mobileEdt.getText().toString(),signupViewBinding.addressEdt.getText().toString(),signupViewBinding.pwdEdt.getText().toString(),signupViewBinding.pwdEdt.getText().toString());
//                registrationVM.startRegistration(regRequest);
            }
            fieldValidations();
        });
    }

    private void onError(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    private void onRegistrationSuccess() {
        Toast.makeText(getActivity(), "Registration Successfull!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void unSubScribeObserver() {
        super.unSubScribeObserver();

        registrationVM.loadingStatus.removeObservers(this);
        registrationVM.responseMutableLiveData.removeObservers(this);
        registrationVM.errors.removeObservers(this);
        registrationVM.regClick.removeObservers(this);
    }
}
