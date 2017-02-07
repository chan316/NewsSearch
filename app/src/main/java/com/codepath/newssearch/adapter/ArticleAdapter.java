package com.codepath.newssearch.adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
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
        holder.webUrl = currentArticle.getWebUrl();

        String thumbnailUrl = currentArticle.getThumbnail();

        if (thumbnailUrl != null && !thumbnailUrl.isEmpty()) {
            Picasso.with(getContext()).load(thumbnailUrl).into(holder.thumbnail);
        }

    }
    @Override
    public int getItemCount() {
        return articles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView headline;
        String webUrl;
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);

            this.headline = (TextView) itemView.findViewById(R.id.tvHeadline);
            this.headline.setOnClickListener(this);
            this.thumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);
            this.thumbnail.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Article currentArticle = articles.get(position);
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();

                Bitmap bitmap = BitmapFactory.decodeResource(v.getResources(), R.drawable.ic_action_share);
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, currentArticle.getWebUrl());
                PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 200, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setActionButton(bitmap, "Share Link", pendingIntent, true);
                builder.setShowTitle(true);
                builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                builder.addDefaultShareMenuItem();

                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl((Activity) getContext(), Uri.parse(currentArticle.getWebUrl()));

            }
        }


    }
}
