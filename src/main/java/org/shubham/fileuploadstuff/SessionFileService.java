package org.shubham.fileuploadstuff;

import org.shubham.fileuploadstuff.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class SessionFileService {
    public final StorageService storageService;

    public SessionFileService(StorageService storageService){
        this.storageService = storageService;
    }

    public void store(String sessionId, MultipartFile file){
        storageService.store(sessionId, file);
    }

    public Stream<Path> loadAll(String sessionId){
        return storageService.loadAll(sessionId);
    }

    public Path load(String sessionId, String fileName){
        return storageService.load(sessionId, fileName);
    }

    public Resource loadAsResource(String sessionId, String fileName){
        return storageService.loadAsResource(sessionId, fileName);
    }

    public void deleteAll(String sessionId){
        storageService.deleteAll(sessionId);
    }

    public void delete(String sessionId, String fileName){
        storageService.delete(sessionId, fileName);
    }
}
