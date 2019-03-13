package com.chehejia.yrxjava.rx;

import android.util.Log;

/**
 * Created by deshui on 2019/03/05
 */
public abstract class Observable<T> implements ObservableSource<T> {

    // 创建
    public static <T> Observable<T> create(ObservableOnSubscribe<T> onSubscribe) {
        Log.e("yds", "Observable --create");
        return new ObservableCreate<>(onSubscribe);
    }

    public Observable<T> subscribeOn(int threadId) {
        Log.e("yds", "Observable --subscribeOn : " + threadId);
        return new ObservableSubscribeOn<>(this, threadId);
    }

    // 执行
    @Override
    public void subscribe(Observer<? super T> observer) {
        Log.e("yds", "Observable --subscribe");
        subscribeActual(observer);
    }

    protected abstract void subscribeActual(Observer<? super T> observer);
}
