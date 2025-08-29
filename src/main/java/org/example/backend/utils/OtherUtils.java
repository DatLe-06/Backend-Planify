package org.example.backend.utils;

import lombok.AllArgsConstructor;

import java.security.SecureRandom;

@AllArgsConstructor
public class OtherUtils {
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
