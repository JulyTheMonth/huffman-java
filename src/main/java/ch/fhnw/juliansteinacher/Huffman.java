package ch.fhnw.juliansteinacher;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Huffman {

    private FileManager fm;

    public static void main(String[] args) {
        String fileDirectory = args[1];

        FileManager fm = new FileManager(fileDirectory);

        Huffman huf = new Huffman(fm);
        if (args[0].equals("encode")) {
            String asciiText = fm.readAsciiContent();

            huf.encode(asciiText);

        }

    }

    public Huffman(FileManager fm) {
        this.fm = fm;
    }

    public void encode(String string) {
        HuffmanCodec codec = new HuffmanCodec(string);

        this.fm.writeCodec(codec.getCodecString());

        String bitString = string.chars().mapToObj(codec::getBytesForAscii).collect(Collectors.joining(""));

        bitString += "1";

        System.out.println(bitString.length());
        Integer numberZerosToBeAdded = 8 - bitString.length() % 8;
        if (numberZerosToBeAdded < 8) {
            bitString += "0".repeat(numberZerosToBeAdded);
        }

        List<Byte> bytes = new ArrayList<>();
        System.out.println(numberZerosToBeAdded);
        System.out.println(bitString);
        System.out.println(bitString.length());

        ByteArrayOutputStream byteOutStream = new ByteArrayOutputStream();

        //idea from https://stackoverflow.com/a/9276720
        int index = 0;
        while (bitString.length() > index) {
            String substring = bitString.substring(index, index + 8);
            if (index == 8) {
                System.out.println(substring);
            }
            byte by = (byte) Integer.parseInt(substring, 2);
//            Byte by = Byte.valueOf(substring, 2);
            byteOutStream.write(by);
            index += 8;
        }
        System.out.println(bitString.length());

        byte[] out = byteOutStream.toByteArray();

        for (byte byt : out
        ) {
            System.out.println(byt);
        }
        fm.writeByteArray(out);
        System.out.println(codec.getCodecString());
    }

}
