package com.example.android.udacitycapstoneproject.ui.detail.article_detail;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.ui.main.MainActivityViewModel;
import com.example.android.udacitycapstoneproject.utils.AppConstants;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class ArticleDetailFragment extends Fragment {

    private MainActivityViewModel viewModel;
    @BindView(R.id.toolbar_detail)
    Toolbar mToolbar;

    @BindView(R.id.news_image_detail)
    ImageView newsImage;

    @BindView(R.id.news_title_detail)
    TextView newsTitle;

    @BindView(R.id.news_author_detail)
    TextView newsAuthor;

    @BindView(R.id.news_description_detail)
    TextView newsDescription;

    private Context context;
    private LikeButton likeButton;
    private Article articleData;
    private boolean isTwoPane;


    public static ArticleDetailFragment newInstance(Article article) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(AppConstants.KEY_BUNDLE_PARCELLABLE, article);
        ArticleDetailFragment fragment = new ArticleDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);

        if (getArguments() != null) {
            articleData = getArguments().getParcelable(AppConstants.KEY_BUNDLE_PARCELLABLE);
        }
        context = getContext();
        isTwoPane = context.getResources().getBoolean(R.bool.isTablet);
        if(isTwoPane) {
            viewModel = ViewModelProviders.of(getActivity()).get(MainActivityViewModel.class);
        }
        return inflater.inflate(R.layout.article_detail_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(isTwoPane) {
            viewModel.getArticleMutableLiveData().observe(this, new Observer<Article>() {
                @Override
                public void onChanged(@Nullable Article article) {
                    articleData = article;
                    setValuesToViews();
                }
            });
        }
        likeButton = view.findViewById(R.id.heart_button);
        setValuesToViews();
        heartButtonListener();
    }

    private void heartButtonListener() {
        likeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Timber.d("liked");
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Timber.d("disliked");
            }
        });
    }

    private void setValuesToViews() {
        if (!TextUtils.isEmpty(articleData.getUrlToImage())) {
            Picasso.with(context)
                    .load(articleData.getUrlToImage())
                    .error(R.drawable.error)
                    .into(newsImage);
        }
        // set title
        newsTitle.setText(articleData.getTitle());

        // set author
        newsAuthor.setText(getString(R.string.by, articleData.getAuthor()));

        // set title
        newsDescription.setText(articleData.getDescription());
    }

    @OnClick(R.id.share_button)
    public void shareButtonListener() {
        String mimeType = "text/plain";
        String title = "Share Article";
        String newsTitle = articleData.getTitle();
        String newsUrl = articleData.getUrl();

        if (getActivity() != null) {
            ShareCompat.IntentBuilder.from(getActivity())
                    .setChooserTitle(title)
                    .setType(mimeType)
                    .setText(newsTitle + "\n" + newsUrl)
                    .startChooser();
        }

    }

    @OnClick(R.id.btn_open_article)
    public void openButtonListener() {
        Timber.d("open button");

    }

}
