package com.zavan.dedesite.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api/uploads")
public class UploadController {

    @Value("${app.upload.dir:/var/www/uploads}")
    private String uploadDir;

    @PostMapping("/image")
    @PreAuthorize("hasRole('ADMIN') or hasRole('AUTHOR')")
    public Map<String, String> upload(@RequestParam("file") MultipartFile file) throws IOException {
        // validações básicas
        if (file.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Arquivo vazio");
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/"))
          throw new ResponseStatusException(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Somente imagens");

        // cria diretório
        Path dir = Paths.get(uploadDir, "blog");
        Files.createDirectories(dir);

        // nome seguro
        String ext = Optional.ofNullable(file.getOriginalFilename())
            .filter(n -> n.contains("."))
            .map(n -> n.substring(n.lastIndexOf('.')))
            .orElse(".bin");
        String filename = UUID.randomUUID() + ext.toLowerCase();

        // salva
        Path dest = dir.resolve(filename);
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        // retorna URL pública
        return Map.of("url", "/uploads/blog/" + filename);
    }
}
