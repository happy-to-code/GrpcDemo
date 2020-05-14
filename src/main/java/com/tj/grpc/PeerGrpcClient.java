package com.tj.grpc;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.tj.proto.Msg;
import com.tj.proto.MyPeer;
import com.tj.proto.PeerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static com.tj.util.UtilTool.hexToByteArray;

public class PeerGrpcClient {
    private final ManagedChannel channel;
    private final PeerGrpc.PeerBlockingStub blockingStub;


    public PeerGrpcClient(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext(true)
                .build();

        blockingStub = PeerGrpc.newBlockingStub(channel);

        System.out.println("blockingStub = " + blockingStub);
    }


    public void greet() {
        String pubKeyStr = "2c7f6f353d828e99692bb8bf960186f218674581495b399db753c00dd636c4f0583f7a833ce67d352e7d32be5d6e3fc899d7004efe1f450fc1a078ee856a8b75";

        byte[] bytes1 = hexToByteArray(pubKeyStr);


        ByteString bytes2 = ByteString.copyFrom(bytes1);
        ByteString bytes = ByteString.copyFromUtf8(pubKeyStr);
        System.out.println("----->:" + bytes);

        MyPeer.PeerRequest request = MyPeer.PeerRequest.newBuilder().setPubkey(bytes2).build();
        MyPeer.PeerResponse response = blockingStub.blockchainGetHeight(request);
        System.out.println("-->::" + response);


        try {
            Msg.BlockchainNumber blockchainNumber = Msg.BlockchainNumber.parseFrom(response.getPayload());
            long number = blockchainNumber.getNumber();
            System.out.println("number:" + number);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

//        response.getPayload();
//        MyPeer.

    }

    public static void main(String[] args) throws InterruptedException {
        PeerGrpcClient client = new PeerGrpcClient("10.1.3.150", 9008);
        client.greet();

    }
}
