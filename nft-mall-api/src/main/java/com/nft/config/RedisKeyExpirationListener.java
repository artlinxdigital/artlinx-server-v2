package com.nft.config;

import com.nft.mall.service.ICollectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

/**
 * @Author: szw
 * @Date: 2020/8/20 15:54
 */
@Component
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private ICollectionService collectionService;
    public static final String EX_PAY = "TOPAY_";

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 获取过期的key
        String expiredKey = message.toString();
        log.info("监听redis的过期：" + expiredKey);
        // 判断是否是想要监听的过期key
        if (expiredKey.contains(EX_PAY)) {
            log.info("redis key过期：{}", expiredKey);
            // TODO 业务逻辑
            String orderIdStr = expiredKey.replace(EX_PAY, "");
            log.info(orderIdStr);
            collectionService.cancelPay(Long.parseLong(orderIdStr));
        }
    }

}
