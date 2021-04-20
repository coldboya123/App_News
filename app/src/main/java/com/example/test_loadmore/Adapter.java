package com.example.test_loadmore;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    Context context;
    List<Page> list;

    public Adapter(Context context, List<Page> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.custom_rcv_page, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Page page = list.get(position);
        Picasso.with(context).load("http://vidoco.vn/uploads/news/" + page.getImg()).into(holder.img);
        holder.img.setClipToOutline(true);
        holder.title.setText(page.getTitle());
        holder.date.setText(page.getDate());
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
        Date date;
        try {
            date = originalFormat.parse(page.getDate());
            SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formatedDate = newFormat.format(date);
            holder.date.setText(formatedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            holder.block.setOnClickListener(v -> {
                Intent intent = new Intent(context, PageContentActivity.class);
                intent.putExtra("page", page);
                context.startActivity(intent);
            });
        }
        holder.block.setOnClickListener(v -> {
            Intent intent = new Intent(context, PageContentActivity.class);
            intent.putExtra("page", page);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title, date;
        ConstraintLayout block;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            block = itemView.findViewById(R.id.block);
        }
    }
}
