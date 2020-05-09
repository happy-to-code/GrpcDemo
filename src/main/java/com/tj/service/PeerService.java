package com.tj.service;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.tj.proto.Msg;
import com.tj.proto.MyPeer;
import com.tj.proto.PeerGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import static com.tj.util.UtilTool.convertPubKeyToByteString;

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
    public JSONObject getHeight() {
        // 将16进制的pubKey转换成ByteString
        ByteString peerPubKey = convertPubKeyToByteString(pubKey);

        // 封装请求对象
        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder().setPubkey(peerPubKey).build();

        // 获取PeerBlockingStub
        PeerGrpc.PeerBlockingStub stub = commonPeerService.getStubByIpAndPort();

        // 获取高度
        //PeerRequest: PeerRequest, PeerResponse:BlockchainNumber
        MyPeer.PeerResponse response = stub.blockchainGetHeight(request);

        JSONObject jsonObject = new JSONObject();
        if (!response.getOk()) {
            jsonObject.put("ok", false);
            jsonObject.put("data", "查询出错");

            return jsonObject;
        }


        try {
            Msg.BlockchainNumber blockchainNumber = Msg.BlockchainNumber.parseFrom(response.getPayload());
            long number = blockchainNumber.getNumber();
            jsonObject.put("ok", true);
            jsonObject.put("data", number);

            return jsonObject;
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
            jsonObject.put("ok", false);
            jsonObject.put("data", e.getMessage());

            return jsonObject;
        }

    }
}
