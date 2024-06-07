package com.example.stomp.socket.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketService {

    /**
     * Session에 들어있는 유저들
     */
    private static Set<String> sessions = new HashSet<>();

    /**
     * 세션 ID
     */
    private UUID sessionId = UUID.randomUUID();

    private final SimpMessagingTemplate messagingTemplate; // 메시지를 보내기 위한 것

    /**
     * 같은 세션에 있는 본인을 제외한 모든 팀에게 전달하기
     * @param message 유저 정보 + 보낼 메시지
     */
    public void sendMessage(Map<String, Object> message) {
        String sender = message.get("sender").toString();
        String content = message.get("content").toString();
        for (String username: sessions) {
            if (!username.equals(sender)) {

                Map<String, Object> result = createResult(sender, content);

                messagingTemplate.convertAndSendToUser(username, "/queue/messages", result);
                log.info(username + "에게 " + message + "를 보냄");
            }
        }
    }

    /**
     * 모든 사람에게 보내기
     * @param message 모든 사람에게 보낼 메시지
     */
    public void notice(String destination, String sender, Map<String, Object> message) {
        // 받은 데이터 가져오기
        boolean important = Boolean.parseBoolean(message.get("important").toString());
        String content = message.get("content").toString();
        Date date = Date.from(Instant.parse(message.get("date").toString()));

        // log 찍기, 받은 데이터로 로직 처리
        log.info("내용: " + content);
        log.info("중요: " + important);
        log.info("시간: " + date);

        Map<String, Object> result = createResult(sender, content);

        messagingTemplate.convertAndSend(destination, result);
    }

    /**
     * 세션에 본인 등록하기
     * @param username 본인의 username
     */
    public void joinSession(String username) {
        if (sessions.contains(username)) {
            throw new IllegalArgumentException("Session already exists");
        }
        WebSocketService.sessions.add(username);
        messagingTemplate.convertAndSend("/queue/register", sessionId);
    }

    /**
     * 보낼 응답 생성
     * @param username 보낸 사람 이름
     * @param content 보낼 메시지
     * @return JSON 형식으로 보낼 수 있도록 Map 형태로
     */
    private Map<String, Object> createResult(String username, String content) {
        Map<String, Object> result = new HashMap<>();
        result.put("sender", username);
        result.put("content", content);
        return result;
    }
}
