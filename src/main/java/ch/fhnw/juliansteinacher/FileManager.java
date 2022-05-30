package ch.fhnw.juliansteinacher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    final Path directory;

    final String ASCIIFILE = "asciFile.txt";
    final String CODECFILE = "dec_tab.txt";
    final String OUTPUTFILE = "output.dat";
    final String DECOMPRESS = "decompress.txt";

    public FileManager(String directory) {
        this.directory = Path.of(directory);
    }

    public String readAsciiContent() {
        try {
            return Files.readString(this.directory.resolve(ASCIIFILE));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void writeCodec(String codec) {
        try {
            Path path = this.directory.resolve(CODECFILE);
            FileWriter fw = new FileWriter(path.toFile());
            PrintWriter pw = new PrintWriter(fw);
            pw.print(codec);
            pw.close();
            System.out.println("File saved at: " + path.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public void writeByteArray(byte[] out) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.directory.resolve(OUTPUTFILE).toString());
            fos.write(out);
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] readEncodedFile() {
        File file = new File(this.directory.resolve(OUTPUTFILE).toString());
        byte[] bFile = new byte[(int) file.length()];


        try {
            FileInputStream fis = new FileInputStream(file);

            fis.read(bFile);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bFile;
    }

    public String readCodec() {
        try {
            return Files.readString(this.directory.resolve(CODECFILE));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeDecompress(String toString) {
        try {
            Path path = this.directory.resolve(DECOMPRESS);
            FileWriter fw = new FileWriter(path.toFile());
            PrintWriter pw = new PrintWriter(fw);
            pw.print(toString);
            pw.close();
            System.out.println("File saved at: " + path.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
