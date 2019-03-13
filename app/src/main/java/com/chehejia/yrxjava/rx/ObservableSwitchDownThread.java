package com.chehejia.yrxjava.rx;

import android.os.Handler;
import android.util.Log;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Created by deshui on 2019/03/08
 */
public class ObservableSwitchDownThread<T> extends Observable<T> {
    private static final String TAG = "SwitchDownThread";
    final ObservableSource<T> source;
    private int threadId;
    private Handler mHandler = new Handler();
    private Thread mThread;

    public ObservableSwitchDownThread(ObservableSource<T> source, int threadId) {
        Log.e(TAG, "ObservableSwitchDownThread --");
        // source == ObservableSwitchUpThread
        this.source = source;
        this.threadId = threadId;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        // observer == Main中的回调
        // source == ObservableSwitchUpThread
        // 对observer封装，通过subscribe传入ObservableSwitchUpThread
        Log.e(TAG, "subscribeActual -- threadId:" + threadId);
        DownThreadObserver threadObserver = new DownThreadObserver(mHandler, threadId, observer);
        source.subscribe(threadObserver);
    }

    // -- 包装
    class DownThreadObserver<T> implements Observer<T>, Runnable {

        private Handler handler;
        private int threadId;
        private Observer<? super T> observer;
        private Queue<Integer> queue;

        private T t;
        private Throwable e;
        private final int next = 0;
        private final int error = 1;
        private final int complete = 2;

        // observer == Main中的回调
        public DownThreadObserver(Handler handler, int threadId, Observer<? super T> observer) {
            this.handler = handler;
            this.observer = observer;
            this.threadId = threadId;
            this.queue = new PriorityQueue<>();
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(final T t) {
            // 调用main回调的onNext
            // 这里分配子/主线程
            queue.add(next);
            Log.e(TAG, "Down -- onNext : " + t);
            this.t = t;
            prepareRun();
        }

        @Override
        public void onError(final Throwable e) {
            queue.add(error);
            Log.e(TAG, "Down -- onError : " + e);
            this.e = e;
            prepareRun();
        }

        @Override
        public void onComplete() {
            queue.add(complete);
            Log.e(TAG, "Down -- onComplete");
            prepareRun();
        }

        private synchronized void prepareRun() {
            if (threadId == MAIN_THREAD) {
                handler.post(this);
            } else {
                new Thread(this).start();
            }
        }

        @Override
        public void run() {
            for (; ; ) {
                Integer poll = queue.poll();
                if (poll == null) {
                    break;
                } else {
                    int status = poll;
                    Log.e(TAG, "run -status-> " + status);
                    switch (status) {
                        case next: {
                            observer.onNext(t);
                            break;
                        }
                        case error: {
                            observer.onError(e);
                            break;
                        }
                        case complete: {
                            observer.onComplete();
                            break;
                        }
                    }
                }

            }
        }
    }
}
