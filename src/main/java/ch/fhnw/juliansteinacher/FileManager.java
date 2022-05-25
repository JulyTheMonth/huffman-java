package ch.fhnw.juliansteinacher;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    final Path directory;

    final String ASCIIFILE = "asciFile.txt";
    final String CODECFILE = "dec_tab.txt";
    final String OUTPUTFILE = "output.dat";

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


    public boolean writeCodec(String codec) {
        try {
            Path path = this.directory.resolve(CODECFILE);
            FileWriter fw = new FileWriter(path.toFile());
            PrintWriter pw = new PrintWriter(fw);
            pw.print(codec);
            pw.close();
            System.out.println("File saved at: " + path.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }

        return true;
    }

    public boolean writeByteArray(byte[] out) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(this.directory.resolve(OUTPUTFILE).toString());
            fos.write(out);
            fos.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
