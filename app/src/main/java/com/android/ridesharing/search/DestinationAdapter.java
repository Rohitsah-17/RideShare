package com.android.ridesharing.search;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ridesharing.R;

import java.util.List;

public class DestinationAdapter extends RecyclerView.Adapter<DestinationAdapter.ViewHolder> {

    private final List<Destination> destinationList;
    private final Context context;

    public DestinationAdapter(Context context, List<Destination> destinationList) {
        this.context = context;
        this.destinationList = destinationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_popular_destination, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Destination destination = destinationList.get(position);
        holder.destinationName.setText(destination.getName());
        holder.destinationDifficulty.setText(destination.getDifficulty());
        holder.destinationDistance.setText(destination.getDistance());
        // You can load images using Glide or Picasso here.
        // Example: Glide.with(context).load(destination.getImageUrl()).into(holder.destinationImage);
    }

    @Override
    public int getItemCount() {
        return destinationList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView destinationImage;
        TextView destinationName, destinationDifficulty, destinationDistance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            destinationImage = itemView.findViewById(R.id.destinationImage);
            destinationName = itemView.findViewById(R.id.destinationName);
            destinationDifficulty = itemView.findViewById(R.id.destinationDifficulty);
            destinationDistance = itemView.findViewById(R.id.destinationDistance);
        }
    }
}

