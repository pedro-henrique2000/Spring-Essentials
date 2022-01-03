package com.projects.essentials.service;

import com.projects.essentials.repository.DevdojoUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DevdojoUserDetailsService implements UserDetailsService {

    @Autowired
    private DevdojoUserRepository devdojoUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return Optional.ofNullable(devdojoUserRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(String.format("%s not found", username)));
    }
}
