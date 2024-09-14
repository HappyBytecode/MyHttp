package com.sesxh.smoothhttp.lifecycle;

import com.sesxh.smoothhttp.annotation.LifeStates;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.BehaviorSubject;

/**
 * @author LYH
 * @date 2021/2/3
 * @time 16:42
 * @desc 自动绑定生命周期
 **/
public class LifeCycle {

    private  BehaviorSubject<Integer> mSubject;

    private LifeCycle() {
    }

    public static LifeCycle create() {
        return SingletonHolder.instance;
    }
    private static class SingletonHolder {
        private static LifeCycle instance = new LifeCycle();
        private SingletonHolder() {
        }
    }

    public static LifeCycle getInstance() {
        return SingletonHolder.instance;
    }

    /**
     * 重置Subject
     */
    public  LifeCycle initSubject(BehaviorSubject<Integer> subject){
        mSubject = subject;
        return this;
    }


    public static BehaviorSubject<Integer> createSubject(){
        return BehaviorSubject.create();
    }

    public static void bindLife(BehaviorSubject<Integer> subject,int life) {
        subject.onNext(life);
    }

   public  <T> ObservableTransformer<T, T> bindLife() {
        if(mSubject ==null){
            initSubject(BehaviorSubject.create());
        }
        return new ObservableTransformer<T, T>() {
            @NonNull
            @Override
            public ObservableSource<T> apply(@NonNull Observable<T> upstream) {
                return upstream.takeUntil(mSubject.skipWhile(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer state) throws Exception {
                        return state != LifeStates.DESTROY && state != LifeStates.DESTROY_VIEW;
                    }
                }));
            }

        };
    }
}
