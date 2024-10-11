package com.Ecommerce_backend.service;

import com.Ecommerce_backend.exception.OurException;
import com.cloudinary.Cloudinary;
import com.cloudinary.Singleton;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Component
public class CloudinaryService {
    final Cloudinary cloudinary;

    @Autowired
    public CloudinaryService(@Value("${cloud.key}") String key,
                             @Value("${cloud.secret}") String secret,
                             @Value("${cloud.name}") String cloud) {
        cloudinary = Singleton.getCloudinary();
        cloudinary.config.cloudName=cloud;
        cloudinary.config.apiSecret=secret;
        cloudinary.config.apiKey=key;
    }

    public String  saveImageToCloud(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return uploadResult.get("url").toString();
        } catch (Exception  e) {
            e.printStackTrace();
            throw new OurException("Unable to upload image to cloudinary cloud" + e.getMessage());
        }
    }
}
