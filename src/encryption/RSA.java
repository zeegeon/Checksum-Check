package encryption;

import java.io.UnsupportedEncodingException;
import java.security.*;
//import java.security.spec.*;
import java.util.Base64;

import javax.crypto.*;

public class RSA {
    // Generate 1024bit RSA key pair
    public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(1024, new SecureRandom());
        return gen.genKeyPair();
    }
  
    //RSA Encryption by using Public Key
    public static String encryptRSA(String plainText, PublicKey publicKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytePlain = cipher.doFinal(plainText.getBytes());
        return Base64.getEncoder().encodeToString(bytePlain);
    }

    // RSA Encryption by using Private Key
    public static String decryptRSA(String encrypted, PrivateKey privateKey)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {
        Cipher cipher = Cipher.getInstance("RSA");
        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] bytePlain = cipher.doFinal(byteEncrypted);
        return new String(bytePlain, "utf-8");
    }

//    public static PublicKey getPublicKeyFromBase64Encrypted(String base64PublicKey)
//            throws NoSuchAlgorithmException, InvalidKeySpecException {
//        byte[] decodedBase64PubKey = Base64.getDecoder().decode(base64PublicKey);
//
//        return KeyFactory.getInstance("RSA")
//                .generatePublic(new X509EncodedKeySpec(decodedBase64PubKey));
//    }
//
//    public static PrivateKey getPrivateKeyFromBase64Encrypted(String base64PrivateKey)
//            throws NoSuchAlgorithmException, InvalidKeySpecException {
//        byte[] decodedBase64PrivateKey = Base64.getDecoder().decode(base64PrivateKey);
//
//        return KeyFactory.getInstance("RSA")
//                .generatePrivate(new PKCS8EncodedKeySpec(decodedBase64PrivateKey));
//    }
}