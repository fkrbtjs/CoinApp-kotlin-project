# CoinApp-kotlin-project

## 개 요

- Kotlin을 활용하여 제작한 코인앱

## 개발환경

| 구 분 | 내 용 |
| --- | --- |
| OS | Windows 10 home |
| Language | Kotlin |
| Editor | Android Studio Dolphin |
| DB | Room |
| API | 빗썸 현재가 정보 조회 : https://apidocs.bithumb.com/reference/%ED%98%84%EC%9E%AC%EA%B0%80-%EC%A0%95%EB%B3%B4-%EC%A1%B0%ED%9A%8C-all <br>빗썸 최근 체결 내역 : https://apidocs.bithumb.com/reference/%EC%B5%9C%EA%B7%BC-%EC%B2%B4%EA%B2%B0-%EB%82%B4%EC%97%AD |
| Github | https://github.com/fkrbtjs/CoinApp-kotlin-project |

## 개발기간

2022.02.07(화) ~ 2022.02.16(목)


## 기능 요약 및 설명


- Lottie animation과 splash screen을 활용하여 인트로화면 구성
- Navigation을 이용한 fragment간 화면전환
- dataStore를 활용하여 접속이력이 있는지 확인
- 받아온 Api 데이터들을 room에 저장
https://user-images.githubusercontent.com/115532120/219955940-6b6b1310-9a58-4ecb-bc8f-aacf9a61051f.mp4


- LiveData를 활용하여 실시간 데이터 감지
- WorkManager를 활용한 백그라운드 작업 (관심있는 코인의 변동사항을 시간대별로 확인할 수 있음)
https://user-images.githubusercontent.com/115532120/219956464-a53b02af-d352-4141-b86c-b11660fdce48.mp4



- ForegroundService를 활용하여 상단바에 실시간 코인정보 노출
https://user-images.githubusercontent.com/115532120/219956811-e1d7234b-126d-47f8-852a-7599861cd977.mp4





