package com.capstone.backend.service;

import com.capstone.backend.entity.User;
import com.capstone.backend.model.dto.resource.FileDTOResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface FileService {
    public List<FileDTOResponse> uploadMultiFile(MultipartFile[] files, Long lessonId);

    public Resource downloadFile(String fileName);

    public String setAvatar(MultipartFile avatar, User userLoggedIn);
    public FileDTOResponse uploadSingleFile(MultipartFile multipartFile, String fileName, Long lessonId);

    public Boolean deleteFileResource(String filename);
}
