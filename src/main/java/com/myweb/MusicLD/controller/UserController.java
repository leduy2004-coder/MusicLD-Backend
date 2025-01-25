package com.myweb.MusicLD.controller;


import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.UserInputDTO;
import com.myweb.MusicLD.dto.request.UserRequest;
import com.myweb.MusicLD.dto.response.ApiResponse;
import com.myweb.MusicLD.dto.response.AuthenticationResponse;
import com.myweb.MusicLD.dto.response.UserResponse;
import com.myweb.MusicLD.entity.UserEntity;
import com.myweb.MusicLD.service.EmailService;
import com.myweb.MusicLD.service.UserService;
import com.myweb.MusicLD.service.security.AuthenticationService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final AuthenticationService service;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @PostMapping("/register")
    public ApiResponse<AuthenticationResponse> register(
            @RequestBody UserRequest request
    ) {
        return ApiResponse.<AuthenticationResponse>builder().result(service.register(request)).build();
    }

    @PostMapping("/verify-account")
    public ApiResponse<Boolean> verifyAccount(@RequestParam(required = false) String email,
                                              @RequestParam(value = "otp") String otp,
                                              @RequestParam(value = "type") String type) {
        Boolean check = emailService.checkOTP(otp, email,type);
        return ApiResponse.<Boolean>builder().result(check).build();
    }

    @PostMapping("/generate-otp")
    public ApiResponse<Boolean> generateOtp(@RequestParam(value = "email") String email,
                                            @RequestParam(value = "type") String type) {
        emailService.sendOtp(email,type);
        return ApiResponse.<Boolean>builder().result(true).build();
    }

    @PostMapping("/add-user")
    public ApiResponse<UserResponse> addUser(
            @RequestBody UserRequest request
    ) {
        UserEntity user = userService.insert(request);
        return ApiResponse.<UserResponse>builder().result(modelMapper.map(user, UserResponse.class)).build();
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

    @PatchMapping("/change-password")
    public ApiResponse<Boolean> changePassword(
            @RequestBody ChangePassword request
    ) {
        Boolean status = userService.changePassword(request);
        return ApiResponse.<Boolean>builder().result(status).build();
    }

    @GetMapping("/get-user")
    public ApiResponse<UserResponse> getUser(@RequestParam(value = "id") String id) {
        UserResponse userResponse = userService.findById(BigInteger.valueOf(Long.parseLong(id)));
        return ApiResponse.<UserResponse>builder().result(userResponse).build();
    }

    @GetMapping("/get-user-for-admin")
    public ApiResponse<UserInputDTO> getUserOfAdmin(@RequestParam(value = "id") String id) {
        UserInputDTO userResponse = userService.findUserForAdminById(BigInteger.valueOf(Long.parseLong(id)));
        return ApiResponse.<UserInputDTO>builder().result(userResponse).build();
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

    @DeleteMapping("/delete-user")
    public ApiResponse<Boolean> deleteUser(@RequestParam(value = "id") String id) {
        Boolean status = userService.delete(BigInteger.valueOf(Long.parseLong(id)));
        return ApiResponse.<Boolean>builder().result(status).build();
    }
}
