package com.example.youcloudp1000sdk.y2000.constants;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.basewin.aidl.OnPBOCListener;
import com.basewin.aidl.OnPinInputListener;
import com.basewin.commu.define.CommuListener;
import com.basewin.commu.define.CommuParams;
import com.basewin.commu.define.CommuStatus;
import com.basewin.commu.define.CommuType;
import com.basewin.define.CardType;
import com.basewin.define.GlobalDef;
import com.basewin.define.InputPBOCOnlineData;
import com.basewin.define.OutputCardInfoData;
import com.basewin.define.OutputMagCardInfo;
import com.basewin.define.OutputPBOCAAData;
import com.basewin.define.OutputQPBOCResult;
import com.basewin.define.PBOCTransactionResult;
import com.basewin.services.PBOCBinder;
import com.basewin.services.ServiceManager;
import com.basewin.utils.BCDHelper;
import com.basewin.utils.BytesUtil;
import com.example.youcloudp1000sdk.P1000CallBacks;
import com.example.youcloudp1000sdk.R;
import com.example.youcloudp1000sdk.TransactionCallback;
import com.example.youcloudp1000sdk.custom_view.EnterDialog;
import com.example.youcloudp1000sdk.custom_view.PinInputDialog;
import com.example.youcloudp1000sdk.custom_view.SignatureView;
import com.example.youcloudp1000sdk.custom_view.SuccessCustomDialog;
import com.example.youcloudp1000sdk.retrofit.NetworkController;
import com.example.youcloudp1000sdk.retrofit.RequestParams;
import com.example.youcloudp1000sdk.retrofit.ResponseListener;
import com.example.youcloudp1000sdk.retrofit.ResponseParams;
import com.example.youcloudp1000sdk.service.Update5513;
import com.example.youcloudp1000sdk.utils.AndyUtility;
import com.example.youcloudp1000sdk.utils.Constants;
import com.example.youcloudp1000sdk.utils.MyConst;
import com.example.youcloudp1000sdk.utils.ResponseCodeConst;
import com.example.youcloudp1000sdk.utils.Session;
import com.example.youcloudp1000sdk.utils.SessionConstants;
import com.example.youcloudp1000sdk.utils.StaticValues;
import com.example.youcloudp1000sdk.utils.emvdecoder.BERTLV;
import com.example.youcloudp1000sdk.y2000.constants.listeners.OnChoseListener;
import com.example.youcloudp1000sdk.y2000.constants.listeners.OnConfirmListener;
import com.example.youcloudp1000sdk.y2000.constants.listeners.OnGetLayoutSucListener;
import com.example.youcloudp1000sdk.y2000.constants.listeners.OnPinDialogListener;
import com.example.youcloudp1000sdk.y2000.constants.pinpad.PinpadTestActivityForDukpt;
import com.google.gson.Gson;
import com.pos.sdk.emvcore.PosEmvCoreManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import retrofit2.Response;

/**
 * Created by shankar.savant on 31-10-2017.
 */

public class onlinePBOCListener implements OnPBOCListener {

    P1000CallBacks p1000CallBacks;
    TransactionCallback transactionCallback;
    RequestParams req = new RequestParams();
    ResponseParams resp = new ResponseParams();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    String formattedDate = dateFormatter.format(new Date());

    private static boolean isEmvForced = false;

    private static final int ONLINE_PROCESS_COMMU = 1;
    private static final int ONLINE_PROCESS_FINISH = 2;
    private final int PIN_DIALOG_SHOW = 1;      //display pinpad(Pinpad弹出)
    private final int PIN_DIALOG_DISMISS = 2;   //close pinpad(Pinpad关闭)
    private final int PIN_SHOW = 3;             //display inputting the pinpad(PIN输入值的显示)
    private final int SETLAYOUT = 4;            //set key layout the pinpad(设置keys布局)
    private final int GETLAYOUT = 5;            //get key layout the pinpad(获取keys布局)
    private Context context;                    //context(上下文)
    private PinInputDialog pindialog = null;
    private byte[] keylayout = new byte[96];
    private byte[] receiveData = null;
    private int cardtype = 0;
    private String cardno;
    private String amt;
    boolean originalTxStatus = false, isIssuerScriptPresent = false;
    String SCRIPTRESULT = "";

    boolean ifOnlinePIN = false;
    boolean isTxOL = false;

