package com.mecsbalint.backend.service;

import com.mecsbalint.backend.utility.Fetcher;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ImageStorageService {

    private final String uploadDir;

    private final UUID uuid;

    private final Fetcher fetcher;

    private final Logger logger;

    public ImageStorageService(@Value("${mecsbalint.app.file-upload-dir}") String uploadDir, UUID uuid, Fetcher fetcher, Logger logger) {
        this.uploadDir = uploadDir;
        this.uuid = uuid;
        this.fetcher = fetcher;
        this.logger = logger;
    }

    public Set<String> downloadAndSaveImages(Set<String> links, String savePath) {
        Set<String> savedPaths = new HashSet<>();
        for (String link : links) {
            savedPaths.add(downloadAndSaveImage(link, savePath));
        }

        return savedPaths;
    }

    public String downloadAndSaveImage(String link, String savePath) {
        String contentType = fetcher.fetchContentType(link);
        String extension = getExtensionFromContentType(contentType);

        if (extension.equals("bin")) return null;

        byte[] imageBytes = fetcher.fetch(link, byte[].class);

        return saveImage(imageBytes, "image." + extension, savePath);
    }

    public boolean deleteFolderContent(String folderName, Set<String> exceptions) {
        Set<Path> exceptionPaths = exceptions.stream()
                .map(exception -> Paths.get(uploadDir, exception).normalize())
                .collect(Collectors.toSet());
        Path folderPath = Paths.get(uploadDir, folderName);
        try {
            Files.walk(folderPath)
                    .sorted(Comparator.reverseOrder())
                    .filter(path -> !exceptionPaths.contains(path.normalize()))
                    .map(Path::toFile)
                    .forEach(File::delete);
            return true;
        } catch (IOException e) {
            logger.error(String.format("Failed to delete folder: %S", folderPath), e);
            return false;
        }
    }

    public boolean deleteFolder(String folderName) {
        return deleteFolderContent(folderName, Set.of());
    }

    public void deleteFiles(Set<String> filePaths) {
        Set<String> internalFilePaths = filePaths.stream()
                .map(path -> path.replace("/api/images/", ""))
                .collect(Collectors.toSet());
        for (String internalFilePath: internalFilePaths) {
            Path fileToDeletePath = Paths.get(uploadDir, internalFilePath);
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
        return saveImage(createBytesFromMultipartFile(image), image.getOriginalFilename(), folderName);
    }

    public String saveImage(byte[] imageBytes, String ogFilename, String folderName) {
        String extension = FilenameUtils.getExtension(ogFilename);
        String generatedFilename = uuid.randomUUID() + "." + extension;

        Path relativePath = Paths.get(folderName, generatedFilename);
        Path targetPath = Paths.get(uploadDir).toAbsolutePath().resolve(relativePath);

        try {
            Files.createDirectories(targetPath.getParent());
            Files.write(targetPath, imageBytes);
        } catch (IOException e) {
            throw new UncheckedIOException(
                    String.format("The system can't save this file: %s", relativePath), e);
        }

        return "/api/images/" + relativePath.toString().replace(File.separatorChar, '/');
    }

    public boolean validateMultipartFileImages(List<MultipartFile> files) {
        for (MultipartFile file : files) {
            if (!validateMultipartFileImages(file)) return false;
        }
        return true;
    }

    public boolean validateMultipartFileImages(MultipartFile file) {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        List<String> validExtensions = List.of("jpg", "jpeg", "png", "gif", "webp", "bmp", "svg");

        return validExtensions.contains(extension);
    }

    private byte[] createBytesFromMultipartFile(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(String.format("The system can't read the file (original filename: %s)", file.getOriginalFilename()), e);
        }
    }

    private String getExtensionFromContentType(String contentType) {
        if (contentType == null) return "bin";

        return switch (contentType) {
            case "image/jpeg", "image/jpg" -> "jpg";
            case "image/png"               -> "png";
            case "image/gif"               -> "gif";
            case "image/webp"              -> "webp";
            case "image/bmp"               -> "bmp";
            case "image/svg+xml"           -> "svg";
            default                        -> "bin";
        };
    }
}
