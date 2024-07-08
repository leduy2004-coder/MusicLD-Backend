package com.myweb.MusicLD.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myweb.MusicLD.utility.TokenType;
import jakarta.persistence.*;
import lombok.*;



@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "token")
public class TokenEntity extends BaseEntity{

    @Column(unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType = TokenType.BEARER;

    private boolean revoked;

    private boolean expired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}