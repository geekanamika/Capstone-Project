package com.example.android.udacitycapstoneproject.ui.favourites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.android.udacitycapstoneproject.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anamika Tripathi on 22/11/18.
 */
public class FavouriteActivity extends AppCompatActivity {

    @BindView(R.id.main_activity_toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        ButterKnife.bind(this);
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().add(R.id.news_article_list_container,
                new FavouriteListFragment(), getString(R.string.tag_fav_list_fragment)).commit();

    }

}
