package com.myweb.MusicLD.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.myweb.MusicLD.dto.response.CloudinaryResponse;
import com.myweb.MusicLD.exception.AppException;
import com.myweb.MusicLD.exception.ErrorCode;
import com.myweb.MusicLD.service.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImpl implements CloudinaryService {

    private final Cloudinary cloudinary;

    @Override
    public CloudinaryResponse uploadFile(final MultipartFile file, final String fileName) {
        try {

            final long maxSizeInBytes = 6 * 1024 * 1024;
            if (file.getSize() > maxSizeInBytes) {
                throw new AppException(ErrorCode.FILE_TOO_LARGE);
            }

            // Upload lên Cloudinary
            final Map result = this.cloudinary.uploader()
                    .upload(file.getBytes(),
                            Map.of(
                                    "public_id", "MusicLD/product/" + fileName.trim(),
                                    "resource_type", "auto"
                            ));

            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url)
                    .build();

        } catch (final Exception e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }
    }




    @Override
    public void deleteFile(String publicId, String resourceType) {
        try {
            Map<String, Object> options = ObjectUtils.asMap(
                    "resource_type", resourceType
            );
            cloudinary.uploader().destroy(publicId, options);
            System.out.println("Deleted successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
