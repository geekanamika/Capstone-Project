package com.example.android.udacitycapstoneproject.ui.favourites;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.ui.detail.DetailActivity;
import com.example.android.udacitycapstoneproject.ui.detail.article_detail.ArticleDetailFragment;
import com.example.android.udacitycapstoneproject.ui.main.SharedViewModel;
import com.example.android.udacitycapstoneproject.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Created by Anamika Tripathi on 22/11/18.
 */
public class FavouriteActivity extends AppCompatActivity implements FavouriteListFragment.OnArticleListListener {

    @BindView(R.id.main_activity_toolbar)
    Toolbar toolbar;
    private boolean isTwoPane;
    private SharedViewModel sharedViewModel;
    private int orientation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        isTwoPane = getResources().getBoolean(R.bool.isTablet);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        orientation = getResources().getConfiguration().orientation;
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel.class);
        setUpUIForDifferentScreenSize();
    }

    /**
     * @param article : open new activity if mobile device
     */
    @Override
    public void setArticleSelectedInDetailScreen(Article article, boolean flag) {
        if(isTwoPane && (orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            if(flag){
                getSupportFragmentManager().beginTransaction().replace(R.id.news_article_detail_container,
                        ArticleDetailFragment.newInstance(article),
                        getString(R.string.tag_detail_fragment)).commit();
            }
            sharedViewModel.setArticleMutableLiveData(article);
        }
        else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(AppConstants.KEY_BUNDLE_PARCELLABLE, article);
            startActivity(intent);
        }
    }

    /**
     * check if it's tablet or mobile phone, set layout accordingly
     */
    private void setUpUIForDifferentScreenSize() {
        setNewListFragment();
    }

    private void setNewListFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.news_article_list_container,
                new FavouriteListFragment(), getString(R.string.tag_fav_list_fragment)).commit();
    }

}
