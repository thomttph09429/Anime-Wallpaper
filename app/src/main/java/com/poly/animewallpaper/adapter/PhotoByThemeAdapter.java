package com.poly.animewallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.animewallpaper.R;
import com.poly.animewallpaper.model.PhotoByTheme;
import com.poly.animewallpaper.view.activity.PhotoByThemeActivity;
import com.poly.animewallpaper.view.activity.PhotoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoByThemeAdapter extends RecyclerView.Adapter<PhotoByThemeAdapter.ListPhotoViewHolder> {
    private Context context;
    private List<PhotoByTheme>  photoByThemes;

    public PhotoByThemeAdapter(Context context, List<PhotoByTheme> photoByThemes) {
        this.context = context;
        this.photoByThemes = photoByThemes;
    }

    @NonNull
    @Override
    public PhotoByThemeAdapter.ListPhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo_by_theme, parent, false);
        return new PhotoByThemeAdapter.ListPhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoByThemeAdapter.ListPhotoViewHolder holder, int position) {
        PhotoByTheme photoByTheme= photoByThemes.get(position);
        if (!photoByTheme.getThumb().equals("")){
            Picasso.with(context).load(photoByTheme.getThumb()).fit().centerCrop().into(holder.ivPhoto);

        }
        holder.itemView.setOnClickListener(v -> {
            Intent intent= new Intent(context, PhotoDetailActivity.class);
            intent.putExtra("thumb",photoByTheme.getThumb());
            intent.putExtra("large",photoByTheme.getLarge());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return photoByThemes.size();
    }
    public  class ListPhotoViewHolder extends RecyclerView.ViewHolder{
        ImageView ivPhoto;

        public ListPhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);

        }
    }
}
