package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

@Repository
public class SnipFileRepository {

    @Value("${file.path.storage}")
    private String fileStoragePath;

    public Optional<String> getSnipFileContent(String fileName) {
        String filePath = fileStoragePath + fileName;
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(content);
    }

    public void writeSnipFile(String fileName, String content) {
        String filePath = fileStoragePath + fileName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
