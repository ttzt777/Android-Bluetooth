package com.tt.android_ble.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * -------------------------------------------------
 * Description：
 * Author：TT
 * Since：2017/3/17
 * Version：V0.0.1
 * -------------------------------------------------
 * History：
 * V0.0.1 --
 * -------------------------------------------------
 */
public class AppUtil {
    public static final String PACKAGE_URL_SCHEME = "package:";

    public static void openAppSystemSetting(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + context.getPackageName()));
        context.startActivity(intent);
    }
}
