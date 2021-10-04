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
import com.poly.animewallpaper.model.Favorite;
import com.poly.animewallpaper.view.activity.PhotoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    private Context context;
    private List<Favorite> favorites;

    public FavoriteAdapter(Context context, List<Favorite> favorites) {
        this.context = context;
        this.favorites = favorites;
    }

    @NonNull
    @Override
    public FavoriteAdapter.FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteAdapter.FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.FavoriteViewHolder holder, int position) {
        Favorite favorite= favorites.get(position);
        Picasso.with(context).load(favorite.getPhoto()).fit().centerCrop().into(holder.ivPhoto);
        holder.itemView.setOnClickListener(v -> {
            Intent intent= new Intent(context, PhotoDetailActivity.class);
            intent.putExtra("large",favorite.getPhoto());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }
    public class FavoriteViewHolder extends RecyclerView.ViewHolder{
         ImageView ivPhoto;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto=itemView.findViewById(R.id.iv_photo);
        }
    }
}
