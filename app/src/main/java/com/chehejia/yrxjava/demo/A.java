package com.chehejia.yrxjava.demo;

/**
 * Created by deshui on 2019/03/08
 */
public abstract class A {
    public abstract void go();

    public static A createA1() {
        return new A1();
    }

    public A createA2() {
        return new A2(this);
    }

    public A createA3() {
        return new A3(this);
    }
}
