# 개인 재무 관리 서비스

이 프로젝트는 사용자가 자신의 수입, 지출, 저축을 효율적으로 관리할 수 있도록 돕는 종합적인 개인 재무 관리 서비스 입니다. 사용자는 이 서비스를 통해 지출을 추적하고, 예산을 설정하며, 지출 패턴을 분석하고, 저축 목표를 달성할 수 있습니다.

## 사용 기술
- **Java**
- **Spring Boot**
- **JPA**
- **MySQL**
- **QueryDsl**
- **Redis**
- **SSE**
- **Git**



## 기능

### 1. 회원 가입 및 로그인
  - 회원 가입시 아이디, 비밀번호, 비밀번호 확인, 이름, 전화번호 입력.
  - 아이디 중복 체크
  - 비밀번호 암호화 처리

### 2. 수입 및 지출 관리 (가계부) 
  - 월별 수입과 지출 내역을 금액, 날짜, 설명과 함께 입력. 
  - 반복적인(고정 지출) 항목은 스케줄러를 통해 자동 등록.
    - Transactions table type 컬럼에 enum 형식으로 (FIXED_EXPENSES ,FIXED_INCOME, EXPENSES ,INCOME)으로 저장
  - 수입/지출 항목을 다양한 카테고리로 구분하여 관리. 
  - 사용자가 직접 카테고리를 생성하여 맞춤형 가계부를 구성.
  - 입력된 수입/지출 데이터를 수정하거나 삭제.

### 3. 예산 설정 및 관리
  - 사용자는 각 카테고리별로 월별 예산을 설정하여 지출을 관리.
  - 설정된 예산을 초과할 경우 경고 알림이 제공.
    - 경고 알림은 SSE를 활용하여 알림을 제공.
    - 받았던 경고 알림은 따로 조회가 가능.
  - 현재 지출이 설정된 예산 범위 내에 있는지 실시간으로 확인. 
  - 사용자는 필요에 따라 예산을 수정 및 조정.

### 4. 지출 분석 및 경고 제공
  - 사용자의 지출 데이터를 분석하여, 특정 카테고리에서 지출 비율 및 패턴을 파악할 수 있습니다.
  - 사용자의 일반적인 지출 패턴과 비교하여 비정상적인 지출을 감지하고 경고를 제공.
    - 기준 : 이번달 지출 금액이 최근 3개월 지출의 평균 금액 150% 이상
  - 특정 카테고리에서 비정상적으로 높은 지출이 있을 경우 경고 알림이 제공.
    - 기준 : 이번달 지출 금액이 카테고리별 설정한 예산보다 100% 이상
  - 비정상적으로 높은 지출이 발생한 카테고리에 대해 절약 팁을 제공.  
    - "재무 관리 원칙"을 기준으로 수입의 30% 이상일 경우 각 카테고리 항목 중 지출이 많은 카테고리의 지출에 대해 간단한 메시지를 제공.
    - 절약 팁 메시지는 고정된 문구에 카테고리명을 삽입하여 전달. 카테고리명은 카테고리 테이블에서 가져와 사용할 예정.

### 5. 저축 목표 설정 및 달성 계획
  - 사용자는 특정 기간 내에 달성하고자 하는 저축 목표를 설정.
  - 사용자의 소비 패턴에 따라 저축을 위한 계획을 추천.
    - "재무 관리 원칙"을 기준으로 수입의 20% 저축하도록 추천
    - 예시) 지난달 대비 “배달비”가 30% 증가했다면, 이를 줄이도록 제안.

### 6. 사용 내역 조회
  - 사용자가 입력한 데이터를 바탕으로 월별 사용 내역을 조회.
  - 사용자가 입력한 데이터를 바탕으로 연간 사용 내역을 조회.

## ERD
![image](https://github.com/user-attachments/assets/5b98b090-c4fc-4c9c-94fc-9d35a879d654)







