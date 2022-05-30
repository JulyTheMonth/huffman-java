package ch.fhnw.juliansteinacher;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Huffman {

    private FileManager fm;

    /**
     * Main methode
     * @param args Parameters
     */
    public static void main(String[] args) {
        String fileDirectory = args[1];

        FileManager fm = new FileManager(fileDirectory);

        Huffman huf = new Huffman(fm);
        // Mode selection
        if (args[0].equals("encode")) {
            String asciiText = fm.readAsciiContent();

            huf.encode(asciiText);

        } else {

            byte[] messageInBytes = fm.readEncodedFile();

            String codecString = fm.readCodec();

            huf.decode(messageInBytes, codecString);

        }

    }

    public Huffman(FileManager fm) {
        this.fm = fm;
    }

    /**
     * Encodes the string with huffman.
     * @param string
     */
    public void encode(String string) {
        //huffman Codec from string.
        HuffmanCodec codec = new HuffmanCodec(string, false);

        this.fm.writeCodec(codec.getCodecString());

        //Converts each character to their bitstring according to their ascii integer.
        String bitString = string.chars().mapToObj(codec::getBytesForAscii).collect(Collectors.joining(""));

        //add filler byte.
        bitString += "1";
        int numberZerosToBeAdded = 8 - bitString.length() % 8;
        if (numberZerosToBeAdded < 8) {
            bitString += "0".repeat(numberZerosToBeAdded);
        }

        // Convert the BitString to byte Array
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

        byte[] out = byteOutStream.toByteArray();

        //Writes byteArray to output-File.
        fm.writeByteArray(out);
        System.out.println(codec.getCodecString());
    }

    /**
     * Decodes the encoded message according to the given codecString.
     * @param messageInBytes Message as bytearray.
     * @param codecString Codecstring
     */
    public void decode(byte[] messageInBytes, String codecString) {
        String bitString = "";
        //conversion inspired by : https://mkyong.com/java/java-how-to-convert-a-byte-to-a-binary-string/
        for (byte b : messageInBytes) {
            // byte to an unsigned integer
            // & 0xff to prevent sign extension, no effect on positiv
            int unsignedByte = b & 0xff;

            bitString += String.format("%8s", Integer.toBinaryString(unsignedByte)).replace(" ", "0");
        }
        // remove filler String (1 and zero to infinity 0).
        bitString = bitString.replaceFirst("10*$", "");
        // creates code from codecstring.
        HuffmanCodec codec = new HuffmanCodec(codecString, true);
        StringBuilder sbBuffer = new StringBuilder();
        StringBuilder sbMessage = new StringBuilder();
        // Loops through each bit and adds it to the buffer. if bits in buffer match a ascii code in codec convert it and reset buffer.
        bitString.chars().forEach(i -> {
            sbBuffer.append((char) i);

            if (codec.getAsciiForBytes(sbBuffer.toString()) != null) {
                sbMessage.append((char) codec.getAsciiForBytes(sbBuffer.toString()).intValue());
                sbBuffer.setLength(0);
            }
        });


        System.out.println(sbMessage);
        // save decoded message.
        this.fm.writeDecompress(sbMessage.toString());
    }

}
