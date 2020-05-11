package com.tj.domain;

import com.google.protobuf.ByteString;
import lombok.Data;

@Data
public class TransactionHashDTO {
    private Integer version;
    private Integer type;
    private Integer subType;
    private Long timestamp;
    private String data;
    private ByteString pubKey;
    private String extra;
    private String sign;
}
