package com.thinkconstructive.book_store.feature_auth.models;


public class AuthResponse {
    private String token;
    private String message;
    private String userId;


    public AuthResponse(String token, String message, String userId) {
        this.token = token;
        this.message = message;
        this.userId = userId;
    }


    public String getToken() {
        return token;
    }


    public String getMessage() {
        return message;
    }

    public String getUserId() {
        return userId;
    }
}
