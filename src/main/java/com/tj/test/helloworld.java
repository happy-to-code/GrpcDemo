package com.tj.test;

import com.googlecode.jsonrpc4j.JsonRpcClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class helloworld {

    public static void main(String[] args) {

        try {
//            Socket socket = new Socket("10.1.3.150", 3333);
            Socket socket = new Socket("10.1.3.150", 9008);
            JsonRpcClient client = new JsonRpcClient();

            InputStream ips = socket.getInputStream();
            OutputStream ops = socket.getOutputStream();

//            int reply = client.invokeAndReadResponse("Counter.Add", new Object[]{1998}, int.class, ops, ips);
//            Object reply = client.invokeAndReadResponse("peerClient.BlockchainGetHeight", peerRequest,  Object.class, ops, ips);
//            Object reply = client.invokeAndReadResponse("peerClient.BlockchainGetHeight", peerRequest, Object.class, ops, ips);
            Object reply = client.invokeAndReadResponse("/peer.Peer/BlockchainGetHeight", null, Object.class, ops, ips);

            System.out.println("reply: " + reply);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }
}