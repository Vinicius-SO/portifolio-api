package com.viniciusSo.portifolio.service;


import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService {

    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private GridFSBucket gridFSBucket;

    // Método para salvar um arquivo
    public String saveFile(MultipartFile file) throws IOException {
        ObjectId id = gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        return id.toString(); // Retorna o ID do arquivo salvo
    }


    public byte[] getFile(String fileId) {
        if (fileId == null) {
            return null;
        }
        GridFSDownloadStream downloadStream = gridFSBucket.openDownloadStream(new ObjectId(fileId));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = downloadStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        return outputStream.toByteArray();
    }

    public InputStream getFileStream(String fileId) {
        if (fileId == null) {
            return null;
        }
        return gridFSBucket.openDownloadStream(new ObjectId(fileId));
    }

    public String saveFile(InputStream fileStream, String fileName, String contentType) {
        GridFSUploadOptions options = new GridFSUploadOptions().metadata(new org.bson.Document("contentType", contentType));
        ObjectId fileId = gridFSBucket.uploadFromStream(fileName, fileStream, options);
        return fileId.toHexString();  // Retorna o ID do arquivo salvo
    }
}
