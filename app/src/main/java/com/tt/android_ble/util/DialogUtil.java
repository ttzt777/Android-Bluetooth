package com.tt.android_ble.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.tt.android_ble.R;

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
public class DialogUtil {

    public static void showOpenSettingDialog(Context context, String content, final DialogListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(context.getResources().getString(R.string.app_warning));
        builder.setMessage(content);
        builder.setCancelable(false);
        builder.setPositiveButton(context.getResources().getString(R.string.app_setting), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onPositiveButtonClick();
            }
        });
        builder.setNegativeButton(context.getResources().getString(R.string.app_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onNegativeButtonClick();
            }
        });
        builder.show();
    }

    public interface DialogListener {
        void onPositiveButtonClick();
        void onNegativeButtonClick();
    }
}
