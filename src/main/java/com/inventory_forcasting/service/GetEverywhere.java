package com.inventory_forcasting.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class GetEverywhere {

    public long userId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if(auth==null)
            return -1L;
        long userId = Long.parseLong(auth.getCredentials().toString());
        return userId;
    }
}
