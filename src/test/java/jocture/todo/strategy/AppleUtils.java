package jocture.todo.strategy;

import java.util.ArrayList;
import java.util.List;

public class AppleUtils {

    public static List<Apple> getRandomApples(int count) {
        List<Apple> result = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            // 사과 색상
            int randomNumberForColor = (int) (Math.random() * 6); // 0~5 숫자 중 랜덤
            Color color = Color.valueOfOrdinal(randomNumberForColor);
            // 사과 무게
            int weight = (int) (Math.random() * 150 + 100);
            // 사과 객체 생성
            Apple apple = new Apple(color, weight);
            result.add(apple);
        }
        return result;
    }
}
