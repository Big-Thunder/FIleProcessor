package org.shubham.fileuploadstuff;

public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable throwableCause){
      super(message, throwableCause);
    }
}
