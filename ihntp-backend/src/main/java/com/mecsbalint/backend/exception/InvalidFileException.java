package com.mecsbalint.backend.exception;

import org.apache.commons.imaging.ImagingException;

import java.io.UncheckedIOException;

public class InvalidFileException extends UncheckedIOException {
    public InvalidFileException(String filename, String requiredFileType, ImagingException e) {
        super(String.format("%s file isn't the required file type: %s", filename, requiredFileType), e);
    }
}
