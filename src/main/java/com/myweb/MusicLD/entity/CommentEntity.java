package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "comment")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommentEntity extends BaseEntity{
    @Column(name = "content", nullable = false, length = 500)
    String content;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id", nullable = false)
    UserEntity userEntity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "music_id", nullable = false)
    MusicEntity musicEntity;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "parent_comment_id")
    CommentEntity parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL)
    List<CommentEntity> replies;

}
