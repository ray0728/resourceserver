package com.rcircle.service.resource.controller;

import com.rcircle.service.resource.model.Log;
import com.rcircle.service.resource.utils.NetFile;
import com.rcircle.service.resource.utils.ResultInfo;
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
import java.util.Map;

@RestController
@RequestMapping("/lab")
public class LiveController {
    @GetMapping("live/{name}")
    public ResponseEntity getVideoFile(Principal principal, @PathVariable("lid") int logid, @PathVariable("name") String name) {
        Log log = resourceService.getLog(logid);
        String errinfo = verifyAccount(principal, log, ResultInfo.CODE_GET_RES_FILES, true);
        if (errinfo == null) {
            try {
                for (Map.Entry<String, String> entry : log.getDetail().getFiles().entrySet()) {
                    if (entry.getKey().equals(name) && entry.getValue().contains(File.separatorChar + "video" + File.separatorChar)) {
                        return createResponseEntity("application/x-mpegURL", NetFile.translateLocalVideoFileToHlsFile(entry.getValue()));
                    }
                }
            } catch (Exception e) {
                errinfo = e.getMessage();
            }
        }
        return ResponseEntity.status(404).body(errinfo);
    }

    private ResponseEntity createResponseEntity(String type, String filePath) throws IOException {
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
