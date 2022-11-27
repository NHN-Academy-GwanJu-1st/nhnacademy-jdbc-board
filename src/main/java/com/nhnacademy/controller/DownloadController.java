package com.nhnacademy.controller;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class DownloadController {

    private static String FIlE_PATH = "/Users/gwanii/Downloads/";

    @GetMapping("/download")
    public ResponseEntity<Resource> download(@RequestParam("filename") String filename) throws IOException {

        Path filePath = Paths.get(FIlE_PATH + filename);

        if (!filePath.toFile().isFile()) {
            throw new FileNotFoundException();
        }

        String mimeType = Files.probeContentType(filePath);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentDisposition(ContentDisposition.builder("attachment")
                .filename(filename, StandardCharsets.UTF_8)
                .build());
        httpHeaders.add(HttpHeaders.CONTENT_TYPE, mimeType);
        Resource resource = new InputStreamResource(Files.newInputStream(filePath));

        return new ResponseEntity<>(resource, httpHeaders, HttpStatus.OK);
    }

}
