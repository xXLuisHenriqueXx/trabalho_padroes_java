package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        ConsoleUtils.class
    );

    public static void clear() {
        try {
            new ProcessBuilder("cmd", "/c", "cls")
                .inheritIO()
                .start()
                .waitFor();
        } catch (Exception e) {
            LOGGER.error("Nao foi possivel limpar o console.");
        }
    }
}
