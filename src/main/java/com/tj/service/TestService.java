package com.tj.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.Map;

@Service
public class TestService {

    @Resource
    private RestTemplate restTemplate;

    public Map getHeight() {
//        String url = "http://10.1.3.150:9100/getheight";
//        String url = "http://10.1.3.150:9008/peer.Peer/BlockchainGetHeight";
        String url = "http://10.1.3.150:9008/peer/Peer/BlockchainGetHeight";
        Map forObject = restTemplate.getForObject(url, Map.class);
        System.out.println("forObject = " + forObject);
        return forObject;
    }
}
