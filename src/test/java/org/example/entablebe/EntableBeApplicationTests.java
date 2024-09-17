package org.example.entablebe;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpringBootTest
class EntableBeApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {

    }

    @Test
    public void testTokenEncryption() throws Exception {
        byte[] keyBytes = Arrays.copyOf("12345".getBytes(StandardCharsets.UTF_8), 32);
        String message = "user=test_user";
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        byte[] iv = new byte[16];
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);

        byte[] encryptedBytes = cipher.doFinal(message.getBytes());

        String encryptedAsString = Base64.getEncoder().encodeToString(encryptedBytes);

        System.out.println("Encrypted data: " + encryptedAsString);


        Cipher decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        decryptCipher.init(Cipher.DECRYPT_MODE, keySpec, ivParameterSpec);

        byte[] base64Decrypted = Base64.getDecoder().decode(encryptedAsString);

        byte[] decryptedData = decryptCipher.doFinal(base64Decrypted);

        System.out.println("Decrypted: " + new String(decryptedData, StandardCharsets.UTF_8));


    }

    @Test
    public void testQualificationExtraction() {
        String userInfo = "competences:{mock condition test mock condition test mock condition test ;mock condition test  mock condition test  mock condition test ;} " +
                "qualifications:{D1 Physical;D4 Emotional;D6 Environmental;}";

        String[] parts = userInfo.split("(?<=[}])\\s+");
        Map<String, List<String>> result = new HashMap<>();
        for (String part : parts) {
            String[] keyValue = part.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];
                value = value.replace("{","").replace("}", "");

                List<String> list = Arrays.stream(value.split(";"))
                        .map(String::trim)
                        .toList();
                result.put(key,list);
            }
        }
        System.out.println();
    }
}
