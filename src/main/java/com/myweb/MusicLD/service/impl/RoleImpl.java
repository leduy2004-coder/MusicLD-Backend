package com.myweb.MusicLD.service.impl;

import com.myweb.MusicLD.dto.request.RoleRequest;
import com.myweb.MusicLD.dto.response.RoleResponse;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.jpa.RoleRepository;
import com.myweb.MusicLD.service.RoleService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleImpl implements RoleService {
    RoleRepository repository;
    ModelMapper modelMapper;

    @Override
    @Transactional
    public RoleResponse insert(RoleRequest roleDto) {
        return modelMapper.map(repository.save(modelMapper.map(roleDto, RoleEntity.class)), RoleResponse.class);
    }

    @Override
    public RoleResponse findByCode(String code) {
        RoleEntity role = repository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return modelMapper.map(role,RoleResponse.class);
    }

}
