package com.example.test_loadmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IgetNews{
    RecyclerView recyclerView;
    List<Page> list;
    ProgressBar progressBar;
    int pageNumber = 1;
    Adapter adapter;
    private int visibleItem, scrolloutItem, totalItemCount;
    private boolean isScrolling = false;
    private boolean islast = true;
    LinearLayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        getNews(pageNumber - 1);
        layoutManager = new LinearLayoutManager(this);
        adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrolling = true;
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = layoutManager.getItemCount();
                visibleItem = layoutManager.getChildCount();
                scrolloutItem = layoutManager.findFirstVisibleItemPosition();
                if (isScrolling && islast && totalItemCount == visibleItem + scrolloutItem){
                    isScrolling = false;
                    islast = false;
//                    progressBar.setVisibility(View.VISIBLE);
                    loadMore();
                }
            }
        });

    }

    private void init() {
        recyclerView = findViewById(R.id.rcv);
        list = new ArrayList<>();
        progressBar = findViewById(R.id.loading);
    }

    private void getNews(int num) {
        GetNews getNews = new GetNews(this, this);
        getNews.execute(String.valueOf(num));
    }

    @Override
    public void OngetNews(List<Page> list) {
        this.list.addAll(list);
        adapter.notifyDataSetChanged();
    }

    private void loadMore(){
        pageNumber++;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getNews(pageNumber-1);
                islast = true;
//                progressBar.setVisibility(View.GONE);
            }
        }, 5000);
    }
}