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

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticleListFragment extends Fragment implements ArticleAdapter.ArticleOnClickListener{

    private OnArticleListListener mListener;
    @BindView(R.id.main_activity_recycler_view)
    RecyclerView newListView;
    private Context context;
    private ArticleAdapter adapter;
    private ArticleListViewModel viewModel;
    @BindView(R.id.article_list_swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artcile_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        context = getContext();
        
        // init recycler view
        initRecyclerView();
        // fetch data & update list
        viewModelSetUp();
        // swipe to refresh layout listener
        swipeLayoutListener();
        
    }

    private void swipeLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                viewModel.startFetchingData(viewModel.getCurrentChannel());
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void viewModelSetUp() {

        viewModel = ViewModelProviders.of(this).get(ArticleListViewModel.class);
        
        viewModel.getNewsNetworkLiveData().observe(this, new Observer<List<Article>>() {
            @Override
            public void onChanged(@Nullable List<Article> articles) {
                if (articles != null) {
                    adapter.setList(articles);
                }
            }
        });
        
    }

    private void initRecyclerView() {
        adapter = new ArticleAdapter(context, ArticleListFragment.this);
        newListView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(context);
        newListView.setLayoutManager(manager);
       // adapter.setList();
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
}
