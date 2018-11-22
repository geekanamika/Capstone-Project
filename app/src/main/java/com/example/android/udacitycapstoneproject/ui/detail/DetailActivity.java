package com.example.android.udacitycapstoneproject.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.ui.detail.article_detail.ArticleDetailFragment;
import com.example.android.udacitycapstoneproject.utils.AppConstants;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        retrieveData();
    }

    private void retrieveData() {
        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }
        if (intent != null) {
            Bundle data = intent.getExtras();
            if (data != null) {
                Article article = data.getParcelable(AppConstants.KEY_BUNDLE_PARCELLABLE);
                if (article != null) {
                    ArticleDetailFragment detailFragment = ArticleDetailFragment.newInstance(article);
                    getSupportFragmentManager().beginTransaction().add(R.id.news_article_detail_container,
                            detailFragment, getString((R.string.detail_fragment))).commit();
                }

            }
        }
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }
}
