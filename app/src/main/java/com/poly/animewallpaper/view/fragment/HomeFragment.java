package com.poly.animewallpaper.view.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.poly.animewallpaper.R;
import com.poly.animewallpaper.adapter.ThemeAdapter;
import com.poly.animewallpaper.model.Collections;
import com.poly.animewallpaper.utils.DialogCheckConnection;
import com.poly.animewallpaper.utils.UtilGlobal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements ThemeAdapter.FilterListeners {
    private RecyclerView rvCategory;
    private View view;
    private RequestQueue requestQueue;
    private List<Collections> collectionsList;
    private EditText edtSearch;
    private ThemeAdapter themeAdapter;
    private LinearLayout lvNotResult;
    private ProgressBar progressBar;
    private String url = "https://dl.dropboxusercontent.com/s/nte1zw7cwsiw9j6/apiAnimeCollection.txt?dl=0";
    private DialogCheckConnection dialogCheckConnection;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initActions();
        fetchThemb();
        if (UtilGlobal.isNetworkConnection(getContext())) {


            edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    searchThemb(s.toString().toLowerCase());


                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }

    private void initViews() {
        rvCategory = view.findViewById(R.id.rv_category);
        edtSearch = view.findViewById(R.id.edt_search);
        lvNotResult = view.findViewById(R.id.ln_not_result);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void initActions() {
        dialogCheckConnection = dialogCheckConnection.getInstance();
        dialogCheckConnection.init(getContext());
        requestQueue = Volley.newRequestQueue(getContext());
        collectionsList = new ArrayList<>();
        LinearLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        rvCategory.setLayoutManager(layoutManager);



    }


    private void searchThemb(String strThemb) {
        themeAdapter.getFilter().filter(strThemb);

    }


    private void fetchThemb() {


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url
                , null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (response != null) {
                    try {
                        JSONObject jsonObject = response.getJSONObject("AnimeCollection");
                        JSONArray jsonArray = jsonObject.getJSONArray("Collections");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object1 = jsonArray.getJSONObject(i);

                            String preview = object1.getString("preview");
                            StringBuilder sb = new StringBuilder();
                            sb.append(preview);
                            sb.replace(0, 23, "https://dl.dropboxusercontent.com");

                            Collections collections = new Collections();
                            collections.setName((String) object1.get("name"));
                            collections.setPreview(sb.toString());
                            collectionsList.add(collections);

                        }
                        rvCategory.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        themeAdapter = new ThemeAdapter(getContext(), collectionsList);
                        themeAdapter.setFilterListeners(HomeFragment.this::filteringFinished);
                        rvCategory.setAdapter(themeAdapter);
                        themeAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    dialogCheckConnection.AlertDialog(getContext(), "An error occurred, please try again!");
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (UtilGlobal.isNetworkConnection(getContext())) {
                    fetchThemb();
                } else {
                    progressBar.setVisibility(View.GONE);
                    dialogCheckConnection.AlertDialog(getContext(), "Please check your network connection!");
                }

            }
        });
        requestQueue.add(request);

    }


    @Override
    public void filteringFinished(int filteredItemsCount) {
        if (filteredItemsCount == 0) {

            rvCategory.setVisibility(View.GONE);
            lvNotResult.setVisibility(View.VISIBLE);


        } else {
            lvNotResult.setVisibility(View.GONE);
            rvCategory.setVisibility(View.VISIBLE);
        }
    }


}