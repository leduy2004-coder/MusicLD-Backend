package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
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
@Table(name = "music")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MusicEntity extends BaseEntity {

    @Column(name = "title", nullable = false, columnDefinition = "NVARCHAR(255)")
    String title;

    @Column(name = "url")
    String url;

    @Column(name = "status")
    Boolean status;

    @Column(name = "lyrics", columnDefinition = "NVARCHAR(MAX)")
    String lyrics;

    String publicId;

    int duration;

    @Enumerated(EnumType.STRING)
    AccessMusic access;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity userEntity;

    @OneToMany(mappedBy = "musicEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<AvatarEntity> avatars;

    @OneToMany(mappedBy = "musicEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    List<CommentEntity> comments;
}
