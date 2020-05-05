package com.example.customlivedata;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;




public class LiveDataManager {

     private static LiveDataManager liveDataManager;

     //应用中所有数据持有类的集合
     private Map<String,GLiveData<Object>> map;


     private LiveDataManager(){
         Log.e("TAG","LiveDataManager执行构造方法");
         map=new HashMap<>();

     }
     public static  LiveDataManager getInstance(){
         if(liveDataManager!=null){
             return liveDataManager;
         }
         synchronized (LiveDataManager.class){
             if(liveDataManager==null){
                 liveDataManager=new LiveDataManager();
             }
         }
         return liveDataManager;
     }
     public<T> GLiveData<T> with(String key,Class<T> clazz){
         if(!map.containsKey(key)){
             map.put(key,new GLiveData<Object>());
         }
         return (GLiveData<T>) map.get(key);
     }

//      public void remove(String key){
//         if(map.containsKey(key)){
//             map.remove(key);
//         }
//}

}
