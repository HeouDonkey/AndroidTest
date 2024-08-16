package com.donkeySchool.handlertest;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HandlerTest1 extends AppCompatActivity {
    public TextView mTextView;
    public Handler mHandler;

    // 步骤1：（自定义）新创建Handler子类(继承Handler类) & 复写handleMessage（）方法
    class Mhandler extends Handler {

        // 通过复写handlerMessage() 从而确定更新UI的操作
        @Override
        public void handleMessage(Message msg) {
            // 根据不同线程发送过来的消息，执行不同的UI操作
            // 根据 Message对象的what属性 标识不同的消息
            switch (msg.what) {
                case 1:
                    mTextView.append("执行了线程1的UI操作\n");
                    break;
                case 2:
                    mTextView.append("执行了线程2的UI操作\n");
                    break;
                case 3:
                    mTextView.append("执行了主线程的操作\n");
                    break;
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_handler_test1);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });




        mTextView = (TextView) findViewById(R.id.HandlerTest1tv0);
        mTextView.setText("开始创建handler！\n");
        // 步骤2：在主线程中创建Handler实例
        mHandler = new Mhandler();
        mTextView.append("开始执行第一个线程！\n");
        // 采用继承Thread类实现多线程演示
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // 步骤3：创建所需的消息对象
                Message msg = Message.obtain();
                msg.what = 1; // 消息标识
                msg.obj = "A"; // 消息内存存放

                // 步骤4：在工作线程中 通过Handler发送消息到消息队列中
                mHandler.sendMessage(msg);
            }
        }.start();
        // 步骤5：开启工作线程（同时启动了Handler）
        mTextView.append("开始执行第二个线程！\n");
        // 此处用2个工作线程展示
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 通过sendMessage（）发送
                // a. 定义要发送的消息
                Message msg = Message.obtain();
                msg.what = 2; //消息的标识
                msg.obj = "B"; // 消息的存放
                // b. 通过Handler发送消息到其绑定的消息队列
                mHandler.sendMessage(msg);
            }
        }.start();
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mTextView.append("主线程调用handler！\n");
        Message message = Message.obtain();
        message.what = 3;
        mHandler.sendMessage(message);


    }
}