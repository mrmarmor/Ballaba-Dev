package com.example.michaelkibenko.ballaba.Utils;

import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringDef;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Guideline;
import android.support.design.widget.Snackbar;
import android.support.transition.TransitionManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.michaelkibenko.ballaba.Activities.MainActivity;
import com.example.michaelkibenko.ballaba.Adapters.GooglePlacesAdapter;
import com.example.michaelkibenko.ballaba.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.content.ContentValues.TAG;
import static com.example.michaelkibenko.ballaba.Utils.UiUtils.ChipsButtonStates.NOT_PRESSED;
import static com.example.michaelkibenko.ballaba.Utils.UiUtils.ChipsButtonStates.PRESSED;

/**
 * Created by User on 11/03/2018.
 */

public class UiUtils {
   public @interface ScreenStates {
        float FULL = 0;
        float HALF = 0.5f;
        float HIDE = 1;
    }

    @StringDef({PRESSED, NOT_PRESSED})
    public @interface ChipsButtonStates{
        String PRESSED = "PRESSED";
        String NOT_PRESSED = "NOT_PRESSED";
    }

    private static UiUtils instance;
    private Context ctx;

    public static UiUtils instance(boolean isOnce, Context context){
        if(!isOnce) {
            if (instance == null) {
                instance = new UiUtils(context);
            }
            return instance;
        }
        return new UiUtils(context);
    }

    private UiUtils(Context context){
        this.ctx = context;
    }

    public void showSoftKeyboard(){
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null)
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    //to get view from context as needed in the function below use: ((Activity)context).getWindow().getDecorView()
    public void hideSoftKeyboard(View view){
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null)
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void buttonChanger(Button btn, boolean is){
        if(is){
            btn.setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
            btn.setAlpha(1f);
            btn.setClickable(true);
            btn.setEnabled(true);
            btn.setTextColor(Color.WHITE);
        }else {
            btn.setBackgroundColor(ctx.getResources().getColor(R.color.gray_button_color));
            btn.setAlpha(0.50f);
            btn.setClickable(false);
            btn.setEnabled(false);
            btn.setTextColor(Color.BLACK);
        }
    }

    /*public void setFilterBarVisibility(boolean isVisible){
        View rootView = ((Activity)ctx).getWindow().getDecorView().findViewById(R.id.mainActivity_filter_included);
        rootView.setVisibility(isVisible? View.VISIBLE : View.GONE);
    }*/

    public void setFilterBarVisibility(@ScreenStates float state){
        View rootView = ((MainActivity)ctx).getWindow().getDecorView();//.findViewById(R.id.mainActivity_filter_included);

        Guideline guideline = rootView.findViewById(R.id.mainActivity_filter_guideline_top);//mainActivityFilterGuidelineTop;
        ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams)guideline.getLayoutParams();
        lp.guidePercent = state;
        guideline.setLayoutParams(lp);

        ConstraintLayout constraintLayout = rootView.findViewById(R.id.mainActivity_filter_included);
        ConstraintSet constraintSetHeight = new ConstraintSet();
        constraintSetHeight.clone(ctx, R.layout.search_filter_screen);
        constraintSetHeight.setGuidelinePercent(R.id.mainActivity_filter_guideline_top, 0.07f); // 7% // range: 0 <-> 1

