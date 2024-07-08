package com.myweb.MusicLD.service;

import com.myweb.MusicLD.dto.RoleDTO;

public interface RoleService {
    RoleDTO insert(RoleDTO roleDto);
    RoleDTO findById(Long id);
}
