package org.nitincompany.framework.exceptions;

public class FileNotFoundException extends Exception {

    public FileNotFoundException() {
        super("File not found on the mentioned path");
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
