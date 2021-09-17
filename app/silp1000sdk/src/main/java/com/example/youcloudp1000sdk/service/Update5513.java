package com.example.youcloudp1000sdk.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.youcloudp1000sdk.custom_view.SuccessCustomDialog;
import com.example.youcloudp1000sdk.retrofit.NetworkController;
import com.example.youcloudp1000sdk.retrofit.RequestParams;
import com.example.youcloudp1000sdk.retrofit.ResponseListener;
import com.example.youcloudp1000sdk.retrofit.ResponseParams;
import com.example.youcloudp1000sdk.utils.Constants;
import com.example.youcloudp1000sdk.utils.Session;
import com.example.youcloudp1000sdk.utils.SessionConstants;
import com.google.gson.Gson;

import retrofit2.Response;


/**
 * Created by shankar.savant on 7/7/2017.
 */

public class Update5513 extends IntentService {

    public static Context ctx = null;

    public Update5513() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestParams r = new RequestParams();
        r.setRequestcode(Constants.wsget5513);
        r.setToken("" + intent.getStringExtra("5513"));
        r.setRrn("" + intent.getStringExtra("rrn"));
        r.setImei("" + Session.getString(getApplicationContext(), SessionConstants.Session_imei));
        r.setImsi("" + Session.getString(getApplicationContext(), SessionConstants.Session_imsi));
        r.setUsername("" + Session.getString(getApplicationContext(), SessionConstants.Session_username));

        Log.d("request to server", new Gson().toJson(r));

        NetworkController.getInstance().sendRequest(r, new ResponseListener() {
            @Override
            public void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d) {
                if (response.body() != null) {
                    Log.d("Status Msg", "" + response.body().getMsg());
                    if (response.body().getStatus() == true) {

                    } else {

                    }
                } else {
                }
            }

            @Override
            public void onResponseFailure(Throwable throwable, SuccessCustomDialog d) {
            }
        });
    }
}