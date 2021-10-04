package com.poly.animewallpaper.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.poly.animewallpaper.R;
import com.poly.animewallpaper.model.Collections;
import com.poly.animewallpaper.utils.UtilGlobal;
import com.poly.animewallpaper.view.activity.PhotoByThemeActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable {
    private Context context;
    private List<Collections> collectionsList;
    private List<Collections> backup;
    private FilterListeners filterListeners;

    public ThemeAdapter(Context context, List<Collections> collectionsList) {
        this.context = context;
        this.collectionsList = collectionsList;
        backup = new ArrayList<>(collectionsList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ThemeAdapter.ThemeViewHolder(view);


    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ThemeViewHolder viewHolder = (ThemeViewHolder) holder;
        final Collections collections = collectionsList.get(position);
        Picasso.with(context).load(collections.getPreview()).fit().centerCrop().into(viewHolder.ivPhoto);
        viewHolder.tvName.setText(collections.getName());
        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PhotoByThemeActivity.class);
            intent.putExtra("name", collections.getName());
            context.startActivity(intent);

        });
    }


    @Override
    public int getItemCount() {
        if (collectionsList != null) {
            return collectionsList.size();

        }
        return 0;
    }
    @Override
    public int getItemViewType(int position) {
        return collectionsList.get(position) == null ? UtilGlobal.VIEW_TYPE_LOADING : UtilGlobal.VIEW_TYPE_ITEM;
    }
    @Override
    public Filter getFilter() {
        return filter;


    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence keyword) {
            ArrayList<Collections> filterCollections = new ArrayList<>();

            if (keyword.toString().isEmpty()) {
                filterCollections.addAll(backup);

            } else {
                for (Collections collections : backup) {
                    if (collections.getName().toString().toLowerCase().contains(keyword.toString().toLowerCase()))
                        filterCollections.add(collections);
                }

            }

            FilterResults results = new FilterResults();
            results.values = filterCollections;

            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            collectionsList.clear();
            collectionsList.addAll((ArrayList<Collections>) results.values);
            if (filterListeners != null)
                filterListeners.filteringFinished(collectionsList.size());
            notifyDataSetChanged();


        }
    };

    public void setFilterListeners(FilterListeners filterFinishedListener) {
        filterListeners = filterFinishedListener;
    }

    public interface FilterListeners {
        void filteringFinished(int filteredItemsCount);
    }

    public class ThemeViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        TextView tvName;

        public ThemeViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            tvName = itemView.findViewById(R.id.tv_name);

        }

    }

//    public class LoadingHolder extends RecyclerView.ViewHolder {
//        ProgressBar progressBar;
//
//        public LoadingHolder(@NonNull View itemView) {
//            super(itemView);
//            progressBar = itemView.findViewById(R.id.progressBar1);
//
//        }
//
//    }

}
