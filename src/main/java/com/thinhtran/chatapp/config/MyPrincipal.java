package com.thinhtran.chatapp.config;

import java.security.Principal;

public class MyPrincipal implements Principal {
    private String userId;
    public MyPrincipal(String userId) {
        this.userId = userId;
    }

    @Override
    public String getName() {
        return userId;
    }
}
