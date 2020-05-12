package com.tj.domain;

import lombok.Data;

@Data
public class TransactionHashDTO {
    private Integer version;
    private Integer type;
    private Integer subType;
    private Long timestamp;
    private byte[] data;
    private byte[] pubKey;
    private byte[] extra;
    private byte[] sign;
}
