package dbclass.movie.controller;

import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.exceptionHandler.ServerException;
import dbclass.movie.service.MovieService;
import lombok.RequiredArgsConstructor;
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
public class ApiController {

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    private final MovieService movieService;

    @GetMapping("/posters")
    public ResponseEntity<Resource> getPoster(@RequestParam("fileName") String fileName) {

        String filePath = Paths.get(uploadPath, "poster") + File.separator + fileName;


        org.springframework.core.io.Resource resource = new FileSystemResource(filePath);


        if(!movieService.hasPoster(filePath) || !resource.exists()) {
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
}
