package com.example.App_News;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Page> list;

    private static final int ITEMVIEW = 0, LOADINGVIEW = 1;

    public Adapter(Context context, List<Page> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case ITEMVIEW:
                v = LayoutInflater.from(context).inflate(R.layout.custom_rcv_page, parent, false);
                return new ItemHolder(v);
            case LOADINGVIEW:
                v = LayoutInflater.from(context).inflate(R.layout.loading_layout, parent, false);
                return new LoadingHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder){
            Page page = list.get(position);
            Picasso.with(context).load("http://vidoco.vn/uploads/news/" + page.getImg()).into(((ItemHolder) holder).img,
            new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    ((ItemHolder) holder).img.setImageResource(R.drawable.error_img);
                }
            });
            ((ItemHolder) holder).img.setClipToOutline(true);
            ((ItemHolder) holder).title.setText(page.getTitle());
            ((ItemHolder) holder).date.setText(page.getDate());
            SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
            Date date;
            try {
                date = originalFormat.parse(page.getDate());
                SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formatedDate = newFormat.format(date);
                ((ItemHolder) holder).date.setText(formatedDate);
            } catch (ParseException e) {
                e.printStackTrace();
                ((ItemHolder) holder).block.setOnClickListener(v -> {
                    Intent intent = new Intent(context, PageContentActivity.class);
                    intent.putExtra("page", page);
                    context.startActivity(intent);
                });
            }
            ((ItemHolder) holder).block.setOnClickListener(v -> {
                Intent intent = new Intent(context, PageContentActivity.class);
                intent.putExtra("page", page);
                context.startActivity(intent);
            });
        } else if (holder instanceof LoadingHolder){
            ((LoadingHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position) != null ? ITEMVIEW : LOADINGVIEW;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, date;
        ConstraintLayout block;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            block = itemView.findViewById(R.id.block);
        }
    }

    public class LoadingHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loading);
        }
    }
}
