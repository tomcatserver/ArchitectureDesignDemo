package com.example.webview.remotewebview;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.example.webview.IWebToUI;

import java.util.concurrent.CountDownLatch;

public class MainProcessConnector {
    private Context mContext;
    private IWebToUI mIWebTo;
    private static volatile MainProcessConnector sInstance;
    private CountDownLatch mCountDownLatch;

    private MainProcessConnector(Context context) {
        mContext = context;
    }

    public static MainProcessConnector getInstance(Context context) {
        if (sInstance == null) {
            synchronized (MainProcessConnector.class) {
                if (sInstance == null) {
                    sInstance = new MainProcessConnector(context);
                }
            }
        }
        return sInstance;
    }

    private synchronized void connectToMainProcessService() {
        mCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, MainProcessConnector.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }


    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            mIWebTo = IWebToUI.Stub.asInterface(iBinder);
//            try {
//                mIWebTo.asBinder().linkToDeath(mDeathRecipient, 0);
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
            mCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
//            mIWebTo.asBinder().unlinkToDeath(mDeathRecipient, 0);
            mIWebTo = null;
            connectToMainProcessService();
        }
    };
}
