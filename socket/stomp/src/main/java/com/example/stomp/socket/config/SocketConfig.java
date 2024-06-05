package com.example.stomp.socket.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class SocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue"); // 브로커에서 사용할 prefix 설정
        registry.setApplicationDestinationPrefixes("/app"); // 클라이언트가 메시지를 보낼 prefix 설정
        registry.setUserDestinationPrefix("/user"); // 유저에게 보낼 때 사용할 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/socket") // socket 연결 시 사용할 endpoint
                .setAllowedOrigins("/*") // CORS처럼 허용할 URL 설정
                .withSockJS(); // SockJs도 지원하도록
    }

}
