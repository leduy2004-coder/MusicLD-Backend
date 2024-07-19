package com.myweb.MusicLD.controller;


import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.service.Impl.AuthenticationService;
import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.service.Impl.LogoutImpl;
import com.myweb.MusicLD.service.UserService;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService service;
    private final LogoutImpl logout;

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(
            @RequestBody UserRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(service.register(request)).build();
    }


    @GetMapping
    public ResponseEntity<List<UserResponse>> findAll(){
        List<UserResponse> list = userService.findAll();
        return ResponseEntity.ok(list);
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePassword request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok("Change password ok");
    }


}
