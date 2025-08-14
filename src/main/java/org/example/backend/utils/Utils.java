package org.example.backend.utils;

import java.security.SecureRandom;

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
}
