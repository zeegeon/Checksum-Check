package encryption;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

public class RSA {

    public static void main(String[] args) throws Exception {
        // RSA Ű���� �����մϴ�.

        KeyPair keyPair = RSA_make.genRSAKeyPair();

        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String plainText = "RAONTECH";

        // Base64 ���ڵ��� ��ȣȭ ���ڿ� �Դϴ�.
        String encrypted = RSA_make.encryptRSA(plainText, publicKey);
        System.out.println("encrypted : " + encrypted);

        // ��ȣȭ �մϴ�.
        String decrypted = RSA_make.decryptRSA(encrypted, privateKey);
        System.out.println("decrypted : " + decrypted);

        // ����Ű�� Base64 ���ڵ��� �������� ����ϴ�.
        byte[] bytePublicKey = publicKey.getEncoded();
        String base64PublicKey = Base64.getEncoder().encodeToString(bytePublicKey);
        System.out.println("Base64 Public Key : " + base64PublicKey);

        // ����Ű�� Base64 ���ڵ��� ���ڿ��� ����ϴ�.
        byte[] bytePrivateKey = privateKey.getEncoded();
        String base64PrivateKey = Base64.getEncoder().encodeToString(bytePrivateKey);
        System.out.println("Base64 Private Key : " + base64PrivateKey);
        
        // ���ڿ��κ��� PrivateKey�� PublicKey�� ����ϴ�.
        PrivateKey prKey = RSA_make.getPrivateKeyFromBase64String(base64PrivateKey);
        PublicKey puKey = RSA_make.getPublicKeyFromBase64String(base64PublicKey);
        
        // ����Ű�� ��ȣȭ �մϴ�.
        String encrypted2 = RSA_make.encryptRSA(plainText, puKey);
        System.out.println("encrypted : " + encrypted2);

        // ��ȣȭ �մϴ�.
        String decrypted2 = RSA_make.decryptRSA(encrypted, prKey);
        System.out.println("decrypted : " + decrypted2);
    }
}
