package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "followers")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FollowerEntity extends BaseEntity{


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "sender_id", nullable = false)
    UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "receiver_id", nullable = false)
    UserEntity receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    RequestFollowStatus status;
}
