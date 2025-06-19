package com.mecsbalint.backend.service;

import com.mecsbalint.backend.exception.InvalidFileException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ImageStorageService {

    @Value("${mecsbalint.app.file-upload-dir}")
    private String uploadDir;

    public void deleteFiles(List<String> filePaths) {
        for (String imagePath: filePaths) {
            Path fileToDeletePath = Paths.get(uploadDir + "\\" + imagePath);
            try {
                Files.deleteIfExists(fileToDeletePath);
            } catch (IOException e) {
                throw new UncheckedIOException(String.format("The system can't delete this file: %s", fileToDeletePath), e);
            }
        }
    }

    public Set<String> saveImages(List<MultipartFile> images, String folderName) {
        Set<String> imagePaths = new HashSet<>();

        for (MultipartFile image: images) {
            imagePaths.add(saveImage(image, folderName));
        }

        return imagePaths;
    }

    public String saveImage(MultipartFile image, String folderName) {
        String extension = FilenameUtils.getExtension(image.getOriginalFilename());
        String generatedFilename = UUID.randomUUID() + "." + extension;
        String relativePath = folderName + "\\" + generatedFilename;

        try {
            File targetFile = new File(Paths.get(uploadDir).toAbsolutePath() + "\\" + relativePath);
            targetFile.getParentFile().mkdirs();
            image.transferTo(new File(Paths.get(uploadDir).toAbsolutePath() + "\\" + relativePath));
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("The system can't save this file: %s", relativePath), e);
        }

        return relativePath;
    }

    public void validateImages(List<MultipartFile> files) {
        for (MultipartFile file: files) {
            try {
                Imaging.getImageInfo(file.getBytes());
            } catch (ImagingException e) {
                throw new InvalidFileException(file.getOriginalFilename(), "JPEG/PNG/BMP/GIF/TIFF/PSD/WBMP/ICO", e);
            } catch (IOException e) {
                throw new UncheckedIOException(String.format("The system can't read the file (original filename: %s)", file.getOriginalFilename()), e);
            }
        }
    }
}
