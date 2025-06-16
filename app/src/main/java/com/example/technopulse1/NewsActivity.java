package com.example.technopulse1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private TextView textWelcome, latestTitle, latestDesc, latestReadMore, textCategoryHeader;
    private ImageView latestImage, iconMenu;
    private DrawerLayout drawerLayout;
    private RecyclerView recyclerNews;
    private BottomNavigationView bottomNavigation;

    private LinearLayout btnDeveloper, btnUserDetails, btnCloseDrawer;

    private List<News> allNewsList = new ArrayList<>();
    private List<News> filteredNewsList = new ArrayList<>();
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        // 🔧 UI References
        textWelcome = findViewById(R.id.textWelcome);
        recyclerNews = findViewById(R.id.recyclerNews);
        latestImage = findViewById(R.id.latestImage);
        latestTitle = findViewById(R.id.latestTitle);
        latestDesc = findViewById(R.id.latestDesc);
        latestReadMore = findViewById(R.id.latestReadMore);
        textCategoryHeader = findViewById(R.id.textCategoryHeader);
        iconMenu = findViewById(R.id.iconMenu);
        ImageView iconProfile = findViewById(R.id.iconProfile);
        drawerLayout = findViewById(R.id.drawerLayout);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        btnDeveloper = findViewById(R.id.btnDeveloper);
        btnUserDetails = findViewById(R.id.btnUserDetails);
        btnCloseDrawer = findViewById(R.id.btnCloseDrawer);

        // icon profile
        iconProfile.setOnClickListener(v -> {
            startActivity(new Intent(NewsActivity.this, UserInfoActivity.class));
        });

        // 📰 RecyclerView Setup
        newsAdapter = new NewsAdapter(this, filteredNewsList);
        recyclerNews.setLayoutManager(new LinearLayoutManager(this));
        recyclerNews.setAdapter(newsAdapter);

        // 🔄 Load Firebase Data
        loadUsername();
        fetchAllNews();  // loads into allNewsList

        // ☰ Drawer Toggle
        iconMenu.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
                drawerLayout.closeDrawer(GravityCompat.END);
            } else {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        btnCloseDrawer.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));
        btnDeveloper.setOnClickListener(v -> {
            // TODO: Open Developer Details
        });
        btnUserDetails.setOnClickListener(v -> {
            // TODO: Open User Info
        });

        // 📚 Bottom Navigation Filter
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                filterNewsByCategory(null);
                textCategoryHeader.setText("All News");
            } else if (itemId == R.id.nav_sports) {
                filterNewsByCategory("Sports");
                textCategoryHeader.setText("Sports News");
            } else if (itemId == R.id.nav_academic) {
                filterNewsByCategory("Academic");
                textCategoryHeader.setText("Academic News");
            } else if (itemId == R.id.nav_faculty) {
                filterNewsByCategory("Faculty");
                textCategoryHeader.setText("Faculty News");
            }
            return true;
        });

        btnCloseDrawer.setOnClickListener(v -> drawerLayout.closeDrawer(GravityCompat.END));

        // Open Developer Info
        btnDeveloper.setOnClickListener(v ->
                startActivity(new Intent(NewsActivity.this, DeveloperActivity.class)));

        // Open User Info
        btnUserDetails.setOnClickListener(v ->
                startActivity(new Intent(NewsActivity.this, UserInfoActivity.class)));
    }

    private void loadUsername() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String uid = user.getUid();
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users").child(uid).child("username");

            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String username = snapshot.exists() ? snapshot.getValue(String.class) : "User";
                    textWelcome.setText("Hi, " + username);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    textWelcome.setText("Hi, User");
                }
            });
        }
    }

    private void fetchAllNews() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("news");

        ref.orderByChild("publishTime").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allNewsList.clear();

                for (DataSnapshot snap : snapshot.getChildren()) {
                    News news = snap.getValue(News.class);
                    if (news != null) {
                        allNewsList.add(news);
                    }
                }

                Collections.sort(allNewsList, (n1, n2) -> Long.compare(n2.publishTime, n1.publishTime));
                filterNewsByCategory(null); // Show all by default
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Optional error logging
            }
        });
    }

    private void filterNewsByCategory(String category) {
        filteredNewsList.clear();

        for (News item : allNewsList) {
            if (category == null || item.category.equalsIgnoreCase(category)) {
                filteredNewsList.add(item);
            }
        }

        newsAdapter.notifyDataSetChanged();

        if (!filteredNewsList.isEmpty()) {
            News latest = filteredNewsList.get(0);
            Picasso.get().load(latest.imageUrl).into(latestImage);
            latestTitle.setText(latest.title);
            latestDesc.setText(latest.description);
            latestReadMore.setOnClickListener(v -> {
                // TODO: show full detail
            });
        } else {
            latestTitle.setText("");
            latestDesc.setText("");
            latestImage.setImageResource(R.drawable.placeholder_image);
        }

        latestReadMore.setOnClickListener(v -> {
            News latest = filteredNewsList.get(0);
            Intent intent = new Intent(NewsActivity.this, NewsDetailActivity.class);
            intent.putExtra("title", latest.title);
            intent.putExtra("description", latest.description);
            intent.putExtra("imageUrl", latest.imageUrl);
            intent.putExtra("author", latest.author);
            intent.putExtra("publishTime", latest.publishTime);
            startActivity(intent);
        });

    }
}
