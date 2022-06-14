package jocture.todo.strategy;

public class AppleRedColorOrHeavyWeightPredicate implements ApplePredicate {

    @Override
    public boolean test(Apple apple) {
        return apple.getColor().equals(Color.RED) || apple.getWeight() >= 200;
    }
}
