package com.encora.codesys.taskmanager.service;

import com.encora.codesys.taskmanager.entity.AuthRequest;
import com.encora.codesys.taskmanager.entity.Users;
import com.encora.codesys.taskmanager.repository.UserRepository;
import com.encora.codesys.taskmanager.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Implement loading user from your data source
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public Users findUserByUsername(String username) throws UsernameNotFoundException {
        // Implement loading user from your data source
        Users user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return user;
    }


    public String login(String username, String jwtToken) {

        Users users = userRepository.findByUsername(username);
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        if(users.getLogin() == Boolean.TRUE){
            return users.getJwtToken();
        }
        users.setLogin(Boolean.TRUE);
        users.setLastLoginDate(Date.from(Instant.now()));
        users.setJwtToken(jwtToken);
        userRepository.save(users);
        return jwtToken;
    }

    public boolean logout(AuthRequest authRequest) {
        String username = jwtUtil.extractUsername(authRequest.getToken());
        if (authRequest.getUsername().equals(username)) {
            Users users = userRepository.findByUsername(authRequest.getUsername());
            if (users == null || !users.getLogin()) {
                return false;
            }
            users.setLogin(Boolean.FALSE);
            users.setLastLogoutDate(Date.from(Instant.now()));
            users.setJwtToken(null);
            userRepository.save(users);
            return true;
        }
        return false;
    }

    public boolean isUserLogin(String username) {
        Users users = userRepository.findByUsername(username);
        if (users == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return users.getLogin();
    }
}
