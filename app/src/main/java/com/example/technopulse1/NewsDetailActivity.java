package com.example.technopulse1;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;

public class NewsDetailActivity extends AppCompatActivity {
    ImageView iconBack;
    private ImageView newsImage;
    private TextView title, description, dateTime, author;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        iconBack = findViewById(R.id.iconBack);
        newsImage = findViewById(R.id.detailImage);
        title = findViewById(R.id.detailTitle);
        description = findViewById(R.id.detailDescription);
        dateTime = findViewById(R.id.detailDateTime);
        author = findViewById(R.id.detailAuthor);

        // Get data from Intent
        String newsTitle = getIntent().getStringExtra("title");
        String newsDescription = getIntent().getStringExtra("description");
        String newsImageUrl = getIntent().getStringExtra("imageUrl");
        String newsAuthor = getIntent().getStringExtra("author");
        long publishTime = getIntent().getLongExtra("publishTime", 0);

        title.setText(newsTitle);
        description.setText(newsDescription);
        author.setText("Posted by: " + newsAuthor);
        dateTime.setText("Date: " + TimeAgoUtil.formatFullDate(publishTime));
        Picasso.get().load(newsImageUrl).into(newsImage);

        // 🔙 Back
        iconBack.setOnClickListener(v -> finish());
    }
}
