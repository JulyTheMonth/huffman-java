package ch.fhnw.juliansteinacher;

import java.util.*;
import java.util.stream.Collectors;

public class HuffmanCodec {

    private char[] text;

    private HashMap<Integer, Integer> asciiCodeOccurences = new HashMap<>();

    private ArrayList<Probability> probabilities = new ArrayList<>();

    private HashMap<Integer, String> codec = new HashMap<>();

    public HuffmanCodec(String text) {
        this.text = text.toCharArray();

        this.countOccurences();

        this.calculateProbabilities();

        this.createCodec();
    }

    private int getLengthOfText() {
        return this.text.length;
    }

    private void countOccurences() {

        for (char c :
                this.text) {
            Integer occurences = this.asciiCodeOccurences.get((int) c);
            if (occurences == null) {
                occurences = 0;
            }
            occurences++;
            this.asciiCodeOccurences.put((int) c, occurences);
        }
    }

    private void calculateProbabilities() {
        Iterator<Map.Entry<Integer, Integer>> it = this.asciiCodeOccurences.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            this.probabilities.add(new Probability(List.of(entry.getKey()), entry.getValue()));
        }
        this.sortProbabilities();
    }

    private void sortProbabilities() {
        this.probabilities.sort(Comparator.comparing(Probability::getProbability));
    }

    private void createCodec() {
        while (this.probabilities.size() > 1) {
            Probability first = this.probabilities.remove(0);
            Probability second = this.probabilities.remove(0);

            addBitToCodes(first.getChars(), "0");
            addBitToCodes(second.getChars(), "1");

            List<Integer> newList = new ArrayList<>(first.getChars());
            newList.addAll(second.getChars());

            this.probabilities.add(new Probability(newList, first.getProbability() + second.getProbability()));
            this.sortProbabilities();
        }
    }

    private void addBitToCodes(List<Integer> codes, String bit) {
        codes.forEach(i -> {
            String existingBits = "";
            if (this.codec.containsKey(i)) {
                existingBits = this.codec.get(i);
            }
            this.codec.put(i, bit + existingBits);
        });
    }

    @Override
    public String toString() {
        return getCodecString();
    }

    public String getCodecString() {
        return this.codec.entrySet().stream().map(es -> es.getKey() + ":" + es.getValue()).collect(Collectors.joining("-"));
    }

    public String getBytesForAscii(int asciiCode){
        return this.codec.get(asciiCode);
    }

}
