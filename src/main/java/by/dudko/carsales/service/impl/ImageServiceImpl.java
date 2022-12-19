package by.dudko.carsales.service.impl;

import by.dudko.carsales.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    @Value(value = "${app.image.storage:C:\\Users\\Misha\\IdeaProjects\\carsales\\images}")
    private final String storage;

    @Override
    @SneakyThrows
    public void uploadImage(String imagePath, InputStream inputStream) {
        Path fullPath = getFullPath(imagePath);
        Files.createDirectories(fullPath.getParent());
        OutputStream outputStream = Files.newOutputStream(fullPath, CREATE, TRUNCATE_EXISTING);
        FileCopyUtils.copy(inputStream, outputStream);
    }

    @Override
    @SneakyThrows
    public Optional<byte[]> loadImage(String imagePath) {
        Path fullPath = getFullPath(imagePath);
        return Files.exists(fullPath)
                ? Optional.of(FileCopyUtils.copyToByteArray(Files.newInputStream(fullPath)))
                : Optional.empty();
    }

    @Override
    @SneakyThrows
    public void deleteImage(String imagePath) {
        Path fullPath = getFullPath(imagePath);
        Files.deleteIfExists(fullPath);
    }

    private Path getFullPath(String imagePath) {
        return Path.of(storage, imagePath);
    }
}
