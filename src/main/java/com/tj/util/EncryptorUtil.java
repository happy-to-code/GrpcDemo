package com.tj.util;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 * @Describle:
 * @Author: zhangyifei
 * @Date: 2020/5/9
 */
public class EncryptorUtil {
    /**
     * 加密
     *
     * @param passeord
     * @return
     */
    public static String encode(String passeord, String salt) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加salt(盐)
        textEncryptor.setPassword(salt);

        return textEncryptor.encrypt(passeord);
    }

    /**
     * 解密
     *
     * @param encode
     * @return
     */
    public static String decrypt(String encode, String salt) {
        BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
        //加salt(盐)
        textEncryptor.setPassword(salt);

        return textEncryptor.decrypt(encode);
    }

    public static void main(String args[]) {
        String salt = "tjfocvDz7oJn6";
        System.out.println(encode("2c7f6f353d828e99692bb8bf960186f218674581495b399db753c00dd636c4f0583f7a833ce67d352e7d32be5d6e3fc899d7004efe1f450fc1a078ee856a8b75", salt));
//        System.out.println(decrypt("1Iy4nynFrQIe2pw9ozdV9VgF+cpIWQdHnaqILd4JRBlt3G0OPa3csQ==", salt));
//        System.out.println(decrypt("qsTnuH2NXhnsJFZeAGaXwFXiqRWEk2YnteWT1jsHgmhimlg6P/Dtvg==", salt));
    }

}
