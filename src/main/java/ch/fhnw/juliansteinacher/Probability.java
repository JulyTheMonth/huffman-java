package ch.fhnw.juliansteinacher;

import java.util.Arrays;
import java.util.List;

public class Probability {

    // List of chars with this probability.
    final List<Integer> chars;

    //Probability Number = Nubmer of occurences
    final Integer probability;

    public Probability(List<Integer> chars, Integer probability) {
        this.chars = chars;
        this.probability = probability;
    }

    public List<Integer> getChars() {
        return chars;
    }

    public Integer getProbability() {
        return probability;
    }

    @Override
    public String toString() {
        return "Probability{" +
                "chars=" + chars.toString() +
                ", probability=" + probability +
                '}';
    }
}
