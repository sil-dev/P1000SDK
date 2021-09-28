package com.example.sdkyoucloudp1000sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.youcloudp1000sdk.P1000CallBacks;
import com.example.youcloudp1000sdk.custom_view.ProcessDialog;
import com.example.youcloudp1000sdk.model.P1000Request;
import com.example.youcloudp1000sdk.utils.TransactionType;
import com.example.youcloudp1000sdk.view.fragment.P1000Manager;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    //SHANKY
    TextView statusTv, responseCodeTv, responseMessageTv, version;
    CustomDialog customDialog;

    protected ProcessDialog processdialog = null;
    Button btnStartTransaction;

    P1000Manager p1000Manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTv = findViewById(R.id.status_code);
        responseCodeTv = findViewById(R.id.response_code);
        responseMessageTv = findViewById(R.id.response_message);


        btnStartTransaction = findViewById(R.id.btnStartTransaction);

            p1000Manager = P1000Manager.getInstance(MainActivity.this,"KEY");

        btnStartTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showStatusDialog(true);

                P1000Request p1000Request = new P1000Request();
                p1000Request.setUsername("USER");
                p1000Request.setPassword("1234");
                p1000Request.setRefCompany("SIL");
                p1000Request.setMid("MID");
                p1000Request.setTid("TID");
                p1000Request.setTransactionId(p1000Manager.getTransactionId());
                p1000Request.setImei("3dc0c6f6b9429aas");
                p1000Request.setImsi("null");
                p1000Request.setTxn_amount("0.0");
                p1000Request.setRequestCode(TransactionType.INQUIRY);
                p1000Manager.execute(p1000Request, new P1000CallBacks() {
                    @Override
                    public void successCallback(JSONObject jsonObject) {
                        hideDialog();
                        Log.d("SHANKY", "successCallback: " + jsonObject);
                        try {
                            String status = "Success";
                            int responseCode = -1;
                            String responseMessage = null;
                            if (jsonObject.has("Msg")) {
                                status = jsonObject.getString("Msg");
                            }
                            if (jsonObject.has("ResponseCode")) {
                                responseCode = jsonObject.getInt("ResponseCode");
                            }
                            if (jsonObject.has("Response")) {
                                //   responseMessage = jsonObject.getJSONObject("Response");

                                responseMessage = jsonObject.getString("Response");
                            }

                            /*{"Msg":"Success","ResponseCode":0,"Response":"{\"aid\":\"A0000000031010\",
                            \"applName\":\"Visa International\",\"arpcdata\":\"012910a4262415376775b263030\",
                            \"authid\":\"013937\",\"batchNo\":\"212420\",\"cardno\":\"401806XXXXXX8185\",
                            \"date\":\"30.08.2021 10:45:31\",\"ed\":\"404.8#404.8\",\"hitachiResCode\":\"00\",
                            \"invoiceNumber\":\"126844\",\"msg\":\"Successful\",\"printmsg\":\"\",
                            \"respCode\":\"00\",\"rrn\":\"124210126844\",\"serviceBin\":\"Offus\",\"status\":true,
                            \"token\":\"124210891504\",
                            \"transhistory\":[],\"tsi\":\"E800\",\"tvr\":\"0080048800\"}"}*/

                            setMessage(status, responseCode, responseMessage.toString());
                        } catch (JSONException jsonexception) {
                            jsonexception.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void progressCallback(String message) {
                        //  hideDialog();
                        Log.d("SHANKY", "progressCallback: " + message);
                        updateTransactionMessage(message);
                    }

                    @Override
                    public void failureCallback(JSONObject jsonObject) {
                        hideDialog();
                        Log.d("SHANKY", "failureCallback: " + jsonObject);
                        try {
                            String status = "Success";
                            int responseCode = -1;

                            if (jsonObject.has("Msg")) {
                                status = jsonObject.getString("Msg");
                            }
                            if (jsonObject.has("ResponseCode")) {
                                responseCode = jsonObject.getInt("ResponseCode");
                            }
                            if (responseCode == 100) {
                                try {
                                    JSONObject responseMessage = new JSONObject();
                                    if (jsonObject.has("Response")) {
                                        responseMessage = jsonObject.getJSONObject("Response");
                                        setMessage(status, responseCode, responseMessage.toString());
                                    }
                                } catch (JSONException jsonexception) {
                                    String responseMessage = "";
                                    if (jsonObject.has("Response")) {
                                        responseMessage = jsonObject.getString("Response");
                                    }
                                    setMessage(status, responseCode, responseMessage);
                                    jsonexception.printStackTrace();
                                }

                            } else {
                                String responseMessage = "";
                                if (jsonObject.has("Response")) {
                                    responseMessage = jsonObject.getString("Response");
                                }
                                setMessage(status, responseCode, responseMessage);
                            }
                        } catch (JSONException jsonexception) {
                            jsonexception.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });
    }

    private void updateTransactionMessage(final String message) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (customDialog != null && customDialog.isShowing()) {
                    customDialog.setMessage(message);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle", "onStart invoked");
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //android.os.Process.killProcess();
        // moveTaskToBack(true);
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle", "onResume invoked");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle", "onPause invoked");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle", "onStop invoked");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle", "onRestart invoked");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle", "onDestroy invoked");
    }


    private void showStatusDialog(boolean show) {
        if (show) {
            if (customDialog == null)
                customDialog = new CustomDialog(MainActivity.this);

            customDialog.show();
        } else {
            if (customDialog != null) {
                customDialog.dismiss();
            }
        }
    }

    private void hideDialog() {
        runOnUiThread(() -> showStatusDialog(false));
    }

    @SuppressLint("SetTextI18n")
    private void setMessage(final String status, final int responseCode,
                            final String responseMessage) {
       /* runOnUiThread(() -> {
            if (statusTv != null && status != null && !status.isEmpty()) {
                statusTv.setText(status);
            }
            if (responseCodeTv != null) {
                responseCodeTv.setText(responseCode + "");
            }
            if (responseMessageTv != null && responseMessage != null && !responseMessage.isEmpty()) {
                responseMessageTv.setText(responseMessage);
            }
        });*/

        Intent i = new Intent(MainActivity.this, ActivityThird.class);
        startActivity(i);
        finish();
        return;
    }

}
