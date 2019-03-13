package com.chehejia.yrxjava.rx;

/**
 * Created by deshui on 2019/03/05
 */
public interface Observer<T> {
    void onSubscribe();

    void onNext(T t);

    void onError(Throwable e);

    void onComplete();
}
