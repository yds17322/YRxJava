package com.chehejia.yrxjava;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.chehejia.yrxjava.demo.A;
import com.chehejia.yrxjava.rx.Observable;
import com.chehejia.yrxjava.rx.ObservableEmitter;
import com.chehejia.yrxjava.rx.ObservableOnSubscribe;
import com.chehejia.yrxjava.rx.Observer;

import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ok(View view) {
        // 以下2个是基础 ----------------
        // rx接口回调最简单的
        simpleRxJava();
        // Rxjava链式流程
        A.createA1().createA2().createA3().go();
        // ----------------


        // Rxjava-没有线程
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Log.e(TAG, "Observable.subscribe -- subscribe " + emitter);
                emitter.onNext("aaa");
                emitter.onNext("bbb");
                emitter.onComplete();
                emitter.onError(new RuntimeException("123123123"));
            }
        })
                .subscribeOn(1)
                .subscribeOn(2)
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        Log.e(TAG, "subscribe --  onSubscribe");
                    }

                    @Override
                    public void onNext(String s) {
                        Log.e(TAG, "subscribe --  onNext " + s);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "subscribe --  onError " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.e(TAG, "subscribe --  onComplete ");
                    }
                });

        // rxjava保证原子性
        testAtomic();
    }

    // rx接口回调最简单的 -------------------------
    private void simpleRxJava() {
        IA ia = new IA() {
            @Override
            public void setIB(IB ib) {
                ib.onNext("aaaa");
                ib.onError();
                ib.onComplete();
            }
        };

        IB ib = new IB() {
            @Override
            public void onNext(String aa) {
                Log.i(TAG, aa);
            }

            @Override
            public void onError() {
            }

            @Override
            public void onComplete() {
            }
        };
        ia.setIB(ib);
    }

    private interface IA {
        void setIB(IB ib);
    }

    private interface IB {
        void onNext(String aa);

        void onError();

        void onComplete();
    }
    // -------------------------

    // Rx的原子性操作，可以保证最终结果正常，中间不管 =、=
    final AtomicReference<Integer> mInt = new AtomicReference<>(0);

    private void testAtomic() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    for (; ; ) { // 如果最新的和+1的值相同，则break，保证值没有问题
                        if (mInt.compareAndSet(mInt.get(), mInt.get() + 1)) {
                            break;
                        }
                    }
                    Log.i(TAG, mInt.get() + ", " + this);
                }
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    for (; ; ) {
                        if (mInt.compareAndSet(mInt.get(), mInt.get() + 1)) {
                            break;
                        }
                    }
                    Log.i(TAG, mInt.get() + ", " + this);
                }
            }
        });
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
            Log.e(TAG, mInt.get() + "");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
