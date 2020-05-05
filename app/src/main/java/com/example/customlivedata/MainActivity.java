package com.example.customlivedata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

   GLiveData<String> mutableLiveData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mutableLiveData=  LiveDataManager.getInstance().with("main_act",String.class);
        mutableLiveData.observer(MainActivity.this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.e("TAG----------1",s);
            }
        });

    }

    //观察者和接口回调的区别？ 接口回调是1对1的，观察者是1对多的
    public void getMessage(View view) {
        //注册观察者，来获取mutableLiveData
        LiveDataManager.getInstance().with("main_act",String.class).postValue("李三");

    }

    /**
     * 跳转
     * @param view
     */
    public void jumpBt(View view) {
        Intent intent=new Intent(MainActivity.this,TwoAct.class);
        startActivity(intent);
    }
}
