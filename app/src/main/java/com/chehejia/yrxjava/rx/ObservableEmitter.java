package com.chehejia.yrxjava.rx;

/**
 * Created by deshui on 2019/03/05
 */
public interface ObservableEmitter<T> extends Emitter<T> {
    void setDisposable(); // void setDisposable(@Nullable Disposable d);
}
