package com.assesment.service.impl;

import com.assesment.persistent.model.PersistentUserEntity;
import com.assesment.persistent.repository.UserRepository;
import com.assesment.service.IAuthenticationFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationFacade implements IAuthenticationFacade {

    @Autowired
    private UserRepository userRepository;

    @Override
    public Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public PersistentUserEntity getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            return null;
        }
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal == null) {
            return null;
        }

        PersistentUserEntity persistentUserEntity = userRepository.findByEmail(principal.getUsername());
        if (persistentUserEntity == null) {
            return null;
        }
        return persistentUserEntity;
    }
}