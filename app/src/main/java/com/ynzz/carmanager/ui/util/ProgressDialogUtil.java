package com.ynzz.carmanager.ui.util;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ynzz.carmanager.ui.view.MyDialog;
import com.ynzz.lbstest.R;


public class ProgressDialogUtil {
    //当前dialog对象
    private static Dialog currentDialog;

    /**
     * 显示alert弹出框3
     *
     * @param context
     * @param title
     * @param oneClick
     * @param twoClick
     * @return
     */
    public static Dialog showAlertDialogThree(Context context, String title,
                                              final OnClickListener oneClick, final OnClickListener twoClick) {
        dismiss();
        currentDialog = new MyDialog(context, R.style.update_dialog);
        currentDialog.setContentView(R.layout.my_dialog);
        Window window = currentDialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        int width = DeviceUtil.getWindowW(context);
        int height = DeviceUtil.getWindowH(context);
        width = width < height ? width : height;
        lp.width = (int) (0.75 * width);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        currentDialog.setCancelable(false);

        TextView titleTxtv = (TextView) currentDialog.findViewById(R.id.dialog_result0);
        titleTxtv.setText(title);
        final Button leftBtn = (Button) currentDialog.findViewById(R.id.dialog_enter);
        final Button rightBtn = (Button) currentDialog.findViewById(R.id.dialog_cancel);

        leftBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDialog.dismiss();
                if (oneClick != null) {
                    oneClick.onClick(leftBtn);
                }
            }
        });

        rightBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                currentDialog.dismiss();
                if (twoClick != null) {
                    twoClick.onClick(leftBtn);
                }
            }
        });

        currentDialog.show();
        return currentDialog;
    }

    /**
     * 消失dialog
     */
    public static void dismiss() {
        if (currentDialog != null) {
            if (currentDialog.isShowing()) {
                try {
                    currentDialog.dismiss();
                } catch (Exception e) {
                }
            }
            currentDialog = null;
        }
    }
}
