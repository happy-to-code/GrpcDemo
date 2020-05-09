package com.tj.domain;


import lombok.Data;

@Data
public class BlockHeader {
    private Integer version;
    private Long height;
    private Long timestamp;
    private String blockHash;
    private String previousHash;
    private String worldStateRoot;
    private String transactionRoot;
}
