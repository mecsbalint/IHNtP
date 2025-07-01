package com.mecsbalint.backend.service.game;

import com.mecsbalint.backend.exception.InvalidFileException;
import com.mecsbalint.backend.model.Game;
import com.mecsbalint.backend.service.image.ImageStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GameImageServiceTest {

    @Mock
    private ImageStorageService imageStorageServiceMock;

    private GameImageService gameImageService;

    @BeforeEach
    public void setUp() {
        gameImageService = new GameImageService(imageStorageServiceMock);
    }

    @Test
    public void handleImagesForAddGame_multipleValidScreenshotFiles_saveScreenshotFiles() {
        Set<String> savedScreenshotPaths = Set.of("screenshot_file_1", "screenshot_file_2");
        when(imageStorageServiceMock.validateMultipartFileImages(anyList())).thenReturn(true);
        when(imageStorageServiceMock.saveImages(any(), any())).thenReturn(savedScreenshotPaths);

        Game game = getGame();
        List<MultipartFile> screenshotFiles = List.of(getMultipartFileMock(), getMultipartFileMock());
        gameImageService.handleImagesForAddGame(game, screenshotFiles, null);
        Set<String> actualGameScreenshots = game.getScreenshots();

        assertEquals(savedScreenshotPaths, actualGameScreenshots);
        verify(imageStorageServiceMock).validateMultipartFileImages(screenshotFiles);
        verify(imageStorageServiceMock).saveImages(eq(screenshotFiles), any());
    }

    @Test
    public void handleImagesForAddGame_invalidScreenshotFiles_deleteFolderAndThrowInvalidFileException() {
        when(imageStorageServiceMock.validateMultipartFileImages(anyList())).thenThrow(new InvalidFileException(""));

        Game game = getGame();
        List<MultipartFile> screenshotFiles = List.of(getMultipartFileMock(), getMultipartFileMock());

        assertThrows(InvalidFileException.class, () -> gameImageService.handleImagesForAddGame(game, screenshotFiles, null));
        verify(imageStorageServiceMock).deleteFolder(game.getId().toString());
    }

    @Test
    public void handleImagesForAddGame_noScreenshotFiles_notSaveScreenshotFiles() {
        Game game = getGame();
        gameImageService.handleImagesForAddGame(game, null, null);

        assertTrue(game.getScreenshots().isEmpty());
        verify(imageStorageServiceMock, never()).saveImages(any(), any());
    }

    @Test
    public void handleImagesForAddGame_multipleValidScreenshotLinks_saveScreenshotLinks() {
        Set<String> savedScreenshotPaths = Set.of("screenshot_file_1", "screenshot_file_2");
        when(imageStorageServiceMock.downloadAndSaveImages(any(), any())).thenReturn(savedScreenshotPaths);

        Game game = getGame();
        Set<String> screenshotLinks = Set.of("screenshot_link_1", "screenshot_link_2");
        game.setScreenshots(screenshotLinks);
        gameImageService.handleImagesForAddGame(game, null, null);
        Set<String> actualGameScreenshots = game.getScreenshots();

        assertEquals(savedScreenshotPaths, actualGameScreenshots);
        verify(imageStorageServiceMock).downloadAndSaveImages(eq(screenshotLinks), any());
    }

    @Test
    public void handleImagesForAddGame_invalidScreenshotLinks_notSaveScreenshotLinks() {
        when(imageStorageServiceMock.downloadAndSaveImages(any(), any())).thenReturn(new HashSet<>(Collections.singleton(null)));

        Game game = getGame();
        gameImageService.handleImagesForAddGame(game, null, null);

        verify(imageStorageServiceMock).downloadAndSaveImages(any(), any());
        assertTrue(game.getScreenshots().isEmpty());
    }

    @Test
    public void handleImagesForAddGame_noScreenshotLinks_notSaveScreenshotLinks() {
        when(imageStorageServiceMock.downloadAndSaveImages(any(), any())).thenReturn(Set.of());

        Game game = getGame();
        gameImageService.handleImagesForAddGame(game, null, null);

        verify(imageStorageServiceMock).downloadAndSaveImages(any(), any());
        assertTrue(game.getScreenshots().isEmpty());
    }

    @Test
    public void handleImagesForAddGame_validHeaderImgFile_saveHeaderImgFile() {
        String savedHeaderImgPath = "saved_header_img";
        when(imageStorageServiceMock.validateMultipartFileImages(any(MultipartFile.class))).thenReturn(true);
        when(imageStorageServiceMock.saveImage(any(), any())).thenReturn(savedHeaderImgPath);

        Game game = getGame();
        MultipartFile headerImgFile = getMultipartFileMock();
        gameImageService.handleImagesForAddGame(game, null, headerImgFile);

        verify(imageStorageServiceMock).saveImage(eq(headerImgFile), any());
        assertEquals(savedHeaderImgPath, game.getHeaderImg());
    }

    @Test
    public void handleImagesForAddGame_invalidHeaderImgFile_deleteFolderAndThrowInvalidFileException() {
        when(imageStorageServiceMock.validateMultipartFileImages(any(MultipartFile.class))).thenReturn(false);

        Game game = getGame();
        assertThrows(InvalidFileException.class, () -> gameImageService.handleImagesForAddGame(game, null, getMultipartFileMock()));
        verify(imageStorageServiceMock).deleteFolder(game.getId().toString());
    }

    @Test
    public void handleImagesForAddGame_validHeaderImgLinkAndNoHeaderImgFile_saveHeaderImgLink() {
        String savedHeaderImgPath = "saved_header_img";
        when(imageStorageServiceMock.downloadAndSaveImage(any(), any())).thenReturn(savedHeaderImgPath);

        Game game = getGame();
        String newHeaderImgLink = "new_header_img_link";
        game.setHeaderImg(newHeaderImgLink);
        gameImageService.handleImagesForAddGame(game, null, null);
        String actualHeaderImgPath = game.getHeaderImg();

        verify(imageStorageServiceMock).downloadAndSaveImage(eq(newHeaderImgLink), any());
        assertEquals(savedHeaderImgPath, actualHeaderImgPath);
    }

    @Test
    public void handleImagesForAddGame_invalidHeaderImgLinkAndNoHeaderImgFile_saveNullHeaderImg() {
        when(imageStorageServiceMock.downloadAndSaveImage(any(), any())).thenReturn(null);

        Game game = getGame();
        String newHeaderImgInvalidLink = "new_header_img_invalid_link";
        game.setHeaderImg(newHeaderImgInvalidLink);
        gameImageService.handleImagesForAddGame(game, null, null);

        verify(imageStorageServiceMock).downloadAndSaveImage(eq(newHeaderImgInvalidLink), any());
        assertNull(game.getHeaderImg());
    }

    @Test
    public void handleImagesForEditGame_validHeaderImgFile_shouldReplaceOldHeaderImgAndSaveNew() {
        String newHeaderImgPath = "new_saved_header_img";
        when(imageStorageServiceMock.validateMultipartFileImages(any(MultipartFile.class))).thenReturn(true);
        when(imageStorageServiceMock.saveImage(any(), any())).thenReturn(newHeaderImgPath);

        Game gameOg = getGame();
        gameOg.setHeaderImg("old_img_path");
        Game gameNew = getGame();
        gameNew.setHeaderImg(null);

        MultipartFile headerImgFile = getMultipartFileMock();
        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, headerImgFile);

        verify(imageStorageServiceMock).deleteFiles(Set.of("old_img_path"));
        verify(imageStorageServiceMock).saveImage(eq(headerImgFile), any());
        assertEquals(newHeaderImgPath, gameNew.getHeaderImg());
    }

    @Test
    public void handleImagesForEditGame_headerImgUnchanged_shouldKeepOldHeaderImg() {
        Game gameOg = getGame();
        String oldImg = "existing_img";
        gameOg.setHeaderImg(oldImg);

        Game gameNew = getGame();
        gameNew.setHeaderImg(oldImg);

        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, null);

        verify(imageStorageServiceMock, never()).deleteFiles(Set.of(oldImg));
        verify(imageStorageServiceMock, never()).saveImage(any(), any());
        assertEquals(oldImg, gameNew.getHeaderImg());
    }

    @Test
    public void handleImagesForEditGame_noNewHeaderImgFileAndNoNewHeaderImgLink_shouldDeleteOldHeaderImg() {
        Game gameOg = getGame();
        String oldImg = "old_img_path";
        gameOg.setHeaderImg(oldImg);

        Game gameNew = getGame();
        gameNew.setHeaderImg(null);

        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, null);

        verify(imageStorageServiceMock).deleteFiles(Set.of(oldImg));
        assertNull(gameNew.getHeaderImg());
    }

    @Test
    public void handleImagesForEditGame_validNewHeaderImgLink_shouldDownloadAndReplaceOldHeaderImg() {
        String newLink = "image.com/new";
        String savedPath = "saved_new_img";
        when(imageStorageServiceMock.downloadAndSaveImage(eq(newLink), any())).thenReturn(savedPath);

        Game gameOg = getGame();
        gameOg.setHeaderImg("old_local_img");

        Game gameNew = getGame();
        gameNew.setHeaderImg(newLink);

        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, null);

        verify(imageStorageServiceMock).deleteFiles(Set.of("old_local_img"));
        verify(imageStorageServiceMock).downloadAndSaveImage(eq(newLink), any());
        assertEquals(savedPath, gameNew.getHeaderImg());

    }

    @Test
    public void handleImagesForEditGame_invalidHeaderImgFile_shouldDeleteFolderContentAndThrowInvalidFileException() {
        when(imageStorageServiceMock.validateMultipartFileImages(any(MultipartFile.class))).thenReturn(false);

        Game gameOg = getGame();
        gameOg.setHeaderImg("old_img");
        Game gameNew = getGame();
        gameNew.setHeaderImg(null);

        MultipartFile invalidFile = getMultipartFileMock();

        assertThrows(InvalidFileException.class, () -> gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, invalidFile));

        verify(imageStorageServiceMock).deleteFolderContent(eq("1"), any());
    }

    @Test
    public void handleImagesForEditGame_screenshotsToDelete_shouldDeleteOnlyOldFiles() {
        Game gameOg = getGame();
        gameOg.setScreenshots(Set.of("local_screenshot_1", "local_screenshot_2"));

        Game gameNew = getGame();
        gameNew.setScreenshots(Set.of("local_screenshot_1")); // keep only 1

        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, null);

        verify(imageStorageServiceMock).deleteFiles(Set.of("local_screenshot_2"));
        assertEquals(Set.of("local_screenshot_1"), gameNew.getScreenshots());
    }

    @Test
    public void handleImagesForEditGame_screenshotsToKeep_shouldKeepOnlySpecifiedScreenshots() {
        Game gameOg = getGame();
        gameOg.setScreenshots(Set.of("img1", "img2", "img3"));

        Game gameNew = getGame();
        gameNew.setScreenshots(Set.of("img2", "img3"));

        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, null);

        assertTrue(gameNew.getScreenshots().contains("img2"));
        assertTrue(gameNew.getScreenshots().contains("img3"));
        assertFalse(gameNew.getScreenshots().contains("img1"));
    }

    @Test
    public void handleImagesForEditGame_newScreenshotLinks_shouldDownloadAndSaveThem() {
        Set<String> downloaded = Set.of("dl1", "dl2");
        when(imageStorageServiceMock.downloadAndSaveImages(any(), any())).thenReturn(downloaded);

        Game gameOg = getGame();
        gameOg.setScreenshots(Set.of("img1"));

        Game gameNew = getGame();
        gameNew.setScreenshots(Set.of("img1", "http_link_1", "http_link_2"));

        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, null);

        verify(imageStorageServiceMock).downloadAndSaveImages(eq(Set.of("http_link_1", "http_link_2")), any());
        assertTrue(gameNew.getScreenshots().containsAll(downloaded));
    }

    @Test
    public void handleImagesForEditGame_validScreenshotFiles_shouldSaveThem() {
        Set<String> saved = Set.of("upload1", "upload2");
        when(imageStorageServiceMock.validateMultipartFileImages(anyList())).thenReturn(true);
        when(imageStorageServiceMock.saveImages(anyList(), any())).thenReturn(saved);

        Game gameOg = getGame();
        Game gameNew = getGame();

        List<MultipartFile> files = List.of(getMultipartFileMock(), getMultipartFileMock());

        gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, files, null);

        verify(imageStorageServiceMock).saveImages(eq(files), any());
        assertTrue(gameNew.getScreenshots().containsAll(saved));
    }

    @Test
    public void handleImagesForEditGame_invalidScreenshotFiles_shouldDeleteFolderContentAndThrowInvalidFileException() {
        when(imageStorageServiceMock.validateMultipartFileImages(anyList())).thenThrow(new InvalidFileException(""));

        Game gameOg = getGame();
        gameOg.setScreenshots(Set.of("img1"));
        Game gameNew = getGame();
        gameNew.setScreenshots(Set.of("img1"));

        List<MultipartFile> files = List.of(getMultipartFileMock());

        assertThrows(InvalidFileException.class, () -> gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, files, null));
        verify(imageStorageServiceMock).deleteFolderContent(eq("1"), any());
    }

    @Test
    public void handleImagesForEditGame_onError_shouldDeleteFolderContentAndThrow() {
        when(imageStorageServiceMock.validateMultipartFileImages(any(MultipartFile.class))).thenThrow(new RuntimeException("Unexpected error"));

        Game gameOg = getGame();
        gameOg.setHeaderImg("old_img");
        Game gameNew = getGame();
        MultipartFile headerImgFile = getMultipartFileMock();

        RuntimeException ex = assertThrows(RuntimeException.class, () -> gameImageService.handleImagesForEditGame(gameNew.getId(), gameNew, gameOg, null, headerImgFile));
        assertEquals("Unexpected error", ex.getMessage());
        verify(imageStorageServiceMock).deleteFolderContent(eq(gameNew.getId().toString()), any());
    }

    private Game getGame() {
        Game game = new Game();
        game.setId(1L);
        game.setName("Game One");
        game.setReleaseDate(LocalDate.of(2020, 1, 15));
        game.setDescriptionShort("");
        game.setDescriptionLong("");
        game.setHeaderImg(null);
        game.setScreenshots(Set.of());
        game.setDevelopers(Set.of());
        game.setPublishers(Set.of());
        game.setTags(new HashSet<>());

        return game;
    }

    private MultipartFile getMultipartFileMock() {
        return new MockMultipartFile("file", "image.png", "image/png", new byte[]{1, 2, 3});
    }
}
