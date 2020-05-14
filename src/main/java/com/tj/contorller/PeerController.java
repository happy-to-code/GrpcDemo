package com.tj.contorller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.tj.common.StatusCode;
import com.tj.common.response.RestResponse;
import com.tj.domain.Block;
import com.tj.service.PeerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

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
    public RestResponse getHeight() {
        long height = peerService.getHeight();
        return RestResponse.success().setData(height);
    }

    /**
     * 根据高度查询详情
     *
     * @return
     */
    @RequestMapping("getBlockByHeight/{height}")
    public RestResponse getBlockByHeight(@PathVariable(value = "height") Integer height) throws InvalidProtocolBufferException, UnsupportedEncodingException {
        Block block = peerService.getBlockByHeight(height);
        return RestResponse.success().setData(block);
    }


    /**
     * 新增交易
     *
     * @return
     */
    @RequestMapping("newTransaction")
    public RestResponse newTransaction() {
        Boolean isOk = peerService.newTransaction();
        if (isOk) {
            return RestResponse.success();
        }
        return RestResponse.failure("创建交易失败！", StatusCode.SERVER_520005.value());
    }
}
