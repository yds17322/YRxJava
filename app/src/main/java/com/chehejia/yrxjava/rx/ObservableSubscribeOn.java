package com.chehejia.yrxjava.rx;

import android.util.Log;

/**
 * Created by deshui on 2019/03/08
 */
public class ObservableSubscribeOn<T> extends Observable<T> {
    final ObservableSource<T> source;
    private int threadId;

    public ObservableSubscribeOn(ObservableSource<T> source, int threadId) {
        Log.e("yds", "ObservableSubscribeOn --");
        this.source = source;
        this.threadId = threadId;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        Log.e("yds", "ObservableSubscribeOn --subscribeActual -- threadId:" + threadId);
        source.subscribe(observer);
    }
}
