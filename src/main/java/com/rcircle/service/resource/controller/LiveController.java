package com.rcircle.service.resource.controller;

import com.rcircle.service.resource.utils.NetFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/live")
public class LiveController {
    @Value("${live.path}")
    private String livepath;
    @GetMapping("/{name}")
    public ResponseEntity getVideoFile(Principal principal, @PathVariable("name") String name) {
        String errinfo = "";
        try {
            return createResponseEntity("application/x-mpegURL", NetFile.getDirAbsolutePath(livepath, name));
        } catch (Exception e) {
            errinfo = e.getMessage();
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    @GetMapping("/{resolution}/{name}")
    public ResponseEntity getVideoFile(Principal principal, @PathVariable("resolution") String resolution, @PathVariable("name") String name) {
        String errinfo = "";
        try {
            return createResponseEntity("application/x-mpegURL", NetFile.getDirAbsolutePath(livepath, resolution, name));
        } catch (Exception e) {
            errinfo = e.getMessage();
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    private ResponseEntity createResponseEntity(String type, String filePath) throws IOException {
        System.out.println(filePath);
        MediaType mediaType = MediaType.parseMediaType(type);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        File file = new File(filePath);
        FileInputStream inputStream = new FileInputStream(file);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        inputStream.close();
        return new ResponseEntity(bytes, headers, HttpStatus.OK);
    }
}
