package com.example.youcloudp1000sdk.custom_view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.youcloudp1000sdk.R;


/**
 * Created by shankar.savant on 30-05-2018.
 */

public class DialogUtils {
    public static void showConfirmDialog(final Context context, final String code, final String title, final String msg) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_confirm);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCanceledOnTouchOutside(false);

        Button btncancel = (Button) dialog.findViewById(R.id.button3);
        Button btnlogout = (Button) dialog.findViewById(R.id.button4);
        LinearLayout relativeLayout1 = (LinearLayout) dialog.findViewById(R.id.relativeLayout1);

        TextView txtTitle = (TextView) dialog.findViewById(R.id.textView1);
        TextView txtMsg = (TextView) dialog.findViewById(R.id.textView26);
        ImageView img = (ImageView) dialog.findViewById(R.id.imglogo);

        txtTitle.setText(title);
        txtMsg.setText(msg);

        btncancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //Button sure
        btnlogout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (code.equalsIgnoreCase("01")) {
                    //new UpdateApk(context).update();

                    String appPackageName = "";
                    try {
                        PackageManager manager = context.getPackageManager();
                        PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
                        appPackageName = info.packageName;

                        Log.d("SHANKY", "PKG Name : " + appPackageName);
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }
                } else {
                    ((Activity) context).finish();
                }
            }
        });

        if (dialog != null)
            dialog.show();

    }
}
