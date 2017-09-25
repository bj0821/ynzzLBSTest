package com.ynzz.carmanager.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

/**
 *  自定义加载框
 *
 * @author jinzufan
 * @since 2014-10-30
 */
public class MyDialog extends Dialog {

    private Context context;

    public MyDialog(Context context) {
        super(context);
        this.context = context;
    }

    public MyDialog(Context context, int theme) {
        super(context, theme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
