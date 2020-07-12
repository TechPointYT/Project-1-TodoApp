package com.practice.simpletodoprecoursework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{
    public interface  OnLongClickListener{
        void onItemLongClick(int position);
    }
    public interface  OnClickListener{
        void onItemClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;
    public ItemAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener) {
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use layout inflator to inflate view
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
        //wrap it inside viewholder and return it
        return new ViewHolder(view);
    }
    //responsible for binding data to particular view holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // grab item at pos
        String item = items.get(position);
        //Bind item into specified view holder
        holder.bind(item);
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    // container provice acces to view
    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }
        //update view inside
        public void bind(String item) {
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClicked(getAdapterPosition());

                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //notift tge listener which position was long pressed
                    longClickListener.onItemLongClick(getAdapterPosition());
                    return true;
                }
            });
        }
    }
}
