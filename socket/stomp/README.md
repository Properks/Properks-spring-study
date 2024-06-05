# Spring Boot STOMP Project

이 프로젝트는 Spring Boot와 STOMP를 사용하여 실시간 데이터를 주고 받는 방법을 테스트합니다.

두 클라이언트가 실시간 채팅을 할 수 있도록 데이터를 JSON 형식으로 주고받는 예제를 포함하고 `UserDestinationPrefix`를 사용하는 방법과 사용하지 않는 두 가지 방식을 테스트했습니다.

## 내용

이 프로젝트는 아래의 내용을 테스트했습니다:

- Spring Boot와 STOMP를 사용한 실시간 통신 설정
- 두 가지 방식의 클라이언트-서버 통신
    - `UserDestinationPrefix`를 사용하는 방식
      - 특정 유저에게만 보내기
    - `UserDestinationPrefix`를 사용하지 않는 방식
      - 전체에게 보내기
- JSON 형식의 메시지 송신 및 수신

## 결과


<details>
<summary>GIF</summary>

![result](https://github.com/Properks/Start_Spring-boot/assets/76582572/9f9f35d0-f9b6-44e9-bfd9-ec0507671328)
</details>
