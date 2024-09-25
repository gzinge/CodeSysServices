package com.encora.codesys.taskmanager.controller;

import com.encora.codesys.taskmanager.entity.AuthRequest;
import com.encora.codesys.taskmanager.entity.AuthResponse;
import com.encora.codesys.taskmanager.entity.Users;
import com.encora.codesys.taskmanager.repository.UserRepository;
import com.encora.codesys.taskmanager.service.CustomUserDetailsService;
import com.encora.codesys.taskmanager.util.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/login")
    @CrossOrigin
    public ResponseEntity<AuthResponse> createAuthenticationToken(@RequestBody AuthRequest authRequest) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            String jwtToken = jwtUtil.generateToken((UserDetails) authentication.getPrincipal());
            jwtToken = customUserDetailsService.login(authRequest.getUsername(), jwtToken);
            return ResponseEntity.ok(new AuthResponse(authRequest.getUsername(),jwtToken));
        } catch (AuthenticationException e) {
            log.error("Incorrect username or password", e);
            return ResponseEntity.internalServerError().
                    body(new AuthResponse("","Incorrect username or password"));
        }
    }

    @PostMapping("/signup")
    @CrossOrigin
    public ResponseEntity<String> signup(@RequestBody Users user) {
        // Check if username already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }
        // Create new user
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode the password
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/signout")
    @CrossOrigin
    public ResponseEntity<String> logout(@RequestBody AuthRequest authRequest) {
        if (authRequest.isLogout() && customUserDetailsService.logout(authRequest)) {
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.badRequest().body("Invalid logout request");
    }

}
