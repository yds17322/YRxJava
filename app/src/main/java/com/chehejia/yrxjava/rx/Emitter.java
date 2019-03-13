package com.chehejia.yrxjava.rx;

/**
 * Created by deshui on 2019/03/05
 */
public interface Emitter<T> {
    void onNext(T value);

    void onError(Throwable error);

    void onComplete();
}
