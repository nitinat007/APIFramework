package com.progathon.framework.exceptions;

/**
 * Author: nitinkumar
 * Created Date: 18/11/19
 * Info: This exception is thrown when path to a particular folder does not exist
 **/

public class FolderDoesNotExistException extends Exception {
    public FolderDoesNotExistException(String message) {
        super(message);
    }
}
