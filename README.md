# 2026 CMS API 과제

## 문서 안내
- 프로젝트 설명 및 실행 방법: `README.md`
- REST API 상세 문서: [API.md](./API.md)

## 1. 프로젝트 소개

Spring Boot 기반의 간단한 CMS REST API입니다.  
콘텐츠 등록, 조회, 수정, 삭제 기능을 제공하며, Spring Security를 이용한 로그인과 권한 제어를 적용했습니다.

이번 과제에서는 단순 CRUD 구현뿐 아니라, 다음 요구사항을 함께 반영했습니다.

- 콘텐츠 목록 조회 시 페이징 처리
- 로그인 기능 구현
- 사용자 권한 분리 (`ADMIN`, `USER`)
- 작성자 본인만 수정/삭제 가능
- 관리자(ADMIN)는 모든 콘텐츠 수정/삭제 가능
- 공통 응답 형식 및 전역 예외 처리 적용

---

## 2. 프로젝트 실행 방법

### 2-1. 개발 환경

- Java 25
- Spring Boot 4.0.3
- Gradle
- H2 Database

### 2-2. 애플리케이션 실행

#### Windows
```bash
gradlew.bat bootRun
```

#### Mac / Linux
```bash
./gradlew bootRun
```

### 2-3. 테스트 실행

#### Windows
```bash
gradlew.bat clean test
```

#### Mac / Linux
```bash
./gradlew clean test
```

> 테스트는 Gradle 기준으로 검증했습니다.

### 2-4. H2 Console 접속

- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:test`
- Username: `sa`
- Password: 없음

---

## 3. 구현 내용

### 3-1. 로그인 기능

본 과제에서는 **Spring Security 기반의 HTTP Basic 인증 방식**을 사용했습니다.

- 로그인 확인용 API: `POST /api/auth/login`
- 콘텐츠 API 요청 시에는 HTTP Basic 인증 정보를 함께 전달해야 합니다.
- 테스트 및 검증은 기본 계정(`admin`, `user1`)을 이용해 수행했습니다.
- 기본 사용자 권한은 `ADMIN`, `USER` 두 가지로 구성했습니다.

### 3-2. 콘텐츠 CRUD

다음 콘텐츠 기능을 구현했습니다.

- 콘텐츠 등록
- 콘텐츠 목록 조회
- 콘텐츠 상세 조회
- 콘텐츠 수정
- 콘텐츠 삭제

### 3-3. 콘텐츠 목록 조회 페이징

- 목록 조회 API에 페이징을 적용했습니다.
- 요청 예시:
  - `GET /api/contents?page=0&size=10`
- 응답에는 목록 데이터와 함께 페이지 정보(`page`, `size`, `totalElements`, `totalPages`, `first`, `last`)를 포함했습니다.

### 3-4. 콘텐츠 상세 조회

- 콘텐츠 상세 조회 시 조회수가 증가하도록 구현했습니다.

### 3-5. 권한 제어

- 로그인하지 않은 사용자는 콘텐츠 API에 접근할 수 없도록 처리했습니다.
- 일반 사용자(`USER`)는 자신이 작성한 콘텐츠만 수정/삭제할 수 있습니다.
- 관리자(`ADMIN`)는 모든 콘텐츠를 수정/삭제할 수 있습니다.

### 3-6. 작성자 정보 자동 처리

- `createdBy`, `lastModifiedBy`를 요청값으로 받지 않고,
  로그인한 사용자 정보를 기준으로 서버에서 자동 저장하도록 구현했습니다.
- 이를 통해 작성자 정보 위변조 가능성을 줄였습니다.

### 3-7. 공통 응답 및 예외 처리

- 모든 API 응답은 공통 응답 형식(`success`, `message`, `data`)을 사용하도록 통일했습니다.
- 전역 예외 처리(`GlobalExceptionHandler`)를 적용했습니다.

처리 대상 예외:
- `400 Bad Request` : 입력값 검증 실패
- `401 Unauthorized` : 인증 실패 또는 미인증 사용자 요청
- `403 Forbidden` : 권한 없음
- `404 Not Found` : 존재하지 않는 콘텐츠
- `500 Internal Server Error` : 서버 내부 오류

---

## 4. 추가 구현 기능

필수 요구사항 외에 아래 항목도 함께 반영했습니다.

- 콘텐츠 생성/수정 시 사용자 정보 자동 반영
- 관리자/일반 사용자 권한 분리
- 통합 테스트 작성
- H2 초기 스키마 및 데이터 자동 세팅
- 공통 응답 구조 적용
- 전역 예외 처리 적용

---

## 5. 데이터 모델

### 5-1. Contents

| 컬럼명 | 설명 | 타입 |
|---|---|---|
| id | 콘텐츠 ID | BIGINT |
| title | 콘텐츠 제목 | VARCHAR(100) |
| description | 콘텐츠 내용 | TEXT |
| view_count | 조회수 | BIGINT |
| created_date | 생성일 | TIMESTAMP |
| created_by | 생성자 | VARCHAR(50) |
| last_modified_date | 수정일 | TIMESTAMP |
| last_modified_by | 수정자 | VARCHAR(50) |

### 5-2. Users

| 컬럼명 | 설명 | 타입 |
|---|---|---|
| id | 사용자 ID | BIGINT |
| username | 로그인 아이디 | VARCHAR(50) |
| password | 비밀번호 | VARCHAR(255) |
| role | 권한 | VARCHAR(20) |

---

## 6. 기본 계정

애플리케이션 실행 시 H2 초기 데이터가 함께 적재됩니다.

### 관리자 계정
- username: `admin`
- password: `admin1234`
- role: `ADMIN`

### 일반 사용자 계정
- username: `user1`
- password: `user1234`
- role: `USER`

---

## 7. 주요 API 예시

### 7-1. 로그인

#### Request
```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "admin1234"
}
```

#### Response
```json
{
  "success": true,
  "message": "로그인 성공",
  "data": {
    "id": 1,
    "username": "admin",
    "role": "ADMIN",
    "message": "로그인 성공"
  }
}
```

### 7-2. 콘텐츠 등록

#### Request
```http
POST /api/contents
Content-Type: application/json
Authorization: Basic ...
```

```json
{
  "title": "새 콘텐츠",
  "description": "설명입니다."
}
```

### 7-3. 콘텐츠 목록 조회

#### Request
```http
GET /api/contents?page=0&size=10
Authorization: Basic ...
```

### 7-4. 콘텐츠 수정

#### Request
```http
PUT /api/contents/{id}
Content-Type: application/json
Authorization: Basic ...
```

```json
{
  "title": "수정된 제목",
  "description": "수정된 설명"
}
```

### 7-5. 콘텐츠 삭제

#### Request
```http
DELETE /api/contents/{id}
Authorization: Basic ...
```

---

## 8. 테스트

통합 테스트를 통해 아래 항목을 검증했습니다.

- 로그인 성공
- 인증 없는 접근 시 401 응답
- 콘텐츠 등록 성공
- 콘텐츠 목록 페이징 조회 성공
- 콘텐츠 상세 조회 성공
- 작성자 본인 수정 성공
- 관리자 수정 성공
- 관리자 삭제 성공
- 일반 사용자의 타인 콘텐츠 수정 시 403 응답

실행 명령어:

#### Windows
```bash
gradlew.bat clean test
```

#### Mac / Linux
```bash
./gradlew clean test
```

---

## 9. 사용한 AI 도구 또는 참고 자료

### AI 도구
- ChatGPT
  - 요구사항 해석 정리
  - 구현 방향 점검
  - README 초안 정리
  - 테스트 코드 및 설정 점검

### 참고 자료
- Spring Boot 공식 문서
- Spring Security 공식 문서
- H2 Database 관련 공식 문서 및 설정 참고 자료

---

## 10. 구현하면서 중점적으로 본 부분

이번 과제에서는 아래 사항을 특히 중요하게 생각했습니다.

1. 단순 CRUD 구현에서 끝나지 않고 인증/인가까지 포함할 것
2. 작성자 정보는 클라이언트가 아니라 서버가 직접 관리할 것
3. 목록 조회는 반드시 페이징 처리할 것
4. 권한 정책을 명확히 분리할 것
5. 예외 상황에서도 일관된 응답 형식을 유지할 것

---

## 11. 마무리

본 프로젝트는 아래 요구사항을 충족하도록 구현했습니다.

- 콘텐츠 CRUD
- 콘텐츠 목록 조회 페이징 처리
- Spring Security 로그인 기능
- 사용자 권한 분리 (`ADMIN`, `USER`)
- 작성자 본인만 수정/삭제 가능
- 관리자는 전체 수정/삭제 가능
- 공통 응답 및 전역 예외 처리
- 통합 테스트 작성 및 검증 완료
