package com.chehejia.yrxjava.rx;

/**
 * Created by deshui on 2019/03/05
 */
public interface ObservableOnSubscribe<T> {

    void subscribe(ObservableEmitter<T> emitter) throws Exception;
}
