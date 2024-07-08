package com.myweb.MusicLD.service;

import org.springframework.web.multipart.MultipartFile;


public interface StorageService {
    public String uploadImage(MultipartFile file);
    public byte[] downloadImage(String fileName);
}
