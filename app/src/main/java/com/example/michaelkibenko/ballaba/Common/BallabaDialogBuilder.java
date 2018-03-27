package com.example.michaelkibenko.ballaba.Common;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;

/**
 * Created by User on 27/03/2018.
 */

public class BallabaDialogBuilder extends AlertDialog.Builder {
    private AlertDialog.Builder dialogBuilder;
    private Context context;
    private AlertDialog.OnClickListener listener;
    private EditText input;

    public BallabaDialogBuilder(Context context) {
        super(context);

        this.context = context;
        this.dialogBuilder = new AlertDialog.Builder(context);
    }

    public BallabaDialogBuilder(Context context, EditText editText, AlertDialog.OnClickListener listener) {
        super(context);

        this.context = context;
        this.dialogBuilder = new AlertDialog.Builder(context);
        this.input = editText;
        this.input.setInputType(InputType.TYPE_CLASS_PHONE);
        this.listener = listener;
    }

    public AlertDialog.Builder setContent(String title, String message, CharSequence[] items) {
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setItems(items, listener);

        return dialogBuilder;
    }

    public AlertDialog.Builder setButtons(String textPositive, String textCancel, DialogInterface.OnClickListener clickListener
            , DialogInterface.OnClickListener cancelListener) {
        dialogBuilder.setPositiveButton(textPositive, clickListener);
        dialogBuilder.setNegativeButton(textCancel, cancelListener);

        return dialogBuilder;
    }

    public AlertDialog.Builder setIcon(@IdRes int icon) {
        dialogBuilder.setIcon(icon);

        return dialogBuilder;
    }

}

