package dbclass.movie.service;

import dbclass.movie.domain.Code;
import dbclass.movie.domain.Image;
import dbclass.movie.domain.movie.*;
import dbclass.movie.dto.ImageDTO;
import dbclass.movie.dto.movie.*;
import dbclass.movie.exceptionHandler.DataExistsException;
import dbclass.movie.exceptionHandler.DataNotExistsException;
import dbclass.movie.exceptionHandler.ServerException;
import dbclass.movie.mapper.MovieMapper;
import dbclass.movie.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieService {

    private final FileService fileService;
    private final MovieRepository movieRepository;
    private final ImageRepository imageRepository;
    private final CastRepository castRepository;
    private final RoleRepository roleRepository;
    private final GenreRegisterRepository genreRegisterRepository;
    private final CodeRepository codeRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Transactional
    public MovieDTO register(MovieRegisterDTO registerDTO) {
        log.debug("movie register start");
        Cast director = castRepository.findById(registerDTO.getDirectorId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 감독 ID입니다.", "cast"));
        Code rating = codeRepository.findById(registerDTO.getRatingCode()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 등급입니다.", "rating"));
        Image poster = createImage(registerDTO.getPoster(), "poster");

        Movie movie = MovieMapper.movieRegisterDTOToMovie(registerDTO, poster, director, rating);
        Movie movieSaved = movieRepository.save(movie);

        List<RoleAddDTO> rolesToAddDTO = registerDTO.getCastRoles();
        if(rolesToAddDTO != null) {
            ListIterator list = rolesToAddDTO.listIterator();
            while (list.hasNext()) {
                addRole(movieSaved.getMovieId(), (RoleAddDTO) list.next());
            }
        }

        if(registerDTO.getGenres() != null) {
            registerDTO.getGenres().stream().forEach(genreDTO -> genreRegisterRepository.save(GenreRegister.builder().movie(movieSaved).genre(MovieMapper.genreDTOToCode(genreDTO, codeRepository.findGenreUpperCode())).build()));
        }

        return MovieMapper.movieToMovieDTO(movie);
    }

    @Transactional
    private Image createImage(MultipartFile file, String target) {
        String originalName = file.getOriginalFilename();
        Path root = Paths.get(uploadPath, target);

        try {
            ImageDTO imageDTO =  fileService.createImageDTO(originalName, root);
            Image poster = Image.builder()
                    .uuid(imageDTO.getUuid())
                    .fileName(imageDTO.getFileName())
                    .fileUrl(imageDTO.getFileUrl())
                    .build();

            file.transferTo(Paths.get(imageDTO.getFileUrl()));

            return imageRepository.save(poster);
        } catch (IOException e) {
            throw new ServerException("파일 저장 실패");
        }
    }

    @Transactional
    public List<Code> updateRating(RatingDTO ratingDTO) {

        if(codeRepository.existsByName(ratingDTO.getName())) {
            throw new DataExistsException("존재하는 rating 이름입니다.", "rating");
        }

        codeRepository.save(MovieMapper.ratingDTOToCode(ratingDTO, codeRepository.findRatingUppderCode()));

        return getRatingList();
    }

    @Transactional(readOnly = true)
    public List<Code> getRatingList() {
        return codeRepository.findAllByUpperCode(codeRepository.findRatingUppderCode());
    }


    //같은 인물 저장되어 있는지 확인할 방법은?
    @Transactional
    public List<CastInMovieDTO> addCast(CastInfoDTO infoDTO) {
        Image profileImage = createImage(infoDTO.getProfileImage(), "cast");

        castRepository.save(MovieMapper.castInfoDTOWithImageToCast(infoDTO, profileImage));
        return getDirectorList();
    }

    @Transactional
    public List<CastInMovieDTO> modifyCast(CastInfoDTO infoDTO) {
        if(infoDTO.getProfileImage().isEmpty()) {
            castRepository.updateWithoutImage(MovieMapper.castInfoDTOToCast(infoDTO));
            return getDirectorList();
        }

        Image profileImage = createImage(infoDTO.getProfileImage(), "cast");

        Cast cast = MovieMapper.castInfoDTOWithImageToCast(infoDTO, profileImage);
        castRepository.save(cast);
        return getDirectorList();
    }

    @Transactional(readOnly = true)
    public List<CastInMovieDTO> getDirectorList() {
        List<Cast> directorList = castRepository.findAll();
        return directorList.stream().map(cast -> MovieMapper.castToCastInMovieDTO(cast)).collect(Collectors.toList());
    }

    @Transactional
    public void addRole(Long movieId, RoleAddDTO roleAddDTO) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie"));
        Cast cast = castRepository.findById(roleAddDTO.getCastId()).orElseThrow(() -> new DataNotExistsException("존재하지 않는 배우 ID입니다.", "cast"));

        Role role = Role.builder()
                .movie(movie)
                .cast(cast)
                .role(roleAddDTO.getRole())
                .starring(roleAddDTO.isStarring())
                .build();

        roleRepository.save(role);
    }

    @Transactional
    public void modifyGenre(String name, String code) {
        Code genre = Code.builder()
                .code(code)
                .name(name)
                .upperCode(codeRepository.findGenreUpperCode())
                .build();

        codeRepository.save(genre);
    }

    @Transactional
    public void deleteGenre(String codeId) {
        codeRepository.deleteById(codeId);
    }

    @Transactional
    public List<Code> addGenre(String name, String code) {
        if(codeRepository.existsByName(name)) {
            throw new DataExistsException("이미 저장된 장르 입니다.", "genre");
        }

        Code genre = Code.builder()
                .code(code)
                .name(name)
                .upperCode(codeRepository.findGenreUpperCode())
                .build();

        codeRepository.save(genre);

        return loadGenreList();
    }

    @Transactional(readOnly = true)
    public List<Code> loadGenreList() {
        return codeRepository.findAllByUpperCode(codeRepository.findGenreUpperCode());
    }

    @Transactional(readOnly = true)
    public MovieDTO getMovie(Long movieId) {
        Movie movie = movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie"));

        return MovieMapper.movieToMovieDTO(movie);
    }

    @Transactional(readOnly = true)
    public boolean hasPoster(String path) {
        return imageRepository.existsByFileUrl(path);
    }

    @Transactional
    public void deleteCast(Long castId) {
        castRepository.deleteById(castId);
    }

    @Transactional
    public void deleteRole(Long movieId, Long castId) {
        RoleId roleId = RoleId.builder()
                .cast(castRepository.findById(castId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 배우 ID입니다.", "cast")))
                .movie(movieRepository.findById(movieId).orElseThrow(() -> new DataNotExistsException("존재하지 않는 영화 ID입니다.", "movie")))
                .build();
        roleRepository.deleteById(roleId);
    }

    @Transactional
    public void deleteRating(String ratingCode) {
        if(movieRepository.existsByRating(codeRepository.findById(ratingCode).orElseThrow(() -> new DataNotExistsException("존재하지 않는 등급 ID입니다.", "Rating")))) {
            throw new DataExistsException("해당 등급을 가진 영화가 존재합니다.", "RATING");
        }

        else {
            codeRepository.deleteById(ratingCode);
        }
    }

    @Transactional(readOnly = true)
    public CastDTO getCast(Long castId) {
        return castRepository.findById(castId).map(cast -> MovieMapper.castToCastDTO(cast)).orElseThrow(() -> new DataNotExistsException("존재하지 않는 인물 ID입니다.", "CAST"));
    }

    @Transactional(readOnly = true)
    public List<MovieTitleDTO> getShortMovieList() {
        return movieRepository.findAll().stream().distinct().map(movie -> MovieMapper.movieToMovieTitleDTO(movie)).collect(Collectors.toList());
    }
//
//    @Transactional
//    public void addGenreToMovie()
}
