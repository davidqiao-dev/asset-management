package com.qiao.assetmanagement.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class EncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void bcryptProducesDifferentHashesForSamePassword() {

        String rawPassword = "password";
        String hash1 = passwordEncoder.encode(rawPassword);
        String hash2 = passwordEncoder.encode(rawPassword);
        System.out.println("hash for password + salt: " + hash1);

        assertThat(hash1).isNotEqualTo(hash2); // salt -> different
        assertThat(passwordEncoder.matches(rawPassword, hash1)).isTrue();
        assertThat(passwordEncoder.matches(rawPassword, hash2)).isTrue();
        assertThat(passwordEncoder.matches("rawPassword", hash1)).isFalse();
    }
}
