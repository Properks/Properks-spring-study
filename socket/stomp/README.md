# Spring Boot STOMP Project

이 프로젝트는 Spring Boot와 STOMP를 사용하여 실시간 데이터를 주고 받는 방법을 설명합니다. 두 클라이언트가 실시간 채팅을 할 수 있도록 데이터를 JSON 형식으로 주고받는 예제를 포함하며, `UserDestinationPrefix`를 사용하는 방법과 사용하지 않는 두 가지 방식을 테스트했습니다.

## 개요

이 프로젝트는 다음과 같은 내용을 다룹니다:

- Spring Boot와 STOMP를 사용한 실시간 통신 설정
- 두 가지 방식의 클라이언트-서버 통신
    - `UserDestinationPrefix`를 사용하는 방식
    - `UserDestinationPrefix`를 사용하지 않는 방식
- JSON 형식의 메시지 전송