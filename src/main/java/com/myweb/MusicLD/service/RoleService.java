package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.request.RoleRequest;
import com.myweb.MusicLD.dto.response.RoleResponse;

public interface RoleService {
    RoleResponse insert(RoleRequest roleDto);
    RoleResponse findByCode(String code);
}
