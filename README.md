# CoupangEatsB-server-ashley
- 컴공선배 Rising Camp 4기 Server class 우수수료작
- 해당 프로젝트를 통해 클라이언트 파트와 협업하여 쿠팡이츠 앱을 클론하였습니다. 
- 이 Repository에서는 앱의 서버단에서 사용하는 REST API를 개발한 프로젝트를 확인하실 수 있습니다.

## 1. 기술 스택
<img src='https://user-images.githubusercontent.com/65891711/163364590-c3cd4160-067d-486a-bf7b-f09fecab0945.png' /><br>

1) Backend : Spring Boot/Java
2) Database : AWS RDS Database - MySQL 8.0.28 ver
3) Server
    1) Deploy : AWS EC2 Linux instance
        1) OS : Ubuntu 20.04
        2) Web Server : Nginx
    3) Domain : Gabia
4) Frontend : Android Studio/Java    


<br>

## 2. 프로젝트 ERD 명세서
<img src='https://user-images.githubusercontent.com/65891711/163364718-4990deab-1a63-4a1f-8580-79ae925e3945.png' /><br>
    
    1) URL : https://aquerytool.com/aquerymain/index/?rurl=5775b7c9-07c9-4242-9705-5f75f2d35d66&
    2) Password : cln7ve

<br>

## 3. 프로젝트 API 명세서
- 이 프로젝트에서는 쿠팡이츠 앱의 주요 REST API들을 구현하였습니다.
    - 유저, 식당, 주문 관련 API 40여개 구현
    - 심화 API 구현
        - 네이버 SENS 문자 API 연동
        - 구글 소셜 로그인 기능 구현
        - 각종 필터링 기능 (거리 계산, 신규 등록, 카테고리, 키워드) 구현
<br><br>
- API 명세서는 아래 링크에서 확인하실 수 있습니다.
    - https://docs.google.com/spreadsheets/d/1gM7hC7DOvJCmPYP7FMoCPJu5lA-3Crb4/edit?usp=sharing&ouid=117173717945581994159&rtpof=true&sd=true 


<br>

## 4. 프로젝트 시연 동영상

- 서버 파트 Postman 시연 동영상 
    - https://drive.google.com/file/d/1qosmAYCzsW76lKVUh7ykp0PMRNNmdS-u/view?usp=sharing
    
- 클라이언트 시연 동영상
    - https://drive.google.com/file/d/19KPyj2AFfV7JVOfx9CVcjjqvgk0Js0FF/view?usp=sharing
  

<br>

## 5. 프로젝트 협업 보드
- 프로젝트 기간 동안 클라이언트-서버 간의 협업과정을 담은 일지는 아래 노션 페이지에서 확인하실 수 있습니다.
    - https://certain-gallimimus-d89.notion.site/Rising-Camp-Coupang-Eats-B-4a83ab50a5f14d2598d14cc79f142336    

## 6. 개발일지 & 이슈사항
- 개발 과정을 담은 일지는 개발일지.md 페이지에서 확인하실 수 있습니다.
- 개발 중 발생한 이슈사항은 Github repo의 issues 탭에서 확인하실 수 있습니다.