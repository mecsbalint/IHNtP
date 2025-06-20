package com.mecsbalint.backend.service;

import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.ImagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class ImageStorageServiceTest {

    private final ImageStorageService imageStorageService = new ImageStorageService("uploadDirectory");

    @Test
    public void validateImages_allImagesValid_returnTrue() {
        try (MockedStatic<Imaging> mockedImaging = mockStatic(Imaging.class)){
            mockedImaging.when(() -> Imaging.getImageInfo((byte[]) any())).thenReturn(null);

            boolean expectedResult = true;
            boolean actualResult = imageStorageService.validateImages(List.of(getMultipartFileMock()));

            assertEquals(expectedResult, actualResult);

        }
    }

    @Test
    public void validateImages_atLeastOneInvalidImage_returnFalse() {
        try (MockedStatic<Imaging> mockedImaging = mockStatic(Imaging.class)){
            mockedImaging.when(() -> Imaging.getImageInfo((byte[]) any())).thenThrow(ImagingException.class);

            boolean expectedResult = false;
            boolean actualResult = imageStorageService.validateImages(List.of(getMultipartFileMock()));

            assertEquals(expectedResult, actualResult);
        }
    }

    @Test
    public void validateImages_fileCannotRead_throwUncheckedIOException() {
        try (MockedStatic<Imaging> mockedImaging = mockStatic(Imaging.class)){
            mockedImaging.when(() -> Imaging.getImageInfo((byte[]) any())).thenThrow(IOException.class);

            assertThrows(UncheckedIOException.class, () -> imageStorageService.validateImages(List.of(getMultipartFileMock())));
        }
    }

    private MultipartFile getMultipartFileMock() {
        return new MockMultipartFile("file", "image.png", "image/png", new byte[]{1, 2, 3});
    }
}