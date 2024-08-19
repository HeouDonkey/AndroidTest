package com.DonkeySchool.aidltest;

import android.app.Service;
import android.content.Intent;
import android.media.Image;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.os.Process;
import com.DonkeySchool.aidltest.*;

import java.security.PublicKey;

public class MyService extends Service {
    private final String TAG = "RemoteService";
    public final IMyAidlInterface.Stub binder = new MyBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: 服务已经开启!");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onCreate: 服务已退出!");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onCreate: 服务已解绑!");
        return super.onUnbind(intent);
    }

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind: 联系人业务");
        return  binder;
    }
    static class MyBinder extends IMyAidlInterface.Stub
    {
        @Override
        public String getContacts(String name) throws RemoteException {
            if (name.equals("张三")) return "张三手机号:1383333333333333";
            else if (name.equals("李四")) return "李四手机号:135555555555555";
            else return "没有找到联系人";
        }

        @Override
        public int getPID() throws RemoteException {
            return Process.myPid();
        }
    }


}