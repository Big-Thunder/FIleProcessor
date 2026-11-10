package org.shubham.fileuploadstuff.services;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public interface StorageService {
    void init();

    String store(String directory, MultipartFile file);

    Stream<Path> loadAll(String directory);

    Path load(String storageKey);

    Resource loadAsResource(String storageKey);

    void deleteAll(String directory);

    void delete(String storageKey);
}
