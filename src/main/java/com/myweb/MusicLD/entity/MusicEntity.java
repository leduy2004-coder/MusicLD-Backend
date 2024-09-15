package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.myweb.MusicLD.utility.enumUtils.AccessMusic;
import com.myweb.MusicLD.utility.enumUtils.AuthenticationType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "music")
public class MusicEntity extends BaseEntity {

    @Column(name = "title", nullable = false, columnDefinition = "NVARCHAR(255)")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "status")
    private Boolean status;

    @Column(name = "lyrics", columnDefinition = "NVARCHAR(MAX)")
    private String lyrics;

    private String publicId;

    private int duration;

    @Enumerated(EnumType.STRING)
    private AccessMusic access;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "musicEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<AvatarEntity> avatars;
}
