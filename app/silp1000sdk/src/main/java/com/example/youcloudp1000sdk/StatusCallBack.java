package com.example.youcloudp1000sdk;

import org.json.JSONObject;

public interface StatusCallBack {

    void successCallback(JSONObject responseSuccess);

    void failureCallback(JSONObject responseSuccess);
}


