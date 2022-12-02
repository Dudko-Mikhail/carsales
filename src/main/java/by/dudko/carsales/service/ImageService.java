package by.dudko.carsales.service;

import java.io.InputStream;
import java.util.Optional;

public interface ImageService {
    void uploadImage(String imagePath, InputStream inputStream);

    Optional<byte[]> loadImage(String imagePath);

    void deleteImage(String imagePath);
}
