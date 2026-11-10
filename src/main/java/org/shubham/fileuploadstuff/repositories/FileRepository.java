package org.shubham.fileuploadstuff.repositories;

import org.shubham.fileuploadstuff.entities.FileEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FileRepository extends CrudRepository<FileEntity, Long> {
    FileEntity findFileEntitiesByFileName(String fileName);
    List<FileEntity> findFileEntitiesByOwnerId(String ownerId);
    FileEntity findFileEntityByFileIdAndOwnerId(Long fileId, String ownerId);
    void deleteAllByOwnerId(String ownerId);
}
