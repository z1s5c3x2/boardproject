# 회원제 게시판 개인 프로젝트(수정중)

## 목표
- 학습 내용과 새로 학습한 기술들을 적용하며 기능을 축소하고 완성도에 초점을 맞춘 기능 구현

## 기록
- [프로젝트 기록](https://velog.io/@z1s5c3x2/series/Spring-Boot-%EA%B2%8C%EC%8B%9C%ED%8C%90-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8)

## 환경
- 언어: Java 17
- 배포: Docker -> AWS EC2 , RDS 추가 예정
- 백엔드: Spring Boot 3.2.2
- 프론트: Next.js 14.1.0 (예정)
- 
## 기능
### 계정 관련
- JWT + Spring Security 6.2.2
- Redis (Refresh Token (RTR) )

### 게시판
- MySQL + JPA

## 기능 추가
- TDD: JUnit 5 + MockMvc
- API 응답 데이터 정규화: Response Advice
- 유효성 검사: Validation
