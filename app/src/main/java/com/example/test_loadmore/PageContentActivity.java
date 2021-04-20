package com.example.test_loadmore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PageContentActivity extends AppCompatActivity {

    ImageView banner;
    TextView title, txtdate, content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_content);
        init();

        Intent intent = getIntent();
        Page page = (Page) intent.getSerializableExtra("page");
        Picasso.with(this).load("http://vidoco.vn/uploads/news/" + page.getImg()).into(banner);
        title.setText(page.getTitle());
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = originalFormat.parse(page.getDate());
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = newFormat.format(date);
            txtdate.setText(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        content.setText(page.getContent());
    }

    private void init() {
        banner = findViewById(R.id.banner);
        title = findViewById(R.id.title);
        txtdate = findViewById(R.id.date);
        content = findViewById(R.id.content);
    }
}