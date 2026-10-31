package org.shubham.fileuploadstuff.configurationProperties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
    private String location = "D:\\RandomBS\\SpringBootStuff\\FileProcessor\\upload-dir\\";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
