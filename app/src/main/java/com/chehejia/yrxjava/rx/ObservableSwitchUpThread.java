package com.chehejia.yrxjava.rx;

import android.util.Log;

/**
 * Created by deshui on 2019/03/08
 */
public class ObservableSwitchUpThread<T> extends Observable<T> {
    private static final String TAG = "SwitchUpThread";
    final ObservableSource<T> source;
    private int threadId;
    private Thread mNewThread;

    public ObservableSwitchUpThread(ObservableSource<T> source, int threadId) {
        Log.e(TAG, "ObservableSwitchUpThread --");
        this.source = source;
        this.threadId = threadId;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        Log.e(TAG, "subscribeActual -- threadId:" + threadId);
        UpThreadObserver upThreadObserver = new UpThreadObserver(observer);
        if (threadId == Observable.NEW_THREAD) {
            UpThreadTask threadTask = new UpThreadTask(upThreadObserver);
            mNewThread = new Thread(threadTask);
            mNewThread.start();
        }
    }

    // -- 包装
    class UpThreadObserver<T> implements Observer<T> {
        private Observer<? super T> observer;

        public UpThreadObserver(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            observer.onNext(t);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            observer.onComplete();
        }
    }

    // 一个Runnable
    class UpThreadTask implements Runnable {

        private UpThreadObserver upThreadObserver;

        public UpThreadTask(UpThreadObserver upThreadObserver) {
            this.upThreadObserver = upThreadObserver;
        }

        @Override
        public void run() {
            Log.e(TAG, "UpThreadTask -- run");
            source.subscribe(upThreadObserver);
        }
    }
}
