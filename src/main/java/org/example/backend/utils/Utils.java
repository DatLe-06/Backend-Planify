package org.example.backend.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
@AllArgsConstructor
public class Utils {
    public static String generateRefreshToken(long length, String characters) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("" + length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

    public static String generateFolderName(String email) {
        return email.split("@")[0].replaceAll("\\.", "_");
    }
}
