package com.imooc.activitiweb.util;


import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.ArrayUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

public class RSAUtils {

    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();


    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate(); // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic(); // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey
                .getEncoded()));
        System.out.println(publicKeyString);
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey
                .getEncoded())));
        System.out.println(privateKeyString);
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString); // 0表示公钥
        keyMap.put(1, privateKeyString); // 1表示私钥
    }

    /**
     * RSA公钥加密
     *
     * @param str
     *            加密字符串
     * @param publicKey
     *            公钥
     * @return 密文
     * @throws Exception
     *             加密过程中的异常信息
     */
    public static String encrypt(String str, String publicKey) throws Exception {
        // base64编码的公钥
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        // RSA加密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        String outStr = Base64.encodeBase64String(cipher.doFinal(str
                .getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA私钥解密
     *
     * @param str
     *            加密字符串
     * @param privateKey
     *            私钥
     * @return 铭文
     * @throws Exception
     *             解密过程中的异常信息
     */
    public static String decrypt(String str, String privateKey)
            throws Exception {
        // 64位解码加密后的字符串
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        // base64编码的私钥
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        // RSA解密
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        String outStr = new String(cipher.doFinal(inputByte));
        return outStr;
    }

    /**
     * 信息公钥加密
     *
     * @param message
     * @param publicKey
     * @return
     * @throws Exception
     */
    public static byte[] encryptMessage(byte[] message, RSAPublicKey publicKey)
            throws Exception {
        int encodeLength = publicKey.getModulus().bitLength() / 8 - 11;
        byte[] buffers = new byte[] {};
        Cipher cipher = Cipher.getInstance("RSA"); // RSA/ECB/PKCS1Padding
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        for (int i = 0; i < message.length; i += encodeLength) {
            byte[] subArray = ArrayUtils.subarray(message, i, i + encodeLength);
            byte[] doFinal = cipher.doFinal(subArray);
            buffers = ArrayUtils.addAll(buffers, doFinal);
        }
        return buffers;
    }

    /**
     * 取出公钥
     *
     * @param keyBytes
     *            base64编码后的 byt
     * @return
     * @throws Exception
     */
    public static RSAPublicKey loadPublicKey(byte[] keyBytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        return (RSAPublicKey) keyFactory.generatePublic(spec);
    }

    /**
     * 取出私钥
     *
     * @param keyBytes
     *            base64编码后的 byt
     * @return
     * @throws Exception
     */
    public static RSAPrivateKey loadPrivateKey(byte[] keyBytes)
            throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        return (RSAPrivateKey) keyFactory.generatePrivate(spec);
    }

    /**
     * 取得SHA1withRSA的密文
     *
     * @param message
     *            加密的字符串
     * @param privateKey
     * @return
     * @throws Exception
     */
    public static byte[] SHA1withRSA(byte[] message, RSAPrivateKey privateKey)
            throws Exception {
        Signature signature = Signature.getInstance("SHA1withRSA");
        signature.initSign(privateKey);
        signature.update(message);
        return signature.sign();
    }

}
