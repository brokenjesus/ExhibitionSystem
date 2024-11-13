package by.lupach.exhibitionsystem.services;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    private final String uploadDirectory = "src/main/resources/static/images/";

    // Save image in a local directory
    public String saveImageToStorage(MultipartFile file) throws IOException {
        Path fileNameAndPath = Paths.get(uploadDirectory, file.getOriginalFilename());
        Files.write(fileNameAndPath, file.getBytes());

        return file.getOriginalFilename();
    }


    public String deleteImage(String imageName) throws IOException {
        Path imagePath = Path.of(uploadDirectory, imageName);

        if (Files.exists(imagePath)) {
            Files.delete(imagePath);
            return "Success";
        } else {
            return "Failed"; // Handle missing images
        }
    }
}