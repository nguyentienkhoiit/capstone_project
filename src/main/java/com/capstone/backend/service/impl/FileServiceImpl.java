package com.capstone.backend.service.impl;

import com.capstone.backend.entity.User;
import com.capstone.backend.entity.type.ResourceType;
import com.capstone.backend.exception.ApiException;
import com.capstone.backend.model.dto.resource.FileDTOResponse;
import com.capstone.backend.service.FileService;
import com.capstone.backend.utils.ConvertResourceToImage;
import com.capstone.backend.utils.DataHelper;
import com.capstone.backend.utils.MessageException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileServiceImpl implements FileService {
    final MessageException messageException;
    final ConvertResourceToImage convertResourceToImage;
    @Value("${application.file.upload.root-path}")
    String rootPath;
    Path root;

    public void init() {
        try {
            root = Paths.get(rootPath);
            if (Files.notExists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw ApiException.internalServerException(messageException.MSG_FOLDER_CREATE_ERROR);
        }
    }

    private String getThumbnailDocument(String path, String fileNameExtension) {
        String pdf = List.of(ResourceType.PDF).toString().toLowerCase();
        String slide = List.of(ResourceType.PPTX, ResourceType.PPT).toString().toLowerCase();
        String doc = List.of(ResourceType.DOC, ResourceType.DOCX).toString().toLowerCase();

        if (pdf.contains(fileNameExtension)) {
            return convertResourceToImage.ConvertFirstPagePdfToImage(path, root);
        } else if (slide.contains(fileNameExtension)) {
            return convertResourceToImage.ConvertFirstPageSlideToImage(path, root);
        } else if (doc.contains(fileNameExtension)) {
            return convertResourceToImage.ConvertFirstPageDocToImage(path, root);
        }
        return null;
    }

    @Override
    public FileDTOResponse uploadSingleFile(
            MultipartFile multipartFile,
            String fileName,
            Long lessonId
    ) {
        init();
        String messageError = messageException.MSG_FILE_SAVE_ERROR;
        try {
            //get file name extension defined
            String fileNameExtensionList = Arrays.toString(ResourceType.values()).toLowerCase();

            //get file name extension from file
            String fileNameExtension = DataHelper.extractFileExtension(multipartFile.getOriginalFilename());

            if (lessonId == null && ResourceType.getResourceByLesson().toString().contains(fileNameExtension.toUpperCase())) {
                messageError = messageException.MSG_FILE_TYPE_INVALID;
                throw ApiException.badRequestException(messageException.MSG_FILE_TYPE_INVALID);
            }

            //only file name extension defined before
            if (!fileNameExtensionList.contains(fileNameExtension)) {
                messageError = messageException.MSG_FILE_TYPE_INVALID;
                throw ApiException.badRequestException(messageException
                        .MSG_FILE_TYPE_INVALID
                        .concat(" ")
                        .concat(fileNameExtensionList));
            }

            //concat new filename and extension
            fileName = fileName.concat(".").concat(fileNameExtension);

            //save file name to folder
            FileCopyUtils.copy(
                    multipartFile.getBytes(),
                    new File(root.resolve(fileName).toString())
            );

            //create path name of resource
            String path = String.valueOf(root.resolve(fileName));

            //create path name thumbnail of resource
            String thumbnailSrc = fileName;

            if (ResourceType.getFeeList().toString().contains(fileNameExtension.toUpperCase())) {
                thumbnailSrc = getThumbnailDocument(path, fileNameExtension);
            } else if (fileNameExtension.equalsIgnoreCase(ResourceType.MP3.toString())) {
                thumbnailSrc = "thumbnail_mp3.jpg";
            } else if (fileNameExtension.equalsIgnoreCase(ResourceType.MP4.toString())) {
                thumbnailSrc = "thumbnail_mp4.png";
            }
            return FileDTOResponse.builder()
                    .size(multipartFile.getSize())
                    .resourceSrc(fileName)
                    .resourceType(ResourceType.valueOf(fileNameExtension.toUpperCase()))
                    .thumbnailSrc(thumbnailSrc)
                    .fileExtension(fileNameExtension)
                    .build();
        } catch (Exception e) {
            throw ApiException.internalServerException(messageException.MSG_FILE_SAVE_ERROR);
        }
    }

    @Override
    public Boolean deleteFileResource(String filename) {
        init();
        try {
            Path file = root.resolve(filename);
            Files.delete(file);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public List<FileDTOResponse> uploadMultiFile(MultipartFile[] files, Long lessonId) {
        List<FileDTOResponse> fileDTOResponseList = new ArrayList<>();
        try {
            Arrays.asList(files).forEach(file -> {
                String uuid = UUID.randomUUID().toString();
                FileDTOResponse fileDTOResponse = uploadSingleFile(file, uuid, lessonId);
                fileDTOResponseList.add(fileDTOResponse);
            });
            return fileDTOResponseList;
        } catch (Exception e) {
            throw ApiException.internalServerException(messageException.MSG_FILE_SAVE_ERROR);
        }
    }

    @Override
    public Resource downloadFile(String fileName) {
        init();
        try {
            Path file = root.resolve(fileName);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
        } catch (MalformedURLException e) {
            throw ApiException.internalServerException(messageException.MSG_FILE_DOWNLOAD_ERROR);
        }
        return null;
    }

    @Override
    public String setAvatar(MultipartFile multipartFile, User userLoggedIn) {
        init();
        try {
            //get file name extension defined
            String fileNameExtensionList = ResourceType.getListImages().toString().toLowerCase();

            //get file name extension from file
            String fileNameExtension = DataHelper.extractFileExtension(multipartFile.getOriginalFilename());

            //only file name extension defined before
            if (!fileNameExtensionList.contains(fileNameExtension)) {
                throw ApiException.badRequestException(messageException.MSG_FILE_TYPE_INVALID);
            }

            //concat new filename and extension
            String fileName = userLoggedIn.getUsername().concat(".").concat(fileNameExtension);
            System.out.println(fileName);
            //save file name to folder
            FileCopyUtils.copy(
                    multipartFile.getBytes(),
                    new File(root.resolve(fileName).toString())
            );

            return fileName;
        } catch (Exception e) {
            throw ApiException.internalServerException(messageException.MSG_FILE_SAVE_ERROR);
        }
    }
}
