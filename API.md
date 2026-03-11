# REST API 문서

## 1. 개요

본 문서는 CMS API의 엔드포인트, 요청/응답 형식, 인증 방식, 예외 응답을 정리한 문서입니다.

- Base URL: `http://localhost:8080`
- 인증 방식: Spring Security 기반 인증
- 콘텐츠 API: 인증 필요
- 응답 형식: 공통 응답 구조 사용

---

## 2. 공통 응답 형식

### 성공 응답

```json
{
  "success": true,
  "message": "처리 결과 메시지",
  "data": {}
}
````

### 실패 응답

```json
{
  "success": false,
  "message": "에러 메시지",
  "data": null
}
```

---

## 3. 인증 방식

## 3-1. 로그인 API

* 로그인 확인용 API
* `POST /api/auth/login`

## 3-2. 콘텐츠 API 인증

콘텐츠 API는 인증이 필요합니다.

예시:

```http
Authorization: Basic base64(username:password)
```

테스트 계정:

* 관리자: `admin / admin1234`
* 일반 사용자: `user1 / user1234`

---

## 4. API 목록

| 기능        | Method | URL                            | 인증 필요 |
| --------- | ------ | ------------------------------ | ----- |
| 로그인       | POST   | `/api/auth/login`              | N     |
| 콘텐츠 등록    | POST   | `/api/contents`                | Y     |
| 콘텐츠 목록 조회 | GET    | `/api/contents?page=0&size=10` | Y     |
| 콘텐츠 상세 조회 | GET    | `/api/contents/{id}`           | Y     |
| 콘텐츠 수정    | PUT    | `/api/contents/{id}`           | Y     |
| 콘텐츠 삭제    | DELETE | `/api/contents/{id}`           | Y     |

---

## 5. 로그인

### 5-1. 요청

**Method**

```http
POST /api/auth/login
```

**Headers**

```http
Content-Type: application/json
```

**Request Body**

```json
{
  "username": "admin",
  "password": "admin1234"
}
```

### 5-2. 응답

**200 OK**

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

### 5-3. 실패 응답

**200 OK**

```json
{
  "success": false,
  "message": "아이디 또는 비밀번호가 올바르지 않습니다.",
  "data": null
}
```

---

## 6. 콘텐츠 등록

### 6-1. 요청

**Method**

```http
POST /api/contents
```

**Headers**

```http
Content-Type: application/json
Authorization: Basic ...
```

**Request Body**

```json
{
  "title": "새 콘텐츠",
  "description": "설명입니다."
}
```

### 6-2. 응답

**200 OK**

```json
{
  "success": true,
  "message": "콘텐츠 등록 성공",
  "data": {
    "id": 3,
    "title": "새 콘텐츠",
    "description": "설명입니다.",
    "viewCount": 0,
    "createdDate": "2026-03-12T10:00:00",
    "createdBy": "user1",
    "lastModifiedDate": "2026-03-12T10:00:00",
    "lastModifiedBy": "user1"
  }
}
```

### 6-3. 비고

* 로그인한 사용자만 등록 가능
* `createdBy`, `lastModifiedBy`는 서버에서 로그인 사용자 기준으로 자동 저장됨

---

## 7. 콘텐츠 목록 조회

### 7-1. 요청

**Method**

```http
GET /api/contents?page=0&size=10
```

**Headers**

```http
Authorization: Basic ...
```

**Query Parameters**

| 이름   | 타입  | 필수 | 설명               |
| ---- | --- | -- | ---------------- |
| page | int | N  | 페이지 번호 (기본값: 0)  |
| size | int | N  | 페이지 크기 (기본값: 10) |

### 7-2. 응답

**200 OK**

```json
{
  "success": true,
  "message": "콘텐츠 목록 조회 성공",
  "data": {
    "content": [
      {
        "id": 2,
        "title": "두 번째 콘텐츠",
        "description": "두 번째 콘텐츠 설명입니다.",
        "viewCount": 0,
        "createdDate": "2026-03-12T09:00:00",
        "createdBy": "user1",
        "lastModifiedDate": "2026-03-12T09:00:00",
        "lastModifiedBy": "user1"
      },
      {
        "id": 1,
        "title": "첫 번째 콘텐츠",
        "description": "첫 번째 콘텐츠 설명입니다.",
        "viewCount": 0,
        "createdDate": "2026-03-12T08:00:00",
        "createdBy": "admin",
        "lastModifiedDate": "2026-03-12T08:00:00",
        "lastModifiedBy": "admin"
      }
    ],
    "page": 0,
    "size": 10,
    "totalElements": 2,
    "totalPages": 1,
    "first": true,
    "last": true
  }
}
```

### 7-3. 비고

* 페이징 처리 필수 요구사항 반영
* 기본 정렬은 `id DESC`

---

## 8. 콘텐츠 상세 조회

### 8-1. 요청

**Method**

```http
GET /api/contents/{id}
```

**Example**

```http
GET /api/contents/1
```

**Headers**

```http
Authorization: Basic ...
```

### 8-2. 응답

**200 OK**

```json
{
  "success": true,
  "message": "콘텐츠 단건 조회 성공",
  "data": {
    "id": 1,
    "title": "첫 번째 콘텐츠",
    "description": "첫 번째 콘텐츠 설명입니다.",
    "viewCount": 1,
    "createdDate": "2026-03-12T08:00:00",
    "createdBy": "admin",
    "lastModifiedDate": "2026-03-12T08:00:00",
    "lastModifiedBy": "admin"
  }
}
```

### 8-3. 비고

* 상세 조회 시 `viewCount` 증가

---

## 9. 콘텐츠 수정

### 9-1. 요청

**Method**

```http
PUT /api/contents/{id}
```

**Example**

```http
PUT /api/contents/2
```

**Headers**

```http
Content-Type: application/json
Authorization: Basic ...
```

**Request Body**

```json
{
  "title": "수정된 제목",
  "description": "수정된 설명"
}
```

### 9-2. 응답

**200 OK**

```json
{
  "success": true,
  "message": "콘텐츠 수정 성공",
  "data": {
    "id": 2,
    "title": "수정된 제목",
    "description": "수정된 설명",
    "viewCount": 0,
    "createdDate": "2026-03-12T09:00:00",
    "createdBy": "user1",
    "lastModifiedDate": "2026-03-12T10:10:00",
    "lastModifiedBy": "user1"
  }
}
```

### 9-3. 권한 정책

* 작성자 본인만 수정 가능
* 관리자(ADMIN)는 모든 콘텐츠 수정 가능

---

## 10. 콘텐츠 삭제

### 10-1. 요청

**Method**

```http
DELETE /api/contents/{id}
```

**Example**

```http
DELETE /api/contents/2
```

**Headers**

```http
Authorization: Basic ...
```

### 10-2. 응답

**200 OK**

```json
{
  "success": true,
  "message": "콘텐츠 삭제 성공",
  "data": null
}
```

### 10-3. 권한 정책

* 작성자 본인만 삭제 가능
* 관리자(ADMIN)는 모든 콘텐츠 삭제 가능

---

## 11. 예외 응답

## 11-1. 입력값 검증 실패

**400 Bad Request**

```json
{
  "success": false,
  "message": "입력값 검증에 실패했습니다.",
  "data": {
    "title": "제목은 필수입니다."
  }
}
```

## 11-2. 인증 실패

**401 Unauthorized**

```json
{
  "success": false,
  "message": "Unauthorized",
  "data": null
}
```

## 11-3. 권한 없음

**403 Forbidden**

```json
{
  "success": false,
  "message": "해당 콘텐츠에 대한 권한이 없습니다.",
  "data": null
}
```

## 11-4. 존재하지 않는 콘텐츠

**404 Not Found**

```json
{
  "success": false,
  "message": "콘텐츠를 찾을 수 없습니다. id=999",
  "data": null
}
```

## 11-5. 서버 내부 오류

**500 Internal Server Error**

```json
{
  "success": false,
  "message": "서버 내부 오류가 발생했습니다.",
  "data": null
}
```

---

## 12. 권한 정책 요약

| 사용자      | 등록 | 목록 조회 | 상세 조회 | 수정       | 삭제       |
| -------- | -- | ----- | ----- | -------- | -------- |
| 비로그인 사용자 | 불가 | 불가    | 불가    | 불가       | 불가       |
| USER     | 가능 | 가능    | 가능    | 본인 글만 가능 | 본인 글만 가능 |
| ADMIN    | 가능 | 가능    | 가능    | 전체 가능    | 전체 가능    |

---

## 13. 테스트 확인 항목

아래 항목을 기준으로 API 동작을 검증했습니다.

* 로그인 성공
* 인증 없는 접근 시 401 응답
* 콘텐츠 등록 성공
* 콘텐츠 목록 페이징 조회 성공
* 콘텐츠 상세 조회 성공
* 작성자 본인 수정 성공
* 관리자 수정/삭제 성공
* 일반 사용자의 타인 콘텐츠 수정 시 403 응답
