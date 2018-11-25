package com.example.android.udacitycapstoneproject.ui.main.article_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
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
import com.example.android.udacitycapstoneproject.ui.main.SharedViewModel;
import com.example.android.udacitycapstoneproject.utils.AppConstants;

import java.util.List;

import timber.log.Timber;

// Todo before fetching data, check internet connectivity
public class ArticleListFragment extends Fragment implements ArticleAdapter.ArticleOnClickListener, SharedPreferences.OnSharedPreferenceChangeListener {

    private OnArticleListListener mListener;
    RecyclerView newListView;
    private String channel;
    private Context context;
    private ArticleAdapter adapter;
    private SharedViewModel sharedViewModel;
    private ArticleListViewModel articleListViewModel;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isTwoPane;
    private int orientation;

//    public static ArticleListFragment newInstance(String name){
//        Bundle bundle = new Bundle();
//        bundle.putString("channel_name", name);
//        ArticleListFragment listFragment = new ArticleListFragment();
//        listFragment.setArguments(bundle);
//        return listFragment;
//    }

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

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artcile_list, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        articleListViewModel.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        articleListViewModel.getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //ButterKnife.bind(this, view);
        initViews(view);
        
        // init recycler view
        initRecyclerView();
        // fetch data & update list
        viewModelSetUp();
        // swipe to refresh layout listener
        swipeLayoutListener(view);
        
    }

    private void initViews(View view) {
        context = getContext();
        isTwoPane = context.getResources().getBoolean(R.bool.isTablet);
        newListView = view.findViewById(R.id.main_activity_recycler_view);
        orientation = getResources().getConfiguration().orientation;
    }

    /**
     * set up recycler-view, set linear layout to it
     */
    private void initRecyclerView() {
        adapter = new ArticleAdapter(context, ArticleListFragment.this);
        newListView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        newListView.setLayoutManager(manager);
    }

    /**
     * set up view-model & set up list with updated network content
     */
    private void viewModelSetUp() {
        if(isTwoPane && (orientation != Configuration.ORIENTATION_LANDSCAPE)) {
            if (getActivity() != null) {
                sharedViewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
                sharedViewModel.getNewsNetworkLiveData().observe(this, new Observer<List<Article>>() {
                    @Override
                    public void onChanged(@Nullable List<Article> articles) {
                        swipeRefreshLayout.setRefreshing(false);
                        if (articles != null) {
                            adapter.setList(articles);
                        }
                        sharedViewModel.setArticleMutableLiveData(articles.get(0));
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
            articleListViewModel.getCurrentChannel().observe(this, new Observer<String>() {
                @Override
                public void onChanged(@Nullable String newChannel) {
                    Timber.d("current channel changed to: " + newChannel);
                    articleListViewModel.startFetchingData(newChannel);
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
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
                    articleListViewModel.startFetchingData(articleListViewModel.getChannelFromRepo());
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
        mListener.setArticleSelectedInDetailScreen(article);
    }

    //Todo shift shared preference access to view-model later
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(AppConstants.CURRENT_CHANNEL_KEY)) {
            channel = sharedPreferences.getString(AppConstants.CURRENT_CHANNEL_KEY,"");
            articleListViewModel.setCurrentChannel(channel);
            Timber.d("channel changed in nav bar " + channel);
        }
    }

    /**
     * interface implemented by main-activity to open detail activity
     */
    public interface OnArticleListListener {
        void setArticleSelectedInDetailScreen(Article article);
    }
}
