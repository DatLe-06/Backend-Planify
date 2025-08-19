package org.example.backend.controller;

import lombok.AllArgsConstructor;
import org.example.backend.service.UploadService;
import org.example.backend.service.user.CustomUserDetailsService;
import org.example.backend.utils.MessageUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
@AllArgsConstructor
public class UserController {
    private final MessageUtils messageUtils;
    private CustomUserDetailsService userDetailsService;
    private UploadService uploadService;

    @PostMapping("/avatar")
    public ResponseEntity<?> uploadAvatar(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.status(HttpStatus.OK).body(userDetailsService.uploadAvatar(file));
    }

    @DeleteMapping("/avatar")
    public ResponseEntity<?> deleteAvatar(@RequestParam("publicId") String publicId) {
        uploadService.delete(publicId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(messageUtils.getMessage("avatar.delete.success"));
    }
}
