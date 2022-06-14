package jocture.todo.strategy;

import java.util.ArrayList;
import java.util.List;

public class AppleFilter {

    // 외부에서 일부 로직을 결정하는 패턴 : 템플릿 메소드 패턴(상속), 전략 패턴

    // 대상 핵심 로직
    // OCP (Open-Closed Principal) -> 확장에는 열려있고, 수정에는 닫혀있다(코드 미수정).
    public static List<Apple> filterApples(List<Apple> apples, ApplePredicate predicate) {
        List<Apple> result = new ArrayList<>();
        for (Apple apple: apples) {
            // 전략 패턴(Strategy Pattern) -> 로직의 일부 행동을 외부에서 주입
            // 특정 메소드를 이용하는 클라이언트가 전체 로직의 일부 행동을 결정 -> 행동(전략) 파라미터화
            if (predicate.test(apple)) {
                result.add(apple);
            }
        }
        return result;
    }

    public static void main(String[] args) {
        List<Apple> apples = AppleUtils.getRandomApples(100); // 확인용 사과 객체 생성

        // 메인 로직 테스트
        // 1. 일반 구현 클래스
        // List<Apple> result = AppleFilter.filterApples(apples, new AppleRedColorOrHeavyWeightPredicate());

        // 2. 익명 (구현) 클래스(Anonymous Class)
        // List<Apple> result = AppleFilter.filterApples(apples, new ApplePredicate() {
        //     @Override
        //     public boolean test(Apple apple) {
        //         return apple.getColor().equals(Color.BLACK);
        //     }
        // });

        // 3. 람다표현식
        // List<Apple> result = AppleFilter.filterApples(apples, apple -> apple.getColor().equals(Color.BLACK));
        // 주석 필요(버릴 사과 선택) -> 주석이 필요한 코드는 클린코드가 아니다.
        List<Apple> result = AppleFilter.filterApples(apples, apple -> apple.getColor().equals(Color.WHITE) && apple.getWeight() < 150);

        for (Apple apple: result) {
            System.out.println(">>> apple = " + apple);
        }
    }
}
