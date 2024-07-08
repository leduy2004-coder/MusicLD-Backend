package com.myweb.MusicLD.controller;


import com.myweb.MusicLD.controller.Auth.AuthenticationResponse;
import com.myweb.MusicLD.controller.Auth.AuthenticationService;
import com.myweb.MusicLD.dto.ChangePassword;
import com.myweb.MusicLD.dto.UserDTO;
import com.myweb.MusicLD.service.Impl.LogoutImpl;
import com.myweb.MusicLD.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserDTO userDto
    ) {
        return ResponseEntity.ok(service.register(userDto));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> findAll(){
        List<UserDTO> list = userService.findAll();
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
