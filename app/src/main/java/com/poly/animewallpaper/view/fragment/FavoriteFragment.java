package com.poly.animewallpaper.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.poly.animewallpaper.R;
import com.poly.animewallpaper.adapter.FavoriteAdapter;
import com.poly.animewallpaper.data.FavoriteDAO;
import com.poly.animewallpaper.model.Favorite;

import java.util.Collections;
import java.util.List;


public class FavoriteFragment extends Fragment {
    private FavoriteAdapter favoriteAdapter;
    private View view;
    private RecyclerView rvFavorite;
    private FavoriteDAO favoriteDAO;
    private List<Favorite> favorites;
    private LinearLayout notifi;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initActions();
    }

    private void initViews() {
        favoriteDAO = new FavoriteDAO(getContext());
        rvFavorite=view.findViewById(R.id.rv_favorite);
        notifi=view.findViewById(R.id.notifi);
    }

    private void initActions() {
        favorites=favoriteDAO.getAllFavorite();
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 3);
        Collections.reverse(favorites);
        rvFavorite.setLayoutManager(layoutManager);
        favoriteAdapter= new FavoriteAdapter(getContext(),favorites);
        rvFavorite.setAdapter(favoriteAdapter);
        if (favorites.size()<1){
            rvFavorite.setVisibility(View.GONE);
            notifi.setVisibility(View.VISIBLE);
        }else {
            rvFavorite.setVisibility(View.VISIBLE);
            notifi.setVisibility(View.GONE);
        }

    }

}