package com.chehejia.yrxjava.rx;

/**
 * Created by deshui on 2019/03/14
 * T是传入的类型
 * R是转换后的类型
 */
public interface Function<T, R> {

    R apply(T t);
}
