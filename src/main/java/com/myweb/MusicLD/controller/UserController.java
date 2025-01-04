package com.myweb.MusicLD.controller;


import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.service.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.security.Principal;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService service;

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(
            @RequestBody UserRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(service.register(request)).build();
    }

    @GetMapping("/search")
    public ApiResponse<List<UserResponse>> searchUser(@RequestParam(value = "q") String result,
                                                      @RequestParam(value = "type") String type) {
        List<UserResponse> list = userService.searchUsers(result, type);
        return ApiResponse.<List<UserResponse>>builder().result(list).build();
    }

    @GetMapping("/get-all")
    public ApiResponse<List<UserResponse>> findAll() {
        List<UserResponse> list = userService.findAll();
        return ApiResponse.<List<UserResponse>>builder().result(list).build();
    }

    @PatchMapping
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePassword request,
            Principal connectedUser
    ) {
        userService.changePassword(request, connectedUser);
        return ResponseEntity.ok("Change password ok");
    }

    @GetMapping("/get-user")
    public ApiResponse<UserResponse> getUser(@RequestParam(value = "id") String id) {
        UserResponse userResponse = userService.findById(BigInteger.valueOf(Long.parseLong(id)));
        return ApiResponse.<UserResponse>builder().result(userResponse).build();
    }

    @PatchMapping("/update-user")
    public ApiResponse<UserResponse> updateUserPartially(
            @RequestBody UserRequest userRequest) {
        UserResponse userResponse = userService.updateById(userRequest);
        return ApiResponse.<UserResponse>builder().result(userResponse).build();
    }

    @GetMapping("/get-top-user")
    public ApiResponse<List<UserResponse>> getTopUser() {
        List<UserResponse> list = userService.getTopUsersByFollower();
        return ApiResponse.<List<UserResponse>>builder().result(list).build();
    }
}
