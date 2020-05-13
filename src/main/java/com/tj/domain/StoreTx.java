package com.tj.domain;

import lombok.Data;

import static com.tj.util.UtilTool.getRandomByte;

@Data
public class StoreTx {

    private byte[] data;

    // 隐私存证安全
    private SecretPair[] secretPair;

    @Override
    public String toString() {
        return "{" +
                "\"data\":\"123\"" +
                ", \"secretPair\":\"\"" +
                "}";
    }

    public static void main(String[] args) {
        StoreTx storeTx = new StoreTx();
        byte[] data = getRandomByte(16);
        storeTx.setData(data);

        System.out.println(storeTx.toString());
    }
}


