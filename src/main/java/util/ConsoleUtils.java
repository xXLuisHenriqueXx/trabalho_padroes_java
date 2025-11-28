package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsoleUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(
        ConsoleUtils.class
    );

    private ConsoleUtils() {}

    public static void clear() {
        try {
            Process process = new ProcessBuilder(
                "C:\\Windows\\System32\\cmd.exe",
                "/c",
                "cls"
            )
                .inheritIO()
                .start();

            process.waitFor();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.error("A execução foi interrompida ao limpar o console.", e);
        } catch (Exception e) {
            LOGGER.error("Não foi possível limpar o console.", e);
        }
    }
}
