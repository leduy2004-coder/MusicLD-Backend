package com.myweb.MusicLD.service.Impl;

import com.myweb.MusicLD.dto.RoleDTO;
import com.myweb.MusicLD.entity.RoleEntity;
import com.myweb.MusicLD.exception.ResourceNotFoundException;
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
    public RoleDTO insert(RoleDTO roleDto) {
        return modelMapper.map(repository.save(modelMapper.map(roleDto, RoleEntity.class)), RoleDTO.class);
    }

    @Override
    public RoleDTO findById(Long id) {
        RoleEntity role = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee is not exists with given id : "+ id));
        return modelMapper.map(role,RoleDTO.class);
    }

}
