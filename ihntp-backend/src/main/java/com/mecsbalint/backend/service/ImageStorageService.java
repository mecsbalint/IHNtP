package com.mecsbalint.backend.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface ImageStorageService {

    Set<String> downloadAndSaveImages(Set<String> links, String savePath);

    String downloadAndSaveImage(String link, String savePath);

    boolean deleteFolderContent(String folderName, Set<String> exceptions);

    boolean deleteFolder(String folderName);

    void deleteFiles(Set<String> filePaths);

    Set<String> saveImages(List<MultipartFile> images, String folderName);

    String saveImage(MultipartFile image, String folderName);

    String saveImage(byte[] imageBytes, String ogFilename, String folderName);

    boolean validateMultipartFileImages(List<MultipartFile> files);

    boolean validateMultipartFileImages(MultipartFile file);
}
