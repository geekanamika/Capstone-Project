package com.example.android.udacitycapstoneproject.ui.main.article_list;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
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
import android.widget.ProgressBar;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;

import java.util.List;

// Todo before fetching data, check internet connectivity
public class ArticleListFragment extends Fragment implements ArticleAdapter.ArticleOnClickListener{

    private OnArticleListListener mListener;
    RecyclerView newListView;
    private String channel;
    private Context context;
    private ArticleAdapter adapter;
    private ArticleListViewModel viewModel;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        if (getArguments() != null) {
            channel = getArguments().getString("channel_name");
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artcile_list, container, false);
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
        newListView = view.findViewById(R.id.main_activity_recycler_view);
        context = getContext();
       // progressBar = view.findViewById(R.id.loading_indicator);
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

        viewModel = ViewModelProviders.of(this).get(ArticleListViewModel.class);
        viewModel.setChannel(channel);
        viewModel.getNewsNetworkLiveData().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                swipeRefreshLayout.setRefreshing(false);
                if (articles != null) {
                    adapter.setList(articles);
                }
            }
        });

    }

    /**
     * on swipe, refresh content by re-fetching data
     */
    private void swipeLayoutListener(View view) {
        swipeRefreshLayout = view.findViewById(R.id.article_list_swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.startFetchingData(viewModel.getChannel());
                //swipeRefreshLayout.setRefreshing(false);
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


    public interface OnArticleListListener {
        void setArticleSelectedInDetailScreen(Article article);
    }

    public void changeMenuItemUpdateNewsList(String channel){
        swipeRefreshLayout.setRefreshing(true);
        viewModel.setChannel(channel);
    }
}