package com.tj.contorller;

import com.alibaba.fastjson.JSONObject;
import com.tj.service.PeerService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/peer/")
public class PeerController {

    @Resource
    private PeerService peerService;

    /**
     * 获取区块链高度
     *
     * @return
     */
    @RequestMapping("getHeight")
    public JSONObject getHeight() {
        JSONObject height = peerService.getHeight();
        return peerService.getHeight();
    }
}
