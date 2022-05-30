package ch.fhnw.juliansteinacher;

import java.util.*;
import java.util.stream.Collectors;

public class HuffmanCodec {

    private char[] text;

    private final HashMap<Integer, Integer> asciiCodeOccurences = new HashMap<>();

    private final ArrayList<Probability> probabilities = new ArrayList<>();

    private final HashMap<Integer, String> codec = new HashMap<>();
    private final HashMap<String, Integer> codecBi = new HashMap<>();

    public HuffmanCodec(String text, boolean recreateCodec) {
        // Check if Codes is created from text or from codec string.
        if (recreateCodec) {
            this.createCodeFromCodecString(text);
        } else {
            this.text = text.toCharArray();
            this.countOccurences();
            this.calculateProbabilities();
            this.createCodec();
        }
    }

    /**
     * Counts occurences of all characters.
     */
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

    /**
     * creates probability Objects for each character
     */
    private void calculateProbabilities() {
        Iterator<Map.Entry<Integer, Integer>> it = this.asciiCodeOccurences.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Integer, Integer> entry = it.next();
            this.probabilities.add(new Probability(List.of(entry.getKey()), entry.getValue()));
        }
        this.sortProbabilities();
    }

    /**
     * sorts the Probilities by their probability. Least Probable to Most Probable
     */
    private void sortProbabilities() {
        this.probabilities.sort(Comparator.comparing(Probability::getProbability));
    }

    /**
     * Creates the codec from probability-list.
     */
    private void createCodec() {
        // Loop until on probability contains all characters.
        // merges the two lowest probable items an adds 0 and 1 before each character code.
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

    //Adds bit string too all ascii codes in codec.
    private void addBitToCodes(List<Integer> codes, String bit) {
        codes.forEach(i -> {
            String existingBits = "";
            if (this.codec.containsKey(i)) {
                existingBits = this.codec.get(i);
            }
            this.codec.put(i, bit + existingBits);
        });
    }

    // creates the codeBi map, which is the reverse of the codec map.
    private void createBiMap() {
        this.codec.forEach((key, value) -> this.codecBi.put(value, key));
    }

    /**
     * Creates codec from codec string.
     * @param string
     */
    private void createCodeFromCodecString(String string) {
        String[] pairs = string.split("-");
        for (String pair :
                pairs) {
            String[] pairElements = pair.split(":");
            if (pairElements.length == 2) {
                this.codec.put(Integer.parseInt(pairElements[0]), pairElements[1]);
            } else {
                throw new RuntimeException("Huffman Codec files contains Format errors");
            }
        }
    }

    @Override
    public String toString() {
        return getCodecString();
    }

    /**
     * Get the codec String.
     * @return
     */
    public String getCodecString() {
        return this.codec.entrySet().stream().map(es -> es.getKey() + ":" + es.getValue()).collect(Collectors.joining("-"));
    }

    /**
     * Gets the bitString for the Integer ascii code.
     * @param asciiCode
     * @return bitString
     */
    public String getBytesForAscii(int asciiCode) {
        return this.codec.get(asciiCode);
    }

    /**
     * Get the ascii Code for the bitString.
     * @param bitString
     * @return Integer Ascii Code
     */
    public Integer getAsciiForBytes(String bitString) {
        if (this.codecBi.size() == 0) {
            this.createBiMap();
        }
        return this.codecBi.getOrDefault(bitString, null);
    }


}
