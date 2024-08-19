package com.donkeyschool.aidltestclient;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
            tv.setText("服务已绑定");
            iRemoteService = IMyAidlInterface.Stub.asInterface(service);
            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "Service has unexpectedly disconnected");
            tv.setText("服务已解绑");
            iRemoteService = null;

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tv=(TextView) findViewById(R.id.tv);


        //绑定服务
        mBindServiceButton = findViewById(R.id.btn_bind_service);
        mBindServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBound) {

                    Intent intent = new Intent(); // 使用服务接口的类名创建Intent
                    intent.setComponent(new ComponentName("com.DonkeySchool.aidltest","com.DonkeySchool.aidltest.server"));
                    bindService(intent, mConnection, Context.BIND_AUTO_CREATE); // 绑定服务
                    tv.setText("已绑定服务");
                } else {
                    unbindService(mConnection); // 解绑服务
                    mBindServiceButton.setText("Bind Service");

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









}
