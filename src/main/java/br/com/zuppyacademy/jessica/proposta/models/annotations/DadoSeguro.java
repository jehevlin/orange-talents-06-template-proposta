package br.com.zuppyacademy.jessica.proposta.models.annotations;

import br.com.zuppyacademy.jessica.proposta.configs.CryptoConfig;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class DadoSeguro implements AttributeConverter<String, String> {

    private final TextEncryptor textEncryptor;

    public DadoSeguro(CryptoConfig cryptoConfig) {
        this.textEncryptor = Encryptors.delux(cryptoConfig.getPassword(), cryptoConfig.getSalt());
    }

    @Override
    public String convertToDatabaseColumn(String s) {
        return textEncryptor.encrypt(s);
    }

    @Override
    public String convertToEntityAttribute(String s) {
        return textEncryptor.decrypt(s);
    }
}
