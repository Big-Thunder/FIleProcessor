package org.shubham.fileuploadstuff;

import org.shubham.fileuploadstuff.configurationProperties.StorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(StorageProperties.class)
public class FileUploadStuffApplication {

    public static void main(String[] args) {
        SpringApplication.run(FileUploadStuffApplication.class, args);
    }

}
