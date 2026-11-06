package org.shubham.fileuploadstuff.entities;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "fileownertable")
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long fileId;

    @Column(name = "file_name")
    private String fileName;


    @Column(name = "file_storage_path")
    private String fileStoragePath;

    @Column(name = "owner_id")
    private String ownerId;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedId;

    public FileEntity(String file_name, String file_storage_path, String owner_id, LocalDateTime uploaded_id) {
        this.fileName = file_name;
        this.fileStoragePath = file_storage_path;
        this.ownerId = owner_id;
        this.uploadedId = uploaded_id;
    }

    public FileEntity() {

    }

    public Long getFile_id() {
        return fileId;
    }

    public void setFile_id(Long file_id) {
        this.fileId = file_id;
    }

    public String getFile_name() {
        return fileName;
    }

    public void setFile_name(String file_name) {
        this.fileName = file_name;
    }

    public String getFile_storage_path() {
        return fileStoragePath;
    }

    public void setFile_storage_path(String file_storage_path) {
        this.fileStoragePath = file_storage_path;
    }

    public String getOwner_id() {
        return ownerId;
    }

    public void setOwner_id(String owner_id) {
        this.ownerId = owner_id;
    }

    public LocalDateTime getUploadedId() {
        return uploadedId;
    }

    public void setUploadedId(LocalDateTime uploadedId) {
        this.uploadedId = uploadedId;
    }
}
