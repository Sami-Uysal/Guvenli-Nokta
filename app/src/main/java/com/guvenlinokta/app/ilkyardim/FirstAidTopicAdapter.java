package com.guvenlinokta.app.ilkyardim;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.guvenlinokta.app.R;
import java.util.List;

public class FirstAidTopicAdapter extends RecyclerView.Adapter<FirstAidTopicAdapter.ViewHolder> {

    private List<FirstAidTopic> topicList;
    private OnTopicClickListener listener;

    public interface OnTopicClickListener {
        void onTopicClick(FirstAidTopic topic);
    }

    public FirstAidTopicAdapter(List<FirstAidTopic> topicList, OnTopicClickListener listener) {
        this.topicList = topicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_first_aid_topic, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FirstAidTopic topic = topicList.get(position);
        holder.topicTitle.setText(topic.getTitle());
        holder.itemView.setOnClickListener(v -> listener.onTopicClick(topic));
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView topicTitle;

        ViewHolder(View itemView) {
            super(itemView);
            topicTitle = itemView.findViewById(R.id.textViewTopicTitle);
        }
    }

}