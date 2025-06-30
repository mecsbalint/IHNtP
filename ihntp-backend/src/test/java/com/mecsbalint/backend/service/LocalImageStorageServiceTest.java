package com.mecsbalint.backend.service;

import com.mecsbalint.backend.service.image.LocalImageStorageService;
import com.mecsbalint.backend.utility.Fetcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class LocalImageStorageServiceTest {

    @Mock
    private UUID uuidMock;

    @Mock
    private Fetcher fetcherMock;

    @Mock
    private Logger loggerMock;

    private LocalImageStorageService imageStorageService;

    @BeforeEach
    public void setup() {
        imageStorageService = new LocalImageStorageService("uploadDirectory", uuidMock, fetcherMock, loggerMock);
    }

    private MultipartFile getMultipartFileMock() {
        return new MockMultipartFile("file", "image.png", "image/png", new byte[]{1, 2, 3});
    }
}