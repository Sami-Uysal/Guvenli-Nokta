package com.guvenlinokta.app;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EmergencyNumbersAdapter extends RecyclerView.Adapter<EmergencyNumbersAdapter.ViewHolder> {

    private List<EmergencyNumber> emergencyNumbers;
    private Context context;

    public EmergencyNumbersAdapter(Context context, List<EmergencyNumber> emergencyNumbers) {
        this.context = context;
        this.emergencyNumbers = emergencyNumbers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_emergency_number, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmergencyNumber emergencyNumber = emergencyNumbers.get(position);
        holder.nameTextView.setText(emergencyNumber.getName());
        holder.numberTextView.setText(emergencyNumber.getNumber());

        holder.itemView.setOnClickListener(v -> {
            Intent dialIntent = new Intent(Intent.ACTION_DIAL);
            dialIntent.setData(Uri.parse("tel:" + emergencyNumber.getNumber()));
            if (dialIntent.resolveActivity(context.getPackageManager()) != null) {
                context.startActivity(dialIntent);
            } else {
                Toast.makeText(context, "Arama uygulaması bulunamadı.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return emergencyNumbers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView numberTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.textViewEmergencyName);
            numberTextView = itemView.findViewById(R.id.textViewEmergencyNumber);
        }
    }
}