package com.tj.util.rsa2;

import org.apache.commons.codec.binary.Base64;

import java.security.*;

/**
 * @author zhangyifei
 */
public class Main {

    public static void main(String[] args) throws Exception {
        /*
         * 1. 先生成一对 RSA 密钥, 用于测试
         */
        // 随机生成一对 RAS 密钥（包含公钥和私钥）
        KeyPair keyPair = generateKeyPair();
        // 获取 公钥 和 私钥
        PublicKey pubKey = keyPair.getPublic();
        PrivateKey priKey = keyPair.getPrivate();

        /*
         * 2. 原始数据
         */
        String data = "你好, World11111";

        /*
         * 3. 私钥签名: 对数据进行签名, 计算签名结果
         */
        // 根据指定算法获取签名工具
        Signature sign = Signature.getInstance("Sha1WithRSA");
        // 用私钥初始化签名工具
        sign.initSign(priKey);
        // 添加要签名的数据
        sign.update(data.getBytes());
        // 计算签名结果（签名信息）
        byte[] signInfo = sign.sign();
        // 输出签名结果的 Base64 字符串
        // System.out.println(new BASE64Encoder().encode(signInfo));
        String s = new Base64().encodeAsString(signInfo);
        System.out.println("s = " + s);

        /*
         * 4. 公钥验签: 用公钥校验数据的签名是否来自指定的私钥
         */
        // 根据指定算法获取签名工具
        sign = Signature.getInstance("Sha1WithRSA");
        // 用公钥初始化签名工具
        sign.initVerify(pubKey);
        // 添加要校验的数据
        sign.update(data.getBytes());
        // 校验数据的签名信息是否正确,
        // 如果返回 true, 说明该数据的签名信息来自该公钥对应的私钥,
        // 同一个私钥的签名, 数据和签名信息一一对应, 只要其中有一点修改, 则用公钥无法校验通过,
        // 因此可以用私钥签名, 然后用公钥来校验数据的完整性与签名者（所有者）
        boolean verify = sign.verify(signInfo);
        System.out.println(verify);
    }

    /**
     * 随机生成 RSA 密钥对（包含公钥和私钥）
     */
    private static KeyPair generateKeyPair() throws Exception {
        // 获取指定算法的密钥对生成器
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器（指定密钥长度, 使用默认的安全随机数源）
        gen.initialize(2048);
        // 随机生成一对密钥（包含公钥和私钥）
        return gen.generateKeyPair();
    }
}
