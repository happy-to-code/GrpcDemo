package com.tj.service;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.tj.common.StatusCode;
import com.tj.common.exception.ServiceException;
import com.tj.domain.Block;
import com.tj.domain.BlockHeader;
import com.tj.proto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static com.tj.util.UtilTool.*;

@Service
public class PeerService {

    @Resource
    private CommonPeerService commonPeerService;

    @Value("${peer.pubKey}")
    private String pubKey;


    /**
     * 获取区块链高度
     *
     * @return
     */
    public long getHeight() {
        // 将16进制的pubKey转换成ByteString
        ByteString peerPubKey = convertPubKeyToByteString(pubKey, 16);

        // 封装请求对象
        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder().setPubkey(peerPubKey).build();

        // 获取PeerBlockingStub
        PeerGrpc.PeerBlockingStub stub = commonPeerService.getStubByIpAndPort();

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
        ByteString peerPubKey = convertPubKeyToByteString(pubKey, 16);

        // 封装请求对象
        Msg.BlockchainNumber blockchainNumber = Msg.BlockchainNumber.newBuilder().setNumber(height).build();
        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder()
                .setPubkey(peerPubKey)
                .setPayload(blockchainNumber.toByteString())
                .build();

        // 获取PeerBlockingStub
        PeerGrpc.PeerBlockingStub stub = commonPeerService.getStubByIpAndPort();
        MyPeer.PeerResponse peerResponse = stub.blockchainGetBlockByHeight(request);
        // PeerRequest: BlockchainNumber, PeerResponse:Block
        MyBlock.Block block = MyBlock.Block.parseFrom(peerResponse.getPayload());

        Block blockPojo = getReturnBlock(block);

        return blockPojo;
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
        ByteString peerPubKey = convertPubKeyToByteString(pubKey, 16);

        // 封装请求对象
        MyTransaction.TransactionHeader transactionHeader = MyTransaction.TransactionHeader.newBuilder()
                .setTimestamp(System.currentTimeMillis())
                .setVersion(1)
                .setSubType(0)
                .build();

        MyTransaction.Transaction transaction = MyTransaction.Transaction.newBuilder()
                .setHeader(transactionHeader)
                .setPubkey(peerPubKey)
                .build();

        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder()
                .setPubkey(peerPubKey)
                .setPayload(transaction.toByteString())
                .build();

        // 获取PeerBlockingStub
        PeerGrpc.PeerBlockingStub stub = commonPeerService.getStubByIpAndPort();
        MyPeer.PeerResponse peerResponse = stub.newTransaction(request);

        System.out.println("peerResponse = " + peerResponse);
    }
}
