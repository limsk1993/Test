## 프로젝트 환경
```
Android Studio Version : Android Studio Ladybug Feature Drop | 2024.2.2
JDK Version : JetBrains Runtime 21.0.4
Android Gradle Plugin Version : 8.8.0
Gradle Version : 8.10.2
Target / Compile SDK Version : 35
Min SDK Version : 26
```

<br />

## 프로젝트 구조에 대한 설명
```
아래의 세 가지의 멀티 모듈로 되어 있으며 최대한 Multi Module + Clean Architecture로 구성하려고 노력했습니다.
app : 앱 화면 및 ViewModel 관리
api : 카카오 API 및 Room DB 관리
view : UI 컴포넌트 관리
```

<br />

## 사용 라이브러리와 사용 용도
```
Retrofit2 : 카카오 API 네트워크 호출
Compose : 선언형 UI 구성 및 화면 렌더링
Coil : 네트워크 이미지의 비동기 로딩 및 캐싱 처리
Hilt : 코드 재사용성을 높이고 결합도를 낮추는 용도
Room : 북마크 저장용 로컬 데이터베이스
```

<br />

## 이미지 처리에 관한 설명
```
Glide와 Picasso 보다 로딩 속도 및 메모리 관리, 캐시 성능이 좋은 Coil 라이브러리를 채택하였습니다.
Coil 내장의 디코딩 및 리사이징 기능을 활용하여 다양한 화면에 맞춰서 이미지 최적화를 하였고
화면 스크롤 시의 비동기 로드로 UI 지연을 최소화, 불필요한 리소스 점유를 방지하도록 하였습니다.
AsyncImage에서 loading progress, error 이미지를 설정하여 로딩 및 실패, null URL 등에 대한 대응을 추가했습니다.
```