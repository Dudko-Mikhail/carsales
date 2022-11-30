package by.dudko.carsales.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@RequiredArgsConstructor
public class ImageService {
    @Value(value = "${app.image.storage:C:\\Users\\Misha\\IdeaProjects\\carsales\\images}")
    private final String storage;

    @SneakyThrows
    public void uploadImage(String imagePath, InputStream inputStream) {
        Path fullPath = Path.of(storage, imagePath);
        Files.createDirectories(fullPath.getParent());
        OutputStream outputStream = Files.newOutputStream(fullPath, CREATE, TRUNCATE_EXISTING);
        FileCopyUtils.copy(inputStream, outputStream);
    }
}
