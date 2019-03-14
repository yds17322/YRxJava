package com.chehejia.yrxjava.rx;

import android.util.Log;

/**
 * Created by deshui on 2019/03/14
 */
public class ObservableMap<T, R> extends Observable<T> {
    private static final String TAG = "ObservableMap";

    private ObservableSource<T> source;
    private Function<T, R> function;

    public ObservableMap(ObservableSource<T> source, Function<T, R> function) {
        Log.e(TAG, "ObservableMap --");
        // 此时泛型T-String
        this.source = source;
        this.function = function;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        Log.e(TAG, "ObservableMap -- subscribeActual");
        MapObserver mapObserver = new MapObserver(observer, function);
        source.subscribe(mapObserver);
    }

    private class MapObserver<T, R> implements Observer<T> {

        private Observer<? super R> observer;
        private Function<T, R> function;

        public MapObserver(Observer<? super R> observer, Function<T, R> function) {
            // 此时泛型R-Integer
            this.observer = observer;
            this.function = function;
        }

        @Override
        public void onSubscribe() {

        }

        @Override
        public void onNext(T t) {
            // 通过这个方法，调到了main里面map创建的内部类，获得其return的返回值R，在onNext传出去，从此
            // ObservableSource<T> 变成了 ObservableSource<R>
            R r = function.apply(t);
            Log.e(TAG, "onNext -- t : " + t + ", r : " + r);
            observer.onNext(r);
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
}
