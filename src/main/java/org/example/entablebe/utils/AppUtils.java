package org.example.entablebe.utils;

import org.example.entablebe.pojo.generic.GenericSuccessPageableResponse;
import org.springframework.data.domain.Pageable;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static java.util.Arrays.asList;

public class AppUtils {
    public static final List<String> angularUrls = asList(
            "/main",
            "/auth"
//            "/email-validation"
    );

    public static boolean isAngularUrl(final String url) {
        return angularUrls.contains(url);
    }

    public static String encrypt(String dataToEncrypt, String key) throws Exception {
        byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 32);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[16];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(dataToEncrypt.getBytes());

        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String dataToDecrypt, String key) throws Exception {
        byte[] keyBytes = Arrays.copyOf(key.getBytes(StandardCharsets.UTF_8), 32);

        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[16];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        byte[] base64Decrypted = Base64.getDecoder().decode(dataToDecrypt);

        byte[] decryptedData = decryptCipher.doFinal(base64Decrypted);

        return new String(decryptedData, StandardCharsets.UTF_8);
    }

    public static <T> GenericSuccessPageableResponse<T> buildPageableResponse(List<T> payload,
                                                                              Pageable pageable,
                                                                              Integer totalCount) {
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int nextPage = totalCount > (pageSize * currentPage) ? currentPage + 1 : currentPage;

        return new GenericSuccessPageableResponse<>(payload, pageSize, currentPage, nextPage, totalCount);
    }
}
