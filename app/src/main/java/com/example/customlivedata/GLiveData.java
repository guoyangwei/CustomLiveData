package com.example.customlivedata;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;


/**
 * 组件通信 ，数据共享
 * @param <T>
 */
public class GLiveData<T> {

    //数据持有类， 持有的数据
    private T mPendingData=null;
    //观察者的集合
    private List<ObserverWrapper>  mObservers=new ArrayList<>();
    private int mVersion = -1;

    /**
     * 注册观察者的方法
     * @param lifecycleOwner
     * @param observer
     */
    public void observer(LifecycleOwner lifecycleOwner,Observer<T>  observer){
        //如果当前传进来的组件的生命周期已经结束，就直接返回
        if(lifecycleOwner.getLifecycle().getCurrentState()== Lifecycle.State.DESTROYED){
            return;
        }
        ObserverWrapper mObserverWrapper=new ObserverWrapper();
        mObserverWrapper.observer=observer;
        //为了解决还没 注册的观察者 ，也能监听到的问题
        mObserverWrapper.mLastVersion=-1;
        mObserverWrapper.lifecycle=lifecycleOwner.getLifecycle();
        mObserverWrapper.myLifecycleBound=new MyLifecycleBound();

        mObservers.add(mObserverWrapper);
        lifecycleOwner.getLifecycle().addObserver(mObserverWrapper.myLifecycleBound);
        //有个问题
        //disPatingValue();
    }

    /**
     * 绑定数据 发送通知
     * @param vaule
     */
    public void postValue(T vaule){
        this.mPendingData=vaule;
        mVersion++;
         disPatingValue();
    }

    /**
     * 遍历所有的观察者
     */
    public void disPatingValue(){

        for (ObserverWrapper mObserverWrapper:mObservers) {
            toChanged(mObserverWrapper);
        }
    }
    /**
     * 回调所有的观察者
     */
    public void toChanged(ObserverWrapper mObserverWrapper){
        //判断生命周期
        if(mObserverWrapper.lifecycle.getCurrentState()!=Lifecycle.State.RESUMED){
            return;
        }
        //判断匹配版本号，为了防止生命周期改变的时候，观察者会被回调
        if (mObserverWrapper.mLastVersion >= mVersion) {
            return;
        }
        mObserverWrapper.mLastVersion = mVersion;
        mObserverWrapper.observer.onChanged(mPendingData);
    }

    /**
     * 组件的生命周期的回调类
     */
    @SuppressLint("RestrictedApi")
    class MyLifecycleBound implements GenericLifecycleObserver {
        @Override
        public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
            if(source.getLifecycle().getCurrentState()==Lifecycle.State.DESTROYED){
                remove(source.getLifecycle());
            }
            Log.e("TAG-----",source.getLifecycle().getCurrentState().toString());
            if(mPendingData!=null){
                disPatingValue();
            }
        }
    }
    /**
     *观察者的封装类
     *
     */
    private class ObserverWrapper{
        //观察者
        Observer<T> observer;
        Lifecycle lifecycle;
        int mLastVersion = -1;
        //绑定生命周期的回调接口
        MyLifecycleBound myLifecycleBound;

    }

    public void remove(Lifecycle lifecycle){
        for (ObserverWrapper mObserverWrapper:mObservers) {
             if(mObserverWrapper.lifecycle==lifecycle){
                 mObserverWrapper.lifecycle.removeObserver(mObserverWrapper.myLifecycleBound);
                 mObservers.remove(mObserverWrapper);
             }
        }
    }
}
