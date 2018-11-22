package com.example.android.udacitycapstoneproject.ui.favourites;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.ui.main.article_list.ArticleAdapter;

import java.util.List;

import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 22/11/18.
 */
public class FavouriteListFragment extends Fragment implements ArticleAdapter.ArticleOnClickListener{
    private boolean isTwoPane;
    private RecyclerView newListView;
    private Context context;
    private ArticleAdapter adapter;
    private FavouriteViewmodel favouriteViewmodel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artcile_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initViews(view);
        initRecyclerView();
        viewModelSetUp();
    }

    private void initViews(View view) {
        context = getContext();
        isTwoPane = context.getResources().getBoolean(R.bool.isTablet);
        newListView = view.findViewById(R.id.main_activity_recycler_view);
    }

    /**
     * set up recycler-view, set linear layout to it
     */
    private void initRecyclerView() {
        adapter = new ArticleAdapter(context, FavouriteListFragment.this);
        newListView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        newListView.setLayoutManager(manager);
    }

    /**
     * set up view-model & set up list with updated network content
     */
    private void viewModelSetUp() {
        favouriteViewmodel = ViewModelProviders.of(this).get(FavouriteViewmodel.class);
        favouriteViewmodel.getFavouriteMovies().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null) {
                    adapter.setList(articles);
                }
            }
        });

    }

    @Override
    public void onArticleClick(Article article) {
        Timber.d(article.getTitle());
    }

    /**
     * interface implemented by main-activity to open detail activity
     */
    public interface OnArticleListListener {
        void setArticleSelectedInDetailScreen(Article article);
    }
}
