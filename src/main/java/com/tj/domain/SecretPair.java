package com.tj.domain;

import lombok.Data;

@Data
public class SecretPair {
    // 非对称加密公钥
    private byte[] pubKey;

    // 对称密钥密文
    private byte[] cipher;
}
