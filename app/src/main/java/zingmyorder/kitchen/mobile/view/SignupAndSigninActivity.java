package zingmyorder.kitchen.mobile.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import zingmyorder.kitchen.mobile.BR;
import zingmyorder.kitchen.mobile.R;
import zingmyorder.kitchen.mobile.base.BaseActivity;
import zingmyorder.kitchen.mobile.databinding.SignupActivityBinding;
import zingmyorder.kitchen.mobile.view.login.LoginFragment;
import zingmyorder.kitchen.mobile.view.orders.OrdersListActivity;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;


public class SignupAndSigninActivity extends BaseActivity<SignupActivityBinding, SignupViewModel> implements HasSupportFragmentInjector {

    private SignupActivityBinding signupActivityBinding;
    public static FragmentManager fragmentManager;
    @Inject
    DispatchingAndroidInjector<Fragment> fragmentDispatchingAndroidInjector;

    @Inject
    SignupViewModel signupViewModel;

    @Override
    public int getBindingVariable() {
        return BR.viewModel;
    }

    @Override
    public int getLayoutId() {
        return R.layout.signup_activity;
    }

    @Override
    public SignupViewModel getViewModel() {
        return signupViewModel;
    }

    @Override
    public AndroidInjector<Fragment> supportFragmentInjector() {
        return fragmentDispatchingAndroidInjector;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupActivityBinding = getViewDataBinding();
        fragmentManager = getSupportFragmentManager();

//        if (signupViewModel.getSavedCookie()!=null && !signupViewModel.getSavedCookie().equalsIgnoreCase("")) {
//            String cookie=signupViewModel.getSavedCookie();
//            if (CommonUtils.isCookieExpired(cookie)) {
//                signupViewModel.clearSharedPref();
//                return;
//            } else {
//
//            }
//        }
//        if (signupViewModel.isLoggedIn()==1 && signupViewModel.getRoleId()==2) {
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        } else if (signupViewModel.isLoggedIn()==1 && signupViewModel.getRoleId()==1) {
//            startActivity(new Intent(this, AdminActivity.class));
//            finish();
//        }

        if (!TextUtils.isEmpty(signupViewModel.getAppToken())){
            finish();
            Intent intent=new Intent(this, OrdersListActivity.class);
            intent.putExtra("isFirstTime",false);
            startActivity(intent);
        }

        bindViewPagerAdapter(signupActivityBinding.pager, this);
        bindViewPagerTabs(signupActivityBinding.tabLayout, signupActivityBinding.pager);
    }

    public void bindViewPagerAdapter(final ViewPager view, final SignupAndSigninActivity activity) {
        final SignupNdLoginAdapter adapter = new SignupNdLoginAdapter(view.getContext(), fragmentManager);
        view.setAdapter(adapter);
    }


    public void bindViewPagerTabs(final TabLayout view, final ViewPager pagerView) {
        view.setupWithViewPager(pagerView, true);
    }

    public class SignupNdLoginAdapter extends FragmentPagerAdapter {

        private static final int LOGIN = 0;
        private static final int SIGNUP = 1;

        private final int[] TABS = new int[]{LOGIN};

        private Context mContext;

        public SignupNdLoginAdapter(final Context context, final FragmentManager fm) {
            super(fm);
            mContext = context.getApplicationContext();
        }

        @Override
        public Fragment getItem(int position) {
            switch (TABS[position]) {
                case LOGIN:
                    LoginFragment loginFragment = LoginFragment.newInstance();
                    return (loginFragment);
//                case SIGNUP:
//                    RegistrationFragment registrationFragment1 = RegistrationFragment.newInstance();
//                    return (registrationFragment1);
            }
            return null;
        }

        @Override
        public int getCount() {
            return TABS.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (TABS[position]) {
                case LOGIN:
                    return "Login";
//                case SIGNUP:
//                    return "Signup";
            }
            return null;
        }
    }
}
