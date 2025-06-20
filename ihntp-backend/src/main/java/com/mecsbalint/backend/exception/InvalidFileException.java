package com.mecsbalint.backend.exception;

import org.apache.commons.imaging.ImagingException;

import java.io.UncheckedIOException;

public class InvalidFileException extends RuntimeException {
    public InvalidFileException(String requiredFileType) {
        super(String.format("The file isn't the required file type: %s", requiredFileType));
    }
}
