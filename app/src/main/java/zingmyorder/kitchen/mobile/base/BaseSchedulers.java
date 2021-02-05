package zingmyorder.kitchen.mobile.base;

import androidx.annotation.NonNull;

import io.reactivex.Scheduler;

public interface BaseSchedulers {
    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();
}
