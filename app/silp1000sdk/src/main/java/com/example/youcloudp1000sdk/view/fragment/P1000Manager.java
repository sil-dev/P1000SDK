package com.example.youcloudp1000sdk.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.basewin.database.DataBaseManager;
import com.basewin.define.InputPBOCInitData;
import com.basewin.define.PBOCOption;
import com.basewin.log.LogUtil;
import com.basewin.services.DeviceInfoBinder;
import com.basewin.services.PBOCBinder;
import com.basewin.services.ServiceManager;
import com.basewin.utils.BytesUtil;
import com.basewin.utils.LoadParamManage;
import com.example.youcloudp1000sdk.P1000CallBacks;
import com.example.youcloudp1000sdk.StatusCallBack;
import com.example.youcloudp1000sdk.TransactionCallback;
import com.example.youcloudp1000sdk.custom_view.ProcessDialog;
import com.example.youcloudp1000sdk.custom_view.SuccessCustomDialog;
import com.example.youcloudp1000sdk.model.P1000Request;
import com.example.youcloudp1000sdk.retrofit.NetworkController;
import com.example.youcloudp1000sdk.retrofit.RequestParams;
import com.example.youcloudp1000sdk.retrofit.ResponseListener;
import com.example.youcloudp1000sdk.retrofit.ResponseParams;
import com.example.youcloudp1000sdk.utils.AndyUtility;
import com.example.youcloudp1000sdk.utils.Constants;
import com.example.youcloudp1000sdk.utils.Session;
import com.example.youcloudp1000sdk.utils.SessionConstants;
import com.example.youcloudp1000sdk.utils.StaticValues;
import com.example.youcloudp1000sdk.y2000.constants.GlobalData;
import com.example.youcloudp1000sdk.y2000.constants.MyCUPParam;
import com.example.youcloudp1000sdk.y2000.constants.PinpadInterfaceVersion;
import com.example.youcloudp1000sdk.y2000.constants.onlinePBOCListener;
import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.pos.sdk.emvcore.PosEmvCoreManager;
import com.pos.sdk.emvcore.PosEmvParam;
import com.pos.sdk.emvcore.PosTermInfo;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;


/**
 * Created by shankar.savant on 6/9/2017.
 */

public class P1000Manager implements TransactionCallback {

    // View view;
    Button btnLogin, btnLoadMainKeys, btnLoadWorkKeys;
    private String from = "", getAmt = "1", getRemark = "";
    String tipamt = "0", currencyname = "", currencycode = "", op = "", status = "", cash_back_amt = "",
            cvm_tag = "", loc_flag = "", modeofPay = "", recharge_type = "", mobile_no = "",
            operator_name = "", operator_code = "", tvr = "", tsi = "", aid = "";
    RequestParams req = new RequestParams();
    String latLang = "";

    private int area = 1;
    private int tmkindex = 1;

    int cnt = 0;
    boolean isReady = false, isLocAvail = false;

    String respCode = "";

    //SIL Keys
    private String defProtectKey = "11111111111111111111111111111111";
    private String defMainKey = "E5B6AC8CB1147317EDBB065494F20BED";
    private String defMainKeyKcv = "163AC02F";//163AC02F
    private String defMacKey = "1F690B570A32F8A05AE7C3AE4D5F437D";
    private String defMacKeyKcv = null;//91CD3A3A
    private String defPinKey = "1F690B570A32F8A05AE7C3AE4D5F437D";
    private String defPinKeyKcv = null;
    private String defTDKey = "1F690B570A32F8A05AE7C3AE4D5F437D";
    private String defTDKeyKcv = null;
    private static P1000Request P1000Request = null;
    public static byte[] plainTrackKey;

    //Maximus keys
     /*private String defProtectKey = "11111111111111111111111111111111";
     private String defMainKey = "58F79846627581D602BBC8E2DCE333C3";
     private String defMainKeyKcv = "5E22CB27";//163AC02F
     private String defMacKey = "AFEBF49CA262712933F4DAB70C093B9C";
     private String defMacKeyKcv = null;//91CD3A3A
     private String defPinKey = "AFEBF49CA262712933F4DAB70C093B9C";
     private String defPinKeyKcv = null;
     private String defTDKey = "AFEBF49CA262712933F4DAB70C093B9C";
     private String defTDKeyKcv = null;*/

