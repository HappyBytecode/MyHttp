package com.sesxh.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sesxh.smoothhttp.SmoothHttpThrowable;
import com.sesxh.smoothhttp.annotation.LifeStates;
import com.sesxh.smoothhttp.lifecycle.LifeCycle;
import com.sesxh.smoothhttp.observer.SmoothHttpObserver;
import com.sesxh.smoothhttp.utils.FileUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

public class MainActivity extends AppCompatActivity {

    Button mBtnGet;
    Button mBtnPostQuery;
    Button mBtnPostBody;
    Button mBtnUpload;
    Button mBtnDownload;
    private static String tag;
    private String text;
    private boolean mIsLoading;
    private static final String json="{\"resultcode\":\"200\",\"reason\":null,\"result\":{\"province\":\"山东\",\"city\":\"青岛11111\",\"areacode\":null,\"zip\":\"266000\",\"company\":\"移动\",\"card\":\"\"},\"error_code\":0}";

    public static final String TEST_FILE_PATH=FileUtils.getDownloadPath()+"dXB-4.1.2-release.apk";
    private BehaviorSubject<Integer> mSubject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSubject=LifeCycle.createSubject();
        tag=getClass().getName();
        initview();
    }

    private void initview() {
        mBtnGet=findViewById(R.id.bnt_get);
        mBtnGet.setOnClickListener(new MyOnClickListener(mBtnGet));
        mBtnPostQuery=findViewById(R.id.bnt_post);
        mBtnPostQuery.setOnClickListener(new MyOnClickListener(mBtnGet));
        mBtnDownload=findViewById(R.id.bnt_download);
        mBtnDownload.setOnClickListener(new MyOnClickListener(mBtnDownload));
        mBtnPostBody=findViewById(R.id.bnt_post1);
        mBtnPostBody.setOnClickListener(new MyOnClickListener(mBtnPostBody));
        mBtnUpload=findViewById(R.id.bnt_upload);
        mBtnUpload.setOnClickListener(new MyOnClickListener(mBtnUpload));
    }


    @Override
    protected void onResume() {
        super.onResume();
        mIsLoading=false;
    }

    class MyOnClickListener implements View.OnClickListener {

        private Button mBtn;

        public MyOnClickListener(Button btn) {
            mBtn = btn;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void onClick(View v) {
            Map m=new HashMap();
            m.put("key","5e1389ea729d16efce2a26c7027dff40");
           if(mIsLoading){
               return;
           }
           mIsLoading=true;
           resetText();
           switch (v.getId()){

               case R.id.bnt_get:
                   m.put("phone","15764236684");
                   Api.getNum(m,"mobile/get",mSubject).subscribe(new MyObserver<PhoneDTO>());
                   break;
               case R.id.bnt_post:
                   AddressDTO d=new AddressDTO();
                   d.setCity("济南");
                   m.put("ip","112.112.11.11");
                   m.put("key","8179ba6ba2e8bc9da8fffdbd73826456");
                   m.put("t",d);
                   Api.getIP(m).subscribe(new MyObserver<AddressDTO>());
                   break;
               case R.id.bnt_download:
                   goTextActivity(HttpResultActivity.DOWNLOAD);
                   break;

               case R.id.bnt_post1:
                   Observable
                           .just(0)
                           .doOnSubscribe(new Consumer<Disposable>() {
                               @Override
                               public void accept(Disposable disposable) throws Exception {
                                   Log.e("线程4",Thread.currentThread().getName());
                               }
                           })
                           .subscribeOn(Schedulers.io())
                           .observeOn(Schedulers.computation())
                           .map(new Function<Integer, Integer>() {
                               @Override
                               public Integer apply(@androidx.annotation.NonNull Integer integer) throws Exception {
                                   Log.e("线程",Thread.currentThread().getName());
                                   return integer;
                               }
                           })
                           .map(new Function<Integer, Integer>() {
                               @Override
                               public Integer apply(@androidx.annotation.NonNull Integer integer) throws Exception {
                                   Log.e("线程1",Thread.currentThread().getName());
                                   return integer;
                               }
                           }).observeOn(Schedulers.io())
                           .subscribeOn(Schedulers.newThread())
                           .subscribeOn(Schedulers.io()) // 多了这一行
                           .subscribe(new Observer<Integer>() {

                               @Override
                               public void onSubscribe(@androidx.annotation.NonNull Disposable d) {
                                   Log.e("线程2",Thread.currentThread().getName());
                               }

                               @Override
                               public void onNext(@androidx.annotation.NonNull Integer integer) {
                                   Log.e("线程3",Thread.currentThread().getName());
                               }

                               @Override
                               public void onError(@androidx.annotation.NonNull Throwable e) {

                               }

                               @Override
                               public void onComplete() {

                               }
                           });
                   break;

               case R.id.bnt_upload:
                   boolean b=FileUtils.fileExists(TEST_FILE_PATH);
                   if(!b){
                       Toast.makeText(MainActivity.this, "文件路径不存在！"+TEST_FILE_PATH, Toast.LENGTH_SHORT).show();
                       mIsLoading=false;
                       return;
                   }
                   goTextActivity(HttpResultActivity.UPLOAD);
                   break;

           }
        }

        private void resetText() {
//            text="正在请求中";
//            mBtn.setText(text);
        }
    }


    class  MyObserver<T> extends SmoothHttpObserver<T> {

        StringBuilder sb=new StringBuilder();



        @Override
        protected void onSuccess(@NonNull T data) {
            sb.append("请求成功！").append(data.toString()).append("\n");
        }

        @Override
        protected void onFailure(SmoothHttpThrowable t) {
            sb.append("请求失败！"+t.toString()).append("\n");
        }

        @Override
        protected void onFinish(boolean success) {
            mIsLoading=false;
            goTextActivity(sb.toString());
            Log.e("http：onFinish",Thread.currentThread().getName());
        }

    }

    private void goTextActivity(String info) {
        Intent intent=new Intent(this,HttpResultActivity.class);
        intent.putExtra(HttpResultActivity.INFO,info);
        startActivity(intent);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LifeCycle.bindLife(mSubject, LifeStates.DESTROY_VIEW);
        Log.e("http","销毁了");
    }
}
