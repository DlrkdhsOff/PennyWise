# 개인 재무 관리 서비스

이 프로젝트는 사용자가 자신의 수입, 지출, 저축을 효율적으로 관리할 수 있도록 돕는 종합적인 개인 재무 관리 서비스 입니다. 사용자는 이 서비스를 통해 지출을 추적하고, 예산을 설정하며, 지출 패턴을 분석하고, 저축 목표를 달성할 수 있습니다.

## 사용 기술
- **Java**
- **Spring Boot**
- **Spring Security**
- **Spring Batch**
- **JPA**
- **MySQL**
- **QueryDsl**
- **Redis**
- **SSE**
- **Git**



## 기능

### 1. 회원 가입 및 로그인
  - 회원 가입
    - 아이디, 비밀번호, 비밀번호 확인, 이름, 전화번호 입력. 
  - 회원 정보 수정
    - 비밀번호, 전화번호, 이름
   
### 2. 카테고리 관리
  - 카테고리 조회
  - 카테고리 생성
  - 카테고리 수정
  - 카테고리 삭제

### 3. 수입 및 지출 관리 (가계부) 
  - 수입/지출 정보 조회
    - 전체 수입/지출 정보 조회
    - 카테고리별 수입/지출 정보 조회
  - 수입/지출 정보 등록
    - 카테고리, 수입/지출 금액, 설명, 고정 수입/지출 설정
    - 고정적인(수입 지출) enum 형식으로(FIXED_EXPENSES ,FIXED_INCOME, EXPENSES ,INCOME)으로 저장
    - 고정적인(수입 지출) 항목은 스케줄러를 통해 자동 등록
  - 수입/지출 정보 수정
    - 카테고리, 수입/지출, 금액, 설명 수정
  - 수입/지출 정보 삭제

### 4. 예산 설정 및 관리
  - 카테고리별로 월별 예산을 설정
    - 예산 및 남은 금액은 Redis Cache애 저장
    - 설정된 예산을 초과할 경우 경고 알림 ->  Redis pub/sub + SSE 사용
  - 카테고리별 예산 및 남은 금액 조회
  - 카테고리별 예산 수정
    - 카테고리명, 금액 
  - 카테고리별 예산 삭제

### 5. 지출 분석 및 경고 제공
  - 사용자의 지출 데이터를 분석
    - 최근 3개월 총 지출의 평균 및 카테고리별 평균 금액
    - 이번달 총 지출 및 카테고리별 평균 금액
  - 높은 지출 발생시 경고 알림
    - 이번달 지출 금액이 최근 3개월 지출의 평균 금액 150% 이상일 경우 높은 지출이 발생한 카테고리 분석 후 경고 알림

### 6. 저축 목표 설정 및 달성 계획
  - 저축 목표 설정
    - 목표 금액, 매달 저축 금액, 저축 시작일, 만기일 설정
    - 저축 목표 설정시 스케줄러를 통해 매달 자동 등록
  - 저축 정보 조회
    - 목표 금액, 총 저축 금액, 저축 시작일, 만기일 조회
  - 저축 정보 삭제
  - 저축 계획 추천
    - "재무 관리 원칙" 수입의 20% 저축하도록 추천
      - 평균 지출 금액에서 높은 비중을 차지한 카테고리 절약 팁 제공
     
### 7. 경고 알림 조회
  - 경고 조회


## ERD
![image](https://github.com/user-attachments/assets/d5e864cd-f02b-48d6-8731-025e5ad868cf)









