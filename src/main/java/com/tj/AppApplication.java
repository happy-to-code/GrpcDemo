package com.tj;

import com.tj.proto.PeerGrpc;
import io.grpc.ManagedChannel;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AppApplication {
    public static void main(String[] args) {
        SpringApplication.run(AppApplication.class, args);
    }

    @Value("${peer.ip}")
    private String ip;

    @Value("${peer.port}")
    private int port;

    @Bean
    public PeerGrpc.PeerBlockingStub getBlockingStubByIpAndPort() {
        ManagedChannel channel = NettyChannelBuilder.forAddress(ip, port)
                .negotiationType(NegotiationType.PLAINTEXT)
                .build();

        return PeerGrpc.newBlockingStub(channel);
    }
}