    //pinpad the callback(pinpad的回调)
    private OnPinInputListener pinpadListener = new OnPinInputListener() {

        @Override
        public void onInput(int len, int key) throws RemoteException {
            //returns the pinpad the length of the input, the key is invalid(返回pinpad输入中的长度，Key无效)
            Log.d("SHANKY", "Pinpad password length in the display:" + len);
            Message message = new Message();
            message.what = PIN_SHOW;
            Bundle bundle = new Bundle();
            bundle.putInt("len", len);
            bundle.putInt("key", key);
            message.setData(bundle);
            pinpad_model.sendMessage(message);
        }

        @Override
        public void onError(int errorCode) throws RemoteException {
            //pinpad result to error(pinpad出错)
            Log.d("SHANKY", "Pinpad error code:" + errorCode);
            p1000CallBacks.failureCallback(getFailJSON("Pinpad Error Code:", errorCode));
            try {
                pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if ((errorCode & 0x63c0) == 0x63c0) {
                    //TODO wrong offline PIN ask again
                    pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
                    Toast.makeText(context, "Incorrect PIN Entered.Please try again", Toast.LENGTH_LONG).show();
                } else {
                    //go online in every remaining cases
                    try {
                        ServiceManager.getInstence().getPboc().comfirmPinpad(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


                /*if(errorCode == 27012){
                    //offline pin verify error : go online
                    try {
                        ServiceManager.getInstence().getPboc().comfirmPinpad(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else if (errorCode == DeviceErrorCodes.INVALID_PIN) {
                    pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS);
                    wrongPinCount++;
                    ba.getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            String msg = "Incorrect PIN Entered.\n Do you want to re-enter PIN again ?";
                            if (wrongPinCount < 2) {
                                msg = "Incorrect PIN Entered.\n Do you want to re-enter PIN again ?";
                            } else if (wrongPinCount == 2) {
                                msg = "Incorrect PIN Entered.Last Attempt Left.\n Do you want to re-enter PIN again ?";
                            }
                            if (wrongPinCount >= 3) {
                                //TODO suggested by deyu (for wrong PIN 3 time , go online case)
                                try {
                                    ServiceManager.getInstence().getPboc().comfirmPinpad(null);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                //online_transaction.sendEmptyMessage(ONLINE_PROCESS_COMMU);
                            } else {
                                new EnterDialog(ba.getActivity()).showConfirmDialog("INVALID PIN", msg, "Yes", "No", new OnConfirmListener() {
                                    @Override
                                    public void OK() {
                                        pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
                                    }

                                    @Override
                                    public void Cancel() {
                                        pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS);
                                        resp.setStatus(false);
                                        resp.setMsg("INCORRECT PIN OFFLINE");
                                        resp.setRrn(" - ");
                                        resp.setRespCode("65535");
                                        resp.setAuthid("  - ");
                                        resp.setInvoiceNumber(" - ");
                                        resp.setCardno(" -- ");
                                        resp.setPrintmsg("");
                                        resp.setBatchNo(" - ");
                                        resp.setDate(formattedDate);
                                        resp.setAid("");
                                        resp.setTvr("");
                                        resp.setTsi("");
                                        resp.setApplName("");
                                        resp.setCardType("-");
                                        resp.setHitachiResCode("65535");
                                        online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                                    }
                                });
                            }
                        }
                    });
                } else {
                    pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS);
                    resp.setStatus(false);
                    resp.setMsg("Pinpad Error : " + errorCode);
                    resp.setRrn(" - ");
                    resp.setRespCode("" + errorCode);
                    resp.setAuthid("  - ");
                    resp.setInvoiceNumber(" - ");
                    resp.setCardno(" -- ");
                    resp.setPrintmsg("");
                    resp.setBatchNo(" - ");
                    resp.setDate(formattedDate);
                    resp.setAid("");
                    resp.setTvr("");
                    resp.setTsi("");
                    resp.setApplName("");
                    resp.setCardType("-");
                    resp.setHitachiResCode("" + errorCode);
                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                }*/

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfirm(byte[] data, boolean isNonePin) {
            //the user to identify the input password,this Data is cryptography encrypted to the password(用户确定了输入的密码,Data为加密了的密码密文)
            if (!isNonePin) {
                //Encrypted transaction(加密交易)
                Log.d("SHANKY", "HEX PIN:" + BCDHelper.hex2DebugHexString(data, data.length));

                req.setPindata(BCDHelper.hex2DebugHexString(data, data.length));
                pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS);
                if (cardtype == CardType.IC_CARD) {
                    try {
                        Log.d("SHANKY", "SEND ICC REQ");
                        ServiceManager.getInstence().getPboc().comfirmPinpad(data);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("SHANKY", "SEND MS REQ");
                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_COMMU);
                }
            } else {
                req.setPindata("");
                //no secret trading(无密交易)
                Log.d("SHANKY", "Pinpad not encrypt transaction");
                pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS);
                if (cardtype == CardType.IC_CARD) {
                    try {
                        ServiceManager.getInstence().getPboc().comfirmPinpad(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_COMMU);
                }
            }
        }

        @Override
        public void onCancel() throws RemoteException {
            //if you click on the cancel button(点了取消按钮)
            Log.d("SHANKY", "Pinpad User cancel");
            pinpad_model.sendEmptyMessage(PIN_DIALOG_DISMISS);

            p1000CallBacks.failureCallback(getFailJSON("Transaction Canceled By User",101));
        }

        @Override
        public void onPinpadShow(byte[] data) throws RemoteException {
            Log.d("SHANKY", "Pinpad data is enter password coordinate values");
            Message message = new Message();
            message.what = SETLAYOUT;
            message.obj = data;
            pinpad_model.sendMessage(message);
        }
    };

    //pinpad process control(pinpad流程控制)
    @SuppressLint("HandlerLeak")
    private Handler pinpad_model = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PIN_DIALOG_SHOW:
                    Log.d("SHANKY", "Pinpad show");
                    try {
                        ServiceManager.getInstence().getPinpad().setPinpadMode(GlobalDef.MODE_RANDOM);
                         Log.d("SHANKY", "Pinpad Open");
                    } catch (Exception e1) {
                        e1.printStackTrace();
                         Log.d("SHANKY", e1.getMessage());
                    }

                    String maskedCardNo = "";
                    if (cardno == null || cardno.length() < 11) {
                        maskedCardNo = "000000******0000";
                    } else {
                        maskedCardNo = cardno.substring(0, 6) + "******" + cardno.substring(cardno.length() - 4);
                    }

                    boolean isDsiplayAmt = true;
                    if (req.getRequestcode().equalsIgnoreCase(Constants.MICRO_CASH_WITHDRAW) || req.getRequestcode().equalsIgnoreCase(Constants.MICRO_BAL_ENQ) || req.getRequestcode().equalsIgnoreCase(Constants.MICRO_MINI_STMT)) {
                        isDsiplayAmt = false;
                    }
                    //Pinpad parameter settings[context,card no,tips,amount,callback](pinpad参数设置[上下文,卡号,提示,金额,回调])
                    Log.d("SHETTY HANDE", "handleMessage: fsdfdgdfgffhfghjgjgj");
                    pindialog = new PinInputDialog(context, maskedCardNo, "Enter PIN Code", amt, new OnPinDialogListener() {
                        public void OnPinInput(int result) {
                            Log.d("SHANKY", "PIN Input Result:" + result);
                        }

                        public void OnCreateOver() {
                            Log.d("SHANKY", "Pinpad View create success");
                            pinpad_model.sendEmptyMessage(GETLAYOUT);
                        }
                    }, isDsiplayAmt);

                    break;

                case PIN_DIALOG_DISMISS:
                    //close pinpad(关闭pinpad)
                    Log.d("SHANKY", "Pinpad Close");
                    if (pindialog != null) {
                        pindialog.dismiss();
                        pindialog = null;
                    }
                    break;
                case PIN_SHOW:
                    //according to the length of the input password(显示输入的密码长度)
                    Log.d("SHANKY", "Pinpad display password length");
                    if (pindialog != null) {
                        Bundle bundle = msg.getData();
                        pindialog.setPins(bundle.getInt("len"), bundle.getInt("key"));
                    }
                    break;
                case SETLAYOUT:
                    //set layout(设置布局)
                    Log.d("SHANKY", "Pinpad start set view" + BCDHelper.bcdToString((byte[]) msg.obj, 0, ((byte[]) msg.obj).length));
                    pindialog.setKeyShow((byte[]) msg.obj, new OnGetLayoutSucListener() {
                        @Override
                        public void onSuc() {
                            keylayout = pindialog.getKeyLayout();
                            Log.d("SHANKY", "Pinpad start setting view:" + BCDHelper.bcdToString(keylayout, 0, keylayout.length));
                            try {
                                ServiceManager.getInstence().getPinpad().setPinpadLayout(keylayout);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    break;
                case GETLAYOUT:
                    //get layout(获取布局)
                    try {
                        ServiceManager.getInstence().getPinpad().setOnPinInputListener(pinpadListener);
                        //set the pinpad information[key index,card no,password length](设置pinpad相关信息)

                        if (ifOnlinePIN) {  // TODO condition suggested by Deyu
                            Log.d("SHANKY", "COME Online PIN");
                            switch (GlobalData.getInstance().getPinpadVersion()) {
                                case PinpadInterfaceVersion.PINPAD_INTERFACE_VERSION1:
                                    // ServiceManager.getInstence().getPinpad().inputOnlinePin(cardno, new byte[]{0, 4, 6, 12});
                                    break;
                                case PinpadInterfaceVersion.PINPAD_INTERFACE_VERSION2:
                                    //ServiceManager.getInstence().getPinpad().inputOnlinePinNew(GlobalData.getInstance().getTmkId(), cardno, new byte[]{0, 4, 6, 12});
                                    break;
                                case PinpadInterfaceVersion.PINPAD_INTERFACE_VERSION3:
                                    ServiceManager.getInstence().getPinpad().inputOnlinePinByArea(GlobalData.getInstance().getArea(), GlobalData.getInstance().getTmkId(), cardno, new byte[]{0, 4, 6, 12});
                                    break;
                               /* case PinpadInterfaceVersion.PINPAD_INTERFACE_DUKPT:
                                    ServiceManager.getInstence().getPinpad().inputOnlinePinDukpt(1, 0, 60, cardno, new byte[]{0, 4, 6, 12});
                                    break;*/
                                default:
                                    ServiceManager.getInstence().getPinpad().inputOnlinePinByArea(1, 1, cardno, new byte[]{0, 4, 6, 12});
                                    break;
                            }
                        } else {
                            Log.d("SHANKY", "COME Offline PIN");
                            ServiceManager.getInstence().getPinpad().inputOfflinePin(new byte[]{0, 4, 6, 12}, 60);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

    private CommuListener mCommuListener = new CommuListener() {
        @Override
        public void OnStatus(int paramInt, byte[] paramArrayOfByte) {
            switch (paramInt) {
                case CommuStatus.INIT_COMMU:
                    Log.d("SHANKY", "PBOC Communication init:" + CommuStatus.getStatusMsg(CommuStatus.INIT_COMMU));
                    p1000CallBacks.progressCallback("PBOC Communication init:" + CommuStatus.getStatusMsg(CommuStatus.INIT_COMMU));
                    break;
                case CommuStatus.CONNECTING:
                    Log.d("SHANKY", "PBOC Communication connecting:" + CommuStatus.getStatusMsg(CommuStatus.CONNECTING));
                    p1000CallBacks.progressCallback("PBOC Communication connecting:" + CommuStatus.getStatusMsg(CommuStatus.CONNECTING));

                    //          context.freshProcessDialog("Please Wait", "commu connecting...");
                    break;
                case CommuStatus.SENDING:
                    Log.d("SHANKY", "PBOC Communication sending:" + CommuStatus.getStatusMsg(CommuStatus.SENDING));
                    p1000CallBacks.progressCallback("PBOC Communication sending:" + CommuStatus.getStatusMsg(CommuStatus.SENDING));

                    // context.freshProcessDialog("Please Wait", "commu send data...");
                    break;
                case CommuStatus.RECVING:
                    Log.d("SHANKY", "PBOC Communication recving:" + CommuStatus.getStatusMsg(CommuStatus.RECVING));
                    p1000CallBacks.progressCallback("PBOC Communication receiving:" + CommuStatus.getStatusMsg(CommuStatus.RECVING));

                    //    context.freshProcessDialog("Please Wait", "commu recv data...");
                    break;
                case CommuStatus.FINISH:
                    Log.d("SHANKY", "PBOC Communication finish:" + CommuStatus.getStatusMsg(CommuStatus.FINISH));
                    p1000CallBacks.progressCallback("PBOC Communication finish:" + CommuStatus.getStatusMsg(CommuStatus.FINISH));

//                    context.freshProcessDialog("Please Wait", "commu finish...");
                    System.arraycopy(paramArrayOfByte, 0, receiveData, 0, paramArrayOfByte.length);
                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void OnError(int paramInt, String paramString) {
            Log.d("SHANKY", "PBOC Communication error code:" + paramInt + " error:" + paramString);
            //  context.freshProcessDialog("Please Wait", "commu finish...");
            p1000CallBacks.failureCallback(getFailJSON("PBOC Communication error code:" + paramInt + " error:" + paramString, 99));

            online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
        }
    };

    //online trading the process(在线支付过程)
    @SuppressLint("HandlerLeak")
    private Handler online_transaction = new Handler() {
        @SuppressLint("ShowToast")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ONLINE_PROCESS_COMMU:
                    //ISO8583 processing the start(ISO流程开始)
                    p1000CallBacks.progressCallback("Sending Data To Server...");
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            //Reset Flag of Force EMV
                            isEmvForced = false;

                            Log.d("TxAmt", "" + amt);
                            req.setAmt(amt);
                            try {
                                if (req.getCarddata() == null || req.getCarddata().trim().equals("")) {
                                    resp.setStatus(false);
                                    resp.setMsg("Please Swipe the card properly again");
                                    resp.setRrn(" - ");
                                    resp.setRespCode("01");
                                    resp.setAuthid("  - ");
                                    resp.setInvoiceNumber(" - ");
                                    resp.setCardno("---");
                                    resp.setPrintmsg("");
                                    resp.setBatchNo(" - ");
                                    resp.setDate(formattedDate);
                                    resp.setAid("");
                                    resp.setTvr("");
                                    resp.setTsi("");
                                    resp.setApplName("");
                                    resp.setCardType("-");
                                    resp.setHitachiResCode("01");
                                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                                } else {
                                    NetworkController.getInstance().sendRequest(req, new ResponseListener() {
                                        @Override
                                        public void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d) {
                                            isTxOL = true;
                                            if (response.body() != null) {
                                                resp = response.body();
                                                originalTxStatus = resp.isStatus();
                                                Log.d("SHANKY", "" + response.body());
                                                //  setResult(true, response.body().getMsg(), response.code(), new Gson().toJson(resp), p1000CallBacks);

                                                //        p1000CallBacks.successCallback(getFailJSON("Status Msg :" + response.body().getMsg(),response.code()));
                                            } else {
                                               /* if (req.getRequestcode() != null) {
                                                    if (req.getRequestcode().equalsIgnoreCase(Constants.MICRO_CASH_WITHDRAW_SBM) || req.getRequestcode().equalsIgnoreCase("microCashWithdraw") || (req.getRequestcode() != null && req.getRequestcode().equalsIgnoreCase("doCardPayment"))) {
                                                        recall(3, new RequestParams());
                                                        return;
                                                    }
                                                }*/
                                                resp.setStatus(false);
                                                resp.setMsg("Null Responce");
                                                resp.setRrn(" - ");
                                                resp.setRespCode("404");
                                                resp.setAuthid("  - ");
                                                resp.setInvoiceNumber(" - ");
                                                resp.setCardno("---");
                                                resp.setPrintmsg("");
                                                resp.setBatchNo(" - ");
                                                resp.setDate(formattedDate);
                                                resp.setAid("");
                                                resp.setTvr("");
                                                resp.setTsi("");
                                                resp.setApplName("");
                                                resp.setCardType("-");
                                                resp.setHitachiResCode("404");
                                            }
                                            online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                                        }

                                        @Override
                                        public void onResponseFailure(Throwable t, SuccessCustomDialog d) {
                                            Log.e("", t.toString());
                                            // p1000CallBacks.failureCallback(getFailJSON("Status Msg :" + t.getMessage(),101));
                                            /*if (req.getRequestcode() != null) {
                                                if (req.getRequestcode().equalsIgnoreCase("microCashWithdrawSbm")||req.getRequestcode().equalsIgnoreCase("microCashWithdrawIcici") || req.getRequestcode().equalsIgnoreCase("microCashWithdraw") || (req.getRequestcode() != null && req.getRequestcode().equalsIgnoreCase("doCardPayment"))) {
                                                    recall(3, new RequestParams());
                                                    return;
                                                }
                                            }*/
                                            try {
                                                p1000CallBacks.failureCallback(getFailJSON("Check Internet Connection", 404));
                                                if (d != null)
                                                    d.cancel();
                                            } catch (Exception e) {
                                            }

                                        }
                                    });
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                    break;
                case ONLINE_PROCESS_FINISH:
                    //ISO8583 processing the end(ISO8583流程结束)
                    Log.d("SHANKY", "PBOC Communication over decode data");

                    if (resp.getRespCode().equalsIgnoreCase(ResponseCodeConst.USE_CHIP) || resp.getRespCode().equalsIgnoreCase("" + DeviceErrorCodes.FALLBACK)) {
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                new EnterDialog(context).showConfirmDialog("Faulty Chip",
                                        "Please remove card and use Mag Stripe.\n Do you want to continue with Mag Stripe ?",
                                        "Yes",
                                        "No",
                                        new OnConfirmListener() {
                                            @Override
                                            public void OK() {
                                                transactionCallback.chipFallback(p1000CallBacks, "MS");
                                            }

                                            @Override
                                            public void Cancel() {
                                                p1000CallBacks.failureCallback(getFailJSON("User Cancelled Transacton", 99));
                                            }
                                        });
                            }
                        });

                    } else if (!isTxOL) {
                        p1000CallBacks.failureCallback(getFailJSON(resp.getRespCode() + resp.getMsg(), 99));

                    } else {
                        new Thread() {
                            @Override
                            public void run() {
                                //Added by shanky
                                //context.dismissDialog();
                                Log.d("Resp:", "" + new Gson().toJson(resp));

                                //if (resp.isStatus()) {
                                InputPBOCOnlineData onlineData = new InputPBOCOnlineData();
                                if (resp.isStatus())
                                    onlineData.setResponseCode("00");
                                else
                                    onlineData.setResponseCode("" + resp.getRespCode());

                                if (req.getReader() == null) {
                                    req.setReader("MS");
                                    MyConst.setReader("MS");
                                }
                                if (!req.getReader().equalsIgnoreCase("MS")) {
                                    if (resp.getArpcdata() != null && resp.getArpcdata().length() >= 3) {
                                        TreeMap<String, String> bertlv = BERTLV.parseTLV(resp.getArpcdata().substring(3));
                                        Log.d("SHANKY", "TLV:" + bertlv);
                                        if (bertlv.get("91") != null) {
                                            onlineData.setAuthCode(bertlv.get("91"));
                                            //onlineData.setAuthCode("AAAAAAAAAA");
                                            Log.d("SHANKY", "TAG91 : " + bertlv.get("91"));
                                        }
                                        String icdata = "";
                                        if (bertlv.get("71") != null) {
                                            isIssuerScriptPresent = true;
                                            icdata += concatEMVTag("71", "" + bertlv.get("71"));
                                            Log.d("SHANKY", "TAG71 : " + bertlv.get("71"));
                                        }
                                        if (bertlv.get("72") != null) {
                                            isIssuerScriptPresent = true;
                                            icdata += concatEMVTag("72", "" + bertlv.get("72"));
                                            Log.d("SHANKY", "TAG72 : " + bertlv.get("72"));
                                        }
                                        if (icdata != null && !icdata.equalsIgnoreCase("")) {
                                            onlineData.setICData("" + icdata);
                                            Log.d("SHANKY", "ICDATA : " + icdata);
                                        }
                                        //onlineData.setICData("" + resp.getArpcdata().substring(7).toUpperCase());
                                        try {
                                            ServiceManager.getInstence().getPboc().inputOnlineProcessResult(onlineData.getIntent());
                                            Log.d("SHANKY", "INJECT RESPONSE DATA");
                                        } catch (Exception e) {
                                            Log.d("SHANKY", "INJECT RESPONSE DATA ERROR");
                                            e.printStackTrace();
                                        }
                                    } else {

                                        // ba.updateData(resp);

                                        //TODO for mastercard Case
                                        //if no arpc received inject "000.." as arpc in device

                                        onlineData.setAuthCode("00000000000000000000");
                                        //onlineData.setAuthCode("");
                                        Log.d("SHANKY", "TAG91 : 00000000000000000000");
                                        try {
                                            ServiceManager.getInstence().getPboc().inputOnlineProcessResult(onlineData.getIntent());
                                            Log.d("SHANKY", "INJECT RESPONSE DATA");
                                        } catch (Exception e) {
                                            Log.d("SHANKY", "INJECT RESPONSE DATA ERROR");
                                            e.printStackTrace();
                                        }
                                        //ba.updateData(resp);
                                    }
                                } else {
                                    //if MS update Result
                                    setResult(true, "Success", 0, (new Gson().toJson(resp)), p1000CallBacks);
                                }
                            }
                        }.start();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public onlinePBOCListener(Context context, String amt, RequestParams req, P1000CallBacks p1000CallBacks,TransactionCallback transactionCallback) {
        this.context = context;
        this.amt = amt;
        this.req = req;
        this.p1000CallBacks = p1000CallBacks;
        this.transactionCallback = transactionCallback;
    }

    private void recall(final int i, final RequestParams recallRequest) {
        if (i > 0) {
            Log.e("Shanky", i + " repeat");

            recallRequest.setRequestcode(Constants.TRANSACTION_STATUS);
            recallRequest.setUsername(req.getUsername());
            recallRequest.setSessionId("-");

            recallRequest.setImei("-");
            recallRequest.setImsi("-");
            recallRequest.setRrn(req.getRrn());
            if (req.getRequestcode() != null && req.getRequestcode().equalsIgnoreCase("microCashWithdraw") && req.getRequestcode().equalsIgnoreCase("microCashWithdrawIcici")) {
                recallRequest.setOp("withdraw");
            } else if (req.getRequestcode() != null && req.getRequestcode().equalsIgnoreCase("doCardPayment")) {
                recallRequest.setOp("sale");
            }

            NetworkController.getInstance().sendRequest(recallRequest, new ResponseListener() {
                @Override
                public void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d) {
                    isTxOL = true;
                    if (response.body() != null) {
                        resp = response.body();
                        originalTxStatus = resp.isStatus();
                        Log.d("Shanky", "Recall" + response.body().getMsg());
                        setResult(response.body().isStatus(), response.body().getMsg(), response.code(), new Gson().toJson(resp), p1000CallBacks);

                    } else {
                        if (req.getRequestcode() != null) {
                            if (req.getRequestcode().equalsIgnoreCase("microCashWithdrawIcici") || req.getRequestcode().equalsIgnoreCase("microCashWithdraw") || (req.getRequestcode() != null && req.getRequestcode().equalsIgnoreCase("doCardPayment"))) {
                                recall(i - 1, recallRequest);
                                return;
                            }
                        }
                        resp.setStatus(false);
                        resp.setMsg("Null Responce");
                        resp.setRrn(" - ");
                        resp.setRespCode("91");
                        resp.setAuthid("  - ");
                        resp.setInvoiceNumber(" - ");
                        resp.setCardno("---");
                        resp.setPrintmsg("");
                        resp.setBatchNo(" - ");
                        resp.setDate(formattedDate);
                        resp.setAid("");
                        resp.setTvr("");
                        resp.setTsi("");
                        resp.setApplName("");
                        resp.setCardType("-");
                        resp.setHitachiResCode("91");
                    }
                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                }

                @Override
                public void onResponseFailure(Throwable t, SuccessCustomDialog d) {
                    Log.e("", t.toString());
                    p1000CallBacks.failureCallback(getFailJSON(t.getMessage(), 99));

                    if (req.getRequestcode() != null) {
                        if (req.getRequestcode().equalsIgnoreCase("microCashWithdrawIcici") || req.getRequestcode().equalsIgnoreCase("microCashWithdraw") || (req.getRequestcode() != null && req.getRequestcode().equalsIgnoreCase("doCardPayment"))) {
                            recall(i - 1, recallRequest);
                            return;
                        }
                    }
                    try {
                        //  p1000CallBacks.failureCallback(getFailJSON("Check Internet Connection", 404));
                    } catch (Exception e) {

                    }
                }
            });
        } else {
            try {
                resp.setStatus(false);
                resp.setMsg("Null Responce");
                resp.setRrn(" - ");
                resp.setRespCode("91");
                resp.setAuthid("  - ");
                resp.setInvoiceNumber(" - ");
                resp.setCardno("---");
                resp.setPrintmsg("");
                resp.setBatchNo(" - ");
                resp.setDate(formattedDate);
                resp.setAid("");
                resp.setTvr("");
                resp.setTsi("");
                resp.setApplName("");
                resp.setCardType("-");
                resp.setHitachiResCode("91");
            } catch (Exception e) {

            }

        }
    }


    private CommuParams getParams() {
        //Dynamic setting configuration file(动态设置配置文件)
        CommuParams params = new CommuParams();
        params.setIp("140.206.168.98");
        params.setType(CommuType.SOCKET);
        params.setPort(4900);
        params.setTimeout(5);
        return params;
    }

    @Override
    public void onStartPBOC() throws RemoteException {
        //PBOC process the start(PBOC 流程开始)
        Log.d("SHANKY", "PBOC Start");
        try {
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "EXC : " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestAmount() {
        //if you don't set the amount before,can be in this setting(如果之前没有设置金额，可以再次设置)
        Log.d("SHANKY", "PBOC Setting amount");

        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {

                try {
                    ServiceManager.getInstence().getPboc().setAmount(Integer.parseInt(StringHelper.changeAmout(req.getAmt()).replace(".", "")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onSelectApplication(final List<String> applicationList) {
        //selection card application(在此选着卡应用)
        Log.d("SHANKY", "PBOC Select Application");
        final String[] namesArr = new String[applicationList.size()];
        for (int i = 0; i < applicationList.size(); i++) {
            namesArr[i] = applicationList.get(i);
        }

        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                new EnterDialog(context).showListChoseDialog("please chose application!", namesArr, new OnChoseListener() {
                    @Override
                    public void Chose(int i) {
                        Log.d("SHANKY", "chose :" + i);
                        try {
                            ServiceManager.getInstence().getPboc().selectApplication(i + 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void onFindingCard(int cardType, Intent data) {
        switch (cardType)
        {
            case CardType.MAG_CARD:
                cardtype = CardType.MAG_CARD;
                Log.d("SHANKY", "PBOC CardType:Mag card");
                //MAG card data entity class
                OutputMagCardInfo magCardInfo = new OutputMagCardInfo(data);

                Log.d("SHANKY", "PBOC Mag card number:" + magCardInfo.getPAN());
                Log.d("SHANKY", "PBOC Mag card track 2:" + magCardInfo.getTrack2HexString());
                Log.d("SHANKY", "PBOC Mag card track 2 Encrypted :" + PinpadTestActivityForDukpt.encryptedTrack(magCardInfo.getTrack2HexString()));
                Log.d("SHANKY", "PBOC Mag card track 3:" + magCardInfo.getTrack3HexString());
                Log.d("SHANKY", "PBOC Term of validity:" + magCardInfo.getExpiredDate());
                Log.d("SHANKY", "PBOC Service Code: " + magCardInfo.getServiceCode());

                req.setCarddata(PinpadTestActivityForDukpt.encryptedTrack(magCardInfo.getTrack2HexString()));
                req.setReader("MS");
                MyConst.setReader("MS");
                cardno = magCardInfo.getPAN();

                ifOnlinePIN = true;
                try {
                    String track2 = magCardInfo.getTrack2HexString();
                    if (track2 == null || track2.equalsIgnoreCase("") || track2.equalsIgnoreCase("null")) {
                        resp.setStatus(false);
                        resp.setMsg("INVALID TRACK, TRY AGAIN.");

                        resp.setRrn(" - ");
                        resp.setRespCode("91");
                        resp.setAuthid("  - ");
                        resp.setInvoiceNumber(" - ");
                        resp.setCardno(" -- ");
                        resp.setPrintmsg("");
                        resp.setBatchNo(" - ");
                        resp.setDate(formattedDate);
                        resp.setAid("");
                        resp.setTvr("");
                        resp.setTsi("");
                        resp.setApplName("");
                        resp.setCardType("-");
                        resp.setHitachiResCode("91");

                        online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                    } else {
                        String serviceCode = "";
                        if (track2.contains("D")) {
                            serviceCode = track2.split("D")[1].substring(4, 7);
                        } else if (track2.contains("=")) {
                            serviceCode = track2.split("=")[1].substring(4, 7);
                        }
                        Log.d("SHANKY", "SERVICECODE : " + serviceCode);
                        Log.d("SHANKY", "PARAMTOCHK : " + serviceCode.substring(2).equalsIgnoreCase("1"));

                        if (isEmvForced) {
                            //if already forced for EMV
                            if (serviceCode.substring(2).equalsIgnoreCase("1")) {
                                //No Pin For MS
                                Log.d("SHANKY", "No PIN Required for MS");
                                String bin = track2.substring(0, 6);
                                Log.d("SHANKY", "BIN : " + bin);
                                if (AndyUtility.isAskPinForMSByCDT(bin)) {
                                    pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
                                } else {
                                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_COMMU);
                                }
                            } else {
                                //PIN Required
                                pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
                            }
                        } else {
                            if (serviceCode.startsWith("2") || serviceCode.startsWith("6")) {
                                //Fallback Case : Force User for EMV
                                isEmvForced = true;
                                // context.dismissDialog();
                                resp.setStatus(false);
                                resp.setMsg("Use Chip Card");
                                resp.setRrn(" - ");
                                resp.setRespCode("" + ResponseCodeConst.USE_CHIP);
                                resp.setAuthid("  - ");
                                resp.setInvoiceNumber(" - ");
                                resp.setCardno(" -- ");
                                resp.setPrintmsg("");
                                resp.setBatchNo(" - ");
                                resp.setDate(formattedDate);
                                resp.setAid("");
                                resp.setTvr("");
                                resp.setTsi("");
                                resp.setApplName("");
                                resp.setCardType("-");
                                resp.setHitachiResCode("" + ResponseCodeConst.USE_CHIP);
                                online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                            } else {
                                if (serviceCode.substring(2).equalsIgnoreCase("1")) {
                                    //No Pin For MS
                                    Log.d("SHANKY", "No PIN Required for MS");
                                    String bin = track2.substring(0, 6);
                                    Log.d("SHANKY", "BIN : " + bin);
                                    if (AndyUtility.isAskPinForMSByCDT(bin)) {
                                        pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
                                    } else {
                                        online_transaction.sendEmptyMessage(ONLINE_PROCESS_COMMU);
                                    }
                                } else {
                                    //PIN Required
                                    pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    resp.setStatus(false);
                    resp.setMsg("Exception Occured");
                    resp.setRrn(" - ");
                    resp.setRespCode("91");
                    resp.setAuthid("  - ");
                    resp.setInvoiceNumber(" - ");
                    resp.setCardno("" + magCardInfo.getMaskedPAN());
                    resp.setPrintmsg("");
                    resp.setBatchNo(" - ");
                    resp.setDate(formattedDate);
                    resp.setAid("");
                    resp.setTvr("");
                    resp.setTsi("");
                    resp.setApplName("");
                    resp.setCardType("-");
                    resp.setHitachiResCode("91");
                    p1000CallBacks.failureCallback(getFailJSON("Response :"+resp, 99));
                    online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                }

                break;
            case CardType.IC_CARD:
                cardtype = CardType.IC_CARD;
                req.setReader("EMV");
                MyConst.setReader("EMV");
                Log.d("SHANKY", "PBOC CardType:IC card");
                break;
            case CardType.RF_CARD:
                cardtype = CardType.RF_CARD;
                Log.d("SHANKY", "PBOC CardType:RF card");
                break;

            case CardType.CARD_TYPE_UNKNOWN:
                resp.setStatus(false);
                resp.setMsg("UNKNOWN CARD TYPE");
                resp.setRrn(" - ");
                resp.setRespCode("91");
                resp.setAuthid("  - ");
                resp.setInvoiceNumber(" - ");
                resp.setCardno(" -- ");
                resp.setPrintmsg("");
                resp.setBatchNo(" - ");
                resp.setDate(formattedDate);
                resp.setAid("");
                resp.setTvr("");
                resp.setTsi("");
                resp.setApplName("");
                resp.setCardType("-");
                resp.setHitachiResCode("91");
                Log.d("SHANKY", "PBOC CardType:RF card");
                online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                break;
        }
    }

    @Override
    public void onContactlessCardType(int i) throws Exception {

    }

    @Override
    public void onRequestInputPIN(boolean isOnlinePin, int retryTimes) throws RemoteException {
        //Call For EMV only

        // Need a password,At this point you need to call password pinpad(底层返回需要设置密码，这个时候需要调用pinpad模块进行密码输入，只有IC PBOC流程)
        Log.d("SHANKY", "PBOC Request input PIN");
        //thi will call only in EMV case and if isOnlinePin=true we have to ask for pin otherwise do offline pin

        //TODO changes suggested by Deyu
        ifOnlinePIN = isOnlinePin;
        Log.d("SHANKY", "ifOnlinePIN : " + ifOnlinePIN);
        pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);


        /*if (isOnlinePin) {
            //online pin
            pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
        } else {
            //offline Pin
            Log.d("SHANKY", "SHANKY : NO PIN FOR THIS ICC");
            pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
            //online_transaction.sendEmptyMessage(ONLINE_PROCESS_COMMU);
        }*/

    }

    @Override
    public void onConfirmCardInfo(Intent info) {
        Log.d("SHANKY", "PBOC Confirm Card Info");
        // context.dismissDialog();
        OutputCardInfoData out = new OutputCardInfoData(info);
        Log.d("SHANKY", "IC card SN:" + out.getCardSN());
        Log.d("SHANKY", "IC card number:" + out.getPAN());
        Log.d("SHANKY", "IC card expired date:" + out.getExpiredDate());
        Log.d("SHANKY", "IC card service code:" + out.getServiceCode());
        Log.d("SHANKY", "IC card track:" + out.getTrack());
        Log.d("SHANKY", "Encrypted Track : " + PinpadTestActivityForDukpt.encryptedTrack(out.getTrack()));



        req.setCarddata(PinpadTestActivityForDukpt.encryptedTrack(out.getTrack()));

        Log.d("SHANKY","TRACK TRIPPLE : "+PinpadTestActivityForDukpt.encryptedTrack(out.getTrack()));




        cardno = out.getPAN();

        //TODO Card Confirm Break By Shanky As Vinayak Sir Suggest
        try {
            ServiceManager.getInstence().getPboc().confirmCardInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onConfirmCertInfo(String certType, String certInfo) throws RemoteException {
        //confirm the identity(确认身份信息)
        Log.d("SHANKY", "PBOC Confirm credentials info");
    }

    @Override
    public void onAARequestOnlineProcess(Intent actionAnalysisData) throws RemoteException {
        //online trading(联机交易)
        Log.d("SHANKY", "PBOC the Online trade process");

        OutputPBOCAAData out = new OutputPBOCAAData(actionAnalysisData);

        String EMVDATA = "" + out.get55Field();

        Log.d("SHANKY", "EMVDATA out.get55Field" + EMVDATA);
        try {
            byte[] tag9a = ServiceManager.getInstence().getPboc().getEmvTlvData(0x9A);
            Log.d("SHANKY", "TAG 9A : " + BCDHelper.hex2DebugHexString(tag9a, tag9a.length).toString().replaceAll(" ", ""));
            byte[] tag9b = ServiceManager.getInstence().getPboc().getEmvTlvData(0x9B);

            if (tag9b != null) {
                Log.d("SHANKY", "TAG 9B : " + concatEMVTag("9B", BCDHelper.hex2DebugHexString(tag9b, tag9b.length).toString().replaceAll(" ", "")));
                EMVDATA += concatEMVTag("9B", BCDHelper.hex2DebugHexString(tag9b, tag9b.length).toString().replaceAll(" ", ""));
            }
            byte[] tag5F34 = ServiceManager.getInstence().getPboc().getEmvTlvData(0x5F34);
            if (tag5F34 != null) {
                Log.d("SHANKY", "TAG tag5F34 : " + concatEMVTag("5F34", BCDHelper.hex2DebugHexString(tag5F34, tag5F34.length).toString().replaceAll(" ", "")));
                EMVDATA += concatEMVTag("5F34", BCDHelper.hex2DebugHexString(tag5F34, tag5F34.length).toString().replaceAll(" ", ""));
            }

            byte[] tag9F06 = ServiceManager.getInstence().getPboc().getEmvTlvData(0x9F06);
            if (tag9F06 != null) {
                Log.d("SHANKY", "TAG tag9F06 : " + concatEMVTag("9F06", BCDHelper.hex2DebugHexString(tag9F06, tag9F06.length).toString().replaceAll(" ", "")));
                EMVDATA += concatEMVTag("9F06", BCDHelper.hex2DebugHexString(tag9F06, tag9F06.length).toString().replaceAll(" ", ""));
            }

            byte[] tag9F07 = ServiceManager.getInstence().getPboc().getEmvTlvData(0x9F07);
            if (tag9F07 != null) {
                Log.d("SHANKY", "TAG tag9F07 : " + concatEMVTag("9F07", BCDHelper.hex2DebugHexString(tag9F07, tag9F07.length).toString().replaceAll(" ", "")));
                EMVDATA += concatEMVTag("9F07", BCDHelper.hex2DebugHexString(tag9F07, tag9F07.length).toString().replaceAll(" ", ""));
            }
            byte[] tag4F = ServiceManager.getInstence().getPboc().getEmvTlvData(0x4F);
            if (tag4F != null) {
                Log.d("SHANKY", "TAG tag4F : " + concatEMVTag("4F", BCDHelper.hex2DebugHexString(tag4F, tag4F.length).toString().replaceAll(" ", "")));
                EMVDATA += concatEMVTag("4F", BCDHelper.hex2DebugHexString(tag4F, tag4F.length).toString().replaceAll(" ", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("SHANKY", "PBOC 55 field:" + EMVDATA);
        Log.d("SHANKY", "PBOC AA result:" + out.getAAResult());
        Log.d("SHANKY", "PBOC Card seq number:" + out.getCardSeqNum());
        Log.d("SHANKY", "PBOC IC data:" + out.getICData());
        Log.d("SHANKY", "PBOC reversal data:" + out.getReversalData());
        Log.d("SHANKY", "PBOC TC:" + out.getTCData());

        //TODO 9F5B issue solving
        SCRIPTRESULT = out.getTCData();

        req.setTag55(EMVDATA);
        //break by shanky
        online_transaction.sendMessage(online_transaction.obtainMessage(ONLINE_PROCESS_COMMU));
    }

    @Override
    public void onTransactionResult(int result, Intent data) throws RemoteException {
        //Transaction result(交易结果)
        //Bundle[{Term Cap=E0E9C8, result=0, tcData=6DACAD89E4962015, reversalData=950502800088009F1E0830303030303132339F100706010A03608D00, Unpr Num=A9996A7A, TC=6DACAD89E4962015, AID=A0000000031010, AIP=5C00, APN=null, ATC=019B, CSN=01, IAD=06010A03608D00, TSI=E800, TVR=0280008800, CVMR=1E0300, APP_LABEL=VISA CREDIT}]
        try {
            Bundle extras = data.getExtras();
            extras.isEmpty(); // unparcel
            Log.d("SHANKY", "PBOC the Transaction result : " + result);
            Log.d("SHANKY", "Data : " + extras);
            byte[] data9f27 = PosEmvCoreManager.getDefault().EmvGetTlvData(0x9F27);
            if (data9f27 != null)
                Log.d("SHANKY", "9F27 : " + BytesUtil.bytes2HexString(data9f27));

            if (extras != null) {
                try {
                    if (extras.getString("TC") != null)
                        SCRIPTRESULT = extras.getString("TC");
                    p1000CallBacks.progressCallback("Data"+SCRIPTRESULT);
                    Log.d("SHANKY", "SCRIPT RESULT : " + SCRIPTRESULT);

                   /* if (SCRIPTRESULT == null||SCRIPTRESULT.equalsIgnoreCase("NULL")) {
                        PBOCBinder pbocBinder = ServiceManager.getInstence().getPboc();
                        SCRIPTRESULT = BytesUtil.bytes2HexString(pbocBinder.getScriptResult());
                        Log.d("SHANKY", "SCRIPT RESULT1 : " + SCRIPTRESULT);
                    }*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String DE39 = "";

            if (result == PBOCTransactionResult.QPBOC_ARQC) {
                //quick pay to process(快速交易流程)
                OutputQPBOCResult rf_data = new OutputQPBOCResult(data);
                String field55String = rf_data.get55Field();
                Log.d("SHANKY", "Field 55 : " + field55String);
                String pan = rf_data.getPAN();
                Log.d("SHANKY", "PAN : " + pan);
                cardno = rf_data.getPAN();
                String maskedpan = rf_data.getMaskedPan();
                Log.d("SHANKY", "MASKED PAN : " + maskedpan);
                String trackString = rf_data.getTrack();
                Log.d("SHANKY", "TRACK2 : " + trackString);
                Log.d("SHANKY", "PBOC Trade result track 2:" + trackString);
                byte[] bcdTrack = BCDHelper.StrToBCD(trackString, trackString.length());
                Log.d("SHANKY", "PBOC Trade result track 2 the bcd:" + BCDHelper.hex2DebugHexString(bcdTrack, bcdTrack.length));

                String expiredate = rf_data.getExpiredDate();
                Log.d("SHANKY", "PBOC call PinPad");
                pinpad_model.sendEmptyMessage(PIN_DIALOG_SHOW);
            } else if (result == PBOCTransactionResult.APPROVED) {
                //normal pay to process(普通交易流程)
                try {
                    PBOCBinder pbocBinder = ServiceManager.getInstence().getPboc();
                    Log.d("SHANKY", "PBOC EC balance：" + pbocBinder.readEcBalance());
                    Log.d("SHANKY", "PBOC DATA : " + pbocBinder.getEmvTlvData(80));
                    byte[] data1 = pbocBinder.getEmvTlvData(0x9F5D);

                    if (data1 != null) {
                        Log.d("SHANKY", "data 1:" + BCDHelper.hex2DebugHexString(data1, data1.length));
                    }

                    byte[] data2 = pbocBinder.getEmvTlvData(0x9F79);
                    if (data2 != null) {
                        Log.d("SHANKY", "data 2:" + BCDHelper.hex2DebugHexString(data2, data2.length));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                setResult(true, "Success", 00, new Gson().toJson(resp), p1000CallBacks);
                Log.d("Dhanu","Respons"+new Gson().toJson(resp));
                //context.updateData(resp);
            } else if (result == PBOCTransactionResult.TERMINATED) {
                Log.d("SHANKY", "PBOC Transaction fail");
                //setResult(false, "PBOC Transaction fail Card Terminate", 404, new Gson().toJson(resp), p1000CallBacks);
                Log.d("Dhanu","Respons"+new Gson().toJson(resp));
                boolean allowRev = true;
                if (!req.getRequestcode().equalsIgnoreCase(Constants.MICRO_BAL_ENQ)) {
                    if (resp.getRespCode() == null) {
                        allowRev = false;
                    } else {
                        if (resp.getRespCode().equalsIgnoreCase("00"))
                            allowRev = true;
                        else
                            allowRev = false;
                    }
                } else {
                    allowRev = false;
                }

                int code = data.getIntExtra("code", 0);
                Log.d("SHANKY", "CODE : " + code);
                String maskedCardNo = "";
                if (cardno == null || cardno.length() < 11) {
                    maskedCardNo = "000000******0000";
                } else {
                    maskedCardNo = cardno.substring(0, 6) + "******" + cardno.substring(cardno.length() - 4);
                }
                //code : -3 : Blocked/Locked Card,  -11 : Declined By Device 9F27, -8 : Timeout case, -2 = Chip remove before second generate TC command

                if (code == 0) {
                    //Success at router but declined by device without remove chip/fail second TC
                    if (allowRev) {
                        resp.setStatus(false);
                        resp.setMsg("Router=Success,Device=Declined");
                    }
                } else if (code == DeviceErrorCodes.CHIP_REMOVE_BEFORE_TC) {
                    //Success at router but declined by device chip remove before second TC 9F27=80
                    //TODO send DE39=E2 in reversal for chip remove for rupay
                    if (allowRev) {
                        if (originalTxStatus) {
                            DE39 = "E2";
                            resp.setStatus(false);
                            resp.setMsg("Reversal Sent");
                        }
                    }
                } else if (code == DeviceErrorCodes.DECLINEDBY_DEVICE) {
                    //Success at router but declined by device chip remove before second TC 9F27=80
                    //TODO send DE39=E1 in reversal for declined by device for rupay
                    if (allowRev) {
                        if (originalTxStatus) {
                            DE39 = "E1";
                            resp.setStatus(false);
                            resp.setMsg("Declined By Device");
                        }
                    }
                } else {
                    if (isTxOL && !resp.isStatus()) {
                        // context.updateData(resp);
                        setResult(false, "Failed", 99, new Gson().toJson(resp), p1000CallBacks);

                    } else {
                        resp.setStatus(false);
                        if (code == DeviceErrorCodes.BLOCKED_CARD)
                            resp.setMsg("Card Is Blocked");
                        else if (code == DeviceErrorCodes.CARD_READ_TIMEOUT)
                            resp.setMsg("Tomeout, trade terminates");

                        else if (code == DeviceErrorCodes.EMV_APP_BLOCKED)
                            resp.setMsg("EMV Application Blocked");
                        else
                            resp.setMsg("Declined By Device(" + code + ")");

                        resp.setRrn(" - ");
                        resp.setRespCode("" + code);
                        resp.setAuthid("  - ");
                        resp.setInvoiceNumber(" - ");
                        resp.setCardno("" + maskedCardNo);
                        resp.setPrintmsg("");
                        resp.setBatchNo(" - ");
                        resp.setDate(formattedDate);
                        resp.setAid("");
                        resp.setTvr("");
                        resp.setTsi("");
                        resp.setApplName("");
                        resp.setCardType("-");
                        resp.setHitachiResCode("" + code);
                        online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                        setResult(false, "Device Declined", 99, new Gson().toJson(resp), p1000CallBacks);
                    }
                }
                //online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
                if (code == DeviceErrorCodes.CHIP_REMOVE_BEFORE_TC || code == DeviceErrorCodes.DECLINEDBY_DEVICE) {
                    if (allowRev) {
                        if (originalTxStatus) {
                            p1000CallBacks.progressCallback("Reversal Please wait...");
                            //  context.freshProcessDialog("Reversal", "Please wait...");
                            callRevRequest(req,"arpc", "data5513", resp, DE39);
                        } else {
                            setResult(false, "Declined By Device : "+code, 99, new Gson().toJson(resp), p1000CallBacks);
                            //  context.updateData(resp);
                        }
                    } else {
                        try {
                            setResult(false, "Declined By Device : "+code, 99, new Gson().toJson(resp), p1000CallBacks);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //  context.updateData(resp);
                    }
                }/* else {
                    try {
                        //context.dismissDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (isTxOL)
                        setResult(false, "Failed", 99, new Gson().toJson(resp), p1000CallBacks);
                }*/
            } else {
                Log.d("SHANKY", "PBOC Transaction fail : " + result);

                int code = data.getIntExtra("code", 0);
                if (code != DeviceErrorCodes.DECLINEDBY_DEVICE) {

                    if (isTxOL) {
                        setResult(false, "Failed", 99, new Gson().toJson(resp), p1000CallBacks);
                    } else {
                        String maskedCardNo = "";
                        if (cardno == null || cardno.length() < 11) {
                            maskedCardNo = "000000******0000";
                        } else {
                            maskedCardNo = cardno.substring(0, 6) + "******" + cardno.substring(cardno.length() - 4);
                        }

                        resp.setStatus(false);
                        resp.setMsg("PBOC FAIL BY : " + result);
                        resp.setRrn(" - ");
                        resp.setRespCode("" + result);
                        resp.setAuthid("  - ");
                        resp.setInvoiceNumber(" - ");
                        resp.setCardno("" + maskedCardNo);
                        resp.setPrintmsg("");
                        resp.setBatchNo(" - ");
                        resp.setDate(formattedDate);
                        resp.setAid("");
                        resp.setTvr("");
                        resp.setTsi("");
                        resp.setApplName("");
                        resp.setCardType("-");
                        resp.setHitachiResCode("" + result);
                        setResult(false, "Failed", 99, new Gson().toJson(resp), p1000CallBacks);
                    }
                } else {
                    DE39 = "E1";
                    p1000CallBacks.progressCallback("Reversal Please wait...");
                    callRevRequest(req,"arpc", "data5513", resp, DE39);
                }
                //online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onReadECBalance(Intent ecBalance) throws RemoteException {
        //online trading the balance,but temporarily didn't use him(在线余额，暂时没有使用)
        Log.d("SHANKY", "PBOC EC balance");
    }

    @Override
    public void onReadCardOfflineRecord(Intent contents) throws RemoteException {
        //offline trading the balance,but temporarily didn't use him(离线余额，暂时没有使用)
        Log.d("SHANKY", "PBOC Transaction record");
    }

    @Override
    public void onRequestSinature() throws RemoteException {
        Log.d("SHANKY", "Request Signature");
       /* context.runOnUiThread(new Runnable() {
            public void run() {
                takeSignature(context.getContext());
            }
        });*/
    }

   /* @Override
    public void onFindSelectAid(String s) throws Exception {
        Log.d("SHANKY", "onFindSelectAid : ");
    }*/

    @Override
    public void onError(Intent result) throws RemoteException {
        //PBOC process to error(流程出错)
        Log.d("SHANKY", "PBOC Error");

        //Faulty Chip Error Code : 65281,65297
        Log.d("SHANKY", "ERROR : " + result.getExtras().toString());
        Log.d("SHANKY", "ERROR Code: " + result.getExtras().get("code").toString());
        // context.dismissDialog();
        if (result.getExtras().get("code").toString().equalsIgnoreCase("" + DeviceErrorCodes.FALLBACK)) {
            isEmvForced = true;
            resp.setStatus(false);
            resp.setMsg("" + getPBOCErrorMsg(result.getExtras().get("code").toString()));
            resp.setRrn(" - ");
            resp.setRespCode("" + result.getExtras().get("code").toString());
            resp.setAuthid(" - ");
            resp.setInvoiceNumber(" - ");
            resp.setCardno(" -- ");
            resp.setPrintmsg("");
            resp.setBatchNo(" - ");
            resp.setDate(formattedDate);
            resp.setAid("");
            resp.setTvr("");
            resp.setTsi("");
            resp.setApplName("");
            resp.setCardType("-");
            resp.setHitachiResCode("" + result.getExtras().get("code").toString());
            online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
            //ba.updateData(resp);
        } else {
            String resCode = result.getExtras().get("code").toString();
            resp.setStatus(false);
            resp.setMsg("" + getPBOCErrorMsg(result.getExtras().get("code").toString()));
            resp.setRrn(" - ");
            resp.setRespCode("" + resCode);
            resp.setAuthid(" - ");
            resp.setInvoiceNumber(" - ");
            resp.setCardno(" -- ");
            resp.setPrintmsg("");
            resp.setBatchNo(" - ");
            resp.setDate(formattedDate);
            resp.setAid("");
            resp.setTvr("");
            resp.setTsi("");
            resp.setApplName("");
            resp.setCardType("-");
            resp.setHitachiResCode("" + resCode);
            online_transaction.sendEmptyMessage(ONLINE_PROCESS_FINISH);
        }
    }

    public static String concatEMVTag(String TAG_NAME, String TAG_VALUE) {
        try {
            return (TAG_NAME + String.format("%2s", Integer.toHexString(TAG_VALUE.replaceAll(" ", "").length() / 2)).replace(' ', '0') + TAG_VALUE.replaceAll(" ", "")).toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }


    public void takeSignature(final Context context) {
        // Dialog Function
        final Dialog dialog = new Dialog(context);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.digitalsign);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final SignatureView mSignature = new SignatureView(context);
        LinearLayout mContent = (LinearLayout) dialog.findViewById(R.id.sign_layout);
        // Dynamically generating Layout through java code
        mContent.addView(mSignature, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button mClear = (Button) dialog.findViewById(R.id.clear);
        final Button mGetSign = (Button) dialog.findViewById(R.id.getsign);
        TextView txttitle = (TextView) dialog.findViewById(R.id.txttitle);

        mGetSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticValues.setCustSign(mSignature.getSignature());
                dialog.dismiss();
                try {
                    //   ServiceManager.getInstence().getPboc().comfirmSinature();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSignature.clearSignature();
            }
        });
        dialog.show();
    }

    public void callRevRequest(RequestParams req, String arpc, String data5513, final ResponseParams resp, String DE39) {
        String ops = "" + DE39;

        if (this.req.getRequestcode().equalsIgnoreCase(Constants.MICRO_CASH_WITHDRAW_SBM))
            if (ops.equalsIgnoreCase("E2"))
                ops = "21";
        Log.d("OPS", "" + ops);
        RequestParams r = new RequestParams();
        if (this.req.getRequestcode().equalsIgnoreCase(Constants.MICRO_CASH_WITHDRAW_SBM))
            r.setRequestcode(Constants.wsReversalMatmSbm);
        else
            r.setRequestcode(Constants.wsReversal);

        r.setImei("-");
        r.setImsi("-");
        r.setUsername(req.getUsername());
        r.setMid(req.getMerchantId());
        r.setMerchantId(req.getMerchantId());
        r.setAmt("" + req.getAmt());
        r.setInvoiceNo("" + resp.getInvoiceNo());
        r.setRrn("" + resp.getRrn());
        r.setCarddata(this.req.getCarddata());
        if (this.req.getRequestcode().equalsIgnoreCase(Constants.MICRO_CASH_WITHDRAW_SBM)) {
            if (isIssuerScriptPresent)
                r.setTag55(this.req.getTag55() + concatEMVTag("9F5B", "2030303030"));
            else
                r.setTag55(this.req.getTag55());
        } else
            r.setTag55(this.req.getTag55() + concatEMVTag("9F5B", SCRIPTRESULT));
        r.setOp("" + ops);

        NetworkController.getInstance().sendRequest(r, new ResponseListener() {
            @Override
            public void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d) {
                if (response.body() != null) {
                    Log.d("Status Msg", "" + response.body().getMsg());
                    if (response.body().getStatus() == true) {
                        if (d != null)
                            d.cancel();

                        resp.setStatus(false);
                        resp.setMsg("Reversal Done");
                    } else {
                        if (d != null)
                            d.cancel();
                        resp.setMsg("Reversal Fail");
                    }
                } else {
                    if (d != null)
                        d.cancel();
                    resp.setMsg("Reversal Fail");
                }
                try {
                    setResult(false, "Reversal", 100, (new Gson().toJson(resp)), p1000CallBacks);
                } catch (Exception e) {
                }
                // setResult(true, "Reversal", 100, (new Gson().toJson(resp)), p1000CallBacks);
            }

            @Override
            public void onResponseFailure(Throwable throwable, SuccessCustomDialog d) {
                p1000CallBacks.failureCallback(getFailJSON("Transaction Declined By Device - Error while sending reversal", 404));
            }
        });
    }


    public static void main(String arg[]) {
        System.out.println(concatEMVTag("91", "34567891212345678912"));
    }

    public static String getPBOCErrorMsg(String code) {

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("-8", "READ TIMED OUT");
        hashMap.put("-5", "EMV APP BLOCKED");
        hashMap.put("-3", "BLOCKED CARD");
        hashMap.put("143", "OTHER_ERROR");
        hashMap.put("768", "OFFSET");
        hashMap.put("769", "PBOC_SERVICE_IS_OCCUPIED");
        hashMap.put("770", "NOT_REQUEST_INPUT_PIN");
        hashMap.put("771", "NOT_REQUEST_SELECT_APP");
        hashMap.put("772", "NOT_REQUEST_CONFIRM_CARD_INFO");
        hashMap.put("773", "PBOC_SERVICE_IS_NOT_ABORTING");
        hashMap.put("774", "PBOC_SERVICE_IS_IN_QPBOC");
        hashMap.put("775", "PBOC_HAS_NOT_START");
        hashMap.put("65280", "EMV_RESULT_OFFSET");
        hashMap.put("65281", "EMV_FALLBACK");
        hashMap.put("65282", "EMV_ERROR");
        hashMap.put("65283", "EMV_DATA_AUTH_FAIL");
        hashMap.put("65284", "EMV_APP_BLOCKED");
        hashMap.put("65285", "EMV_NOT_ECCARD");
        hashMap.put("65286", "EMV_UNSUPPORT_ECCARD");
        hashMap.put("65287", "EMV_AMOUNT_EXCEED_ON_PURELYEC");
        hashMap.put("65288", "EMV_SET_PARAM_ERROR");
        hashMap.put("65289", "EMV_PAN_NOT_MATCH_TRACK2");
        hashMap.put("65290", "EMV_CARD_HOLDER_VALIDATE_ERROR");
        hashMap.put("65291", "EMV_PURELYEC_REJECT");
        hashMap.put("65292", "EMV_BALANCE_INSUFFICIENT");
        hashMap.put("65293", "EMV_AMOUNT_EXCEED_ON_RFLIMIT_CHECK");
        hashMap.put("65294", "EMV_CARD_BIN_CHECK_FAIL");
        hashMap.put("65295", "EMV_CARD_BLOCKED");
        hashMap.put("65296", "EMV_MULTI_CARD_ERROR");
        hashMap.put("65297", "EMV_CANCEL");
        hashMap.put("65298", "EMV_AMOUNT_EXCEED_ON_ECLIMIT_CHECK");

        try {
            return hashMap.get(code);
        } catch (Exception e) {
            return "UNKNOWN(" + code + ")";
        }
    }



    private void setResult(boolean status, String message, int responseCode, String responseJson, P1000CallBacks p1000CallBacks) {
        try {
            if (p1000CallBacks != null) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Msg", message);
                jsonObject.put("ResponseCode", responseCode);
                jsonObject.put("Response", responseJson);
                if (status) {
                    p1000CallBacks.successCallback(jsonObject);
                    p1000CallBacks = null;
                } else {
                    p1000CallBacks.failureCallback(jsonObject);
                    p1000CallBacks = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private JSONObject getFailJSON(String msg, int responseCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("Msg", msg);
            jsonObject.put("ResponseCode", responseCode);
            jsonObject.put("Response", msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
