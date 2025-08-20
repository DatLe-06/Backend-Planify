package org.example.backend.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.example.backend.constant.CloudinaryTarget;
import org.example.backend.entity.User;
import org.example.backend.exception.custom.UploadException;
import org.example.backend.utils.MessageUtils;
import org.example.backend.utils.Utils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UploadService {
    private final MessageUtils messageUtils;
    private final Cloudinary cloudinary;

    public String upload(MultipartFile file, User user, CloudinaryTarget target) {
        String folderPath = "Planify/" + Utils.generateFolderName(user.getEmail()) + "/" + target;
        String name = Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0];

        try {
            Map<?,?> params = ObjectUtils.asMap(
                    "folder", folderPath,
                    "public_id",name + "_" + UUID.randomUUID(),
                    "resource_type", "auto"
            );

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(
                    file.getBytes(), params
            );

            return uploadResult.get("public_id").toString();
        } catch (IOException e) {
            System.err.println(e);
            throw new UploadException(messageUtils.getMessage("upload.fail"));
        }
    }

    public String delete(String publicId) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> result = (Map<String, Object>) cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "auto"));
            return result.get("result").toString();
        } catch (Exception e) {
            throw new RuntimeException("Could not delete the file!", e);
        }
    }

    public String buildCloudinaryUrl(String publicId) {
        if (publicId == null || publicId.isEmpty()) return null;

        return cloudinary.url()
                .transformation(new Transformation<>().width(200).height(200).crop("fill"))
                .generate(publicId);
    }
}
