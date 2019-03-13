package com.chehejia.yrxjava.rx;

import android.util.Log;

/**
 * Created by deshui on 2019/03/05
 */
public class ObservableCreate<T> extends Observable<T> {
    private ObservableOnSubscribe<T> mOnSubscribe;

    public ObservableCreate(ObservableOnSubscribe<T> onSubscribe) {
        Log.e("yds", "ObservableCreate --");
        this.mOnSubscribe = onSubscribe;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        Log.e("yds", "ObservableCreate --subscribeActual");
        CreateEmitter<T> emitter = new CreateEmitter<T>(observer);
        observer.onSubscribe();
        try {
            mOnSubscribe.subscribe(emitter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class CreateEmitter<T> implements ObservableEmitter<T> {
        Observer<? super T> mObserver;

        public CreateEmitter(Observer<? super T> observer) {
            this.mObserver = observer;
        }

        @Override
        public void setDisposable() {

        }

        @Override
        public void onNext(T value) {
            mObserver.onNext(value);
        }

        @Override
        public void onError(Throwable error) {
            mObserver.onError(error);
        }

        @Override
        public void onComplete() {
            mObserver.onComplete();
        }
    }
}
