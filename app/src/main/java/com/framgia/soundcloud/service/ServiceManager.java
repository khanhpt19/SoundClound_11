package com.framgia.soundcloud.service;

import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class ServiceManager {

    private Context mContext;
    private Intent mIntent;
    private ServiceConnection mConnection;
    private int mFlags;

    public ServiceManager(Context context, Intent intent) {
        mContext = context;
        mIntent = intent;
    }

    public ServiceManager(Context context, Intent intent, ServiceConnection connection, int flags) {
        mContext = context;
        mIntent = intent;
        mConnection = connection;
        mFlags = flags;
    }

    public void startService() {
        mContext.startService(mIntent);
    }

    public void stopService() {
        mContext.stopService(mIntent);
    }

    public void bindService() {
        mContext.bindService(mIntent, mConnection, mFlags);
    }

    public void unbindService() {
        mContext.unbindService(mConnection);
    }

}
