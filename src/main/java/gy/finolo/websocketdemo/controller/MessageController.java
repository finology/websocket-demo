package gy.finolo.websocketdemo.controller;

import gy.finolo.websocketdemo.websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * @description:
 * @author: Simon
 * @date: 2020-05-22 14:10
 */
@RestController
public class MessageController {

    @Autowired
    private WebSocket webSocket;

    @GetMapping("send")
    public String send(String message) throws IOException {
        webSocket.broadcast(message);
        return "message sent successfully";
    }
}
