package com.tj.util;

import com.google.protobuf.ByteString;
import com.tj.proto.PeerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.commons.lang3.StringUtils;

public class UtilTool {

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex) {
        int hexlen = inHex.length();
        byte[] result;
        if (hexlen % 2 == 1) {
            //奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {
            //偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = hexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }


    /**
     * Hex字符串转byte
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte
     */
    public static byte hexToByte(String inHex) {
        return (byte) Integer.parseInt(inHex, 16);
    }


    /**
     * 将pubKey转换成ByteString
     *
     * @param str
     * @return
     */
    public static ByteString convertPubKeyToByteString(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        byte[] bytes = hexToByteArray(str);
        return ByteString.copyFrom(bytes);
    }

    /**
     * 通过ip和端口获取PeerBlockingStub
     *
     * @param host
     * @param port
     * @return
     */
    public static PeerGrpc.PeerBlockingStub getBlockingStubByIpAndPort(String host, int port) {
        ManagedChannel channel = NettyChannelBuilder.forAddress(host, port)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();

        return PeerGrpc.newBlockingStub(channel);
    }
}
