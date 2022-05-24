package ch.fhnw.juliansteinacher;

import java.util.HashMap;
import java.util.stream.Stream;

public class Huffman {

    public static void main(String[] args) {
        String fileDirectory = args[1];

        FileManager fm = new FileManager(fileDirectory);

        Huffman huf = new Huffman();
        if (args[0].equals("encode")) {
            String asciiText = fm.readAsciiContent();

            huf.encode(asciiText);

        }

    }


    public void encode(String string) {
        HuffmanCodec codec = new HuffmanCodec(string);

        System.out.println(codec.getCodecString());
    }

}
