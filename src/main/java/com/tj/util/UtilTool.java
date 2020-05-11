package com.tj.util;

import com.google.protobuf.ByteString;
import com.tj.proto.PeerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
            result[j] = hexToByte(inHex.substring(i, i + 2), 16);
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

    public static byte[] int2Bytes(int value, int len) {
        byte[] b = new byte[len];
        for (int i = 0; i < len; i++) {
            b[len - i - 1] = (byte) ((value >> 8 * i));
        }
        return b;
    }

    public static byte[] int2Bytes1(int integer) {
        byte[] bytes = new byte[4];
        bytes[3] = (byte) (integer >> 24);
        bytes[2] = (byte) (integer >> 16);
        bytes[1] = (byte) (integer >> 8);
        bytes[0] = (byte) integer;

        return bytes;
    }

    public static int bytes2Int2(byte[] bytes) {
        int int1 = bytes[0] & 0xff;
        int int2 = (bytes[1] & 0xff) << 8;
        int int3 = (bytes[2] & 0xff) << 16;
        int int4 = (bytes[3] & 0xff) << 24;

        return int1 | int2 | int3 | int4;
    }

    /**
     * 利用Apache的工具类实现SHA-256加密
     *
     * @param str 加密后的报文
     * @return
     */
    public static String getSHA256Str(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes("UTF-8"));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encdeStr;
    }

    public static void main(String[] args) {
        String sha256Str = getSHA256Str("123");
        System.out.println("sha256Str = " + sha256Str);
        byte[] bytes = int2Bytes(200, 4);
        for (byte aByte : bytes) {
            System.out.print(aByte + " ");
        }
        System.out.println("----------");
        byte[] bytes1 = int2Bytes1(200);
        for (byte b : bytes1) {
            System.out.print(b + " ");
        }
//        String s = "2c7f6f353d828e99692bb8bf960186f218674581495b399db753c00dd636c4f0583f7a833ce67d352e7d32be5d6e3fc899d7004efe1f450fc1a078ee856a8b75";
//        byte[] bytes = hexToByteArray(s, 16);
//        for (byte aByte : bytes) {
//
//            System.out.println("bytes = " + aByte);
//        }
    }
}
