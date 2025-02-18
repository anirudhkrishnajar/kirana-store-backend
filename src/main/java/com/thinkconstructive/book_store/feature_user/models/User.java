package com.thinkconstructive.book_store.feature_user.models;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public record User(
        String userId,
        String password
) {}
