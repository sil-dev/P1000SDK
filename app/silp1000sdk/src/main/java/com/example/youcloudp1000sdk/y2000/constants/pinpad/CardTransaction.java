package com.example.youcloudp1000sdk.y2000.constants.pinpad;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.basewin.define.InputPBOCInitData;
import com.basewin.services.ServiceManager;
import com.basewin.utils.LoadParamManage;
import com.example.youcloudp1000sdk.R;
import com.example.youcloudp1000sdk.custom_view.ProcessDialog;
import com.example.youcloudp1000sdk.retrofit.ResponseParams;
import com.example.youcloudp1000sdk.y2000.constants.GlobalData;
import com.example.youcloudp1000sdk.y2000.constants.MyCUPParam;

/**
 * Created by shankar.savant on 31-10-2017.
 */

public class CardTransaction extends Activity {
    Button btnlogin, btnTx;

    protected ProcessDialog processdialog = null;
    private static final int SHOWLOG = 1;
    /**
     * clear logs
     */
    private static final int CLEARLOG = 2;
    private StringBuffer sb = new StringBuffer("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_tx);
        btnTx = (Button) findViewById(R.id.btnTx);
        btnlogin = (Button) findViewById(R.id.btnlogin);

        GlobalData.getInstance().init(CardTransaction.this);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnTx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onlineTrans("100");
            }
        });

    }

    private void login() {
        try {
            boolean bRet;
            LoadParamManage.getInstance().DeleteAllTerParamFile();
            for (int j = 0; j < MyCUPParam.aid_data.length; j++) {
                bRet = ServiceManager.getInstence().getPboc().updateAID(0, MyCUPParam.aid_data[j]);
                Log.d("SHANKY", "download " + j + " aid [" + MyCUPParam.aid_data[j] + "]" + " bRet = " + bRet);
            }
            for (int i = 0; i < MyCUPParam.ca_data.length; i++) {
                bRet = ServiceManager.getInstence().getPboc().updateRID(0, MyCUPParam.ca_data[i]);
                Log.d("SHANKY", "download " + i + " rid [" + MyCUPParam.ca_data[i] + "]" + " bRet = [" + bRet + "]");
            }
            GlobalData.getInstance().setLogin(true);
            Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Online Trans(联机交易)
    private void onlineTrans(String amt) {
        if (!GlobalData.getInstance().getLogin()) {
            Toast.makeText(getApplicationContext(), "Login First", Toast.LENGTH_LONG).show();
        } else {
            try {
                Log.d("SHANKY", "Start Tx");
                Intent in = new Intent();
                in.putExtra(InputPBOCInitData.AMOUNT_FLAG, Long.parseLong(amt));
                in.putExtra(InputPBOCInitData.USE_DEVICE_FLAG, InputPBOCInitData.USE_MAG_CARD | InputPBOCInitData.USE_RF_CARD | InputPBOCInitData.USE_IC_CARD);
                in.putExtra(InputPBOCInitData.TIMEOUT, 60);

               /* PosEmvParam posEmvParam = new PosEmvParam();
                PBOCBinder binder = ServiceManager.getInstence().getPboc();

                posEmvParam.TransCurrCode = BytesUtil.hexString2Bytes("0356");
                posEmvParam.CountryCode = BytesUtil.hexString2Bytes("0356");

                binder.setPosTermPara(posEmvParam);
                binder.startTransfer(PBOCOption.ONLINE_PAY, in, new onlinePBOCListener(CardTransaction.this, amt));
*/


               //ServiceManager.getInstence().getPboc().startTransfer(PBOCOption.ONLINE_PAY, in, new onlinePBOCListener(CardTransaction.this, amt));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SHOWLOG:
                    sb.append(msg.obj + "\n");
                    break;
                case CLEARLOG:
                    sb = new StringBuffer("");
                    break;
            }
        }

        ;

    };

    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case SHOWLOG:
                    Log.d("SHANKYWS",msg.obj.toString());
                    sb.append(msg.obj + "\n");
                    break;
                case CLEARLOG:
                    sb = new StringBuffer("");
                    break;
            }
        }

        ;
    };

    public void LOGD(String msg) {
        Message message = new Message();
        message.what = SHOWLOG;
        message.obj = msg;
        handler.sendMessage(message);
    }

    public void updateData(ResponseParams resp) {
        Message message = new Message();
        message.what = SHOWLOG;
        message.obj = resp;
        handler1.sendMessage(message);
    }


    public void freshProcessDialog(final String title) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showProcessDialog(title);
            }
        });
    }

    public void dismissDialog() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                dismissProcessDialog();
            }
        });
    }


    public void showProcessDialog(String title) {
        if (processdialog == null)
            processdialog = new ProcessDialog(this, title,"");
        else {
            processdialog.freshTitle(title,"");
        }
    }

    /**
     *
     */
    public void dismissProcessDialog() {
        if (processdialog != null && processdialog.isShowing()) {
            processdialog.stopTimer();
            processdialog.dismiss();
            processdialog = null;
        }
    }


}