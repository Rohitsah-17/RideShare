package com.android.ridesharing.yourride;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.android.ridesharing.R;
import com.android.ridesharing.inbox.Booking;
import java.util.List;

public class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.BookingViewHolder> {

    private Context context;
    private List<Booking> bookingList;

    // Constructor for the adapter
    public BookingAdapter(Context context, List<Booking> bookingList) {
        this.context = context;
        this.bookingList = bookingList;
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the layout for each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.item_booking, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        Booking booking = bookingList.get(position);

        // Set the booking details to the TextViews
        holder.bookingIdTextView.setText("Booking ID: " + booking.getBookingId());
        holder.driverNameTextView.setText("Driver: " + booking.getDriverName());
        holder.rideIdTextView.setText("Ride ID: " + booking.getRideId());
        holder.userIdTextView.setText("User ID: " + booking.getUserId());
    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    // ViewHolder class for binding data to the views
    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        TextView bookingIdTextView;
        TextView driverNameTextView;
        TextView rideIdTextView;
        TextView userIdTextView;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            // Find the TextViews by their IDs
            bookingIdTextView = itemView.findViewById(R.id.tvBookingId);
            driverNameTextView = itemView.findViewById(R.id.tvDriverName);
            rideIdTextView = itemView.findViewById(R.id.tvRideId);
            userIdTextView = itemView.findViewById(R.id.tvUserId);
        }
    }
}

