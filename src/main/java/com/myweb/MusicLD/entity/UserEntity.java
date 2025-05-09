package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myweb.MusicLD.utility.enumUtils.AuthenticationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
//@Document(indexName = "users")
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@FieldDefaults(level = AccessLevel.PRIVATE)

public class UserEntity extends BaseEntity {

    @Column(name = "username", nullable = false)
    String username;

    @Column(name = "nickname", columnDefinition = "NVARCHAR(150)")
    String nickName;

    @Column(name = "password")
    String password;


    String email;

    @Column(name = "status")
    Boolean status;

    @Column(name = "dateofbirth")
    Date dateOfBirth;

    @Column(name = "gender")
    Boolean gender;

    @Enumerated(EnumType.STRING)
    AuthenticationType authType;


    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    List<PaymentEntity> payments;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY,orphanRemoval = true, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<AvatarEntity> avatars;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    List<RoleEntity> roles;

    @OneToMany(mappedBy = "sender", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    List<FollowerEntity> following;

    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    List<FollowerEntity> followers;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<MusicEntity> musics;

    @OneToMany(mappedBy = "userEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL , orphanRemoval = true)
    @JsonManagedReference
    List<CommentEntity> comments;

}
