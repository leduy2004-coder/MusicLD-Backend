package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "avatar")
public class AvatarEntity extends BaseEntity{
    private String name;

    private String type;

    @Lob
    @Column(name = "data", columnDefinition = "VARBINARY(MAX)")
    private byte[] data;

    @Column(name = "status")
    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
