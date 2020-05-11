package com.tj.domain;

import lombok.Data;

@Data
public class StoreTx {

    private byte[] data;

    // 隐私存证安全
    private SecretPair[] secretPair;
}
