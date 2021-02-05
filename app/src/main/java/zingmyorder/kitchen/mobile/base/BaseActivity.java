package zingmyorder.kitchen.mobile.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import zingmyorder.kitchen.mobile.CommonUtils;

import dagger.android.AndroidInjection;
import zingmyorder.kitchen.mobile.R;

public abstract class BaseActivity<T extends ViewDataBinding,V extends BaseViewModel> extends AppCompatActivity implements BaseFragment.Callback {

    private T mViewDataBinding;
    private V mViewModel;
    private ProgressDialog mProgressDialog;
    private AlertDialog alertDialog;

    public abstract int getBindingVariable();

    public abstract
    @LayoutRes
    int getLayoutId();

    public abstract V getViewModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        performDependencyInjection();
        super.onCreate(savedInstanceState);
        performDataBinding();
        subScribeObserver();
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    private void performDataBinding() {
        mViewDataBinding = DataBindingUtil.setContentView(this, getLayoutId());
        this.mViewModel = mViewModel == null ? getViewModel() : mViewModel;
        mViewDataBinding.setVariable(getBindingVariable(), mViewModel);
        mViewDataBinding.setLifecycleOwner(this);
        mViewDataBinding.executePendingBindings();
    }

    public T getViewDataBinding() {
        return mViewDataBinding;
    }

    public void performDependencyInjection() {
        AndroidInjection.inject(this);
    }

    public void showLoading() {
        hideLoading();
        mProgressDialog = CommonUtils.showLoadingDialog(this);
    }

    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }

    public void subScribeObserver() {

    }

    public void unSubScribeObserver() {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unSubScribeObserver();
    }

    public void hideKeyboard() {

    }

    public void showAlertDialogWithOptionWithAnim(String msg, String ok, String cancel, Context mContext, OnDialogActionListener actionListener) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog_with_btns_anim_view, null);
        dialogBuilder.setView(dialogView);
        TextView txtView = dialogView.findViewById(R.id.msgTV);
        txtView.setText(msg);
        Button btnOk = dialogView.findViewById(R.id.btnOK);
        btnOk.setText(ok);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);
        btnCancel.setText(cancel);
        btnOk.setOnClickListener(v -> {
            actionListener.onContinue();
            this.alertDialog.dismiss();
        });
        btnCancel.setOnClickListener(v -> {
            actionListener.onCancel();
            this.alertDialog.dismiss();
        });
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(false);
    }

    public interface OnDialogActionListener {

        void onContinue();

        void onCancel();
    }

    public boolean isNetworkConnected() {
        return false;
    }
}
