# CustomViewPractice
CustomViewPractice 레포지토리!
# Why
- customized view가 필요할 때(애니메이션 등이 필요한 뷰)
- 뷰의 그룹이 앱 내의 다른 부분에서 사용될 때 -> 같은 코드를 중복 작성
![](https://images.velog.io/images/cchloe2311/post/7d51ba44-774e-4325-8e35-d0bcd03e2250/image.png)

- 한 개의 뷰코드가 여러 depth로 이루어질 때
![](https://images.velog.io/images/cchloe2311/post/8d11f846-c476-4762-8b1f-107f5c1d1a03/image.png)
    - 맵과 맵 포인트를 설정하는 코드
    - 장소정보를 보여주고 숨기는 코드
    - Search here 버튼을 다루는 코드
    - 로딩바를 보여주는 코드
    - ... -> 여러 책임을 한번에
# How
그룹으로 만들어 커스텀 컴포넌트로 만들어버리자
![](https://images.velog.io/images/cchloe2311/post/41e120e2-f9ac-42d8-9a58-0d851e671fa7/image.png)
# What
## Custom View
> 미리 빌드된 위젯 또는 레이아웃이 요구에 맞지 않을 때 기본 레이아웃 클래스 View 및 ViewGroup에 기반하여 UI를 구축할 수 있는 방법, 뷰

### 효과
- 중복된 코드를 피할 수 있음
- 관심사를 분리 (1클래스 1책임)
- 클래스를 깔끔하고 이해하기 쉽게 함
### View가 그려지는 과정
#1. 뷰가 포커스를 얻으면 레이아웃을 그리도록 요청
- 루트뷰가 필요
- 코드로 다음과 같이 rootView 제공 setContentView(rootView))

#2. 루트 노드에서 시작되어 트리를 따라 전위순회방식으로 그림
- 그림?: measure -> layout -> draw 
![](https://images.velog.io/images/cchloe2311/post/4c55220a-0289-401a-bea1-396c492c8b03/image.png)

**measure(int widthMeasureSpec, int heightMeasureSpec)**
: 뷰의 크기를 알아내는 과정으로, 부모 노드에서 자식노드를 경유하며 실행
- 뷰의 좌표계를 설정하는 단계(측정단계)
- 내부적으로 onMeasure()을 호출
- 부모뷰와 자식뷰 간의 크기 정보를 전달하기 위해 두 가지 클래스를 사용(자신의 주변환경과 자기 자신의 정보를 측정하기 위해)

_① ViewGroup.LayoutParams_
> [ 부모뷰 ] <----------------- [ 자식뷰 ]
자신이 어떻게 측정되고 위치를 정할지 요청

- 특정 크기(50dp)
- match_parent
- wrap_content

_② ViewGroup.MeasureSpec_
> [ 부모뷰 ] -----------------> [ 자식뷰 ]
ㅤㅤㅤㅤㅤㅤ요구사항 전달

- **UNSPECIFIED**: 자식뷰 마음대로(view can take as much space as it wants) -> parent가 child가 얼마나 큰 크기를 원하는지 알기 위해 사용함. 이후 measure를 다시 호출함
- **EXACTLY**: 정확한 크기 강요 / 64dp처럼 fixed size 또는 match_parent 사용 시
- **AT MOST**: 최대 크기 강요(view can be as big as it wants up to specified size) / wrap_content 또는 match_parent 사용 시

- 내부적으로 super.onMeasure(widthMeasureSpec, heightMeasureSpec) > setMeasuredDimension()을 호출
- 매개변수는 xml에서 받아옴 ex. layout_width = "100dp", layout_height="100dp"으로 설정하면 여기서 그 크기를 측정
- 따라서 super.onMeasure()을 없애고 임의 값으로 넣는다면? 그 값으로만 크기 측정

**layout(int l, int y, int r, int b)**
: 부모노드에서 자식 노드를 경유하며 실행되며 뷰와 자식뷰들의 크기와 위치를 할당
- 뷰의 좌표계를 사용하는 단계(뷰의 내용물을 배치)
- measure(int, int)에 의해 각 뷰에 저장된 크기를 사용해 위치 저장
- 내부적으로 onLayout()을 호출하며 그 안에서 실제 뷰의 위치를 할당

**draw**
: 뷰를 실제로 그리는 단계
- 뷰의 좌표계를 사용하는 단계(뷰의 내용을 그리는 곳)
- Canvas 객체: 뷰의 모양
- Paint 객체: 뷰의 색

+) onDraw() 호출 시 많은 시간소요되고 Scroll, Swipe 시 다시 onDraw()가 호출되기 때문에 함수 내에서 객체 할당을 피하고 한 번 할당된 객체를 재사용하는게 좋음!
### 전체 흐름
![](https://images.velog.io/images/cchloe2311/post/d32dc259-4159-4f08-a622-13b4f9ae78a6/image.png)
- **Constructor**
초기화를 진행하며 default 값을 설정. 초기 설정을 쉽게 하기 위해 AttributeSet 인터페이스 지원
- **onMeasure**
#1. 뷰가 원하는 사이즈 계산
#2. MeasureSpec에 따라 크기와 mode get
#3. mode에 따라 뷰의 크기 설정
- 다시 뷰를 그리는 **invalidata(), requestLayout()**?
    - invalidata(): 단순히 뷰를 다시 그릴 때 사용 ex. text, color qusrud
    - requestLayout(): 뷰의 사이즈가 변경되는 경우, onMeasure()부터 다시!

# ref.
- https://medium.com/@douglas.iacovelli/the-beauty-of-custom-views-and-how-to-do-it-79c7d78e2088
- https://medium.com/mobile-app-development-publication/three-uses-of-custom-views-d286599c9bca
- https://medium.com/@supahsoftware/custom-android-views-graph-view-and-drawing-on-the-canvas-d03c2ea2b703
- https://medium.com/@mayurjajoomj/custom-graphs-custom-view-android-862e16813cc
