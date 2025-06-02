package com.guvenlinokta.app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DisasterItemAdapter extends RecyclerView.Adapter<DisasterItemAdapter.ViewHolder> {

    private List<DisasterInfo> disasterList;
    private Context context;

    public DisasterItemAdapter(Context context, List<DisasterInfo> disasterList) {
        this.context = context;
        this.disasterList = disasterList;
    }

    @NonNull
    @Override
    public DisasterItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_disaster_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DisasterItemAdapter.ViewHolder holder, int position) {
        DisasterInfo disaster = disasterList.get(position);
        holder.textTitle.setText(disaster.getName());
        holder.imageIcon.setImageResource(disaster.getIconResId());
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = null;
            switch (disaster.getName()) {
                case "Deprem":
                    intent = new Intent(context, com.guvenlinokta.app.info.Deprem.class);
                    break;
                case "Sel":
                    intent = new Intent(context, com.guvenlinokta.app.info.Sel.class);
                    break;
                case "Yangın":
                    intent = new Intent(context, com.guvenlinokta.app.info.Yangin.class);
                    break;
                case "Hortum":
                    intent = new Intent(context, com.guvenlinokta.app.info.Hortum.class);
                    break;
                case "Çığ":
                    intent = new Intent(context, com.guvenlinokta.app.info.Cig.class);
                    break;
                case "Heyelan":
                    intent = new Intent(context, com.guvenlinokta.app.info.Heyelan.class);
                    break;
            }
            if (intent != null) context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return disasterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIcon;
        TextView textTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.imageViewDisasterIcon);
            textTitle = itemView.findViewById(R.id.textViewDisasterTitle);
        }
    }
}

