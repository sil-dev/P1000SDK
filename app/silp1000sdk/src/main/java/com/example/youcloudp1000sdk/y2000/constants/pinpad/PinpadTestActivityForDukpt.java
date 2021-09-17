package com.example.youcloudp1000sdk.y2000.constants.pinpad;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.basewin.define.BwPinpadSource;
import com.basewin.define.KeyType;
import com.basewin.services.ServiceManager;
import com.example.youcloudp1000sdk.R;
import com.example.youcloudp1000sdk.y2000.constants.GlobalData;
import com.example.youcloudp1000sdk.y2000.constants.PinpadInterfaceVersion;


public class PinpadTestActivityForDukpt extends Activity {

    private static final String TAG = PinpadTestActivityForDukpt.class.getName();
    private TextView tv_pinpad_result;

    byte[] TLKKCV = new byte[] {(byte)0x8C,(byte)0xA6,0x4D};
    String kcv = null;
    String pedleydestdata2 = "0123456789ABCDEFFEDCBA9876543210";
    String ksn = "F8765432100F0F100000";

    Button btnmksk;

    //MKSK Keys
    private String defProtectKey = "11111111111111111111111111111111";
    private String defMainKey = "F40379AB9E0EC533F40379AB9E0EC533"; //
    private String defMainKeyKcv = "82E13665";
    private String defMacKey = "58D46F8C4CA35891C76595E92D499E0F";
    private String defMacKeyKcv = "B865B501";
    private String defPinKey = "58D46F8C4CA35891C76595E92D499E0F";
    private String defPinKeyKcv = "B865B501";
    private String defTDKey = "58D46F8C4CA35891C76595E92D499E0F";
    private String defTDKeyKcv = "B865B501";
    private int area = 1;
    private int tmkindex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinpad_test_dukpt);
        btnmksk = (Button) findViewById(R.id.btnmksk);

        GlobalData.getInstance().init(PinpadTestActivityForDukpt.this);

        btnmksk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadProtectKey();
            }
        });

    }

    private void setHint(String s) {
        Log.e("LOG:", s);
    }

    /*public void loadDukptKey(View view) {
        try {
        	boolean iRet = ServiceManager.getInstence().getPinpad().loadDukptTIK(1,0,pedleydestdata2,ksn,kcv);
            if (iRet) {
                showToast("load dukpt Key Success!");
                Toast.makeText(getApplicationContext(),"load dukpt Key Success!", Toast.LENGTH_LONG).show();
                GlobalData.getInstance().setPinpadVersion(PinpadInterfaceVersion.PINPAD_INTERFACE_DUKPT);
            } else {
                showToast("load protect Key error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void initView() {
    }

    private String getCardNumber() {
        return "6210885200013000000";
    }

    private void showResult(String msg) {
        tv_pinpad_result.setVisibility(View.VISIBLE);
        //pwKeyboard.setVisibility(View.GONE);
        tv_pinpad_result.setText(msg);
    }

    private void showToast(String msg) {
        setHint(msg);
//        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    /**
     * format pinpad ,clear all keys
     * @param view
     */
    public void format(View view)
    {
        try {
            ServiceManager.getInstence().getPinpad().format();
            Toast.makeText(getApplicationContext(),"Format Key Success!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    //MKSK Key inhection

    public void loadProtectKey() {
        String protect = defProtectKey;
        if (TextUtils.isEmpty(protect)) {
            setHint("protect key  is  null  ！");
            return;
        }
        try {
            boolean iRet = ServiceManager.getInstence().getPinpad().loadProtectKeyByArea(area,protect);
            if (iRet) {
                Toast.makeText(getApplicationContext(),"Load Procted Key Success", Toast.LENGTH_LONG).show();
                loadMainKey();
                GlobalData.getInstance().setPinpadVersion(PinpadInterfaceVersion.PINPAD_INTERFACE_VERSION3);
                GlobalData.getInstance().setArea(area);
            } else {
                Toast.makeText(getApplicationContext(),"Load Procted Key Fail", Toast.LENGTH_LONG).show();
                Log.d("SHETTY ", "loadProtectKey: 3");
                showToast("load protect Key error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadMainKey() {
        try {
            //Demo main key
            String main = defMainKey;
            String mainKcv = defMainKeyKcv;
            if (TextUtils.isEmpty(main)) {
                setHint("main key  is  null  ！");
                return;
            }
            boolean iRet = false;
            if (TextUtils.isEmpty(mainKcv)) {
                iRet = ServiceManager.getInstence().getPinpad().loadMainKeyByArea(area,tmkindex,main);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadMainKeyWithKcvByArea(area,tmkindex,main, mainKcv);
            }
            if (iRet) {
                Toast.makeText(getApplicationContext(),"Load Main Key Success", Toast.LENGTH_LONG).show();
                showToast("load Main Key Success!");
                loadPinKey();
                GlobalData.getInstance().setTmkId(tmkindex);
            } else {
                Toast.makeText(getApplicationContext(),"Load Main Key Fail", Toast.LENGTH_LONG).show();
                showToast("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setHint("loadMainKey error !");
        }
    }

    public void loadMacKey() {
        try {
            //Demo main key
            String mac = defMacKey;
            String macKcv = defMacKeyKcv;
            if (TextUtils.isEmpty(mac)) {
                setHint("mac key  is  null  ！");
                return;
            }
            boolean iRet = false;
            if (TextUtils.isEmpty(macKcv)) {
                iRet = ServiceManager.getInstence().getPinpad().loadMacKeyByArea(area,tmkindex,mac, null);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadMacKeyByArea(area,tmkindex,mac, macKcv);
            }

            if (iRet) {
                Toast.makeText(getApplicationContext(),"Load Mac Key Success", Toast.LENGTH_LONG).show();
                showToast("load mac Key Success!");
                loadTDKey();
            } else {
                Toast.makeText(getApplicationContext(),"Load MAC Key Fail", Toast.LENGTH_LONG).show();
                showToast("error");
            }
        } catch (Exception e) {
            e.printStackTrace();
            setHint("loadMacKey error !");
        }
    }

    public void loadPinKey() {
        try {
            //Demo main key
            String pin = defPinKey;
            String pin_kcv = defPinKeyKcv;
            if (TextUtils.isEmpty(pin)) {
                setHint("pin key  is  null  ！");
                return;
            }
            boolean iRet = false;
            if (TextUtils.isEmpty(pin_kcv)) {
                iRet = ServiceManager.getInstence().getPinpad().loadPinKeyByArea(area,tmkindex,pin, null);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadPinKeyByArea(area,tmkindex,pin, pin_kcv);
            }

            if (iRet) {
                Toast.makeText(getApplicationContext(),"Load PIN Key Success", Toast.LENGTH_LONG).show();
                showToast("load pin Key Success!");
                loadMacKey();
                GlobalData.getInstance().setPinkeyFlag(true);
            } else {
                Toast.makeText(getApplicationContext(),"Load PIN Key Fail", Toast.LENGTH_LONG).show();
                showToast("load pin Key error");
                showToast("pin key:" + pin);
                showToast("pin kcv:" + pin_kcv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setHint("loadPinKey error !");
        }
    }

    public void loadTDKey() {
        try {
            //Demo main key
            String tdkey = defTDKey;
            String td_kcv = defTDKeyKcv;
            if (TextUtils.isEmpty(tdkey)) {
                setHint("td key  is  null  ！");
                return;
            }
            boolean iRet = false;
            if (TextUtils.isEmpty(td_kcv)) {
                iRet = ServiceManager.getInstence().getPinpad().loadTDKeyByArea(area,tmkindex,tdkey, null);
            } else {
                iRet = ServiceManager.getInstence().getPinpad().loadTDKeyByArea(area,tmkindex,tdkey, td_kcv);
            }

            if (iRet) {
                Toast.makeText(getApplicationContext(),"Load TRACK Key Success", Toast.LENGTH_LONG).show();
                showToast("load td Key Success!");
            } else {
                showToast("load td Key error");
                showToast("td key:" + tdkey);
                showToast("td kcv:" + td_kcv);
            }
        } catch (Exception e) {
            e.printStackTrace();
            setHint("loadTDKey error !");
        }
    }

    public void calMAC(View view) {
//        KeyType
//        ServiceManager.getInstence().getPinpad().calcMAC()
        String demoData = "55B939BBCEBAC6E0D800229BC6E98BD42BADCC1727A5F243D8";

        try {
            String mac = ServiceManager.getInstence().getPinpad().calcMACByArea(area,tmkindex,demoData, BwPinpadSource.MAC_ECB);
            setHint("calMAC:");
            setHint("demoData :" + demoData);
            setHint("calMAC...");
            setHint("mac key :" + defMacKey);
            setHint("demoData mac value :" + mac);

        } catch (Exception e) {
            e.printStackTrace();
            setHint("calMAC error!");
        }
    }

    public static String encryptedTrack(String plainTrack) {
        //String demoData = "6210985200013865013=00001209540611111";
        String demoData = plainTrack;
        try {
            String s = ServiceManager.getInstence().getPinpad().encryptMagTrackByArea(1, 1, demoData);
            return s;
        } catch (Exception e) {
          return null;
        }

    }

  /*  public icon_void inputonlinepin(View view) {
        String cardNumber = getCardNumber();
        if (TextUtils.isEmpty(cardNumber)) {
            setHint("cat Number  is  null !");
            return;
        }
        PWDialog pwDialog = new PWDialog(this,PinpadInterfaceVersion.PINPAD_INTERFACE_VERSION3,area,tmkindex);
        pwDialog.setListener(new PWDialog.PWListener() {
            @Override
            public icon_void onConfirm(byte[] bytes, boolean b) {
                setHint("Password:" + BCDHelper.bcdToString(bytes));
            }

            @Override
            public icon_void onCancel() {
                setHint("Password Cancel");
            }

            @Override
            public icon_void onError(int i) {
                setHint("Password Error");
            }
        });
        pwDialog.showForPW(cardNumber);
    }
*/
    private String encryptedData = "";

    public void encryptedData(String data) {
        //String demoData = "621098520001122424412421412141209540611111000000";
        String demoData = data;
        try {
            String s = ServiceManager.getInstence().getPinpad().encryptDataByArea(area, tmkindex, KeyType.PIN_KEY, demoData);
            setHint("encryptedData:");
            setHint("demoData :" + demoData);
            setHint("encrypted value :" + s);
        } catch (Exception e) {
            e.printStackTrace();
            setHint("encryptedData error!");
        }
    }

    public void decryptData() {
        if (TextUtils.isEmpty(encryptedData)) {
            encryptedData = "D384769820F592469360AB286D0B62681C5EC8D67800DB4B";
        }
        try {
            String s = ServiceManager.getInstence().getPinpad().decryptDataByArea(area, tmkindex, KeyType.PIN_KEY, encryptedData);
            setHint("decryptData:");
            setHint("demoData :" + encryptedData);
            setHint("decrypt value :" + s);
        } catch (Exception e) {
            e.printStackTrace();
            setHint("decryptData error!");
        }
    }

    public void main(String args[]){
        encryptedData("");
        encryptedTrack("");
        decryptData();
    }

}
