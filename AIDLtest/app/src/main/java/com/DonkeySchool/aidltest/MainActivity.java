package com.DonkeySchool.aidltest;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Process;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private final String TAG = "ClientActivity";
    private Button mBindServiceButton;
    private IMyAidlInterface iRemoteService; // 声明服务接口的Stub
    private boolean isBound = false; // 标记服务是否已绑定
    private TextView tv;

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            iRemoteService = IMyAidlInterface.Stub.asInterface(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            iRemoteService = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this, MyService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
        tv=(TextView) findViewById(R.id.tv);


        //绑定服务
        mBindServiceButton = findViewById(R.id.btn_bind_service);
        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBound) {
                    Intent intent = new Intent("com.DonkeySchool.aidltest.server"); // 使用服务接口的类名创建Intent
                    intent.setPackage(getPackageName()); // 设置包名
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 绑定服务
                    tv.setText("服务已绑定");
                } else {
                    unbindService(mConnection); // 解绑服务
                    mBindServiceButton.setText("Bind Service");
                    isBound = false;
                }
            }
        });



        //拿联系人
        mBindServiceButton = findViewById(R.id.btn_getContects);
        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    try {
                        String result = iRemoteService.getContacts("张三");
                        tv.setText(result);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    tv.setText("还未绑定服务!");
                }
            }
        });


        //拿pid
        mBindServiceButton = findViewById(R.id.btn_getPid);
        mBindServiceButton.setText("Bind Service"); // 设置按钮文本
        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isBound) {
                    try {
                        int result = iRemoteService.getPID();
                        tv.setText("本进程pid为：" + Process.myPid() +  "，  服务进程pid为：" +  result);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    tv.setText("还未绑定服务!");
                }
            }
        });
    }








    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(mConnection); // 确保在Activity销毁时解绑服务
        }
    }
}

