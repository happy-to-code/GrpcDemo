package com.tj.service;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.ByteBuffer;

import static com.tj.util.UtilTool.*;

/**
 * @Author: zhangyifei
 * @Date: 2020/5/13 10:30
 * @Describe:
 */
public class ByteUtil {


    private ByteBuffer buffer;

    public ByteUtil() {
        this.buffer = ByteBuffer.allocate(1024 * 1024);
    }


    public void putInt(int i) {
        this.buffer.putInt(i);
    }

    public void putBytes(byte[] bytes) {
        this.buffer.putInt(bytes.length);
        this.buffer.put(bytes);
    }

    public byte[] marshal() {
        System.out.println("buffer" + buffer);
        // this.buffer.flip();
        byte[] b = new byte[buffer.remaining()];
        buffer.get(b);
        return b;
    }


    public static void main(String[] args) {
        // ByteUtil util = new ByteUtil();
        // util.putInt(1);
        // util.putInt(1);
        // //util.
        // util.putBytes("abc".getBytes());
        // byte[] bytes = util.marshal();
        // System.out.println("s = len:" + bytes.length);
        // String s = toHexString(bytes);
        // System.out.println("s = " + s);


        System.out.println("--------------------------------------");
        // 01000000  01000000  03000000  616263 00000000
        // 01000000  01000000  03000000  616263 00000000
        // 00000001  00000001  00000003  616263 00000000
        byte[] bytes = "abc".getBytes();
        // ByteBuffer byteBuffer = ByteBuffer.allocate(bytes.length + 4 * 4);
        // byteBuffer.putInt(1);
        // byteBuffer.putInt(1);
        //
        // byteBuffer.putInt(bytes.length);
        // byteBuffer.put(bytes);
        //
        // byteBuffer.putInt(0);
        //
        // byteBuffer.order(ByteOrder.BIG_ENDIAN);
        //
        // String s = toHexString(byteBuffer.array());
        // System.out.println("s = " + s);
        // System.out.println("---------------------------------");
        //
        // System.out.println(ByteOrder.nativeOrder());

        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes(int2Bytes(1));
        buf.writeBytes(int2Bytes(1));

        buf.writeBytes(int2Bytes(bytes.length));
        buf.writeBytes(bytes);

        buf.writeInt(0);

        byte[] bytes1 = new byte[buf.writerIndex()];

        byte[] array = buf.array();
        for (int i = 0; i < bytes1.length; i++) {
            bytes1[i] = array[i];
        }

        String ss = toHexString(bytes1);
        System.out.println("ss = " + ss);


        System.out.println("=================================");
        byte[] bytes2 = new byte[1];
        for (byte b : bytes2) {
            System.out.println(b);
        }

    }

}
