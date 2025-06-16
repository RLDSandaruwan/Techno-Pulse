package com.example.technopulse1;

import android.content.Intent;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private Context context;
    private List<News> newsList;

    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = newsList.get(position);

        holder.newsTitle.setText(news.title);
        holder.newsDescription.setText(news.description);

        // ✅ Format time
        String timeAgo = TimeAgoUtil.getTimeAgo(news.publishTime);
        holder.newsTime.setText("Posted " + timeAgo + " by " + news.author);

        // ✅ Load image using Picasso
        Picasso.get()
                .load(news.imageUrl)
                .placeholder(R.drawable.placeholder_image) // make sure this image exists in drawable
                .error(R.drawable.placeholder_image)
                .into(holder.newsImage);

        // Optional: handle "Read More" click
        holder.newsReadMore.setOnClickListener(v -> {
            // You can later implement detailed view here
        });

        holder.newsReadMore.setOnClickListener(v -> {
            Intent intent = new Intent(context, NewsDetailActivity.class);
            intent.putExtra("title", news.title);
            intent.putExtra("description", news.description);
            intent.putExtra("imageUrl", news.imageUrl);
            intent.putExtra("author", news.author);
            intent.putExtra("publishTime", news.publishTime);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        ImageView newsImage;
        TextView newsTitle, newsDescription, newsTime, newsReadMore;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsImage = itemView.findViewById(R.id.newsImage);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsDescription = itemView.findViewById(R.id.newsDescription);
            newsTime = itemView.findViewById(R.id.newsTime);
            newsReadMore = itemView.findViewById(R.id.newsReadMore);
        }
    }
}
