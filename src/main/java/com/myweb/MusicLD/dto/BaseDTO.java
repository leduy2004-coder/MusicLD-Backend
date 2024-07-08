package com.myweb.MusicLD.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public abstract class BaseDTO {

    protected Long id;
    protected Long createdBy;
    protected LocalDateTime createdDate;
    protected Long modifiedBy;
    protected LocalDateTime modifiedDate;
}