package com.tj.service;

import com.tj.proto.PeerGrpc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.tj.util.UtilTool.getBlockingStubByIpAndPort;

@Service
public class CommonPeerService {

    @Value("${peer.ip}")
    private String ip;

    @Value("${peer.port}")
    private int port;


    /**
     * 根据ip和port获取PeerBlockingStub
     *
     * @return
     */
    public PeerGrpc.PeerBlockingStub getStubByIpAndPort() {
        return getBlockingStubByIpAndPort(ip, port);
    }
}
