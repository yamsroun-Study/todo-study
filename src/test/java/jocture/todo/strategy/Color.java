package jocture.todo.strategy;

import java.util.EnumSet;
import java.util.concurrent.atomic.AtomicReference;

public enum Color { // Enum -> Java 1.5 도입
    RED, YELLOW, GREEN, BLUE, BLACK, WHITE;

    public static Color valueOfOrdinal(int ordinal) {
        EnumSet<Color> colors = EnumSet.allOf(Color.class);
        String name = "NAME 1";
        AtomicReference<Color> result = new AtomicReference<>(); // 참조형으로 변경 필요
        colors.forEach(color -> { // Color.RED -> Color.YELLOW -> ...
            if (color.ordinal() == ordinal) {
                result.set(color); // 람다 캡쳐링 주의
            }
        });
        return result.get();
    }
}
