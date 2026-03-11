package com.malgn.auth.controller;

import com.malgn.auth.dto.LoginRequest;
import com.malgn.auth.dto.LoginResponse;
import com.malgn.configure.security.CustomUserDetails;
import com.malgn.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            LoginResponse response = new LoginResponse(
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getRole(),
                    "로그인 성공"
            );

            return ApiResponse.ok("로그인 성공", response);
        } catch (AuthenticationException e) {
            return ApiResponse.fail("아이디 또는 비밀번호가 올바르지 않습니다.", null);
        }
    }
}