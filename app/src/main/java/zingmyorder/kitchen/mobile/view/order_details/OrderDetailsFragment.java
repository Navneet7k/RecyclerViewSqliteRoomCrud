package zingmyorder.kitchen.mobile.view.order_details;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.base.BaseFragment;
import zingmyorder.kitchen.mobile.databinding.FragmentOrderDetailsBinding;
import zingmyorder.kitchen.mobile.view.order_details.vm.OrderDetailsFragmentVM;

import javax.inject.Inject;

public class OrderDetailsFragment extends BaseFragment<FragmentOrderDetailsBinding, OrderDetailsFragmentVM> {


    private FragmentOrderDetailsBinding binding;

    @Inject
    OrderDetailsFragmentVM detailsFragmentVM;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_details;
    }

    @Override
    public OrderDetailsFragmentVM getViewModel() {
        detailsFragmentVM = ViewModelProviders.of(this, mViewModelFactory).
                get(OrderDetailsFragmentVM.class);
        return detailsFragmentVM;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding=getViewDataBinding();
    }
}
