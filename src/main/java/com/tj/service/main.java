package com.tj.service;

import com.google.protobuf.ByteString;
import com.tj.domain.StoreTx;
import com.tj.domain.TransactionHashDTO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import static com.tj.util.UtilTool.convertPubKeyToByteString;
import static com.tj.util.UtilTool.getRandomByte;

/**
 * @Author: zhangyifei
 * @Date: 2020/5/13 10:24
 * @Describe:
 */
public class main {
    public static void main(String[] args) {
        StoreTx storeTx = new StoreTx();
        byte[] data = getRandomByte(16);

        storeTx.setData(data);

        ByteBuf buf = Unpooled.buffer(16);
        buf.writeBytes(storeTx.getData());
        byte[] storeArray = buf.array();

        // 将16进制的pubKey转换成ByteString
        String hexPub = "2c7f6f353d828e99692bb8bf960186f218674581495b399db753c00dd636c4f0583f7a833ce67d352e7d32be5d6e3fc899d7004efe1f450fc1a078ee856a8b75";
        ByteString peerPubKey = convertPubKeyToByteString(hexPub);


        long currentTime = 1589336556628L;
        // log.info("currentTime::::{}", currentTime);
        TransactionHashDTO transactionHashDTO = new TransactionHashDTO();
        transactionHashDTO.setVersion(0);
        transactionHashDTO.setType(0);
        transactionHashDTO.setSubType(0);
        transactionHashDTO.setTimestamp(currentTime);

        transactionHashDTO.setData(storeArray);
        transactionHashDTO.setPubKey(peerPubKey.toByteArray());

        // transactionHashDTO.

    }


}
