package com.example.michaelkibenko.ballaba.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntegerRes;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Fragments.Filter.DateOfEntranceFragment;
import com.example.michaelkibenko.ballaba.R;
import com.example.michaelkibenko.ballaba.Utils.DeviceUtils;

/**
 * Created by User on 27/03/2018.
 */

public class BallabaDialogBuilder extends AlertDialog.Builder {
    private AlertDialog.Builder dialogBuilder;
    private Context context;
    private View dialogLayout;
    private AlertDialog.OnClickListener listener;
    private EditText input;

    public BallabaDialogBuilder(Context context) {
        super(context);
        this.context = context;
    }

    //for a dialog with a custom view
    public BallabaDialogBuilder(Context context, @LayoutRes int res) {
        super(context);

        this.context = context;

        //LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        dialogLayout = View.inflate(context, res, null);//inflater.inflate(res, null);
        dialogLayout.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        this.setView(dialogLayout);

        //dialogBuilder = new AlertDialog.Builder(context);
    }

    //private static BallabaDialogBuilder instance;

    /*public BallabaDialogBuilder create(){
        this.dialogBuilder = new BallabaDialogBuilder()
    }*/
    /*public static BallabaDialogBuilder newInstance() {
        BallabaDialogBuilder dialogBuilder = new BallabaDialogBuilder();
        return dialogBuilder;
    }
*/
    public BallabaDialogBuilder(Context context, EditText editText, AlertDialog.OnClickListener listener) {
        super(context);

        this.context = context;
        dialogBuilder = new BallabaDialogBuilder(context);
        this.input = editText;
        this.input.setInputType(InputType.TYPE_CLASS_PHONE);
        this.listener = listener;
    }

    public void setContent(String title, String message, CharSequence[] items) {
        setTitle(title);
        setMessage(message);
        setItems(items, listener);
    }

    public void setButtons(String textPositive, String textNegative, DialogInterface.OnClickListener clickListener
            , DialogInterface.OnClickListener cancelListener) {
        setPositiveButton(textPositive, clickListener);
        setNegativeButton(textNegative, cancelListener);
    }

    public void setContentInView(String title, String message, CharSequence[] items) {
        //View rootView = dialogBuilder.create().getWindow().getDecorView();
        TextView textViewTitle = dialogLayout.findViewById(R.id.dialog_regular_title);
        TextView textViewMessage = dialogLayout.findViewById(R.id.dialog_regular_message);
        textViewTitle.setText(title);
        textViewMessage.setText(message);
    }

    public void setButtonsInView(String textPositive, String textNegative, @ColorRes int color, View.OnClickListener clickListener
            , View.OnClickListener cancelListener) {

        //View rootView = dialogBuilder.create().getWindow().getDecorView();
        Button buttonPositive = dialogLayout.findViewById(R.id.dialog_regular_button_positive);
        buttonPositive.setText(textPositive);
        buttonPositive.setTextColor(context.getResources().getColor(color, context.getTheme()));
        buttonPositive.setOnClickListener(clickListener);

        Button buttonNegative = dialogLayout.findViewById(R.id.dialog_regular_button_negative);
        buttonNegative.setText(textNegative);
        buttonNegative.setOnClickListener(cancelListener);
    }

}