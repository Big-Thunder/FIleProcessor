package org.shubham.fileuploadstuff;

import org.shubham.fileuploadstuff.configurationProperties.StorageProperties;
import org.shubham.fileuploadstuff.entities.FileEntity;
import org.shubham.fileuploadstuff.repositories.FileRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileUploadStuffApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(FileUploadStuffApplication.class, args);
    }

}
