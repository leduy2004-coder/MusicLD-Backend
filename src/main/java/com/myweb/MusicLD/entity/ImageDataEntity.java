package com.myweb.MusicLD.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
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

}
