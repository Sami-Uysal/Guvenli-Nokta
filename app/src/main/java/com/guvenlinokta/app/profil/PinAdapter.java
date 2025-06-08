package com.guvenlinokta.app.profil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guvenlinokta.app.R;
import java.util.List;
import java.util.Locale;

public class PinAdapter extends RecyclerView.Adapter<PinAdapter.PinViewHolder> {

    private List<Pin> pinListesi;
    private OnPinClickListener onPinClickListener;

    public interface OnPinClickListener {
        void onPinClick(Pin pin, int position);
    }

    public PinAdapter(List<Pin> pinListesi, OnPinClickListener onPinClickListener) {
        this.pinListesi = pinListesi;
        this.onPinClickListener = onPinClickListener;
    }

    @NonNull
    @Override
    public PinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pin_item, parent, false);
        return new PinViewHolder(view, onPinClickListener, pinListesi);
    }

    @Override
    public void onBindViewHolder(@NonNull PinViewHolder holder, int position) {
        Pin pin = pinListesi.get(position);
        holder.pinAdiTextView.setText(pin.getAd() != null ? pin.getAd() : "Adsız Pin");
        holder.pinKoordinatTextView.setText(String.format(Locale.getDefault(), "Enlem: %.4f, Boylam: %.4f", pin.getLat(), pin.getLng()));
    }

    @Override
    public int getItemCount() {
        return pinListesi.size();
    }

    static class PinViewHolder extends RecyclerView.ViewHolder {
        TextView pinAdiTextView;
        TextView pinKoordinatTextView;

        PinViewHolder(View itemView, OnPinClickListener listener, List<Pin> pinListesi) {
            super(itemView);
            pinAdiTextView = itemView.findViewById(R.id.pinAdiTextView);
            pinKoordinatTextView = itemView.findViewById(R.id.pinKoordinatTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onPinClick(pinListesi.get(position), position);
                        }
                    }
                }
            });
        }
    }
}