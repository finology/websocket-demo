package gy.finolo.websocketdemo.websocket;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @description:
 * @author: Simon
 * @date: 2020-05-22 11:49
 */
@ServerEndpoint("/webSocket")
@Component
public class WebSocket {

    private static int onlineCount = 0;

    private static CopyOnWriteArraySet<WebSocket> webSockets = new CopyOnWriteArraySet<>();

    private Session session;

    /**
     * socket建立成功回调
     */
    @OnOpen
    public void onOpnen(Session session) throws IOException {
        this.session = session;
        webSockets.add(this);
        synchronized (WebSocket.class) {
            WebSocket.onlineCount++;
        }
        System.out.println("a new connection added. online count: " + onlineCount);
        sendMessage("connection successful");
    }

    @OnClose
    public void onClose() {
        webSockets.remove(this);
        synchronized (WebSocket.class) {
            WebSocket.onlineCount--;
        }
        System.out.println("a connection closed. online count: " + onlineCount);
    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {
        System.out.println("message received from client: " + message);

        // broadcast
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println("error occurs...");
        throwable.printStackTrace();
    }

    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
    }

    public void broadcast(String message) throws IOException {
        for (WebSocket member : webSockets) {
            member.sendMessage(message);
        }
    }

}
