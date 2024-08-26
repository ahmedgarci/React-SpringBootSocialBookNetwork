package com.example.demo.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.Book.Book;

import lombok.NoArgsConstructor;
import lombok.NonNull;

@Service
@NoArgsConstructor
public class FileStorageService {

    public String saveFile(@NonNull MultipartFile sourceFile, @NonNull Book book, @NonNull Integer id) {
        final String fileUploadSubPath = "users" + File.separator + id;
        return uploadFile(sourceFile, fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile, @NonNull String fileUploadSubPath) {
        final String finalUploadPath = File.separator + fileUploadSubPath;
        File targetFolder = new File(finalUploadPath);
        if (!targetFolder.exists()) {
            boolean folderCreated = targetFolder.mkdir();
            if(!folderCreated){
                System.out.println("cannot create the folder ");
            }
        }
        final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
        String targetFilePath = finalUploadPath + File.separator + System.currentTimeMillis()+ "."+ fileExtension;
        Path targetPath = Paths.get(targetFilePath);
        try{
            Files.write(targetPath, sourceFile.getBytes());
            return targetFilePath;
        }catch(IOException e){
            System.out.println("file not saved");
        }
        return null;
    }

    private String getFileExtension(String fileName) {
        if(fileName.isEmpty() || fileName== null){
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if(lastDotIndex == -1){
            return "";
        }
        return fileName.substring(lastDotIndex+1).toLowerCase();
    }
}
