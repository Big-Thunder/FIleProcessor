package org.shubham.fileuploadstuff.services;

import jakarta.validation.constraints.NotNull;
import org.shubham.fileuploadstuff.Exceptions.StorageException;
import org.shubham.fileuploadstuff.Exceptions.StorageFileNotFoundException;
import org.shubham.fileuploadstuff.configurationProperties.StorageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.stream.Stream;

@Service
class FileSystemStorageService implements StorageService {
    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageException("You are trying to upload on empty location");
        }

        this.rootLocation = Paths.get(properties.getLocation()).toAbsolutePath().normalize();
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    @Override
    public String store(String directory, @NotNull MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new IOException("Failed to store empty file");
            }

            String subDir = rootLocation + File.separator + directory;

            Path directoryPath = Paths.get(subDir);
            if(!Files.exists(directoryPath)){
                Files.createDirectories(directoryPath);
            }

            String fileName = file.getOriginalFilename();
            String newFileName = LocalDateTime.now(ZoneId.systemDefault()).toString().replace(":", "-") + "-" + fileName;


            Path destinationFile = directoryPath.resolve(
                    Paths.get(Objects.requireNonNull(newFileName))).normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(directoryPath.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory");
            }
//
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return rootLocation.relativize(destinationFile).toString();
        } catch (IOException e) {
            throw new StorageException("Failed to store file", e);
        }
    }

    @Override
    public Stream<Path> loadAll(String directory) {
        try {
            String subDir = rootLocation + File.separator + directory;

            Path directoryPath = Paths.get(subDir);
            if(!Files.exists(directoryPath)){
                Files.createDirectories(directoryPath);
            }
            return Files.walk(directoryPath, 1).filter(path -> !path.equals(directoryPath)).map(directoryPath::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String storageKey) {
        Path file = rootLocation.resolve(storageKey).normalize();

        if (!file.startsWith(rootLocation)) {
            throw new StorageException("Cannot access file outside storage");
        }

        if (!Files.isRegularFile(file)) {
            throw new StorageFileNotFoundException("File not found");
        }

        return file;
    }

    @Override
    public Resource loadAsResource(String storageKey) {
        System.out.println("FileStorage:: loading file as resource" + storageKey);
        try {
            Path file = load(storageKey);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() && resource.isReadable()) {
                return resource;
            } else {
                System.out.println("HERE");
                throw new StorageFileNotFoundException("Could not read file ");
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + e);
        }
    }

    @Override
    public void deleteAll(String directory) {
        String subDir = rootLocation + File.separator + directory;
        Path directoryPath = Paths.get(subDir);

        if(!Files.exists(directoryPath)){
            try {
                Files.createDirectories(directoryPath);
            } catch (IOException e) {
                throw new StorageException("couldnt delete", e);
            }
        }
        FileSystemUtils.deleteRecursively(directoryPath.toFile());
    }

    @Override
    public void delete(String storageKey){
        try {
            Files.delete(load(storageKey));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}