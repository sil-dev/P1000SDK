package com.example.youcloudp1000sdk;

import org.json.JSONObject;

public interface P1000CallBacks {
    void successCallback(JSONObject responseSuccess);

    void progressCallback(String string);

    void failureCallback(JSONObject responseSuccess);
}
