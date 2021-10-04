package com.poly.animewallpaper.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.poly.animewallpaper.model.Favorite;

import java.util.ArrayList;
import java.util.List;

public class FavoriteDAO {
    public static final String TABLE_NAME = "favorite";
    public DatabaseHelper dbHelper;
    public SQLiteDatabase db;
    public static final String CREATE_TABLE_VEHICLE = "create table favorite (photo text primary key  )";

    public FavoriteDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public long insert(Favorite favorite) {
        ContentValues values = new ContentValues();
        values.put("photo", favorite.getPhoto());

        long result = db.insert(TABLE_NAME, null, values);
        return result;

    }

    public List<Favorite> getAllFavorite() {
        List<Favorite> favoriteList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (cursor.isAfterLast() == false) {
            Favorite vehicle = new Favorite(cursor.getString(0));
            favoriteList.add(vehicle);

            cursor.moveToNext();
        }
        cursor.close();
        return favoriteList;
    }

    public long delete(String id) {
        long result = db.delete(TABLE_NAME, "photo" + "=?", new String[]{id});
        return result;

    }

    public boolean isFavorite(String str) {
        String truyvan = "SELECT * FROM  " + TABLE_NAME + " WHERE  " + "photo" + " = '" + str +  "'";

        Cursor cursor = db.rawQuery(truyvan, null);
        if (cursor.getCount() != 0) {
            return true;

        } else {
            return false;
        }
    }


}
