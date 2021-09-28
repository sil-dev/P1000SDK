package com.example.youcloudp1000sdk.retrofit;


import android.app.Activity;
import android.content.Context;

import com.example.youcloudp1000sdk.custom_view.SuccessCustomDialog;
import com.example.youcloudp1000sdk.utils.MyConst;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.CertificatePinner;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Shankar Savant on 4/21/2017.
 */

public class NetworkController {

    //SDK URL
    public static final String MAIN_URL = "https://app.youcloudpayment.in/";
    public static String BASE_URL = MAIN_URL + "sdk/";

    private ApiListener apiService;

    public NetworkController() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(180, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiListener.class);

    }

    public static NetworkController getInstance() {
        return new NetworkController();
    }

    public void sendRequest(RequestParams requestParams, ResponseListener responseListener) {
        requestParams.setSessionId(MyConst.sessionToken);
        Call<ResponseParams> call = apiService.callService(requestParams);
        serverCall(call, responseListener, null);
    }

    public void sendRequest(RequestParams requestParams, ResponseListener responseListener, SuccessCustomDialog d) {
        requestParams.setSessionId(MyConst.sessionToken);
        Call<ResponseParams> call = apiService.callService(requestParams);
        serverCall(call, responseListener, d);
        d.show();
    }

    private void serverCall(Call call, final ResponseListener responseListener, final SuccessCustomDialog d) {
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                responseListener.onResponseSuccess(response, d);
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                t.printStackTrace();
                responseListener.onResponseFailure(t, d);
            }
        });
    }
}
