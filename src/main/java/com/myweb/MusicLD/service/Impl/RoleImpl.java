package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.request.RoleRequest;
import com.myweb.MusicLD.dto.response.RoleResponse;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.repository.RoleRepository;
import com.myweb.MusicLD.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleImpl implements RoleService {
    private final RoleRepository repository;
    private final ModelMapper modelMapper;

    @Override
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
