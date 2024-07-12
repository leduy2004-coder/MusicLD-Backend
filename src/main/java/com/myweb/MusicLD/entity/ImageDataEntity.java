package com.myweb.MusicLD.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ImageData")
public class ImageDataEntity extends BaseEntity{
    private String name;

    private String type;

    @Lob
    @Column(name = "imagedata", length = 5000, columnDefinition = "VARBINARY(MAX)")
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonIgnore
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
