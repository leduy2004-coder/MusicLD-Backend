package com.myweb.MusicLD.service;

import org.springframework.web.multipart.MultipartFile;


public interface StorageService {
     String uploadImage(MultipartFile file);
     byte[] downloadImage(String fileName);
}
