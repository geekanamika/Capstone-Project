package com.example.android.udacitycapstoneproject.ui.favourites;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.AppConstants;

/**
 * Created by Anamika Tripathi on 22/11/18.
 */
public class FavouriteDetailFragment extends Fragment {

    private Article articleData;
    private Context context;

    public static FavouriteDetailFragment newInstance(Article article) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.KEY_BUNDLE_PARCELLABLE, article);
        FavouriteDetailFragment fragment = new FavouriteDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (getArguments() != null) {
            articleData = getArguments().getParcelable(AppConstants.KEY_BUNDLE_PARCELLABLE);
        }
        context = getContext();
        return inflater.inflate(R.layout.article_detail_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}
