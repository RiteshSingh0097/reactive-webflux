package com.ritesh.reactiveprogramming.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.nio.file.Files;

@RestController
public class DemoController {

    @GetMapping("/getpdf")
    public ResponseEntity<byte[]> getPDF() {
        var file = new File("/home/ritesh/Documents/Resume-Ritesh-Singh.pdf"); // change to relative path
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        var filename = "output.pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = null;
        try {
            response = new org.springframework.http.ResponseEntity<>(Files.readAllBytes(file.toPath()), headers, org.springframework.http.HttpStatus.OK);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return response;
    }
}