        TransitionManager.beginDelayedTransition(constraintLayout);
        constraintSetHeight.applyTo(constraintLayout);
    }

    public Button onChipsButtonClick(Button button, String state){
        if(state.equals(ChipsButtonStates.NOT_PRESSED)){
            //change the state to pressed
            button.setBackgroundResource(R.drawable.chips_button_pressed);
            button.setTextColor(ctx.getResources().getColor(android.R.color.white));
            button.setTag(ChipsButtonStates.PRESSED);
        }else if(state.equals(ChipsButtonStates.PRESSED)){
            //change the state to not pressed
            button.setBackgroundResource(R.drawable.chips_button);
            button.setTextColor(ctx.getResources().getColor(R.color.colorPrimary));
            button.setTag(ChipsButtonStates.NOT_PRESSED);
        }else{
            Log.e(TAG, "Chips does not have tag");
        }

        return button;
    }

    public Bitmap uriToBitmap(Uri uri){
        try {
            InputStream stream = ctx.getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(stream);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public byte[] drawableToUri(Drawable d) {
        if (d != null) {
            Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            return stream.toByteArray();
        }
        return null;
    }

    public byte[] uriToBytes(Uri uri) {
        if (uri == null) return null;
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();

        try {
            InputStream inputStream = ctx.getContentResolver().openInputStream(uri);
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return byteBuffer.toByteArray();
    }

    private GooglePlacesAdapter dataAdapter;
    public void initAutoCompleteAddressInCity(final AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextView viewCity){
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                autoCompleteTextView.setAdapter(dataAdapter);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String citySelected = viewCity.getText().toString();
                dataAdapter = new GooglePlacesAdapter(ctx, android.R.layout.simple_list_item_1
                        , GooglePlacesAdapter.GooglePlacesFilter.GEOCODE, citySelected);
                dataAdapter.notifyDataSetInvalidated();//this is against a strange crash and against bad-eye
                autoCompleteTextView.requestLayout();//this is against a strange crash and against bad-eye
                dataAdapter.getFilter().filter(citySelected+" "+s.toString());
            }
        });
    }

    public void initAutoCompleteCity(final AutoCompleteTextView autoCompleteTextView) {
        final GooglePlacesAdapter dataAdapter = new GooglePlacesAdapter(ctx, android.R.layout.simple_list_item_1
                , GooglePlacesAdapter.GooglePlacesFilter.CITIES);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                autoCompleteTextView.setAdapter(dataAdapter);
                //dataAdapter.notifyDataSetInvalidated();
                //autoCompleteTextView.requestLayout();
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataAdapter.notifyDataSetInvalidated();//this is against a strange crash and against bad-eye
                autoCompleteTextView.requestLayout();//this is against a strange crash and against bad-eye
                dataAdapter.getFilter().filter(s.toString());
            }
        });
    }

    public TextView generateCustomTextView(final String text, final @DrawableRes int icon) {
        TextView tv = new TextView(ctx);

        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 16, 0, 0);

        tv.setText(text);
        tv.setCompoundDrawablesRelativeWithIntrinsicBounds(icon, 0, 0, 0);
        tv.setTextAppearance(ctx, R.style.property_description_textViews);
        tv.setCompoundDrawablePadding(15);
        tv.setPaddingRelative(0, 16, 8, 16);
        tv.setLayoutParams(llp);

        return tv;
    }

    //TODO this function has a very similar function in BaseActivity class...
    public Snackbar showSnackBar(View snackBarView, String message){
        Snackbar snackBar = Snackbar.make(snackBarView, message, Snackbar.LENGTH_LONG);
        snackBar.getView().setBackgroundColor(ctx.getResources().getColor(R.color.colorPrimary));
        snackBar.show();
        return snackBar;
    }

    public void showProfileDialog(boolean show){

    }

    public Matrix adjustPhotoOrientation(Uri photo , String[] orientations) {
        Cursor cur = ctx.getContentResolver().query(photo, orientations, null, null, null);
        int orientation = -1;
        if (cur != null && cur.moveToFirst()) {
            orientation = cur.getInt(cur.getColumnIndex(orientations[0]));
            cur.close();
        }

        Matrix matrix = new Matrix();
        matrix.postRotate(orientation);

        return matrix;
    }

    private Bitmap createBitmapFromUri(Uri photo, Matrix matrix) {
        try {
            Bitmap bmp = MediaStore.Images.Media.getBitmap(ctx.getContentResolver(), photo);
            Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            return bitmap;
        } catch (IOException | OutOfMemoryError e) {
            e.printStackTrace();
            Log.e(TAG, "error: " + e.getMessage());
            return null;
        }
    }

    private Bitmap imageOrientationValidatorOLD(Bitmap bitmap, String path) {
        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

    private Bitmap imageOrientationValidator(Uri selectedImage) {
        try {
            File f = new File(getPath(selectedImage));
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            int angle = 0;

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }

            Matrix matrix = new Matrix();
            matrix.postRotate(angle);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;

            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f), null, options);
            Bitmap bitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return bitmap;

        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return null;
        }
    }

    public String getPath(Uri uri) {
        String[]  data = { MediaStore.Images.Media.DATA };
        CursorLoader loader = new CursorLoader(ctx, uri, data, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}
