package xyz.lurkyphish2085.snipshare.snip;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;

@Component
public class SnipRecordGenerator {

    public static final String FILE_NAME_PREFIX = "snip.";
    public static final String FILE_NAME_EXTENSION = ".txt";
    public static final int RETRIEVAL_ID_LENGTH = 8;

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";

    private final SecureRandom random = new SecureRandom();

    public Snip generate(LocalDate expiryDate) {
        String id = generateRandomId();
        String fileName = FILE_NAME_PREFIX + id + FILE_NAME_EXTENSION;

        return new Snip(id, fileName, expiryDate, false);
    }

    private String generateRandomId() {
        StringBuilder generatedId = new StringBuilder();

        for (int i = 0; i < RETRIEVAL_ID_LENGTH; i++) {
            int randomIndex = random.nextInt(ALPHA_NUMERIC_STRING.length());
            char randomCharacter = ALPHA_NUMERIC_STRING.charAt(randomIndex);
            generatedId.append(randomCharacter);
        }

        return generatedId.toString();
    }
}
