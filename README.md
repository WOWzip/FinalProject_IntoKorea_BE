# [프로젝트명] IntoKorea 
> 여행지 선택에 어려움을 겪는 사람을 위한 여행지 추천 및 상세 정보 제공 <br/>
> 개발 기간 : 2024.01 ~ 2024.02 <br/>
> 개발 인원 : 3명

---

## 기술 스택

### 백엔드  
- Java 11
- SpringBoot
- Spring Data JPA
- OAuth2.0
- Oracle DB
- AWS(LightSail)

---

## 나의 역할

### 백엔드
- 회원가입 및 로그인
- 소셜 로그인
- 회원정보 수정
- 회원 탈퇴
- 아이디 및 비밀번호 찾기
- 본인인증 (이메일 인증)

### 그 외
- AWS LightSail을 이용하여 DB서버 구축


---

## 자체평가
소셜 로그인을 구현할 때 서로 다른 소셜 계정에서 이메일이 중복되는 문제를 해결하기 위해 고민했습니다.
서비스 이용의 편의성을 고려하여 이메일을 고유의 값으로 받아서 여러 계정이 생기지 않도록 회원DB를 설계했습니다.
소셜 로그인을 구현할 때 회원계정을 관리하는 측면에서 고려해야할 사항이 많다는 것을 깨달았습니다.

---

## 🚩프로젝트 주요 기능
- ⭐(나의 역할) AWS DB서버 설계, 회원가입, 로그인, 소셜 로그인, 아이디 비밀번호 찾기, 회원 정보 수정, 회원 탈퇴
- TourAPI를 활용하여 여행지 리스트 및 행사/공연에 대한 정보 추출하여 제공 
- 메인 페이지에서 랜덤으로 여행지 5곳 추천
- 관심있는 여행지를 저장하여 볼 수 있도록 '즐겨찾기' 기능 제공
- 여행에 대한 추억을 남길 수 있도록 '다이어리' 페이지 제공
- 'Q&A 게시판'을 통해 회원과 관리자 소통 가능 (첨부파일 업로드 및 다운로드 가능)

---

## ERD
<img src="https://github.com/WOWzip/FinalProject_IntoKorea_BE/assets/142926896/d71afe18-8c26-4927-aa57-833055236ee8" width="80%" height="80%"/>

---

## 흐름도
<img src="https://github.com/WOWzip/FinalProject_IntoKorea_BE/assets/142926896/f6926054-caf8-4a5f-88ba-e268d4a5e2b8)https://github.com/WOWzip/FinalProject_IntoKorea_BE/assets/142926896/f6926054-caf8-4a5f-88ba-e268d4a5e2b8" width="70%" height="60%"/>

