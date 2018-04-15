package com.example.michaelkibenko.ballaba.Common;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.michaelkibenko.ballaba.R;

/**
 * Created by User on 27/03/2018.
 */

public class BallabaDialogBuilder extends AlertDialog.Builder {
    private AlertDialog.Builder dialogBuilder;
    private Context context;
    private AlertDialog.OnClickListener listener;
    private EditText input;
    private Fragment fragment;

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


    public static class MapDialog extends DialogFragment {
        public MapDialog newInstance(int myIndex) {
            MapDialog mapDialog = new MapDialog();

            //example of passing args
            //Bundle args = new Bundle();
            //args.putInt("anIntToSend", myIndex);
            //yourDialogFragment.setArguments(args);

            return mapDialog;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //read the int from args
            //int myInteger = getArguments().getInt("anIntToSend");

            View view = inflater.inflate(R.layout.fragment_map, null);

            //here read the different parts of your layout i.e :
            //tv = (TextView) view.findViewById(R.id.yourTextView);
            //tv.setText("some text")

            return view;
        }
    }


    public AlertDialog.Builder setFragment(Activity activity, Fragment fragment, @LayoutRes int layout) {
        LayoutInflater inflater = activity.getLayoutInflater();

        //Inflate the layout but ALSO store the returned view to allow us to call findViewById
        View view = inflater.inflate(layout,null);

        //Do all the initial code regarding the view, as you would in onCreate normally
        //view.findViewById(R.id.some_view);

        //Finally, give the custom view to the AlertDialog builder
        //dialogBuilder.setView(view);

        dialogBuilder.setView(fragment.getView());

        return dialogBuilder;
    }
}

