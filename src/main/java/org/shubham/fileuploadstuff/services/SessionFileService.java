package org.shubham.fileuploadstuff.services;

import org.shubham.fileuploadstuff.Exceptions.StorageFileNotFoundException;
import org.shubham.fileuploadstuff.entities.FileEntity;
import org.shubham.fileuploadstuff.repositories.FileRepository;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
public class SessionFileService {
    public final StorageService storageService;
    public final FileRepository fileRepository;

    public SessionFileService(StorageService storageService, FileRepository fileRepository){
        this.storageService = storageService;
        this.fileRepository = fileRepository;
    }

    public void store(String sessionId, MultipartFile file){
        String storagePath = storageService.store(sessionId, file).toString();
        fileRepository.save(new FileEntity(file.getOriginalFilename(), storagePath, sessionId, LocalDateTime.now()));
    }

    public Stream<FileEntity> loadAll(String sessionId){
        List<FileEntity> fileEntityList = fileRepository.findFileEntitiesByOwnerId(sessionId);
        return fileEntityList.stream();
    }

    public FileEntity load(String sessionId, Long fileId) {
        FileEntity fileEntity = fileRepository.findFileEntityByFileIdAndOwnerId(fileId, sessionId);

        if (fileEntity == null) {
            throw new StorageFileNotFoundException("Requested file not found");
        }

        return fileEntity;
    }

    public Resource loadAsResource(String sessionId, Long fileId){
        FileEntity fileEntity = load(sessionId, fileId);
        return storageService.loadAsResource(fileEntity.getFile_storage_path());
    }

    public void deleteAll(String sessionId){
        storageService.deleteAll(sessionId);
        fileRepository.deleteAllByOwnerId(sessionId);
    }

    public void delete(String sessionId, Long fileId){
        FileEntity fileEntity = load(sessionId, fileId);

        storageService.delete(fileEntity.getFile_storage_path());
        fileRepository.deleteById(fileId);
    }
}
