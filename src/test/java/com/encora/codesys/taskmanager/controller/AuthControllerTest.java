package com.encora.codesys.taskmanager.controller;

import com.encora.codesys.taskmanager.entity.AuthRequest;
import com.encora.codesys.taskmanager.entity.AuthResponse;
import com.encora.codesys.taskmanager.entity.Users;
import com.encora.codesys.taskmanager.repository.UserRepository;
import com.encora.codesys.taskmanager.service.CustomUserDetailsService;
import com.encora.codesys.taskmanager.util.JWTUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AuthControllerTest {


    @MockBean
    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JWTUtil jwtUtil;

    @InjectMocks
    private AuthController authController;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testCreateAuthenticationToken() throws Exception {
        AuthRequest authRequest = new AuthRequest("user1", "password",false,"");
        AuthResponse authResponse = new AuthResponse("user1", "dummy-token");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mock(UsernamePasswordAuthenticationToken.class));
        when(jwtUtil.generateToken(any())).thenReturn("dummy-token");
        when(customUserDetailsService.login(anyString(), anyString())).thenReturn("dummy-token");

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("user1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("dummy-token"))
                .andDo(print());
    }

//    /@Test
    void testSignup() throws Exception {
        Users user = new Users("testuser", "testpassword","ghansham@ghansham.com");
        mockMvc.perform(MockMvcRequestBuilders.post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(user))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    void testLogout() throws Exception {
        AuthRequest authRequest = new AuthRequest("user1", "password",true,"");
        authRequest.setLogout(true);
        when(customUserDetailsService.logout(any())).thenReturn(true);
        mockMvc.perform(MockMvcRequestBuilders.post("/signout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(authRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}