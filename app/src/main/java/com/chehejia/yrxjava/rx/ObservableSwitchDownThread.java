package com.chehejia.yrxjava.rx;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

/**
 * Created by deshui on 2019/03/08
 */
public class ObservableSwitchDownThread<T> extends Observable<T> {
    private static final String TAG = "SwitchDownThread";
    final ObservableSource<T> source;
    private int threadId;
    private Handler handler = new Handler();

    public ObservableSwitchDownThread(ObservableSource<T> source, int threadId) {
        Log.e(TAG, "ObservableSwitchDownThread --");
        this.source = source;
        this.threadId = threadId;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        Log.e(TAG, "subscribeActual -- threadId:" + threadId);
        if (threadId == MAIN_THREAD) {
            DownThreadObserver threadObserver = new DownThreadObserver(handler, observer);
            source.subscribe(threadObserver);
        }
    }

    // -- 包装
    class DownThreadObserver<T> implements Observer<T> {

        private Handler handler;
        private Observer<? super T> observer;

        public DownThreadObserver(Handler handler, Observer<? super T> observer) {
            this.handler = handler;
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(final T t) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Down -- onNext -> " + (Looper.getMainLooper() == Looper.myLooper()) + ", t -->" + t);
                    observer.onNext(t);
                }
            });
        }

        @Override
        public void onError(final Throwable e) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    observer.onError(e);
                }
            });
        }

        @Override
        public void onComplete() {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    observer.onComplete();
                }
            });
        }
    }
}
