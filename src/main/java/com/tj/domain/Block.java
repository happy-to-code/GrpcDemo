package com.tj.domain;

import lombok.Data;

import java.util.List;

@Data
public class Block {
    private BlockHeader header;
    private List<String> txs;
    private String extra;
}
