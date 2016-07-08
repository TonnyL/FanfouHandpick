package com.marktony.fanfouhandpick;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * Created by lizhaotailang on 2016/7/8.
 */

public class Demo {

    private Context context;

    public void f(Context context){
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        dialog.show();

    }

}
