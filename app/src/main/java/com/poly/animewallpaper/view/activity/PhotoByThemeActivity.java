package com.poly.animewallpaper.view.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.poly.animewallpaper.MainActivity;
import com.poly.animewallpaper.R;
import com.poly.animewallpaper.adapter.PhotoByThemeAdapter;
import com.poly.animewallpaper.model.PhotoByTheme;
import com.poly.animewallpaper.utils.DialogCheckConnection;
import com.poly.animewallpaper.utils.UtilGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class PhotoByThemeActivity extends AppCompatActivity {
    private String names;
    private Intent intent;
    private RequestQueue requestQueue;
    private String token = "";
    private String keyMD5 = "";
    private String jsonArraypPhoto = "";
    private String url = "";
    private List<PhotoByTheme> photoByThemes;
    private PhotoByThemeAdapter photoByThemeAdapter;
    private RecyclerView rvPhotoByTheme;
    private TextView tvName;
    private ProgressBar progressBar;
    private DialogCheckConnection dialogCheckConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_photo_theme);
        initViews();
        initActions();
        photoByThemes = new ArrayList<>();
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        requestQueue = Volley.newRequestQueue(this);
        intent = getIntent();
        names = intent.getStringExtra("name");
        String name = names.replace(" ", "_");
        url = " https://wallpaper-api.youaregorgeous.net?skip=0&limit=300&folder=Anime_" + name;
        keyMD5 = 0 + "!" + 300 + "@" + "Anime_" + name + "#";
        fetchListPhoto();

        tvName.setText(names);

    }

    private void initViews() {
        rvPhotoByTheme = findViewById(R.id.rv_photo_by_theme);
        tvName = findViewById(R.id.tv_name);
        progressBar = findViewById(R.id.progress_bar);
    }

    private void initActions() {
        dialogCheckConnection = dialogCheckConnection.getInstance();
        dialogCheckConnection.init(this);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvPhotoByTheme.setLayoutManager(layoutManager);

    }

    private void fetchListPhoto() {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        token = response.getString("token");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    jsonArraypPhoto = decrypt(token, md5(keyMD5));
                    Log.e("StringJson", jsonArraypPhoto + "");

                    try {
                        JSONArray jsonArray = new JSONArray(jsonArraypPhoto);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            PhotoByTheme photoByTheme = new PhotoByTheme();
                            JSONObject object = jsonArray.getJSONObject(i);
                            String photo = object.getString("thumb");
                            String largePhoto = object.getString("large");

                            photoByTheme.setThumb(photo);
                            photoByTheme.setLarge(largePhoto);
                            photoByThemes.add(photoByTheme);
                            progressBar.setVisibility(View.GONE);
                            rvPhotoByTheme.setVisibility(View.VISIBLE);

                            photoByThemeAdapter = new PhotoByThemeAdapter(getApplicationContext(), photoByThemes);
                            rvPhotoByTheme.setAdapter(photoByThemeAdapter);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    dialogCheckConnection.AlertDialog(PhotoByThemeActivity.this, "An error occurred, please try again!");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (UtilGlobal.isNetworkConnection(PhotoByThemeActivity.this)) {
                    fetchListPhoto();
                } else {
                    progressBar.setVisibility(View.GONE);
                    dialogCheckConnection.AlertDialog(PhotoByThemeActivity.this, "Please check your network connection again!");
                }

            }
        });


        requestQueue.add(request);

    }


    public String md5(String text) {

        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(text.getBytes());
            byte[] messageDigest = digest.digest();
            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                StringBuilder h = new StringBuilder(Integer.toHexString(0xFF & aMessageDigest));
                while (h.length() < 2)
                    h.insert(0, "0");
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return MD5;
    }

    public String decrypt(String input, String key) {
        String result = null;
        try {
            if (input == null) {
                return "";
            }
            byte[] output = null;
//            LogUtil.getLogger().d("real key for flurry : " + key);
            try {
                SecretKeySpec skey = new SecretKeySpec(key.getBytes(), "AES");
                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
                cipher.init(Cipher.DECRYPT_MODE, skey);
                output = cipher.doFinal(Base64.decode(input, Base64.NO_WRAP));
            } catch (Exception e) {

            }

            result = "";
            try {
                result = new String(output);
            } catch (Exception e) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}