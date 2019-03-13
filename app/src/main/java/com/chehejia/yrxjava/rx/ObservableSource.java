package com.chehejia.yrxjava.rx;

/**
 * Created by deshui on 2019/03/08
 */
public interface ObservableSource<T> {

    /**
     * Subscribes the given Observer to this ObservableSource instance.
     *
     * @param observer the Observer, not null
     * @throws NullPointerException if {@code observer} is null
     */
    void subscribe(Observer<? super T> observer);
}