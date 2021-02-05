package zingmyorder.kitchen.mobile.di;

import androidx.annotation.NonNull;


import zingmyorder.kitchen.mobile.base.BaseSchedulers;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider implements BaseSchedulers {
    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.newThread();
    }

    @NonNull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}

