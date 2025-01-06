package com.android.ridesharing.inbox;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.ridesharing.ActivityChatting;
import com.android.ridesharing.R;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxViewHolder> {

    private final List<InboxItem> inboxItemList;
    private OnChatClickListener onChatClickListener;
    private Context context;

    // Constructor
    public InboxAdapter(List<InboxItem> inboxItemList, Context context) {
        this.inboxItemList = inboxItemList;
        this.context = context;
    }

    // Set listener for chat item clicks
    public void setOnChatClickListener(OnChatClickListener listener) {
        this.onChatClickListener = listener;
    }

    @NonNull
    @Override
    public InboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the correct layout (MaterialCardView or any other layout type you're using)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new InboxViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxViewHolder holder, int position) {
        InboxItem item = inboxItemList.get(position);
        holder.nameTextView.setText(item.getName());
        holder.messageTextView.setText(item.getMessage());
        holder.profileImageView.setImageResource(item.getProfileImageResId());

        // Setting a click listener to open the chat
        holder.itemView.setOnClickListener(v -> {
            openChatActivity(item);
        });
    }

    private void openChatActivity(InboxItem item) {
        if (item.getUserId() == null || item.getUserId().isEmpty()) {
            Log.e("InboxAdapter", "User ID is null or empty");
            return;  // Exit the method if no valid user ID
        }

        // Create an Intent to open the chat activity
        Intent intent = new Intent(context, ActivityChatting.class);
        Bundle bundle = new Bundle();
        bundle.putString("rideID", item.getUserId()); // Using rideID or unique chat ID
        bundle.putString("driverID", item.getUserId());
        bundle.putString("driverName", item.getName());
        intent.putExtras(bundle);

        // Start the activity with the chat details
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return inboxItemList.size();
    }

    static class InboxViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImageView;
        TextView nameTextView, messageTextView;

        // Assuming the root layout for each item is a MaterialCardView or other layout type
        View chatItemLayout;

        public InboxViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImageView = itemView.findViewById(R.id.profileImage);
            nameTextView = itemView.findViewById(R.id.ownerName);
            messageTextView = itemView.findViewById(R.id.vehicleDetails);
            chatItemLayout = itemView.findViewById(R.id.llChatItem); // Ensure this ID matches the item layout
        }
    }

    // Interface for chat item click listener
    public interface OnChatClickListener {
        void onChatClick(InboxItem item);
    }
}
