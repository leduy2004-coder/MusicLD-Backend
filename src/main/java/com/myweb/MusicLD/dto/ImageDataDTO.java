package com.myweb.MusicLD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ImageDataDTO extends BaseDTO {
    private String name;
    private String type;
    private byte[] imageData;
}
