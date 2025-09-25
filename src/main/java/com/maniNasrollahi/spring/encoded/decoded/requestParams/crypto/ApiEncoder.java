package com.maniNasrollahi.spring.encoded.decoded.requestParams.crypto;

import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class ApiEncoder {

    private static final String SECRET_KEY = "SuperDuper16BitK"; // 16 bytes

    private final Cipher encryptCipher;
    private final Cipher decryptCipher;

    public ApiEncoder() throws Exception {
        SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");

        encryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        encryptCipher.init(Cipher.ENCRYPT_MODE, key);

        decryptCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);
    }

    public String encode(String plainText) throws Exception {
        byte[] encrypted = encryptCipher.doFinal(plainText.getBytes());
        return Base64.getUrlEncoder().encodeToString(encrypted);
    }

    public String decode(String encodedText) throws Exception {
        byte[] decoded = Base64.getUrlDecoder().decode(encodedText);
        return new String(decryptCipher.doFinal(decoded));
    }
}
