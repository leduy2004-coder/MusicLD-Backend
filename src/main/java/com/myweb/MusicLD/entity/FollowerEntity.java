package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.myweb.MusicLD.utility.enumUtils.RequestFollowStatus;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "followers")
public class FollowerEntity extends BaseEntity{


    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "sender_id", nullable = false)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserEntity receiver;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RequestFollowStatus status;
}