    private String validateId = "";
    String fName;
    Context getActivity;
    protected ProcessDialog processdialog = null;
    Button btnStartLogs;
    P1000CallBacks p1000CallBacks;
    private static P1000Manager ourInstance;
    private static String ucubeKey = null;
    private static boolean KEYSLOADED = false;
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        view = inflater.inflate(R.layout.you_activity_main, container, false);
        initialize(view);
        return null;
    }
*/

    public String getTransactionId() {
        return AndyUtility.getRRN((getISOField11(6)));
    }

    public static String getISOField11(int length) {
        // field 11 == stan
        String result = "";
        int random;
        while (true) {
            random = (int) ((Math.random() * (10)));
            if (result.length() == 0 && random == 0) {
                random += 1;
                result += random;
            } else if (!result.contains(Integer.toString(random))) {
                result += Integer.toString(random);
            }
            if (result.length() >= length) {
                break;
            }
        }
        return result;
    }


    private void connectDevice(P1000Request P1000Request, P1000CallBacks p1000CallBacks) {

        String isMainKeyInjected = Session.getString(getActivity, SessionConstants.isMainKeyInjected);


        if (!isMainKeyInjected.equalsIgnoreCase("T")) {
            boolean a = login();
            if (a) {
                getMainKeys(P1000Request, p1000CallBacks);
            } else {
                p1000CallBacks.failureCallback(getFailJSON("Error Login Fail", 101));
            }
        } else {
            loadPinKey(P1000Request, p1000CallBacks);
        }
    }

    private P1000Manager(Context context) {
        this.getActivity = context;
    }

    public static P1000Manager getInstance(Context mainActivity, String key) {

      if (ourInstance == null) {
            ourInstance = new P1000Manager(mainActivity);
            ucubeKey = key;

            try {
                //Stetho initializer
                Stetho.initializeWithDefaults(mainActivity);

                /**
                 * init device server
                 */
                ServiceManager.getInstence().init(mainActivity);

                /**
                 * init database
                 */

                DataBaseManager.getInstance().init(mainActivity);

                /**
                 *For SDK Log OPEN
                 */
                GlobalData.getInstance().init(mainActivity);
                LogUtil.openLog();

                StaticValues.setIsY2000(true);
                Log.d("SHANKY", "INIT Y2000 SUCCESS");
             //   Toast.makeText(mainActivity, "Success init", Toast.LENGTH_SHORT).show();

                DeviceInfoBinder deviceInfoBinder = ServiceManager.getInstence().getDeviceinfo();
                if (deviceInfoBinder != null) {
                    StaticValues.setDeviceType("" + deviceInfoBinder.getDeviceType());
                    //P2000L, P1000,P500
                    Log.d("SHANKY", "DEV Type : " + deviceInfoBinder.getDeviceType());

                 //   Toast.makeText(mainActivity, "Device Type" + deviceInfoBinder.getDeviceType(), Toast.LENGTH_SHORT).show();

                }
            } catch (LinkageError e) {
                Log.d("SHANKY", "LinkageError "+e.getMessage());
                StaticValues.setIsY2000(false);
                e.printStackTrace();
            } catch (Exception e) {
                Log.d("SHANKY", "Exception");
                StaticValues.setIsY2000(false);
                e.printStackTrace();
            }
        }

        if (ourInstance != null) {
            Toast.makeText(mainActivity, "Instance Created Successfully.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mainActivity, "Instance Created Failed.", Toast.LENGTH_SHORT).show();
        }
        return ourInstance;
    }


    public void getMainKeys(P1000Request p1000Request, P1000CallBacks p1000CallBacks) {

        //Req  : {"requestcode":"doLogin","username":"shankar","password":"1111","imei":"352672078500352","imsi":"405874001163008","verCode":"1","verName":"1.0.10"}

        RequestParams requestParams = new RequestParams();
        requestParams.setRequestcode(Constants.wsgetMainKey);
        requestParams.setUsername(p1000Request.getUsername());
        requestParams.setPassword(p1000Request.getPassword());
        requestParams.setImei("-");
        requestParams.setImsi("-");
        requestParams.setSrno(p1000Request.getTid());
        requestParams.setMid(p1000Request.getMid());


        //isMainKeyLoaded
        NetworkController.getInstance().sendRequest(requestParams, new ResponseListener() {
            @Override
            public void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d) {
                d.cancel();
                if (response.body() != null) {
                    if (response.body().getMkskKeys() != null) {
                        if (response.body().isStatus()) {
                            if (response.body().getMkskKeys().contains("#")) {
                                String[] keys = response.body().getMkskKeys().split("#");
                                if (keys.length < 3) {
                                    Toast.makeText(getActivity, "Key Size is small : " + keys.length + " Keys : " + keys.toString(), Toast.LENGTH_LONG).show();
                                } else {
                                    defProtectKey = keys[0];
                                    defMainKey = keys[1];
                                    defMainKeyKcv = keys[2];

                                    loadProtectKey(p1000Request, p1000CallBacks);
                                    Log.d("SHANKY", "KEYS : Prot : " + defProtectKey + " Main : " + defMainKey + " KCV : " + defMainKeyKcv);
                                }
                            } else {
                                Log.d("SHANKY", "KEYS : " + response.body().getMkskKeys());
                            }
                        } else {
                            setResult(response.body().isStatus(), "FAILED", response.code(), response.message(), p1000CallBacks);

                            //  FailCustomDialog failCustomDialog = new FailCustomDialog(getActivity, "Fail", "" + response.body().getMsg(), "");
                            //  failCustomDialog.show();
                        }
                    } else {
                        setResult(response.body().isStatus(), "FAILED", response.code(), response.message(), p1000CallBacks);

                        //  FailCustomDialog failCustomDialog = new FailCustomDialog(getActivity, "Fail", "Key Is NULL", "");
                        // failCustomDialog.show();
                    }
                } else {
                    setResult(response.body().isStatus(), "FAILED", 99, "Check Internet Connection:Null Responce", p1000CallBacks);

                    // FailCustomDialog failCustomDialog = new FailCustomDialog(getActivity, "Fail", "Check Internet Connection:Null Responce", "");
                    //  failCustomDialog.show();
                }
            }

            @Override
            public void onResponseFailure(Throwable throwable, SuccessCustomDialog d) {
                Log.e("", throwable.toString());
                d.cancel();
                setResult(false, "FAILED", 99, "Fail ,Check Internet Connection", p1000CallBacks);
        }

        }, new SuccessCustomDialog(getActivity, "Loading Key", "Please Wait", ""));
    }

    public boolean login() {
        try {
            boolean bRet = false;
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
            return bRet;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void execute(P1000Request P1000Request, P1000CallBacks p1000CallBacks) {
        if (ucubeKey != null && !ucubeKey.isEmpty() && !ucubeKey.trim().isEmpty()) {
            if (getActivity != null) {
                if (P1000Request != null && P1000Request.isValidRequest()) {
                    this.P1000Request = P1000Request;
                    validateId = getActivity.getPackageName();
                    P1000Request.setRefCompany(validateId);
                    if (validateId != null) {
                        validePackage(P1000Request, p1000CallBacks, null, false);
                    } else {
                        if (p1000CallBacks != null) {
                            p1000CallBacks.failureCallback(getFailJSON(Constants.PACKAGE_NAME_ERROR, Constants.PACKAGE_NAME_ERROR_CODE));
                            p1000CallBacks = null;
                        }
                    }
                } else {
                    if (P1000Request != null) {
                        if (p1000CallBacks != null) {
                            p1000CallBacks.failureCallback(getFailJSON(P1000Request.getErroMsg(), 100));
                            p1000CallBacks = null;
                        }
                    } else {
                        if (p1000CallBacks != null) {
                            p1000CallBacks.failureCallback(getFailJSON(Constants.REQUEST_ERROR, Constants.REQUEST_ERROR_CODE));
                            p1000CallBacks = null;
                        }
                    }
                }
            } else {
                if (p1000CallBacks != null) {
                    p1000CallBacks.failureCallback(getFailJSON(Constants.CONTEXT_ERROR_MSG, Constants.CONTEXT_ERROR_CODE));
                    p1000CallBacks = null;
                }
            }
        } else {
            if (p1000CallBacks != null) {
                p1000CallBacks.failureCallback(getFailJSON(Constants.KEY_ERROR, Constants.KEY_ERROR_CODE));
                p1000CallBacks = null;
            }
        }

    }


    private void validePackage(final P1000Request P1000Request, final P1000CallBacks p1000CallBacks, final StatusCallBack statusCallBack, final boolean isStatus) {
        //TODO make a network call if success the move forward else give failure callback
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.setRequestcode(Constants.VALIDATE_REQUEST_CODE);
            requestParams.setImei("-");
            requestParams.setImsi("-");
            requestParams.setUsername(P1000Request.getUsername());
            requestParams.setCompanyid(validateId);
            requestParams.setKey(ucubeKey);
            requestParams.setSrno(P1000Request.getTid());
            requestParams.setMid(P1000Request.getMid());
            if (P1000Request.getSession_Id() != null && !P1000Request.getSession_Id().isEmpty()) {
                requestParams.setSessionId(P1000Request.getSession_Id());
            }
            NetworkController.getInstance().sendRequest(requestParams, new ResponseListener() {
                @Override
                public void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            if (isStatus) {
                                checkTrasctionStatus(P1000Request, statusCallBack);
                            } else {
                                defPinKey = response.body().getMkskKeys();
                                defTDKey = response.body().getMkskKeys();
                                defMacKey = response.body().getMkskKeys();

                                Log.d("SHANKY", "defPinKey : " + defPinKey + " defTDKey : " + defTDKey);
                                connectDevice(P1000Request, p1000CallBacks);
                            }
                        } else {
                            if (isStatus) {
                                if (statusCallBack != null) {
                                    if (response.body().getMsg() != null && !response.body().getMsg().isEmpty()) {
                                        statusCallBack.failureCallback(getFailJSON(response.body().getMsg(), 100));
                                    } else {
                                        statusCallBack.failureCallback(getFailJSON(Constants.PACKAGE_INVALID, Constants.PACKAGE_INVALID_CODE));
                                    }
                                }
                            } else {
                                if (p1000CallBacks != null) {
                                    if (response.body().getMsg() != null && !response.body().getMsg().isEmpty()) {
                                        p1000CallBacks.failureCallback(getFailJSON(response.body().getMsg(), 100));
                                    } else {
                                        p1000CallBacks.failureCallback(getFailJSON(Constants.PACKAGE_INVALID, Constants.PACKAGE_INVALID_CODE));
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onResponseFailure(Throwable throwable, SuccessCustomDialog d) {
                    if (p1000CallBacks != null) {
                        throwable.printStackTrace();
                        p1000CallBacks.failureCallback(getFailJSON(Constants.NETWORK_EXCEPTION, Constants.NETWORK_EXCEPTION_CODE));
                    }
                }
            });
        } catch (Exception e) {
            Log.d("TAG", "validePackage: " + e.toString());
            e.printStackTrace();
        }
    }


    public void loadProtectKey(P1000Request p1000Request, P1000CallBacks p1000CallBacks) {
        //TODO clear all keys first
        p1000CallBacks.progressCallback("Loading Protected Key");
        try {
            ServiceManager.getInstence().getPinpad().format();
            Log.d("SHANKY", "FORMAT SUCCESS");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String protect = defProtectKey;
        if (TextUtils.isEmpty(protect)) {
            Log.d("SHANKY", "protect key  is  null  ！");
            return;
        }
        try {
            boolean iRet = ServiceManager.getInstence().getPinpad().loadProtectKeyByArea(area, protect);
            if (iRet) {
                GlobalData.getInstance().setPinpadVersion(PinpadInterfaceVersion.PINPAD_INTERFACE_VERSION3);
                GlobalData.getInstance().setArea(area);
                p1000CallBacks.progressCallback("Loading Protected Key Success");
                Log.d("SHANKY", "LOAD Protect key success");
                loadMainKey(p1000Request, p1000CallBacks);
            } else {
                Log.d("SHANKY ", "loadProtectKey: 0");
                Toast.makeText(getActivity, "Load Procted Key Fail", Toast.LENGTH_LONG).show();
                Log.d("SHANKY ", "loadProtectKey: 2");
            }
        } catch (Exception e) {
            Toast.makeText(getActivity, "Load Procted Key Exception", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public void loadMainKey(P1000Request p1000Request, P1000CallBacks p1000CallBacks) {
        try {

            p1000CallBacks.progressCallback("Loading Main Key");
            //Demo main key
            String main = defMainKey;
            String mainKcv = defMainKeyKcv;
            if (TextUtils.isEmpty(main)) {
                Log.d("SHANKY", "main key  is  null  ！");
                return;
            }
            boolean iRet = false;
            if (TextUtils.isEmpty(mainKcv)) {
                iRet = ServiceManager.getInstence().getPinpad().loadMainKeyByArea(area, tmkindex, main);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadMainKeyWithKcvByArea(area, tmkindex, main, mainKcv);
            }
            if (iRet) {
                KEYSLOADED = true;
                //Toast.makeText(getActivity(), "load Main Key Success!");
                GlobalData.getInstance().setTmkId(tmkindex);

                Session.setString(getActivity, SessionConstants.isMainKeyInjected, "T");

                Log.d("SHANKY", "Load Main Key success");

                connectDevice(p1000Request, p1000CallBacks);

            } else {
                setResult(false, "FAILED", 101, "unable to load main key", p1000CallBacks);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity, "Load Main Key Exception", Toast.LENGTH_LONG).show();
        }
    }

    public void loadMacKey(P1000Request p1000Request, P1000CallBacks p1000CallBacks) {
        try {
            //Demo main key
            String mac = defMacKey;
            String macKcv = defMacKeyKcv;
            if (TextUtils.isEmpty(mac)) {
                Log.d("SHANKY", "mac key  is  null  ！");
                return;
            }
            boolean iRet = false;

            //Set The algorithm
            GlobalData.getInstance().setPinpadVersion(PinpadInterfaceVersion.PINPAD_INTERFACE_VERSION3);
            GlobalData.getInstance().setArea(area);

            if (TextUtils.isEmpty(macKcv)) {
                iRet = ServiceManager.getInstence().getPinpad().loadMacKeyByArea(area, tmkindex, mac, null);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadMacKeyByArea(area, tmkindex, mac, macKcv);
            }

            if (iRet) {
                Toast.makeText(getActivity, "Load Key Success", Toast.LENGTH_LONG).show();
                startPayment(p1000Request, p1000CallBacks, "ALL");
            } else {
                Toast.makeText(getActivity, "Load MAC Key Fail", Toast.LENGTH_LONG).show();
                //Toast.makeText(getActivity, "error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity, "Load MAC Key Exception", Toast.LENGTH_LONG).show();
        }
    }

    public void loadPinKey(P1000Request p1000Request, P1000CallBacks p1000CallBacks) {
        try {
            //Demo main key
            String pin = defPinKey;
            String pin_kcv = defPinKeyKcv;
            if (TextUtils.isEmpty(pin)) {
                Log.d("SHANKY", "pin key  is  null  ！");
                Toast.makeText(getActivity, "pin key  is  null  ！", Toast.LENGTH_LONG).show();
                return;
            }
            boolean iRet = false;
            if (TextUtils.isEmpty(pin_kcv)) {
                Log.d("SHANKY", "PIN KEY : " + pin);
                iRet = ServiceManager.getInstence().getPinpad().loadPinKeyByArea(area, tmkindex, pin, null);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadPinKeyByArea(area, tmkindex, pin, pin_kcv);
            }

            if (iRet) {
                GlobalData.getInstance().setPinkeyFlag(true);
                loadTDKey(p1000Request, p1000CallBacks);

            } else {
                Toast.makeText(getActivity, "Load PIN Key Fail", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SHANKY", "loadPinKey error !");
            Toast.makeText(getActivity, "Load PIN Key Exception", Toast.LENGTH_LONG).show();
        }
    }

    public void loadTDKey(P1000Request p1000Request, P1000CallBacks p1000CallBacks) {
        try {
            String tdkey = defTDKey;
            String td_kcv = defTDKeyKcv;
            if (TextUtils.isEmpty(tdkey)) {
                Log.d("SHANKY", "td key  is  null  ！");
                return;
            }
            boolean iRet = false;
            if (TextUtils.isEmpty(td_kcv)) {
                Log.d("SHANKY", "TD KEY : " + tdkey);
                iRet = ServiceManager.getInstence().getPinpad().loadTDKeyByArea(area, tmkindex, tdkey, null);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadTDKeyByArea(area, tmkindex, tdkey, td_kcv);
            }

            if (iRet) {
              if (iRet) {
                    loadMacKey(p1000Request, p1000CallBacks);
                } else {
                    p1000CallBacks.failureCallback(getFailJSON("Unable to load TD key", 100));
                }

            } else {
                  Toast.makeText(getActivity, "load td Key error", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("SHANKY", "loadTDKey error !");
        }
    }


    private JSONObject getFailJSON(String msg, int responseCode) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg", msg);
            jsonObject.put("respCode", "AD"+responseCode);
            jsonObject.put("status", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    private void startPayment(P1000Request p1000Request, P1000CallBacks p1000CallBacks, String flag) {
        try {
            String msg = "Swipe/Insert Card...";
            Double d = Double.parseDouble(p1000Request.getTxn_amount()) * 100;
            Integer i = d.intValue();
            Log.d("SHANKY", "Start Tx\n AMT : " + i);
            Intent in = new Intent();
            in.putExtra(InputPBOCInitData.AMOUNT_FLAG, i);
            PBOCBinder binder = ServiceManager.getInstence().getPboc();
            PosEmvParam posEmvParam = new PosEmvParam();
            PosEmvCoreManager mPosEmv = PosEmvCoreManager.getDefault();
            if (flag.equalsIgnoreCase("MS")) {
                msg = "Swipe Card...";
                in.putExtra(InputPBOCInitData.USE_DEVICE_FLAG, InputPBOCInitData.USE_MAG_CARD);
            } else if (flag.equalsIgnoreCase("EMV")) {
                msg = "Insert Card...";
                in.putExtra(InputPBOCInitData.USE_DEVICE_FLAG, InputPBOCInitData.USE_IC_CARD);
                in.putExtra(InputPBOCInitData.IS_SUPPERT_EC_FLAG, false);
                in.putExtra(InputPBOCInitData.IS_QPBOC_FORCE_ONLINE, false);
                posEmvParam.TransCurrCode = BytesUtil.hexString2Bytes("0356");
                posEmvParam.CountryCode = BytesUtil.hexString2Bytes("0356");
                //terminal Capability for offline auth TC1A
                posEmvParam.ExCapability[3] |= 0x40;
                //posEmvParam.Capability[1] |= 0x20;
                posEmvParam.Capability[0] = (byte) 0x60;
                posEmvParam.Capability[1] = (byte) 0xe8;  // TODO plain OFF (byte 2 bit 8)
                posEmvParam.Capability[2] = (byte) 0xc8;
                posEmvParam.TerminalType = 0x22;
                ///
                mPosEmv.EmvSetTermPara(posEmvParam);
                binder.setPosTermPara(posEmvParam);
            } else {
                msg = "Swipe/Insert Card...";
                in.putExtra(InputPBOCInitData.USE_DEVICE_FLAG, InputPBOCInitData.USE_MAG_CARD | InputPBOCInitData.USE_IC_CARD);
                posEmvParam.TransCurrCode = BytesUtil.hexString2Bytes("0356");
                posEmvParam.CountryCode = BytesUtil.hexString2Bytes("0356");
                in.putExtra(InputPBOCInitData.IS_SUPPERT_EC_FLAG, false);
                in.putExtra(InputPBOCInitData.IS_QPBOC_FORCE_ONLINE, false);

                Log.d("SHANKY", "CAPABILITY : " + BytesUtil.bytes2HexString(posEmvParam.Capability));
                //terminal Capability for offline auth TC1A
                posEmvParam.ExCapability[3] |= 0x40;
                posEmvParam.Capability[0] = (byte) 0x60;
                posEmvParam.Capability[1] = (byte) 0xe8;  // TODO plain OFF (byte 2 bit 8)
                posEmvParam.Capability[2] = (byte) 0xc8;
                posEmvParam.TerminalType = 0x22;
                Log.d("SHANKY", "CAPABILITY : " + BytesUtil.bytes2HexString(posEmvParam.Capability));
                mPosEmv.EmvSetTermPara(posEmvParam);
                binder.setPosTermPara(posEmvParam);
            }

            PosTermInfo info = PosEmvCoreManager.getDefault().EmvGetTermInfo();
            info.bForcedOnline = 0;
            mPosEmv.EmvSetTermInfo(info);

            in.putExtra(InputPBOCInitData.TIMEOUT, 60);
            p1000CallBacks.progressCallback(msg);

            int txType = PBOCOption.ONLINE_PAY;
            if (p1000Request.getRequestCode().getCode().equalsIgnoreCase(Constants.SDK_ENQUIRY_SBM)) {
                txType = PBOCOption.ONLINE_INQUIRY;
                Log.d("SHANKY", "TX_TYPE : ONLINE_INQUIRY");
            } else if (p1000Request.getRequestCode().getCode().equalsIgnoreCase(Constants.MICRO_MINI_STMT)) {
                Log.d("SHANKY", "TX_TYPE : MICRO_MINI_STMT");
                txType = PBOCOption.ONLINE_INQUIRY;
            } else if (p1000Request.getRequestCode().getCode().equalsIgnoreCase(Constants.SDK_WITHDRAW_SBM)) {
                Log.d("SHANKY", "TX_TYPE : MICRO_CASH_WITHDRAW");
                txType = PBOCOption.FUN_CASH;
            } else {
                Log.d("SHANKY", "TX_TYPE : ONLINE_PAY");
                txType = PBOCOption.ONLINE_PAY;
            }
            //TODO added for CVMR result issue
            req = new RequestParams(p1000Request);
            binder.startTransfer(txType, in, new onlinePBOCListener(getActivity, p1000Request.getTxn_amount(), req, p1000CallBacks, this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setResult(boolean status, String message, int responseCode, String responseJson, P1000CallBacks p1000CallBacks) {
        try {
            if (p1000CallBacks != null) {
                JSONObject jsonObject = new JSONObject();
                
                 jsonObject.put("msg", message);
                jsonObject.put("respCode", responseCode);
                jsonObject.put("status", status);
                
               // jsonObject.put("Msg", message);
                //jsonObject.put("ResponseCode", responseCode);
                //jsonObject.put("Response", responseJson);
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



    private void checkTrasctionStatus(final P1000Request p1000Request, final StatusCallBack statusCallBack) {
        try {
            RequestParams requestParams = new RequestParams();
            requestParams.setRequestcode(Constants.TRANSACTION_STATUS);
            if (p1000Request.getRequestCode().getCode() == Constants.DEBIT) {
                requestParams.setOp(Constants.TRANSACTION_SALE);
            } else if (p1000Request.getRequestCode().getCode() == Constants.SDK_WITHDRAW_SBM) {
                requestParams.setOp(Constants.TRANSACTION_WITHDRAW);
            } else {
                statusCallBack.failureCallback(getFailJSON("Transaction Type Not found", Constants.TRANSACTION_ID_MISSING_CODE));
                return;
            }
            requestParams.setImei("-");
            requestParams.setImsi("-");
            requestParams.setUsername(p1000Request.getUsername());
            requestParams.setAmt(p1000Request.getTxn_amount());
            requestParams.setTipamt("0");
            requestParams.setRemark(p1000Request.getRemark());
            requestParams.setMid(p1000Request.getMid());
            requestParams.setSrno(p1000Request.getTid());
            requestParams.setPassword(p1000Request.getPassword());
            requestParams.setCompanyid(validateId);
            requestParams.setKey(ucubeKey);
            if (p1000Request.getSession_Id() != null && !p1000Request.getSession_Id().isEmpty()) {
                requestParams.setSessionId(p1000Request.getSession_Id());
            }
            if (p1000Request.getTransactionId() == null || p1000Request.getTransactionId().isEmpty()) {
                statusCallBack.failureCallback(getFailJSON("Kindly provide the Transaction Id", Constants.TRANSACTION_ID_MISSING_CODE));
            } else {
                requestParams.setRrn(p1000Request.getTransactionId());
            }
            NetworkController.getInstance().sendRequest(requestParams, new ResponseListener() {
                @Override
                public void onResponseSuccess(Response<ResponseParams> response, SuccessCustomDialog d) {
                    if (response.body() != null) {
                        if (response.body().getStatus()) {
                            try {
                                statusCallBack.successCallback(new JSONObject(new Gson().toJson(response.body())));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                statusCallBack.failureCallback(getFailJSON(Constants.INITIATE_TRANSACTION_ERROR, Constants.UNKOWN_ERROR));
                            }
                        } else {
                            try {
                                statusCallBack.failureCallback(new JSONObject(new Gson().toJson(response.body())));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                statusCallBack.failureCallback(getFailJSON(Constants.INITIATE_TRANSACTION_ERROR, Constants.UNKOWN_ERROR));
                            }
                        }
                    }
                }

                @Override
                public void onResponseFailure(Throwable throwable, SuccessCustomDialog d) {
                    if (statusCallBack != null) {
                        throwable.printStackTrace();
                        statusCallBack.failureCallback(getFailJSON(Constants.NETWORK_EXCEPTION, Constants.NETWORK_EXCEPTION_CODE));
                    }
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
            statusCallBack.failureCallback(getFailJSON(Constants.INITIATE_TRANSACTION_ERROR, Constants.UNKOWN_ERROR));
        }
    }

    @Override
    public void chipFallback(P1000CallBacks p1000CallBacks, String type) {
        startPayment(P1000Request, p1000CallBacks, type);
    }
}
