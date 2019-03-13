package com.chehejia.yrxjava.demo;

import android.util.Log;

/**
 * Created by deshui on 2019/03/08
 */
public class A3 extends A {
    private A a;

    public A3(A a) {
        this.a = a;
    }

    @Override
    public void go() {
        Log.i("aaa", "a3");
        a.go();
    }
}
