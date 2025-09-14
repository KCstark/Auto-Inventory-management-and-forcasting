package com.inventory_forcasting.service;

import com.inventory_forcasting.enties.AppUsers;
import com.inventory_forcasting.exceptions.ResourceNotFoundException;
import com.inventory_forcasting.repo.AppUserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final AppUserRepo appUserRepo;

    @Override
    public UserDetails loadUserByUsername(String username) {
        Optional<AppUsers> appUsers = appUserRepo.findByEmail(username);
        if (appUsers.isPresent()) {
            return appUsers.get();
        } else {
            throw new ResourceNotFoundException("User not found with email: " + username);
        }
    }
}
