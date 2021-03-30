package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.viewHolder> {
    ArrayList<Item> Items;

    private OnItemClickListener listener;

    public interface OnItemClickListener {
            void onItemClick(int position);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        public TextView textView1;
        public TextView textView2;

        public viewHolder(View view, OnItemClickListener listener) {
            super(view);
            textView1 = view.findViewById(R.id.TextView1);
            textView2 = view.findViewById(R.id.TextView2);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        viewHolder holder = new viewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false), listener);
        return holder;
    }

    public adapter(ArrayList<Item> Items) {
        this.Items = Items;
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Item item = Items.get(position);
        holder.textView1.setText(item.getWord());
        holder.textView2.setText(item.getEnglishD());

    }

    @Override
    public int getItemCount() {
        return Items.size();
    }
}
