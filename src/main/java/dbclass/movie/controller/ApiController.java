package dbclass.movie.controller;

import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.exceptionHandler.ServerException;
import dbclass.movie.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Log4j2
public class ApiController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final FileService fileService;

    @GetMapping("/posters")
    public ResponseEntity<Resource> getPoster(@RequestParam("fileName") String fileName) {

        log.info("poster load request : " + fileName);

        String filePath = Paths.get(uploadPath, "poster") + File.separator + fileName;


        org.springframework.core.io.Resource resource = new FileSystemResource(filePath);


        if(!fileService.hasImage(filePath) || !resource.exists()) {
            throw new DataNotExistsException("존재하지 않는 포스터명입니다.", "POSTER");
        }

        HttpHeaders headers = new HttpHeaders();
        Path path = Paths.get(filePath);

        try {
            headers.add("Content-Type", Files.probeContentType(path));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new ServerException("서버 파일 접근 오류");
        }

    }

    @GetMapping("/profileImage")
    public ResponseEntity<Resource> getProfileImage(@RequestParam("fileName") String fileName) {
        log.info("profile load request : " + fileName);

        String filePath = Paths.get(uploadPath, "cast") + File.separator + fileName;


        org.springframework.core.io.Resource resource = new FileSystemResource(filePath);


        if(!fileService.hasImage(filePath) || !resource.exists()) {
            throw new DataNotExistsException("존재하지 않는 프로필사진입니다.", "PROFILE IMAGE");
        }

        HttpHeaders headers = new HttpHeaders();
        Path path = Paths.get(filePath);

        try {
            headers.add("Content-Type", Files.probeContentType(path));
            return new ResponseEntity<>(resource, headers, HttpStatus.OK);
        } catch (IOException e) {
            throw new ServerException("서버 파일 접근 오류");
        }

    }
}
