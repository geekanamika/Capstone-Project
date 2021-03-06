package com.example.android.udacitycapstoneproject.ui.main.article_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.sync.NewsSyncUtils;
import com.example.android.udacitycapstoneproject.ui.main.SharedViewModel;
import com.example.android.udacitycapstoneproject.utils.AppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

import static com.example.android.udacitycapstoneproject.utils.AppConstants.BUNDLE_RECYCLER_LAYOUT;

public class ArticleListFragment extends Fragment implements ArticleAdapter.ArticleOnClickListener {

    private OnArticleListListener mListener;
    @BindView(R.id.main_activity_recycler_view)
    RecyclerView newListView;
    private Context context;
    private ArticleAdapter adapter;
    private SharedViewModel sharedViewModel;
    private ArticleListViewModel articleListViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isTwoPane;
    private int orientation;
    private boolean isFirstRun;
    private Parcelable recyclerViewState;
    private LinearLayoutManager layoutManager;

    public static ArticleListFragment newInstance(String name){
        Bundle bundle = new Bundle();
        bundle.putString("channel_name", name);
        ArticleListFragment listFragment = new ArticleListFragment();
        listFragment.setArguments(bundle);
        return listFragment;
    }


    public ArticleListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        articleListViewModel = ViewModelProviders.of(this).get(ArticleListViewModel.class);
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable(AppConstants.KEY_RECYCLER_MAIN);
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artcile_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initViews(view);

        
        // init recycler view
        initRecyclerView();
        // fetch data & update list
        viewModelSetUp();
        // swipe to refresh layout listener
        swipeLayoutListener(view);
        if (getArguments() != null) {
            String channel = getArguments().getString("channel_name");
            changeMenuItemUpdateNewsList(channel);
        }
        
    }

    @Override
    public void onResume() {
        super.onResume();
        if (recyclerViewState != null) {
            layoutManager.onRestoreInstanceState(recyclerViewState);
        }
    }

    private void initViews(View view) {
        context = getContext();
        isTwoPane = context.getResources().getBoolean(R.bool.isTablet);
        orientation = getResources().getConfiguration().orientation;
    }

    /**
     * set up recycler-view, set linear layout to it
     */
    private void initRecyclerView() {
        adapter = new ArticleAdapter(context, ArticleListFragment.this);
        newListView.setAdapter(adapter);

        layoutManager = new LinearLayoutManager(context);
        newListView.setLayoutManager(layoutManager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                newListView.getLayoutManager().onSaveInstanceState());
    }

    /**
     * set up view-model & set up list with updated network content
     */
    private void viewModelSetUp() {
        if(isTwoPane && (orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            if (getActivity() != null) {
                sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
                isFirstRun = articleListViewModel.isFirstRun();
                if(isFirstRun) {
                    articleListViewModel.setFirstRun();
                    NewsSyncUtils.scheduleFirebaseJobDispatcherSync(getActivity());
                }
                sharedViewModel.getNewsNetworkLiveData().observe(this, new Observer<List<Article>>() {
                    @Override
                    public void onChanged(@Nullable List<Article> articles) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (articles != null) {
                            adapter.setList(articles);
                        }
                        mListener.setArticleSelectedInDetailScreen(articles.get(0), true);
                    }
                });
                sharedViewModel.getChannel().observe(this, new Observer<String>() {
                    @Override
                    public void onChanged(@Nullable String s) {
                        swipeRefreshLayout.setRefreshing(true);
                        sharedViewModel.startFetchingData(s);
                    }
                });
            }
        } else {
            isFirstRun = articleListViewModel.isFirstRun();
            if(isFirstRun) {
                articleListViewModel.setFirstRun();
                NewsSyncUtils.scheduleFirebaseJobDispatcherSync(getActivity());
            }
            articleListViewModel.getNewsNetworkLiveData().observe(this, new Observer<List<Article>>() {
                @Override
                public void onChanged(@Nullable List<Article> articles) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (articles != null) {
                        adapter.setList(articles);
                    }

                }
            });
        }
    }

    /**
     * on swipe, refresh content by re-fetching data
     */
    private void swipeLayoutListener(View view) {
        swipeRefreshLayout = view.findViewById(R.id.article_list_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(isTwoPane && (orientation != Configuration.ORIENTATION_LANDSCAPE)){
                    sharedViewModel.startFetchingData(sharedViewModel.getChannel().getValue());
                } else {
                    articleListViewModel.startFetchingData(articleListViewModel.getCurrentChannel());
                }

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnArticleListListener) {
            mListener = (OnArticleListListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnArticleListListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override

    public void onArticleClick(Article article) {
        mListener.setArticleSelectedInDetailScreen(article, false);
    }

    /**
     * interface implemented by main-activity to open detail activity
     */
    public interface OnArticleListListener {
        void setArticleSelectedInDetailScreen(Article article, boolean flagForNewFragment);
    }

    public void changeMenuItemUpdateNewsList(String channel){
        swipeRefreshLayout.setRefreshing(true);
        articleListViewModel.setChannel(channel);
    }
}
