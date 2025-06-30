package com.mecsbalint.backend.service.game;

import com.mecsbalint.backend.exception.InvalidFileException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.service.image.ImageStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GameImageService {

    private final ImageStorageService imageStorageService;

    public GameImageService(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    public void handleImagesForAddGame(Game game, List<MultipartFile> screenshotFiles, MultipartFile headerImgFile) {
        try {
            game.setHeaderImg(handleHeaderImg(game.getId(), game.getHeaderImg(), null, headerImgFile));

            Set<String> screenshotsToAdd = game.getScreenshots();
            game.setScreenshots(new HashSet<>());

            Set<String> downloadedScreenshotPaths = downloadAndSaveImages(screenshotsToAdd, game.getId() + "\\screenshots");
            game.getScreenshots().addAll(downloadedScreenshotPaths);

            Set<String> savedScreenshotPaths = getSavedScreenshots(screenshotFiles, game.getId() + "\\screenshots");
            game.getScreenshots().addAll(savedScreenshotPaths);
        } catch (Exception e) {
            imageStorageService.deleteFolder(game.getId().toString());
            throw e;
        }
    }

    public void handleImagesForEditGame(Long gameId, Game gameNew, Game gameOg, List<MultipartFile> screenshotFiles, MultipartFile headerImgFile) {
        Set<String> screenshotsToKeep = null;

        try {
            gameNew.setHeaderImg(handleHeaderImg(gameId, gameNew.getHeaderImg(), gameOg.getHeaderImg(), headerImgFile));

            Set<String> screenshotsToDelete = gameOg.getScreenshots().stream()
                    .filter(screenshot -> !gameNew.getScreenshots().contains(screenshot) && !screenshot.contains("http"))
                    .collect(Collectors.toSet());
            screenshotsToKeep = gameNew.getScreenshots().stream()
                    .filter(screenshot -> gameOg.getScreenshots().contains(screenshot))
                    .collect(Collectors.toSet());
            Set<String> screenshotsToDownload = gameNew.getScreenshots().stream()
                    .filter(screenshot -> !gameOg.getScreenshots().contains(screenshot))
                    .collect(Collectors.toSet());

            gameNew.setScreenshots(new HashSet<>());
            Set<String> gameScreenshots = gameNew.getScreenshots();

            imageStorageService.deleteFiles(screenshotsToDelete);
            gameScreenshots.addAll(getDownloadedScreenshotsPaths(screenshotsToDownload, gameId));
            gameScreenshots.addAll(screenshotsToKeep);
            gameScreenshots.addAll(getSavedScreenshots(screenshotFiles, gameId + "\\screenshots"));
        } catch (Exception e) {
            if (gameId != null) imageStorageService.deleteFolderContent(gameId.toString(), screenshotsToKeep);
            throw e;
        }
    }

    private String handleHeaderImg(Long gameId, String headerImgNew, String headerImgOg, MultipartFile headerImgFile) {
        if (headerImgFile != null) {
            if (headerImgOg != null && !headerImgOg.contains("http")) imageStorageService.deleteFiles(Set.of(headerImgOg));
            return saveHeaderImg(headerImgFile, gameId);
        } else if (headerImgNew == null) {
            if (headerImgOg != null && !headerImgOg.contains("http")) imageStorageService.deleteFiles(Set.of(headerImgOg));
            return null;
        } else if (headerImgNew.equals(headerImgOg)) {
            return headerImgOg;
        } else {
            if (headerImgOg != null && !headerImgOg.contains("http")) imageStorageService.deleteFiles(Set.of(headerImgOg));
            return downloadAndSaveImage(headerImgNew, gameId + "\\header_img");
        }
    }

    private Set<String> getDownloadedScreenshotsPaths(Set<String> links, Long gameId) {
        return imageStorageService.downloadAndSaveImages(links, gameId + "\\screenshots").stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private Set<String> downloadAndSaveImages(Set<String> links, String savePath) {
        Set<String> paths = new HashSet<>();

        for (String link : links) {
            paths.add(downloadAndSaveImage(link, savePath));
        }

        return paths;
    }

    private String downloadAndSaveImage(String link, String savePath) {
        String imagePath = imageStorageService.downloadAndSaveImage(link, savePath);
        if (imagePath == null) throw new InvalidFileException("jpg/png/gif/webp/bmp/svg");
        return imagePath;
    }

    private String saveHeaderImg(MultipartFile headerImg, Long gameId) {
        if (!imageStorageService.validateMultipartFileImages(headerImg)) throw new InvalidFileException("jpg/png/gif/webp/bmp/svg");
        return imageStorageService.saveImage(headerImg, gameId + "\\header_img");
    }

    private Set<String> getSavedScreenshots(List<MultipartFile> screenshotFiles, String savePath) {
        if (screenshotFiles != null) {
            if (!imageStorageService.validateMultipartFileImages(screenshotFiles)) throw new InvalidFileException("jpg/png/gif/webp/bmp/svg");
            return imageStorageService.saveImages(screenshotFiles, savePath);
        }
        return new HashSet<>();
    }
}
