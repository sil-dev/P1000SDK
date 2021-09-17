package com.example.youcloudp1000sdk.retrofit;

import com.example.youcloudp1000sdk.custom_view.SuccessCustomDialog;

import retrofit2.Response;

/**
 * Created by shankar.savant on 4/21/2017.
 */

public interface ResponseListener {
    void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d);
    void onResponseFailure(Throwable throwable, SuccessCustomDialog d);
}
