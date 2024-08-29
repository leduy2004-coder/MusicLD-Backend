package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
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

    @Enumerated(EnumType.STRING)
    private AvatarType type;

    @Column(name = "url", length = 2048)
    private String url;

    private String publicId;

    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "music_id")
    private MusicEntity musicEntity;
}
