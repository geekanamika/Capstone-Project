package com.example.android.udacitycapstoneproject.ui.detail.article_detail;

import android.arch.lifecycle.ViewModel;

import com.example.android.udacitycapstoneproject.data.local.model.Article;

class ArticleDetailViewModel extends ViewModel {
    private Article article;

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
}
