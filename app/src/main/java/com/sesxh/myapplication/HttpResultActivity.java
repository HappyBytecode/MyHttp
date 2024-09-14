package com.sesxh.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sesxh.smoothhttp.SmoothHttpThrowable;
import com.sesxh.smoothhttp.callbacks.DownloadCallBack;
import com.sesxh.smoothhttp.callbacks.UploadCallBack;
import com.sesxh.smoothhttp.utils.ThreadUtils;

import java.io.File;

public class HttpResultActivity extends AppCompatActivity {
    TextView tv;
    String info;
    public static final String INFO="INFO";
    public static final String UPLOAD="UPload";
    public static final String DOWNLOAD="Download";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_result);
        info=getIntent().getStringExtra(INFO);
        tv=findViewById(R.id.tv);
        if(DOWNLOAD.equals(info)){
            info="开始下载";
            download();

        }else if(UPLOAD.equals(info)){
            info="开始上传";
            upload();

        }
        setText(info);
    }

    private void setText(String info) {
        tv.setText(info);
    }


    void download(){
        Api.downloadFile(null, new DownloadCallBack() {


            @Override
            public void onFinish(boolean success, File result) {
                setText("下载" +(success?"成功！":"失败！")+"\n"+(result==null?"":result.getAbsolutePath()));
            }

            @Override
            public void onFailure(SmoothHttpThrowable t) {
                super.onFailure(t);
                setText(t.getMessage());
            }

            @Override
            public void onProgressUpdate(int progress) {
                setText(progress+"%");
            }

        });
    }

    void upload(){
        Api.uploadFile( getClass().getName(), new UploadCallBack() {


            @Override
            public void onFinish(boolean success) {
                setText("上传" +(success?"成功！":"失败！"));
            }

            @Override
            public void onFailure(SmoothHttpThrowable t) {
                setText(t.getMessage());
            }

            @Override
            public void onProgressUpdate(int progress) {
                setText(progress+"%");
            }
        });
    }
}