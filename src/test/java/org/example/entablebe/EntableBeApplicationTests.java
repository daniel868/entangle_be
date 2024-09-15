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
import java.util.Arrays;
import java.util.Base64;
import java.util.Properties;

@SpringBootTest
class EntableBeApplicationTests {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    void contextLoads() {
//        SimpleMailMessage message = new SimpleMailMessage();
//        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
//        javaMailSender.setHost("smtp.gmail.com");
//        javaMailSender.setPort(587);
//
//        javaMailSender.setUsername("sincalexndrudaniel@gmail.com");
//        javaMailSender.setPassword("wmvp ikdq plwa acxf");
//
//        Properties props = javaMailSender.getJavaMailProperties();
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", true);
//
//        message.setFrom("sincalexndrudaniel@gmail.com");
//        message.setTo("sincalexandrudaniel@gmail.com");
//        message.setSubject("test email sending from springboot");
//        message.setText("Hello from Springboot app");
//
//        javaMailSender.send(message);
    }

    @Test
    public void testTokenEncryption() throws Exception {
        byte[] keyBytes = Arrays.copyOf("L2r01kJ/q5foSD1EbjvTi+vP3570cBp9HPc45LNbvHu2skgHzZ8fcqriJLgZH22D".getBytes(StandardCharsets.UTF_8), 32);
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

        String[] split = userInfo.split("qualifications:");

        if (split.length > 1) {
            String extractedQualification = split[1];
            extractedQualification = extractedQualification.replace("{", "")
                    .replace("}", "");

            Arrays.stream(extractedQualification.split(";"))
                    .map(String::trim)
                    .forEach(System.out::println);
        }
    }
}
