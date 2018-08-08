package com.mycarx.wxweb.web.service;

import com.mycarx.utils.LogUtils;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@ServerEndpoint(value = "/webSocketServer/{dataId}")
public class WebSocketServer {
    private static Map<String, Session> sessionMap = new ConcurrentHashMap<String, Session>();

    @OnOpen
    public void onOpen(@PathParam(value = "dataId") String dataId, Session session) {
        sessionMap.put(dataId, session);
        LogUtils.getLogger().info(String.format("WebSocket接入：%s", dataId));
    }

    @OnClose
    public void onClose(@PathParam(value = "dataId") String dataId) {
        if (sessionMap.containsKey(dataId)) {
            sessionMap.remove(dataId);
        }
        LogUtils.getLogger().info(String.format("WebSocket退出：%s", dataId));
    }

    @OnError
    public void onError(Session session, Throwable error) {
        error.printStackTrace();
    }

    @OnMessage
    public void onMessage(@PathParam(value = "dataId") String dataId, String message) {
        LogUtils.getLogger().info(String.format("WebSocket上行消息：%s ====> %s", dataId, message));
    }

    public static void pushMessage(String dataId, PushMessage message) {
        if (null != message) {
            if (sessionMap.containsKey(dataId)) {
                Session session = sessionMap.get(dataId);
                if (null != session) {
                    try {
                        session.getBasicRemote().sendText(message.toString());
                        LogUtils.getLogger().info(String.format("WebSocket推送消息成功：%s ====> %s", dataId, message.toString()));
                    } catch (Exception ex) {
                        LogUtils.getLogger().info(String.format("WebSocket推送消息失败：%s ====> %s", dataId, message.toString()));
                    }
                }
            }
        }
    }
}
