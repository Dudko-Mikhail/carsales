package by.dudko.carsales.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Component
@RequiredArgsConstructor
@Profile("test")
@Slf4j
public class ImageStorageManager {
    @Value("#{T(java.nio.file.Path).of(\"${app.image.storage}\")}")
    private final Path storagePath;
    @Value("#{T(java.nio.file.Path).of(\"${test.image.source}\")}")
    private final Path sourcePath;

    public void fillImageStorage() {
        if (Files.isDirectory(sourcePath)) {
            try (var directoryStream = Files.newDirectoryStream(sourcePath)) {
                if (!Files.isDirectory(storagePath)) {
                    Files.createDirectories(storagePath);
                }
                for (Path path : directoryStream) {
                    Files.copy(path, storagePath.resolve(path.getFileName()), StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (IOException e) {
                log.error("An IO error has occurred", e);
                throw new RuntimeException(e);
            }
        }
    }

    public void clearImageStorage() {
        if (Files.isDirectory(storagePath)) {
            try (var directoryStream = Files.newDirectoryStream(storagePath)) {
                for (Path path : directoryStream) {
                    Files.delete(path);
                }
                Files.deleteIfExists(storagePath);
            } catch (IOException e) {
                log.error("An IO error has occurred", e);
                throw new RuntimeException(e);
            }
        }
    }
}
