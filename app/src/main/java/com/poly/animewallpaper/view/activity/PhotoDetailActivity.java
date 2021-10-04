package com.poly.animewallpaper.view.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.net.wifi.rtt.CivicLocationKeys;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.snackbar.Snackbar;
import com.poly.animewallpaper.R;
import com.poly.animewallpaper.data.FavoriteDAO;
import com.poly.animewallpaper.listenner.SaveImageHelper;
import com.poly.animewallpaper.model.Favorite;
import com.poly.animewallpaper.utils.DialogCheckConnection;
import com.poly.animewallpaper.utils.UtilGlobal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import uk.co.senab.photoview.PhotoViewAttacher;

public class PhotoDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private Intent intent;
    private String linkPhoto;
    private String largePhoto;
    private ImageView ivPhoto, ivDownload, ivShare, ivMore, ivLove, ivLoveEd;
    private RelativeLayout menuBottom;
    private FavoriteDAO favoriteDAO;
    InputStream in = null;
    private static final int PERMISSION_REQUEST_CODE = 1000;
    private View content;
    private DialogCheckConnection dialogCheckConnection;
    private RelativeLayout bottomMenu;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        overridePendingTransition(R.anim.activity_slide_from_bottom, R.anim.stay);
        initViews();
        initActions();
        intent = getIntent();
        linkPhoto = intent.getStringExtra("thumb");
        largePhoto = intent.getStringExtra("large");

        Log.e("anh", linkPhoto + "");
        Log.e("large", largePhoto + "");
        checkFavorite();
        readPhotto(largePhoto);
        showCustomUI();
        content = findViewById(android.R.id.content);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            }, PERMISSION_REQUEST_CODE);


    }


    private void showCustomUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }


    private void initViews() {
        ivPhoto = findViewById(R.id.iv_photo);
        ivDownload = findViewById(R.id.iv_download);
        ivShare = findViewById(R.id.iv_share);
        ivMore = findViewById(R.id.iv_more);
        ivLove = findViewById(R.id.iv_love);
        menuBottom = findViewById(R.id.menu_bottom);
        ivLoveEd = findViewById(R.id.iv_loved);
        bottomMenu = findViewById(R.id.menu_bottom);
        ivMore.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        ivDownload.setOnClickListener(this);
        ivLove.setOnClickListener(this);
        ivLoveEd.setOnClickListener(this);

    }


    private void initActions() {
        favoriteDAO = new FavoriteDAO(this);
        PhotoViewAttacher pAttacher;
        pAttacher = new PhotoViewAttacher(ivPhoto);
        pAttacher.update();
        dialogCheckConnection = dialogCheckConnection.getInstance();

    }


    private void readPhotto(String themb) {
        Picasso.with(this).load(themb).fit().centerCrop().into(ivPhoto, new Callback() {
            @Override
            public void onSuccess() {
                bottomMenu.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
                bottomMenu.setVisibility(View.GONE);


            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.stay, R.anim.activity_slide_to_bottom);
    }

    private void checkFavorite() {
        if (!favoriteDAO.isFavorite(largePhoto.toString())) {
            ivLove.setVisibility(View.VISIBLE);
            ivLoveEd.setVisibility(View.GONE);
        } else {
            ivLove.setVisibility(View.GONE);
            ivLoveEd.setVisibility(View.VISIBLE);
        }


    }

    private void addToFavorite() {
        Favorite favorite = new Favorite();
        favorite.setPhoto(largePhoto.toString());

        long result = favoriteDAO.insert(favorite);
        if (result > 0) {

        } else {

        }


    }

    private void remoteFavorite() {

        long result = favoriteDAO.delete(largePhoto);
        if (result > 0) {

        } else {

        }
    }

    private void checkPermisson() {
        if (ActivityCompat.checkSelfPermission(PhotoDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(PhotoDetailActivity.this, "You should grant permission", Toast.LENGTH_SHORT).show();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{

                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, PERMISSION_REQUEST_CODE);
            }
            return;
        } else {
            if (UtilGlobal.isNetworkConnection(PhotoDetailActivity.this)) {
                String fileName = UUID.randomUUID().toString() + ".jpg";
                Picasso.with(getBaseContext())
                        .load(largePhoto.toString())
                        .into(new SaveImageHelper(getBaseContext(),
                                getContentResolver(),
                                fileName,
                                "Image description"));
                Snackbar snackbar = Snackbar
                        .make(content, "Save!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } else {
                dialogCheckConnection.AlertDialog(PhotoDetailActivity.this, "Please check your network connection!");
            }


        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_share:
                sendPhoto();
                break;
            case R.id.iv_loved:
                if (ivLoveEd.getVisibility() == View.VISIBLE) {
                    ivLove.setVisibility(View.VISIBLE);
                    ivLoveEd.setVisibility(View.GONE);
                    remoteFavorite();
                }


                break;
            case R.id.iv_love:
                if (ivLove.getVisibility() == View.VISIBLE) {
                    ivLoveEd.setVisibility(View.VISIBLE);
                    ivLove.setVisibility(View.GONE);
                    addToFavorite();
                }

                break;
            case R.id.iv_download:
                checkPermisson();
                break;
            case R.id.iv_more:
                showPopupMenu();

                break;

            default:
                break;

        }

    }

    private void showPopupMenu() {
        PopupMenu popup = new PopupMenu(PhotoDetailActivity.this, ivShare);
        popup.getMenuInflater()
                .inflate(R.menu.set_wallpaper, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {

                case R.id.action_home_wallpaper:
                    setWallpaper();
                    break;
                case R.id.action_lock_wallpaper:
                    setLockScreen();
                    break;
                case R.id.action_both:
                    setWallpaper();
                    setLockScreen();
                    break;
                default:
                    break;
            }
            return true;
        });
        popup.show();
    }

    private void sendPhoto() {
        try {
            ivPhoto.buildDrawingCache();
            Bitmap bitmap = ivPhoto.getDrawingCache();
            File cachePath = new File(this.getCacheDir(), "images");
            cachePath.mkdirs(); // don't forget to make the directory
            FileOutputStream stream = new FileOutputStream(new File(cachePath, "image.png")); // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            stream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File imagePath = new File(this.getCacheDir(), "images");
        File newFile = new File(imagePath, "image.png");
        Uri contentUri = FileProvider.getUriForFile(this, "com.poly.animewallpaper", newFile);

        if (contentUri != null) {

            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, getContentResolver().getType(contentUri));
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
            startActivity(Intent.createChooser(shareIntent, "Choose an app"));

        }


    }



    private void setWallpaper() {

        if (UtilGlobal.isNetworkConnection(PhotoDetailActivity.this)) {
            ivPhoto.buildDrawingCache();
            Bitmap bmap = ivPhoto.getDrawingCache();
            WallpaperManager m = WallpaperManager.getInstance(getApplicationContext());
            try {

                m.setBitmap(bmap);
                Snackbar snackbar = Snackbar
                        .make(content, "Wallpaper set!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(content, "Please check your network connection!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

    }

    private void setLockScreen() {
        if (UtilGlobal.isNetworkConnection(PhotoDetailActivity.this)) {

            ivPhoto.buildDrawingCache();
            Bitmap bmap = ivPhoto.getDrawingCache();
            WallpaperManager m = WallpaperManager.getInstance(getApplicationContext());
            try {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    m.setBitmap(bmap, null, true, WallpaperManager.FLAG_LOCK);
                }
                Snackbar snackbar = Snackbar
                        .make(content, "Wallpaper set!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            } catch (IOException e) {
                e.printStackTrace();

            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(content, "Please check your network connection!", Snackbar.LENGTH_SHORT);
            snackbar.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            break;
        }
    }
}