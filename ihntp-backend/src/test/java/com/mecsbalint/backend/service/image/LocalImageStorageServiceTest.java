package com.mecsbalint.backend.service.image;

import com.mecsbalint.backend.utility.Fetcher;
import com.mecsbalint.backend.utility.UUIDProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocalImageStorageServiceTest {

    @Mock
    private Fetcher fetcherMock;

    @Mock
    private Logger loggerMock;

    @Mock
    private UUIDProvider uuidProviderMock;

    private LocalImageStorageService localImageStorageService;

    @BeforeEach
    public void setUp() {
        localImageStorageService = new LocalImageStorageService("uploadDir", fetcherMock, loggerMock, uuidProviderMock);
    }

    @Test
    public void downloadAndSaveImages_getThreeLinks_callDownloadAndSaveImageThreeTimes() {
        when(fetcherMock.fetchContentType(any())).thenReturn(null);

        localImageStorageService.downloadAndSaveImages(Set.of("link1", "link2", "link3"), "savePath");

        verify(fetcherMock).fetchContentType("link1");
        verify(fetcherMock).fetchContentType("link2");
        verify(fetcherMock).fetchContentType("link3");
    }

    @Test
    public void downloadAndSaveImage_fetchSuccessfulAndImageSavingSuccessful_saveImageAndReturnPath() {
        byte[] bytes = new byte[]{1, 2, 3};
        UUID generatedUUID = UUID.randomUUID();

        when(fetcherMock.fetchContentType(any())).thenReturn("image/png");
        when(fetcherMock.fetch(any(), any())).thenReturn(bytes);
        when(uuidProviderMock.getRandomUUID()).thenReturn(generatedUUID);

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.createDirectories(any())).thenReturn(null);
            mockedFiles.when(() -> Files.write(any(), any(byte[].class))).thenReturn(null);

            String expectedResult = "/api/images/" + "folder_name/" + generatedUUID + ".png";
            String actualResult = localImageStorageService.downloadAndSaveImage("image_link", "folder_name");

            assertEquals(expectedResult, actualResult);
            mockedFiles.verify(() -> Files.write(any(), eq(bytes)));
        }
    }

    @Test
    public void downloadAndSaveImage_fetchSuccessfulAndImageSavingUnsuccessful_throwUncheckedIOException() {
        when(fetcherMock.fetchContentType(any())).thenReturn("image/png");
        when(fetcherMock.fetch(any(), any())).thenReturn(new byte[]{1, 2, 3});

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.write(any(), any(byte[].class))).thenThrow(new IOException());

            assertThrows(UncheckedIOException.class, () -> localImageStorageService.downloadAndSaveImage("image_link", "folder_name"));
        }
    }

    @Test
    public void downloadAndSaveImage_fetchUnsuccessful_returnNull() {
        when(fetcherMock.fetchContentType(any())).thenReturn(null);

        String actualResult = localImageStorageService.downloadAndSaveImage("image_link", "folder_name");

        assertNull(actualResult);
    }

    @Test
    public void downloadAndSaveImage_fetchSuccessfulAndFileIsNotInSupportedFormat_returnNull() {
        when(fetcherMock.fetchContentType(any())).thenReturn("file/pdf");

        String actualResult = localImageStorageService.downloadAndSaveImage("image_link", "folder_name");

        assertNull(actualResult);
    }

    @Test
    public void deleteFolderContent_unsuccessfulDeletion_returnFalse() {
        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.walk(any())).thenThrow(new IOException());

            boolean actualResult = localImageStorageService.deleteFolderContent("folder_name", Set.of());

            assertFalse(actualResult);
        }
    }

    @Test
    public void deleteFolderContent_successfulDeletion_returnTrue() {
        Stream<Path> pathStream = Stream.of(Path.of("path_one"), Path.of("path_two"));

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.walk(any())).thenReturn(pathStream);

            boolean actualResult = localImageStorageService.deleteFolderContent("folder_name", Set.of());

            assertTrue(actualResult);
        }
    }

    @Test
    public void deleteFiles_successfulDeletion_filesDeleted() {
        String path1 = "path_one";
        String path2 = "path_two";

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            localImageStorageService.deleteFiles(Set.of(path1, path2));

            mockedFiles.verify(() -> Files.deleteIfExists(Paths.get("uploadDir", path1)));
            mockedFiles.verify(() -> Files.deleteIfExists(Paths.get("uploadDir", path2)));
        }
    }

    @Test
    public void deleteFiles_errorDuringDeletion_throwUncheckedIOException() {
        String path1 = "path_one";

        try(MockedStatic<Files> mockedFiles = mockStatic(Files.class)) {
            mockedFiles.when(() -> Files.deleteIfExists(any())).thenThrow(new IOException());

            assertThrows(UncheckedIOException.class, () -> localImageStorageService.deleteFiles(Set.of(path1)));
        }
    }

    @Test
    public void validateMultipartFileImages_validExtension_returnTrue() {
        MultipartFile multipartFileMock = new MockMultipartFile("file", "image.png", "image/png", new byte[]{1, 2, 3});

        boolean actualResult = localImageStorageService.validateMultipartFileImages(multipartFileMock);

        assertTrue(actualResult);
    }

    @Test
    public void validateMultipartFileImages_invalidExtension_returnFalse() {
        MultipartFile multipartFileMock = new MockMultipartFile("file", "file.pdf", "file/pdf", new byte[]{1, 2, 3});

        boolean actualResult = localImageStorageService.validateMultipartFileImages(multipartFileMock);

        assertFalse(actualResult);
    }

    @Test
    public void validateMultipartFileImages_noFileName_returnFalse() {
        MultipartFile multipartFileMock = new MockMultipartFile("file", null, "file/pdf", new byte[]{1, 2, 3});

        boolean actualResult = localImageStorageService.validateMultipartFileImages(multipartFileMock);

        assertFalse(actualResult);
    }
}