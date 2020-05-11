package com.tj.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.tj.common.StatusCode;
import com.tj.common.exception.ServiceException;
import com.tj.domain.Block;
import com.tj.domain.BlockHeader;
import com.tj.domain.StoreTx;
import com.tj.domain.TransactionHashDTO;
import com.tj.proto.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static com.tj.util.GmUtils.sm2Sign;
import static com.tj.util.GmUtils.sm3Hash;
import static com.tj.util.UtilTool.*;

@Slf4j
@Service
public class PeerService {
    @Resource
    private PeerGrpc.PeerBlockingStub stub;

    @Value("${peer.pubKey}")
    private String pubKey;


    /**
     * 获取区块链高度
     *
     * @return
     */
    public long getHeight() {
        // 将16进制的pubKey转换成ByteString
        ByteString peerPubKey = convertPubKeyToByteString(pubKey);

        // 封装请求对象
        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder().setPubkey(peerPubKey).build();

        // 获取高度
        //PeerRequest: PeerRequest, PeerResponse:BlockchainNumber
        MyPeer.PeerResponse response = stub.blockchainGetHeight(request);

        if (!response.getOk()) {
            throw new ServiceException(StatusCode.SERVER_500000.value(), "请求出错！");
        }


        Msg.BlockchainNumber blockchainNumber = null;
        try {
            blockchainNumber = Msg.BlockchainNumber.parseFrom(response.getPayload());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        long number = blockchainNumber.getNumber();
        return number;
    }

    /**
     * 根据高度查询详情
     *
     * @param height
     */
    public Block getBlockByHeight(Integer height) throws InvalidProtocolBufferException, UnsupportedEncodingException {
        // 将16进制的pubKey转换成ByteString
        ByteString peerPubKey = convertPubKeyToByteString(pubKey);

        // 封装请求对象
        Msg.BlockchainNumber blockchainNumber = Msg.BlockchainNumber.newBuilder().setNumber(height).build();
        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder()
                .setPubkey(peerPubKey)
                .setPayload(blockchainNumber.toByteString())
                .build();

        MyPeer.PeerResponse peerResponse = stub.blockchainGetBlockByHeight(request);
        // PeerRequest: BlockchainNumber, PeerResponse:Block
        MyBlock.Block block = MyBlock.Block.parseFrom(peerResponse.getPayload());

        return getReturnBlock(block);
    }

    /**
     * 构建返回对象
     *
     * @param block
     * @return
     */
    private Block getReturnBlock(MyBlock.Block block) {
        Block blockPojo = new Block();

        BlockHeader blockHeaderPojo = new BlockHeader();
        blockHeaderPojo.setVersion(block.getHeader().getVersion());
        blockHeaderPojo.setHeight(block.getHeader().getHeight());
        blockHeaderPojo.setTimestamp(block.getHeader().getTimestamp());
        blockHeaderPojo.setBlockHash(covent10To16Str(block.getHeader().getBlockHash().toByteArray()));
        blockHeaderPojo.setPreviousHash(covent10To16Str(block.getHeader().getPreviousHash().toByteArray()));
        blockHeaderPojo.setWorldStateRoot(covent10To16Str(block.getHeader().getWorldStateRoot().toByteArray()));
        blockHeaderPojo.setTransactionRoot(covent10To16Str(block.getHeader().getTransactionRoot().toByteArray()));

        blockPojo.setHeader(blockHeaderPojo);
        blockPojo.setExtra(block.getExtra().toStringUtf8());

        List<ByteString> txsList = block.getTxsList();
        if (txsList.size() > 0) {
            List<String> txs = new ArrayList<>(10);
            for (ByteString bytes : txsList) {
                txs.add(covent10To16Str(bytes.toByteArray()));
            }
            blockPojo.setTxs(txs);
        }
        return blockPojo;
    }

    /**
     * 新增交易
     */
    public void newTransaction() {
        // 将16进制的pubKey转换成ByteString
        ByteString peerPubKey = convertPubKeyToByteString(pubKey);

//        message TransactionHeader{
//            uint32 version = 1;
//            uint32 type = 2;
//            uint32 subType = 3;
//            uint64 timestamp = 4;
//            bytes transactionHash = 5;
//        }
//
//
//        message Transaction{
//            TransactionHeader header = 1;
//            bytes data = 2;//交易内容
//            bytes pubkey = 3; //发送方公钥
//            bytes sign =4;//签名
//            bytes result = 5;
//            bytes extra = 6;
//        }

        StoreTx storeTx = new StoreTx();
        byte[] data = {12};

        storeTx.setData(data);

        ByteBuf buf = Unpooled.buffer(32);
        buf.writeBytes(storeTx.getData());
        byte[] storeArray = buf.array();



        long currentTime = System.currentTimeMillis() / 1000;
        log.info("currentTime::::{}", currentTime);
        TransactionHashDTO transactionHashDTO = new TransactionHashDTO();
        transactionHashDTO.setVersion(1);
        transactionHashDTO.setType(0);
        transactionHashDTO.setSubType(0);
        transactionHashDTO.setTimestamp(currentTime);
        transactionHashDTO.setPubKey(peerPubKey);
        transactionHashDTO.setData(new String(storeArray));

        log.info("transactionHashDTO------->", transactionHashDTO.toString());
        // 获取transactionHashByte
        byte[] transactionHashByte = getTransactionHash(transactionHashDTO);
        // 对TransactionHash进行加密处理
        byte[] hashBytes = sm3Hash(transactionHashByte);
        log.info("hashBytes------>{}", hashBytes);

        // 通过transactionHashByte获取签名  static byte[] sm2Encrypt(byte[] publicKey, byte[] data)
        byte[] sign = new byte[0];
        try {
            sign = sm2Sign(hexToByteArray(pubKey), transactionHashByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("=============sign:{}", sign);


        // 封装请求对象
        MyTransaction.TransactionHeader transactionHeader = MyTransaction.TransactionHeader.newBuilder()
                .setVersion(1)
                .setType(1)
                .setSubType(0)
                .setTimestamp(currentTime)
                .setTransactionHash(ByteString.copyFrom(hashBytes))
                .build();

        MyTransaction.Transaction transaction = MyTransaction.Transaction.newBuilder()
                .setHeader(transactionHeader)
                .setPubkey(peerPubKey)
                .setSign(ByteString.copyFrom(sign))
                .build();

        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder()
                .setPubkey(peerPubKey)
                .setPayload(transaction.toByteString())
                .build();

        MyPeer.PeerResponse peerResponse = stub.newTransaction(request);

        System.out.println("peerResponse = " + peerResponse);
    }

    /**
     * 获取TransactionHash
     *
     * @param transactionHashDTO
     * @return
     */
    private byte[] getTransactionHash(TransactionHashDTO transactionHashDTO) {
        ByteBuf buf = Unpooled.buffer(32);
        buf.writeInt(transactionHashDTO.getVersion());
        buf.writeInt(transactionHashDTO.getType());
        buf.writeInt(transactionHashDTO.getSubType());
        buf.writeLong(transactionHashDTO.getTimestamp());
        buf.writeCharSequence(transactionHashDTO.getPubKey().toStringUtf8(), Charset.defaultCharset());
        buf.writeCharSequence(transactionHashDTO.getData(), Charset.defaultCharset());
        log.info("-----------------" + buf);
        return buf.array();
    }
}
