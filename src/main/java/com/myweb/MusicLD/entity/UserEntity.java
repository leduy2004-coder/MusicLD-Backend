package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myweb.MusicLD.utility.enumUtils.AuthenticationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
public class UserEntity extends BaseEntity {

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "nickname", columnDefinition = "NVARCHAR(150)")
    private String nickName;

    @Column(name = "password")
    private String password;


    private String email;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "dateofbirth")
    private Date dateOfBirth;

    @Column(name = "gender")
    private Boolean gender;

    @Enumerated(EnumType.STRING)
    private AuthenticationType authType;


    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    private List<PaymentEntity> payments;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AvatarEntity> avatars;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<RoleEntity> roles;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    private List<FollowerEntity> following;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    private List<FollowerEntity> followers;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<MusicEntity> musics;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    private List<CommentEntity> comments;

}
