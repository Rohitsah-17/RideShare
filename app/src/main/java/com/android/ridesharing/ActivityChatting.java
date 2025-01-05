package com.android.ridesharing;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

// Rohit sah

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ActivityChatting extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText messageInput;
    private ImageButton sendButton;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList;

    // Firebase
    private DatabaseReference chatReference;
    private String chatRoomId;
    private String driverName , driverID;
    private String userId;
    FirebaseUser user;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatting);

        // Initialize views
        recyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        // Apply system bars inset padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Fetch data from the Intent's Bundle
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            driverName = extras.getString("driverName");
            driverID = extras.getString("driverID");
            userId = extras.getString("rideID");
        } else {
            Toast.makeText(this, "Driver name Null", Toast.LENGTH_SHORT).show();
        }

        // Set header information
        TextView driverNameTextView = findViewById(R.id.drivername);
        TextView userIdTextView = findViewById(R.id.driverid);

        if (driverName != null) {
            driverNameTextView.setText(driverName);
        }
        if (userId != null) {
            userIdTextView.setText(userId);
        }

        // Initialize RecyclerView
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(chatAdapter);

        // Initialize Firebase

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Toast.makeText(this, "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        } else {
            setupFirebaseChat();
        }
        // Send button click listener
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void setupFirebaseChat() {
        // Check if driverName is null
        if (driverName == null) {
            driverName = "default_driver"; // Provide a fallback value
        }

        // Create a unique chat room ID using userId and driverName
        chatRoomId = userId ;

//        Toast.makeText(this, chatRoomId, Toast.LENGTH_SHORT).show();

        // Initialize Firebase Database reference
        chatReference = FirebaseDatabase.getInstance().getReference("chats").child(chatRoomId);

        // Listen for new messages
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot messageSnap : snapshot.getChildren()) {
                    ChatMessage message = messageSnap.getValue(ChatMessage.class);
                    if (message != null) {
                        Log.d("message" , message.getSenderId());

                        messageList.add(message);
                    }
                }
                chatAdapter.notifyDataSetChanged();
                if (!messageList.isEmpty()) {
                    recyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ActivityChatting.this,
                        "Failed to load messages: " + error.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        if (!messageText.isEmpty()) {
            // Create message object
            ChatMessage message = new ChatMessage(
                    messageText,
                    System.currentTimeMillis(),
                    uid, // sender ID
                    true    // sent by user
            );

            // Generate a unique key for the message
            String messageId = chatReference.push().getKey();
            if (messageId != null) {
                // Save message to Firebase
                chatReference.child(messageId).setValue(message)
                        .addOnSuccessListener(aVoid -> {
                            // Clear input field after successful send
                            messageInput.setText("");
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(ActivityChatting.this,
                                        "Failed to send message: " + e.getMessage(),
                                        Toast.LENGTH_SHORT).show()
                        );
            }
        }
    }
}

// ChatMessage class
class ChatMessage {
    private String message;
    private long timestamp;
    private String senderId;
    private boolean isSentByUser;

    // Required empty constructor for Firebase
    public ChatMessage() {}

    public ChatMessage(String message, long timestamp, String senderId, boolean isSentByUser) {
        this.message = message;
        this.timestamp = timestamp;
        this.senderId = senderId;
        this.isSentByUser = isSentByUser;
    }

    // Getters and setters required for Firebase
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }

    public boolean getIsSentByUser() { return isSentByUser; }
    public void setIsSentByUser(boolean sentByUser) { isSentByUser = sentByUser; }
}

// ChatAdapter class
class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {
    private final List<ChatMessage> messages;

    public ChatAdapter(List<ChatMessage> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        ChatMessage message = messages.get(position);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        holder.bind(message , user.getUid().toString());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final LinearLayout sentLayout;
        private final LinearLayout receivedLayout;
        private final TextView sentMessageText;
        private final TextView receivedMessageText;
        private final TextView sentMessageTime;
        private final TextView receivedMessageTime;
        private final TextView SenderName;
        private final TextView RcName;

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentLayout = itemView.findViewById(R.id.sentLayout);
            receivedLayout = itemView.findViewById(R.id.receivedLayout);
            sentMessageText = itemView.findViewById(R.id.sentMessageText);
            receivedMessageText = itemView.findViewById(R.id.receivedMessageText);
            sentMessageTime = itemView.findViewById(R.id.sentMessageTime);
            receivedMessageTime = itemView.findViewById(R.id.receivedMessageTime);
            SenderName = itemView.findViewById(R.id.sender);
            RcName = itemView.findViewById(R.id.RcName);
        }

        public void bind(ChatMessage message , String uid) {


            if (Objects.equals(message.getSenderId(), uid)) {
                sentLayout.setVisibility(View.VISIBLE);
                receivedLayout.setVisibility(View.GONE);
                sentMessageText.setText(message.getMessage());
                sentMessageTime.setText(formatTime(message.getTimestamp()));
                getNameByUid(message.getSenderId(), new NameCallback() {
                    @Override
                    public void onNameReceived(String name) {
                        System.out.println("Name: " + name);
                        SenderName.setText(name.toString());

                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("Error: " + error);
                    }
                });

            } else {
                sentLayout.setVisibility(View.GONE);
                receivedLayout.setVisibility(View.VISIBLE);
                receivedMessageText.setText(message.getMessage());
                receivedMessageTime.setText(formatTime(message.getTimestamp()));
                getNameByUid(message.getSenderId(), new NameCallback() {
                    @Override
                    public void onNameReceived(String name) {
                        System.out.println("Name: " + name);
                        RcName.setText(name.toString());

                    }

                    @Override
                    public void onError(String error) {
                        System.out.println("Error: " + error);
                    }
                });

            }
        }

        private String formatTime(long timestamp) {
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
            return sdf.format(new Date(timestamp));
        }
    }



    public static void getNameByUid(String uid, NameCallback callback) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String name = dataSnapshot.child("fullName").getValue(String.class);
                    if (name != null) {
                        callback.onNameReceived(name);
                    } else {
                        callback.onError("Name not found for UID: " + uid);
                    }
                } else {
                    callback.onError("No data found for UID: " + uid);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onError("Database error: " + databaseError.getMessage());
            }
        });
    }

    // Callback interface for Firebase
    public interface NameCallback {
        void onNameReceived(String name);
        void onError(String error);
    }

}


