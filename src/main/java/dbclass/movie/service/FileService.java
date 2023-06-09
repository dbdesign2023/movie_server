package dbclass.movie.service;

import dbclass.movie.dto.ImageDTO;
import dbclass.movie.exceptionHandler.ServerException;
import dbclass.movie.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class FileService {

    private final ImageRepository imageRepository;

    @Transactional
    public ImageDTO createImageDTO(String originalSourceName, Path path) {
        String fileName = originalSourceName.substring(originalSourceName.lastIndexOf("\\") + 1);

        String uuid = UUID.randomUUID().toString();
        String fileUrl = getDirectory(path) + File.separator + uuid + "_" + fileName;
        return ImageDTO.builder()
                .fileName(uuid + "_" + fileName)
                .uuid(uuid)
                .fileUrl(fileUrl)
                .build();

    }

    private String getDirectory(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectory(path);
            }
            return path.toString();
        }
        catch (IOException e) {
            throw new ServerException("파일 저장 경로를 찾을 수 없습니다." + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public boolean hasImage(String path) {
        return imageRepository.existsByFileUrl(path);
    }
}