package com.example.test_loadmore;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IgetNews {

    SwipeRefreshLayout swipeRefreshLayout;
    NestedScrollView nestedScrollView;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    Dialog dialog;
    Button btn_tryagain;
    Adapter adapter;
    List<Page> list;
    int pageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //set adapter and layoutmanager
        adapter = new Adapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerView.setHasFixedSize(true);

        if (!isConnected()) {
            onNoInternet();
        } else {
            //load first 10 news
            getNews(pageNumber);

            swipeRefreshLayout.setOnRefreshListener(() -> {
                if (isConnected()) {
                    onRefresh();
                } else {
                    onNoInternet();
                }
                swipeRefreshLayout.setRefreshing(false);
            });

            //set listener when scroll to last view
            nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (v.getChildAt(v.getChildCount() - 1) != null) {
                    if ((scrollY >= v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight()) && scrollY > oldScrollY) {
                        if (!isConnected()){
                            onNoInternet();
                        } else {
                            loadMore();
                        }
                    }
                }
            });
        }
    }

    private void init() {
        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        nestedScrollView = findViewById(R.id.nestedscrollview);
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

    private void loadMore() {
        //pageNumber++ to load next 10 news from server
        pageNumber++;

        //add a null to list, adapter will create progressbar
        list.add(null);
        adapter.notifyItemInserted(list.size() - 1);

        new Handler().postDelayed(() -> {
            //remove null from list then call getNews to fetch more 10 news to list
            list.remove(list.size() - 1);
            getNews(pageNumber);
        }, 1000);
    }

    private void onRefresh() {
        list.clear();
        pageNumber = 0;
        getNews(pageNumber);
    }

    private void onNoInternet() {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_nointernet_layout);
        btn_tryagain = dialog.findViewById(R.id.btn_tryagain);
        dialog.setCancelable(false);

        btn_tryagain.setOnClickListener(v -> {
            if (isConnected()) {
                dialog.hide();
                getNews(0);
            }
        });
        dialog.show();
    }

    //Check internet connection
    private boolean isConnected() {
        String command = "ping -c 1 google.com";
        try {
            return Runtime.getRuntime().exec(command).waitFor() == 0;
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}