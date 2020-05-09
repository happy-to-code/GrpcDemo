package com.tj.util;

import com.google.protobuf.ByteString;
import com.tj.proto.PeerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

public class UtilTool {

    public static void main(String[] args) {
        String s = "2c7f6f353d828e99692bb8bf960186f218674581495b399db753c00dd636c4f0583f7a833ce67d352e7d32be5d6e3fc899d7004efe1f450fc1a078ee856a8b75";
        byte[] bytes = hexToByteArray(s, 16);
        for (byte aByte : bytes) {

            System.out.println("bytes = " + aByte);
        }
    }

    /**
     * hex字符串转byte数组
     *
     * @param inHex 待转换的Hex字符串
     * @return 转换后的byte数组结果
     */
    public static byte[] hexToByteArray(String inHex, int radix) {
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
            result[j] = hexToByte(inHex.substring(i, i + 2), radix);
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
    public static byte hexToByte(String inHex, int radix) {
        return (byte) Integer.parseInt(inHex, radix);
    }

    /**
     * 将10进制转换成16进制
     *
     * @param byt
     * @return
     */
    public static String covent10To16Str(byte[] byt) {
        StringBuilder sb = new StringBuilder();
        if (byt.length > 0) {
            for (byte b : byt) {
                String s = Integer.toHexString(b);
                sb.append(s);
            }
        }
        return sb.toString();
    }

    /**
     * 将pubKey转换成ByteString
     *
     * @param str
     * @return
     */
    public static ByteString convertPubKeyToByteString(String str, int radix) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        byte[] bytes = hexToByteArray(str, radix);
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

    public static String bytesToString(ByteString src, String charSet) {
        if (StringUtils.isEmpty(charSet)) {
            charSet = "GB2312";
        }
        return bytesToString(src.toByteArray(), charSet);
    }

    public static String bytesToString(byte[] input, String charSet) {
        if (ArrayUtils.isEmpty(input)) {
            return StringUtils.EMPTY;
        }

        ByteBuffer buffer = ByteBuffer.allocate(input.length);
        buffer.put(input);
        buffer.flip();

        Charset charset;
        CharsetDecoder decoder;
        CharBuffer charBuffer;

        try {
            charset = Charset.forName(charSet);
            decoder = charset.newDecoder();
            charBuffer = decoder.decode(buffer.asReadOnlyBuffer());

            return charBuffer.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
