package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myweb.MusicLD.utility.enumUtils.AvatarType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "avatar")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AvatarEntity extends BaseEntity{
    String name;

    @Enumerated(EnumType.STRING)
    AvatarType type;

    @Column(name = "url", length = 2048)
    String url;

    String publicId;

    Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id")
    UserEntity userEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "music_id")
    MusicEntity musicEntity;
}
