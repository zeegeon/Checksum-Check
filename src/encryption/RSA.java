package encryption;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSA {

    public static void main(String[] args) throws Exception {
        // RSA 키쌍을 생성합니다.

        KeyPair keyPair = RSA_make.genRSAKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String plainText = "RAONTECH";

        // Base64 인코딩된 암호화 문자열 입니다.
        String encrypted = RSA_make.encryptRSA(plainText, publicKey);
        System.out.println("encrypted : " + encrypted);

        // 복호화 합니다.
        String decrypted = RSA_make.decryptRSA(encrypted, privateKey);
        System.out.println("decrypted : " + decrypted);

        // 공개키를 Base64 인코딩한 문자일을 만듭니다.
        byte[] bytePublicKey = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(bytePublicKey);
        System.out.println("Base64 Public Key : " + base64PublicKey);

        // 개인키를 Base64 인코딩한 문자열을 만듭니다.
        byte[] bytePrivateKey = privateKey.getEncoded();
        String base64PrivateKey = Base64.getEncoder().encodeToString(bytePrivateKey);
        System.out.println("Base64 Private Key : " + base64PrivateKey);
        
        // 문자열로부터 PrivateKey와 PublicKey를 얻습니다.
        PrivateKey prKey = RSA_make.getPrivateKeyFromBase64String(base64PrivateKey);
        PublicKey puKey = RSA_make.getPublicKeyFromBase64String(base64PublicKey);
        
        // 공개키로 암호화 합니다.
        String encrypted2 = RSA_make.encryptRSA(plainText, puKey);
        System.out.println("encrypted : " + encrypted2);

        // 복호화 합니다.
        String decrypted2 = RSA_make.decryptRSA(encrypted, prKey);
        System.out.println("decrypted : " + decrypted2);
    }
}
