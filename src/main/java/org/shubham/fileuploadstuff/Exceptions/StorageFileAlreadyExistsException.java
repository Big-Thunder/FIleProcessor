package org.shubham.fileuploadstuff.Exceptions;

public class StorageFileAlreadyExistsException extends StorageException{
    public StorageFileAlreadyExistsException(String message) {
        super(message);
    }

    public StorageFileAlreadyExistsException(String message, Throwable throwableCause){
        super(message, throwableCause);
    }
}
