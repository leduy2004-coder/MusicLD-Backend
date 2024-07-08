package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Data
@Table(name = "role")
public class RoleEntity extends BaseEntity{

    @Column
    private String code;
    @Column
    private String name;

    @ManyToMany(mappedBy = "roles",fetch = FetchType.EAGER)
    @JsonIgnore
    private List<UserEntity> users = new ArrayList<>();
}
