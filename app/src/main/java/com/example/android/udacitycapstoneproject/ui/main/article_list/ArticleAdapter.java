package com.example.android.udacitycapstoneproject.ui.main.article_list;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.udacitycapstoneproject.R;
import com.example.android.udacitycapstoneproject.data.local.model.Article;
import com.example.android.udacitycapstoneproject.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Anamika Tripathi on 13/11/18.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.MyViewHolder> {
    private List<Article> listOfArticles;
    private Context mContext;
    private final ArticleOnClickListener articleOnClickListener;

    public ArticleAdapter(Context mContext,
                          ArticleOnClickListener articleOnClickListener) {
        listOfArticles = new ArrayList<>();
        this.mContext = mContext;
        this.articleOnClickListener = articleOnClickListener;
    }

    public void setList(List<Article> articles) {
        listOfArticles = articles;
        notifyDataSetChanged();
    }

    /**
     * The interface that receives onArticleClick messages.
     */
    public interface ArticleOnClickListener {
        void onArticleClick(Article article);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_news_article,
                        viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        //set thumbnail associated with the article
        if (!TextUtils.isEmpty(listOfArticles.get(i).getUrlToImage())) {
            Picasso.with(mContext)
                    .load(listOfArticles.get(i).getUrlToImage())
                    .error(R.drawable.error)
//                    .resize(myViewHolder.itemNewsThumbnail.getMeasuredWidth(),myViewHolder.itemNewsThumbnail.getMeasuredHeight())
//                    .centerCrop()
                    .into(myViewHolder.itemNewsThumbnail);
        }

        //set news title
        myViewHolder.itemNewsTitle.setText(listOfArticles.get(i).getTitle());

        //set time
        myViewHolder.publishedTime.setText(DateUtils.manipulateDateFormat(
                listOfArticles.get(i).getPublishedAt()));

    }

    @Override
    public int getItemCount() {
        if (listOfArticles.size() > 0)
            return listOfArticles.size();
        else return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.item_news_thumbnail)
        ImageView itemNewsThumbnail;
        @BindView(R.id.item_news_title)
        TextView itemNewsTitle;
        @BindView(R.id.item_published_time)
        TextView publishedTime;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();

            articleOnClickListener.onArticleClick(listOfArticles.get(clickedPosition));
        }
    }
}
