package com.android.ridesharing.inbox;

public class InboxItem {
    private final String name;
    private final String message;
    private final int profileImageResId;
    private final String userId;  // Add userId property

    public InboxItem(String name, String message, int profileImageResId, String userId) {
        this.name = name;
        this.message = message;
        this.profileImageResId = profileImageResId;
        this.userId = userId;  // Initialize userId
    }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public int getProfileImageResId() {
        return profileImageResId;
    }

    public String getUserId() {
        return userId;  // Add getter for userId
    }
}
