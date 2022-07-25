package jocture.todo.jasypt;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class JasyptGeneratorTest {

    @Value("${jasypt.encryptor.password}")
    private String encryptKey;

    @Test
    void generateEncValue() {
        String encSa = encryptString("sa");
        System.out.println(">>> encSa = " + encSa);
    }

    private String encryptString(String str) {
        StandardPBEStringEncryptor pbeEnc = new StandardPBEStringEncryptor();
        pbeEnc.setPassword(encryptKey);
        return pbeEnc.encrypt(str);
    }
}
