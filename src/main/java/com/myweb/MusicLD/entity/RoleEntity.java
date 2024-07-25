package com.myweb.MusicLD.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "role")
public class RoleEntity extends BaseEntity{

    private String code;

    private String name;


    @ManyToMany(mappedBy = "roles",fetch = FetchType.LAZY)
    private List<UserEntity> users;
}
