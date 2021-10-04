package com.poly.animewallpaper.view.fragment;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.poly.animewallpaper.BuildConfig;
import com.poly.animewallpaper.R;

import java.io.File;


public class SettingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private LinearLayout setting, shareApp, rateApp, feedback, clearCache, moreApp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_setting, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initActions();

    }

    private void initViews() {
        setting = view.findViewById(R.id.setting);
        shareApp = view.findViewById(R.id.share_app);
        rateApp = view.findViewById(R.id.rate_app);
        feedback = view.findViewById(R.id.feedback);
        clearCache = view.findViewById(R.id.clear_cache);
        moreApp = view.findViewById(R.id.more_app);


    }

    private void initActions() {
        setting.setOnClickListener(this);
        shareApp.setOnClickListener(this);
        rateApp.setOnClickListener(this);
        feedback.setOnClickListener(this);
        clearCache.setOnClickListener(this);
        moreApp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting:
                settingApp();
                break;
            case R.id.share_app:
                shareApp();
                break;
            case R.id.rate_app:
                rateApp(getContext());
                break;
            case R.id.feedback:
                feedbackApp(getContext());
                break;
            case R.id.clear_cache:
                try {
                    File dir = getContext().getCacheDir();
                    deleteDir(dir);
                    Snackbar snackbar = Snackbar.make(clearCache, "Deleted!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.more_app:
                moreApp(getContext());
                break;
            default:
                break;
        }

    }

    public void feedbackApp(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + "abcxyz@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback about Anime Wallpapers application");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Dear...,");

        try {
            context.startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "No email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public void moreApp(Context context) {
        final String devName = context.getPackageName();
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://search?q=pub:" + devName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/search?q=pub:" + devName)));
        }

    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if (dir != null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }


    private void rateApp(Context context) {
        String appPackageName = context.getPackageName();

        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void shareApp() {
        String appPackageName = getContext().getPackageName();

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                "https://play.google.com/store/apps/details?id=" + appPackageName);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    private void settingApp() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", getContext().getPackageName(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }




}