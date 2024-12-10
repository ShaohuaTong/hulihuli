package com.hulihuli.service.websocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@ServerEndpoint("/imserver")
public class WebSocketService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final AtomicInteger ONLINE_COUNT = new AtomicInteger(0);

    // 一个客户端用一个新的WebsocketService 不再是单例模式 变成了 多例模式
    private static final ConcurrentHashMap<String, WebSocketService> WEB_SOCKET_MAP = new ConcurrentHashMap<>();

    private Session session;

    private String sessionId;

    private static ApplicationContext APPLICATION_CONTEXT;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WebSocketService.APPLICATION_CONTEXT = applicationContext;
    }

    @OnOpen
    public void openConnection(Session session) {
        this.sessionId = session.getId();
        this.session = session;
        if (WEB_SOCKET_MAP.containsKey(sessionId)) {
            WEB_SOCKET_MAP.remove(sessionId);
            WEB_SOCKET_MAP.put(sessionId, this);
        } else {
            WEB_SOCKET_MAP.put(sessionId, this);
            ONLINE_COUNT.getAndIncrement();
        }
        logger.info("用户连接成功: {}, 当前在线人数为: {}", sessionId, ONLINE_COUNT.get());
        try {
            this.sendMessage("0");
        } catch (Exception e) {
            logger.error("连接异常！");
        }
    }

    @OnClose
    public void closeConnection() {
        if (WEB_SOCKET_MAP.containsKey(sessionId)) {
            WEB_SOCKET_MAP.remove(sessionId);
            ONLINE_COUNT.getAndDecrement();
        }
        logger.info("用户退出: {}, 当前在线人数: {}", sessionId, ONLINE_COUNT.get());
    }

    @OnMessage
    public void onMessage(String message) {

    }

    @OnError
    public void onError(Throwable t) {

    }

    public void sendMessage(String message) throws Exception {
        this.session.getBasicRemote().sendText(message);
    }
}
