package ch.fhnw.juliansteinacher;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileManager {

    final Path directory;

    final String ASCIIFILE = "asciFile.txt";

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
}
