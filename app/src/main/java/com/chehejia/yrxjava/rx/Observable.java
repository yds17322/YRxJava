package com.chehejia.yrxjava.rx;

import android.util.Log;

/**
 * Created by deshui on 2019/03/05
 */
public abstract class Observable<T> implements ObservableSource<T> {
    private static final String TAG = "Observable";
    public static final int MAIN_THREAD = 0;
    public static final int NEW_THREAD = 1;

    // 创建
    public static <T> Observable<T> create(ObservableOnSubscribe<T> onSubscribe) {
//        Log.e("yds", "Observable --create");
        return new ObservableCreate<>(onSubscribe);
    }

    public Observable<T> switchUpThread(int threadType) {
        // this == ObservableCreate
        return new ObservableSwitchUpThread<>(this, threadType);
    }

    public Observable<T> switchDownThread(int threadType) {
        // this == ObservableSwitchUpThread
        return new ObservableSwitchDownThread<>(this, threadType);
    }

    // map
    public <R> Observable<R> map(Function<T, R> function) {
        // 原有泛型 T 返回泛型 R
        return (Observable<R>) new ObservableMap<T, R>(this, function);
    }

    // 执行
    @Override
    public void subscribe(Observer<? super T> observer) {
        Log.e(TAG, "--subscribe :: " + this);
        // 此时this == ObservableSwitchDownThread
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(Observer<? super T> observer);
}
