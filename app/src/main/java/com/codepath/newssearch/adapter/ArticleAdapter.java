package com.codepath.newssearch.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.newssearch.R;
import com.codepath.newssearch.model.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by chanis on 1/30/17.
 */

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ViewHolder> {
    private List<Article> articles;
    private Context context;

    public ArticleAdapter(Context context, List<Article> articles) {
        this.articles = articles;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LinearLayout layout = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);

        ViewHolder holder = new ViewHolder(layout);
        return holder;
    }

    private Context getContext() {
        return context;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Article currentArticle = articles.get(position);
        holder.headline.setText(currentArticle.getHeadline());

        String thumbnailUrl = currentArticle.getThumbnail();

        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            Picasso.with(getContext()).load(thumbnailUrl).into(holder.thumbnail);
        }

    }
    @Override
    public int getItemCount() {
        return articles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView headline;
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            this.headline = (TextView) itemView.findViewById(R.id.tvHeadline);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
        }
    }
}
