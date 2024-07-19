package com.myweb.MusicLD.controller;

import com.myweb.MusicLD.dto.request.RoleRequest;
import com.myweb.MusicLD.dto.response.RoleResponse;
import com.myweb.MusicLD.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleResponse> insert(@RequestBody RoleRequest roleDto){
        RoleResponse insert = roleService.insert(roleDto);
        return new ResponseEntity<>(insert, HttpStatus.CREATED);
    }

}
