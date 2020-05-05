package com.example.customlivedata;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

public class TwoAct extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.two_activity);
          //livedata缺陷，未执行oncreate方法的时候，也会执行观察者，这十个问题。可以通过hook技术，改变观察者的版本号。
        //也可以在 自定义的GLiveData 里面将版本号的值改变，这样就不满足下一个观察者的需求。
         LiveDataManager.getInstance().with("main_act",String.class).observer(TwoAct.this, new Observer<String>() {
             @Override
             public void onChanged(String s) {
                 Log.e("TAG----------2",s);
             }
         });
    }

    public void sedMessage(View view) {

    }
}
