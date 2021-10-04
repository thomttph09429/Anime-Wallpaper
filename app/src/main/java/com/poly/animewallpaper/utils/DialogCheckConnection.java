package com.poly.animewallpaper.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.speech.tts.TextToSpeech;
import android.text.Html;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.poly.animewallpaper.MainActivity;
import com.poly.animewallpaper.R;

public class DialogCheckConnection {
    static TextToSpeech t1;
    private static DialogCheckConnection ourInstance = new DialogCheckConnection();
    private Context appContext;

    private DialogCheckConnection() {
    }

    public static Context get() {
        return getInstance().getContext();
    }

    public static synchronized DialogCheckConnection getInstance() {
        return ourInstance;
    }

    public void init(Context context) {
        if (appContext == null) {
            this.appContext = context;
        }
    }

    private Context getContext() {
        return appContext;
    }

    public void AlertDialog(final Context mainActivity, String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(mainActivity, R.style.AlertDialogCustom));
        alertDialogBuilder.setMessage(str);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
    }
}
