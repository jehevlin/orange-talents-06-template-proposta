package br.com.zuppyacademy.jessica.proposta.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.vault.annotation.VaultPropertySource;

@Configuration
@VaultPropertySource("${vault.path}")
public class CryptoConfig {

    @Value("${crypto.password}")
    private String password;

    @Value("${crypto.salt}")
    private String salt;

    public String getPassword() {
        return password;
    }

    public String getSalt() {
        return salt;
    }
}
