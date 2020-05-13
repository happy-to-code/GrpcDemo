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

    public TransactionHashDTO() {
        this.extra = new byte[0];
        this.sign = new byte[0];
    }


    // public byte[] Marashal(){
    //
    //     ByteBuf buf = Unpooled.buffer();
    //     buf.writeInt(this.getVersion());
    //     buf.writeInt(this.getType());
    //     buf.writeInt(this.getSubType());
    //     buf.writeLong(this.getTimestamp());
    //
    //     buf.writeInt(this.getData().length);
    //     buf.writeBytes(this.getData());
    //
    //     buf.writeInt(this.getExtra().length);
    //     buf.writeBytes(this.getExtra());
    //
    //     buf.writeInt(this.getPubKey().length);
    //     buf.writeBytes(this.getPubKey());
    //     System.out.println("-----------------" + buf);
    //
    //
    // }
}
