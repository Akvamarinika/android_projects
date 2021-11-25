package com.example.recyclerviewcolors;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class ColorViewHolder extends RecyclerView.ViewHolder {
    TextView nameColorView;

    public ColorViewHolder(@NonNull View itemView) {
        super(itemView);
        nameColorView = itemView.findViewById(R.id.color);
    }

    void bindTo(Integer color) {
        // заполнить объекты интерфейса значениями (данными)
        nameColorView.setText("color: #" + Integer.toHexString(color));
        itemView.setBackgroundResource(color);


    }

}
