package com.example.stomp.socket.controller;

import com.example.stomp.socket.service.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

import java.security.Principal;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class WebSocketController {

    private final WebSocketService webSocketService;

    /**
     * 같은 세션에 있는 본인을 제외한 모든 팀에게 전달하기
     * @param message 클라이언트에서 받은 JSON 형식의 메시지
     */
    @MessageMapping("/messages")
    public void sendToTeam(Map<String, Object> message) {
        webSocketService.sendMessage(message);
    }

    /**
     * Stomp에 연결되고 "/queue/notification"에 구독한 모든 사람들에게 전송
     * @param message 클라이언트에서 받은 JSON 형식의 메시지
     */
    @MessageMapping("/notification")
    public void notice(Principal principal, Map<String, Object> message) {
        webSocketService.notice("/queue/notification", principal.getName(), message);
    }

    /**
     * 세션에 등록하기
     * @param principal 등록하기 위한 유저 정보
     */
    @MessageMapping("register")
    public void register(Principal principal) {
        webSocketService.joinSession(principal.getName());
    }
}
