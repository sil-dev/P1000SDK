package com.example.youcloudp1000sdk;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.util.Log;


import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.basewin.database.DataBaseManager;
import com.basewin.log.LogUtil;
import com.basewin.services.DeviceInfoBinder;
import com.basewin.services.ServiceManager;
import com.example.youcloudp1000sdk.utils.StaticValues;
import com.example.youcloudp1000sdk.y2000.constants.GlobalData;


/**
 * Created on 4/24/2017.
 * Change history : 1.0
 * date : 28 / 8 /2017
 * Description : Closer of VAPT velvalnerability
 * Modified By : Shankar
 * <p>
 * Landing screen : SplashScreenActivity
 * Login screen : LoginActivity
 * Menu on Main Screen: MainActivity
 * ********************** Payment *************************
 * Enter amount, mobilenumber, remark (if any) : SaleByCashFragment
 * Connect bluetoothDevice and Start Transaction : Paired Device Fragment
 * ********************** History *************************
 * Check Transaction history for UPI, card, cash & all : HistoryBaseFragment
 * ********************** Account *************************
 * In Account Menu, user can change password: PasswordManagmentFragment,
 * Start and end cash drawer - StartCashDrawerFragment
 * Performance report for card & cash -  SalesReportFragment
 * ********************* Settings **************************
 * In settings Menu, user can see details- ProfileDetailsFragment
 * Pinpad (ucube) device setting can be done like update firmware - PinpadFragmentSettings
 * Menu enable, disable functionality is provided - MenuManagementFragment
 */

public class App extends MultiDexApplication {

    public String versionName = "";
    public static Typeface openSansLight, bold_font;
    public static int check_status_count = 0;
    public static boolean upi_status = false;
    // Gloabl declaration of variable to use in whole app
    public static float circle_radius = 1000;
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    public static boolean activityVisible; // Variable that will check the
    // current activity state

    public static boolean isActivityVisible() {
        return activityVisible; // return true or false
    }

    public static void activityResumed() {
        activityVisible = true;// this will set true when activity resumed

    }

    public static void activityPaused() {
        activityVisible = false;// this will set false when activity paused
    }

    public static Context getAppContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //startService(new Intent(this, BluetoothConnectionService.class));
        context = getApplicationContext();
        openSansLight = Typeface.createFromAsset(getAssets(), "fonts/calibri.ttf");
        bold_font = Typeface.createFromAsset(getAssets(), "fonts/calibrib.ttf");
        PackageManager manager = this.getPackageManager();
        try {
            MultiDex.install(this);

            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String packageName = info.packageName;
            int versionCode = info.versionCode;
            versionName = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*if(isMyServiceRunning(LocService.class)){
            Toast.makeText(getApplicationContext(), "Service is running in background", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Service is not running in background", Toast.LENGTH_SHORT).show();
        }*/

        try {
            //Stetho initializer
            //Stetho.initializeWithDefaults(this);

            /**
             * init device server
             */
            ServiceManager.getInstence().init(this);

            /**
             * init database
             */

            DataBaseManager.getInstance().init(this);

            /**
             *For SDK Log OPEN
             */
            GlobalData.getInstance().init(this);
            LogUtil.openLog();

            StaticValues.setIsY2000(true);
            Log.d("SHANKY", "INIT Y2000 SUCCESS");

            DeviceInfoBinder deviceInfoBinder = ServiceManager.getInstence().getDeviceinfo();
            if (deviceInfoBinder != null) {
                StaticValues.setDeviceType("" + deviceInfoBinder.getDeviceType());
                //P2000L, P1000,P500
                Log.d("SHANKY", "DEV Type : " + deviceInfoBinder.getDeviceType());
            }
        } catch (LinkageError e) {
            Log.d("SHANKY", "LinkageError");
            StaticValues.setIsY2000(false);
            e.printStackTrace();
        } catch (Exception e) {
            Log.d("SHANKY", "Exception");
            StaticValues.setIsY2000(false);
            e.printStackTrace();
        }

    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

}
