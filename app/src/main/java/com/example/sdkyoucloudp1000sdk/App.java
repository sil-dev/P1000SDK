package com.example.sdkyoucloudp1000sdk;

import androidx.multidex.MultiDexApplication;

import com.example.youcloudp1000sdk.view.fragment.P1000Manager;

public class App extends MultiDexApplication {

    P1000Manager p1000Manager;

    @Override
    public void onCreate() {
        super.onCreate();
        p1000Manager = P1000Manager.initSdk(this);
    }
}
