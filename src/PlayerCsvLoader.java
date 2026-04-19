import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PlayerCsvLoader {
    public void load(String path, RankingController controller) {
        try (BufferedReader reader = open(path)) {
            if (reader == null) {
                return;
            }
            boolean header = true;
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                if (header) {
                    header = false;
                    continue;
                }
                String[] parts = line.split(",");
                if (parts.length < 2) {
                    continue;
                }
                String nickname = parts[0].trim();
                int ranking = Integer.parseInt(parts[1].trim());
                controller.addPlayer(nickname, ranking);
            }
        } catch (IOException | NumberFormatException | IllegalStateException ignored) {
        }
    }

    private BufferedReader open(String path) throws IOException {
        InputStream fromClasspath = PlayerCsvLoader.class.getResourceAsStream("/" + path);
        if (fromClasspath != null) {
            return new BufferedReader(new InputStreamReader(fromClasspath, StandardCharsets.UTF_8));
        }
        Path csvPath = Path.of(path);
        if (Files.exists(csvPath)) {
            return Files.newBufferedReader(csvPath);
        }
        Path underSrc = Path.of("src", path);
        if (Files.exists(underSrc)) {
            return Files.newBufferedReader(underSrc);
        }
        return null;
    }
}
